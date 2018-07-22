package wellcome.common.core

import kotlinx.coroutines.experimental.Deferred
import wellcome.common.mpp.entity.Address
import wellcome.common.mpp.entity.LatLon

expect class LocationProvider {
    fun getLastKnownLocation(): Deferred<LatLon>
    fun getAddressesFromLocation(latLon: LatLon, maxResult: Int): Deferred<List<Address>>
}