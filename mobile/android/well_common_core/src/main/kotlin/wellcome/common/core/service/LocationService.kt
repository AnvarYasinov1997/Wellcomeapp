package wellcome.common.core.service

import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import wellcome.common.core.Cache
import wellcome.common.core.LocationProvider
import wellcome.common.mpp.core.CacheConst
import wellcome.common.mpp.entity.Address
import wellcome.common.mpp.entity.LatLon

interface LocationService {
    fun getCurrentAddress(): Deferred<Address>
    fun getLastKnownLocation(): Deferred<LatLon>
}

class LocationServiceImpl(private val locationProvider: LocationProvider,
                          private val cache: Cache) : LocationService {
    override fun getCurrentAddress(): Deferred<Address> = async {
        val latLon = locationProvider.getLastKnownLocation().await()
        cache.cacheDouble(CacheConst.TMP_LAT, latLon.lat)
        cache.cacheDouble(CacheConst.TMP_LON, latLon.lon)
        return@async getAddress(latLon)
    }

    override fun getLastKnownLocation(): Deferred<LatLon> = locationProvider.getLastKnownLocation()

    private suspend fun getAddress(latLon: LatLon): Address {
        val list = locationProvider.getAddressesFromLocation(latLon, 1).await()
        return if (list.isNotEmpty()) list[0] else Address("", "", "")
    }
}