package wellcome.common.location

import com.wellcome.core.Cache
import kotlinx.coroutines.experimental.Deferred
import wellcome.common.core.CacheConst
import wellcome.common.entity.Address
import wellcome.common.entity.LatLon

interface LocationService {
    suspend fun getCurrentCity(): String
    suspend fun getCurrentAddress(): Address
    suspend fun getLastKnownLocation(): LatLon
}

class LocationServiceImpl(
    private val coroutineLocation: CoroutineLocation,
    private val cache: Cache
) : LocationService {
    override suspend fun getCurrentCity(): String {
        val latLon = coroutineLocation.getLastKnownLocation().await()
        var city = getAddress(latLon).locality
        if (city.isEmpty()) city = "Moscow"
        cache.cacheString(CacheConst.USER_CITY,city)
        return city
    }

    override suspend fun getCurrentAddress(): Address {
        val latLon = coroutineLocation.getLastKnownLocation().await()
        cache.cacheDouble(CacheConst.TMP_LAT,latLon.lat)
        cache.cacheDouble(CacheConst.TMP_LON,latLon.lon)
        return getAddress(latLon)
    }

    override suspend fun getLastKnownLocation(): LatLon = coroutineLocation.getLastKnownLocation().await()

    private fun getAddress(latLon: LatLon): Address {
        val list =  coroutineLocation.getAddressesFromLocation(latLon.lat, latLon.lon, 1)
        return if (list.isNotEmpty()) list[0] else Address("", "", "")
    }
}