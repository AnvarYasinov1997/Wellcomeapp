@file:Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")

import js.externals.firebase.admin.FirebaseFirestore.*
import js.externals.firebase.functions.providers.https.CallableContext
import wellcome.common.core.FirebaseConstants
import wellcome.common.entity.CityData
import wellcome.common.entity.LatLon
import wellcome.common.entity.UserData
import kotlin.js.Promise

external fun require(module: String): dynamic
external val exports: dynamic

val map: dynamic = object {}

val admin = require("firebase-admin")
val functions = require("firebase-functions")
val mapClient by lazy {
    map["key"] = apiKey
    map["Promise"] = Promise
    require("@google/maps").createClient(map)
}
val db by lazy { admin.firestore() as Firestore }


fun main(args: Array<String>) {
    admin.initializeApp()

    exports.initCity = functions.https.onCall { data: LatLon, context: CallableContext ->
        console.log(data)
        console.log(context.auth)
        val geocodeParams: dynamic = object {}
        geocodeParams["latlng"] = "${data.lat},${data.lon}"
        return@onCall promiseLocality(mapClient,geocodeParams).then { localityType ->
            return@then localityType.locality
        }.then { cityName ->
            return@then db
                .collection(FirebaseConstants.CITY)
                .where(CityData.NAME, "==", cityName)
                .limit(1)
                .get().then city@{ snapshot ->
                    if (snapshot.empty){
                        val timezoneParams: dynamic = object {}
                        timezoneParams["location"] = "${data.lat},${data.lon}"
                        return@city promiseTimezone(mapClient, timezoneParams).then snapshot@{ timezoneType ->
                            val zoneId = timezoneType.zoneId
                            val cityRef = db.collection(FirebaseConstants.CITY).doc()
                            val cityData = CityData(cityRef.id, cityName, zoneId)
                            return@snapshot Pair(cityRef,cityData)
                        }.then save@{ pair ->
                            return@save pair.first.set(JSON.parse(JSON.stringify(pair.second)))
                                .then result@{
                                    return@result pair.second
                                }
                        }
                    }else {
                        val documentData = snapshot.docs.first().data()
                        console.log(documentData)
                        val cityData = documentData as CityData
                        console.log(cityData)
                        return@city cityData
                    }
                }
        }
    }

    exports.initUser = functions.https.onCall { data: Any, context: CallableContext ->
        val uid = context.auth!!.uid
        return@onCall db
            .collection(FirebaseConstants.USER)
            .where(UserData.ID, "==", uid)
            .limit(1)
            .get()
            .then user@{ snapshot: QuerySnapshot ->
                if (snapshot.empty){
                    val userRef = db
                        .collection(FirebaseConstants.USER)
                        .doc()
                    val newUser = UserData(uid, userRef.id, displayedName = context.auth?.token?.get("name") as String)
                    return@user userRef.set(JSON.parse(JSON.stringify(newUser as DocumentData)))
                        .then result@{
                            return@result newUser
                        }
                }else{
                    val documentData = snapshot.docs.first().data()
                    console.log(documentData)
                    val userData = documentData as UserData
                    console.log(userData)
                    return@user userData
                }
            }.catch { err ->
                console.log(err)
                throw err
            }
    }

    exports.helloWorld = functions.https.onRequest { req, resp ->
        val cit = CityData("res", "sd", "sd") as DocumentData
        console.log("Here")
        val params: dynamic = object {} //js("({ address: '$t'})")
        params["location"] = "52.52008,13.404954"
        params["timeout"] = 100000

        val p0 = mapClient.timezone(params).asPromise().then { response ->
            console.log(response.json.timeZoneId)
            return@then response.json.timeZoneId
        }
        val params1: dynamic = object {}
        params1["latlng"] = "52.52008,13.404954"
        val p1 = mapClient.reverseGeocode(params1).asPromise().then { response ->
            val t = JSON.parse<Res>(JSON.stringify(response.json))
            for (item in t.results) {
                for (address in item.address_components) {
                    for (type in address.types) {
                        if (type.equals("locality")) {
                            console.log(item)
                            console.log(address)
                            return@then address.long_name
                        }
                    }
                }
            }
        }
        Promise.all<Any>(arrayOf(p0, p1)).then {
            console.log(it[0])
            console.log(it[1])
            resp.send("ok")

        }.catch { err ->
            console.log(err)
            resp.send("ok")
        }

        Unit
    }
}