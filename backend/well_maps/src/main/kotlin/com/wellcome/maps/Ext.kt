package com.wellcome.maps

import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.TimeZoneApi
import com.google.maps.model.AddressComponentType
import com.google.maps.model.AddressType
import com.google.maps.model.LatLng
import kotlinx.coroutines.experimental.async

fun GeoApiContext.getLocality(lat: Double, lon: Double) = async<String> {
    val results = GeocodingApi
        .reverseGeocode(this@getLocality, LatLng(lat, lon))
        .resultType(AddressType.LOCALITY)
        .await()
    results.forEach { res ->
        res.addressComponents.forEach { addressComponent ->
            addressComponent.types.forEach { type ->
                if (type == AddressComponentType.LOCALITY) {
                    return@async addressComponent.longName
                }
            }
        }
    }
    return@async ""
}

fun GeoApiContext.getTimezoneId(lat: Double, lon: Double) = async<String> {
    val result = TimeZoneApi.getTimeZone(this@getTimezoneId, LatLng(lat, lon)).await()

    return@async when {
        result == null    -> ""
        result.id == null -> ""
        else              -> result.id
    }
}