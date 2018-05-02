//package wellcome.common.location
//
//import wellcome.common.coroutines.Deferred
//import wellcome.common.entity.Address
//
//expect class CoroutineLocation{
//
//    suspend fun getLastKnownLocation(): Deferred<Pair<Double, Double>>
//
//    suspend fun getAddressesFromLocation(lat : Double, lon : Double, maxResult : Int) : List<Address>
//}