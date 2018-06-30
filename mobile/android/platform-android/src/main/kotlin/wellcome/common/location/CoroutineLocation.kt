@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package wellcome.common.location

import android.annotation.SuppressLint
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import wellcome.common.entity.Address
import wellcome.common.entity.LatLon
import kotlin.coroutines.experimental.coroutineContext
import kotlin.coroutines.experimental.suspendCoroutine

 class CoroutineLocation(private val fusedLocationProviderClient: FusedLocationProviderClient, private val geocoder: Geocoder){
    @SuppressLint("MissingPermission")
     suspend fun getLastKnownLocation(): Deferred<LatLon> = async(coroutineContext) {
        suspendCoroutine<LatLon> { cont->
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                val location = task.result
                if (location != null) cont.resume(LatLon(location.latitude,location.longitude))
                else cont.resumeWithException(task.exception!!)
            }
        }
    }

     fun getAddressesFromLocation(
        lat: Double,
        lon: Double,
        maxResult: Int
    ): List<Address> = geocoder.getFromLocation(lat,lon,maxResult).toEntities()

}

fun MutableList<android.location.Address?>.toEntities(): List<Address>{
    val mutableList = mutableListOf<Address>()
    this.filter { it != null }.forEach { mutableList.add(Address(it!!.locality, it.thoroughfare, it.subThoroughfare)) }
    return mutableList.toList()
}