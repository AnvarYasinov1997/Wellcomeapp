@file:Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")

import js.externals.firebase.admin.FirebaseFirestore.*
import js.externals.firebase.admin.initializeApp
import js.externals.firebase.functions.providers.https.CallableContext
import wellcome.common.core.FirebaseConstants
import wellcome.common.entity.CityData
import wellcome.common.entity.LatLon
import wellcome.common.entity.UserData
import kotlin.js.Promise

external fun require(module: String): dynamic
external val exports: dynamic

val map: dynamic = object {}

//val admin = require("firebase-admin")
val functions = require("firebase-functions")
val mapClient by lazy {
    map["key"] = ""
    map["Promise"] = Promise
    require("@google/maps").createClient(map)
}


fun main(args: Array<String>) {
    Admin.initializeApp(functions.config().firebase)
    val db = Admin.firestore()

    exports.initCity = functions.https.onCall { data: LatLon, context: CallableContext ->
        val uid = context.auth!!.uid

        return@onCall promiseLocality(mapClient, generateGeocodeParams(data)).then { cityName ->
            val userPromise = db.findUserByUid(uid)
            val cityPromise = db.findCityByName(cityName)

            Promise.all(arrayOf(userPromise, cityPromise)).then city@{ snapshots ->
                val userSnapshot = snapshots[0]
                val citySnapshot = snapshots[1]
                val userRef = userSnapshot.docs.first().ref

                if (citySnapshot.empty) return@city promiseTimezone(mapClient, generateTimezoneParams(data))
                    .then snapshot@{ zoneId ->
                        val cityRef = db.collection(FirebaseConstants.CITY).doc()
                        val cityData = CityData(cityRef.id, cityName, zoneId)

                        val batch = db.batch()
                        batch.set(cityRef, JSON.parse(cityData.toString()))
                        batch.update(userRef, UserData.CITY_NAME, cityName, js("{merge: true}"))

                        return@snapshot batch.commit().returnValue(cityData)
                    }
                val cityData = citySnapshot.docs.first().parse<CityData>()
                return@city userRef.updateAndReturn(cityData, UserData.CITY_NAME, cityName)
            }

        }.catch(::logError)
    }

    exports.initUser = functions.https.onCall { data: Any, context: CallableContext ->
        val uid = context.auth!!.uid
        val userName = context.auth?.token?.get("name") as String

        return@onCall db.findUserByUid(uid).then user@{ snapshot: QuerySnapshot ->
                if (!snapshot.empty) return@user snapshot.docs.first().parse<UserData>()

                val userRef = db.collection(FirebaseConstants.USER).doc()
                val user = UserData(uid, userRef.id, displayedName = userName)
                return@user userRef.setAndReturn(user)
            }.catch(::logError)

    }

    exports.test = functions.https.onRequest { req, resp ->
        val ref = db.collection(FirebaseConstants.USER).doc()
        val userData = UserData(ref.id)
        console.log(userData.toString())
        console.log(JSON.parse(userData.toString()))
        ref.set(JSON.parse(userData.toString())).then {
            ref.updateAndReturn(Unit, UserData.DISPLAYED_NAME, "lol", UserData.CITY_NAME, "myrs")
            resp.send("ok")
        }
        Unit
    }
}

