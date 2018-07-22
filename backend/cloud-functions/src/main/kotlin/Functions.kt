import js.externals.firebase.admin.FirebaseFirestore.DocumentReference
import js.externals.firebase.admin.FirebaseFirestore.Firestore
import js.externals.firebase.admin.FirebaseFirestore.QueryDocumentSnapshot
import js.externals.firebase.functions.providers.https.CallableContext
import wellcome.common.core.FirebaseConstants
import wellcome.common.entity.CityData
import wellcome.common.entity.LatLon
import wellcome.common.entity.UserData
import kotlin.js.Promise

fun generateGeocodeParams(latLon: LatLon): dynamic {
    val geocodeParams: dynamic = object {}
    geocodeParams["latlng"] = "${latLon.lat},${latLon.lon}"
    return geocodeParams
}

fun generateTimezoneParams(latLon: LatLon): dynamic {
    val timezoneParams: dynamic = object {}
    timezoneParams["location"] = "${latLon.lat},${latLon.lon}"
    return timezoneParams
}

fun promiseTimezone(mapClient: dynamic, params: dynamic): Promise<String> =
    mapClient.timezone(params).asPromise().then { response ->
        console.log(response.json)
        return@then response.json.timeZoneId
    } as Promise<String>

fun promiseLocality(mapClient: dynamic, params: dynamic): Promise<String> =
    mapClient.reverseGeocode(params).asPromise().then { response ->
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
    } as Promise<String>


fun logError(err: Throwable) {
    console.log(err)
    throw err
}

fun Firestore.findUserByUid(uid: String) =
    collection(FirebaseConstants.USER).where(UserData.ID, "==", uid).limit(1).get()

fun Firestore.findCityByName(name: String) =
    collection(FirebaseConstants.CITY).where(CityData.NAME, "==", name).limit(1).get()

fun <T> QueryDocumentSnapshot.parse(): T {
    val entity = JSON.parse<T>(JSON.stringify(data()))
    console.log(entity)
    return entity
}

fun <T> DocumentReference.setAndReturn(entity: T): Promise<T> {
    console.log(entity)
    return set(JSON.parse(entity.toString())).returnValue(entity)
}

fun <T> DocumentReference.updateAndReturn(returnedValue: T, key: String, value: Any, vararg fields: Any): Promise<T> {
    console.log(fields)

    return update(key, value, fields).returnValue(returnedValue)
}

fun <T> Promise<*>.returnValue(returnedValue: T): Promise<T> = then {
    console.log(it)
    return@then returnedValue
}