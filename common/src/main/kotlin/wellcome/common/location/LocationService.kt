package wellcome.common.location

import wellcome.common.cache.Cache
import wellcome.common.cache.CacheConst
import wellcome.common.entity.Address

interface LocationService {
    suspend fun getCurrentCity(): String
    suspend fun getCurrentAddress(): Address
}

class LocationServiceImpl(
    private val coroutineLocation: CoroutineLocation,
    private val cache: Cache
) : LocationService {
    override suspend fun getCurrentCity(): String {
        val latLon = coroutineLocation.getLastKnownLocation().await()
        val city = getAddress(latLon).locality
        cache.cacheString(CacheConst.USER_CITY,city)
        return city
    }

    override suspend fun getCurrentAddress(): Address {
        val latLon = coroutineLocation.getLastKnownLocation().await()
        cache.cacheDouble(CacheConst.TMP_LAT,latLon.first)
        cache.cacheDouble(CacheConst.TMP_LON,latLon.second)
        return getAddress(latLon)
    }

    private suspend fun getAddress(latLon: Pair<Double, Double>): Address {
        val list =
            coroutineLocation.getAddressesFromLocation(latLon.first, latLon.second, 1).await()
        return if (list.isNotEmpty()) list[0] else Address("", "", "")
    }
}