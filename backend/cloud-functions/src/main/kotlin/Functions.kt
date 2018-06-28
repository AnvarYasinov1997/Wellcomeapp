import kotlin.js.Promise

fun promiseTimezone(mapClient: dynamic, params: dynamic): Promise<TimezoneType> =
    mapClient.timezone(params).asPromise().then { response ->
        console.log(response.json)
        return@then TimezoneType(response.json.timeZoneId)
    } as Promise<TimezoneType>

fun promiseLocality(mapClient: dynamic, params: dynamic): Promise<LocalityType> =
    mapClient.reverseGeocode(params).asPromise().then { response ->
        val t = JSON.parse<Res>(JSON.stringify(response.json))
        for (item in t.results) {
            for (address in item.address_components) {
                for (type in address.types) {
                    if (type.equals("locality")) {
                        console.log(item)
                        console.log(address)
                        return@then LocalityType(address.long_name)
                    }
                }
            }
        }
    } as Promise<LocalityType>

