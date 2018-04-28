package wellcome.common.location

import kotlinx.coroutines.experimental.Deferred
import wellcome.common.entity.Address

expect class CoroutineLocation{

    fun getLastKnownLocation(): Deferred<Pair<Double, Double>>

    fun getAddressesFromLocation(lat : Double, lon : Double, maxResult : Int) : Deferred<List<Address>>
}