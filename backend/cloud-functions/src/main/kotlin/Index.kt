@file:Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")

import js.externals.firebase.admin.FirebaseFirestore.*
import js.externals.firebase.functions.providers.https.CallableContext
import wellcome.common.core.FirebaseConstants
import wellcome.common.entity.CityData
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
val db by lazy { admin.firestore() }


fun main(args: Array<String>) {

    admin.initializeApp()
    exports.initCity = functions.https.onCall { data: InitCityReq, context: CallableContext ->
        console.log(data)
        console.log(data.name)
        console.log(data.lat)
        console.log(context.auth)
        val params: dynamic = object {}
        params["location"] = "${data.lat},${data.lon}"
        params["timeout"] = 10000
        return@onCall (mapClient.timezone(params).asPromise().then { response ->
            console.log(response.json)
            return@then response.json.timeZoneId
        } as Promise<String>).then { zoneId ->
            val cityRef = db.collection(FirebaseConstants.CITY).doc()
            val cityData = CityData(cityRef.id, data.name, zoneId)
            console.log(cityData)
            return@then cityRef.set(JSON.parse(JSON.stringify(cityData as DocumentData))).then {
                return@then cityData
            }
        }.catch { err ->
            throw err
        }
    }

    exports.helloWorld = functions.https.onRequest { req, resp ->
        val cit = CityData("res","sd","sd") as DocumentData
        console.log("Here")
        val params: dynamic = object {} //js("({ address: '$t'})")
        params["location"] = "50.123,13.323"
        params["timeout"] = 100000
        mapClient.timezone(params).asPromise().then { response ->
            console.log("timezone")
            return@then response.json.timeZoneId
        }.then { zoneId ->
            console.log(zoneId)
            resp.send("ok")
        }.catch { err ->
            console.log("err")
            resp.send("ok")

        }
    }
}

//exports.helloWorld = functions.https.onRequest { req, resp ->
//    console.log("Here")
//    console.log(CityData.NAME)
//    console.log(req.body.location)
//    console.log(req.body.name)
//    val params: dynamic = object {} //js("({ address: '$t'})")
//    params["location"] = req.body.location
//    params["timeout"] = 100000
//    mapClient.timezone(params){ err, response ->
//        console.log("timezone")
//        val cityRef = db.collection(FirebaseConstants.CITY).doc()
//        console.log("before")
//        val cityData = CityData(cityRef.id, req.body.name, response.json.timeZoneId) as DocumentData
//        console.log(cityData)
//
//        cityRef.set(JSON.parse(JSON.stringify(cityData))).then { result ->
//            console.log(result)
//            resp.send(cityData)
//        }.catch {error ->
//            console.log("err")
//            console.log(error)
//            resp.send(error)
//        }
//    }
//}