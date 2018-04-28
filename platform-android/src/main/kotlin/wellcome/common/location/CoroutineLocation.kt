@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package wellcome.common.location

import android.annotation.SuppressLint
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import wellcome.common.entity.Address
import kotlin.coroutines.experimental.suspendCoroutine

actual class CoroutineLocation(private val fusedLocationProviderClient: FusedLocationProviderClient, private val geocoder: Geocoder){
    @SuppressLint("MissingPermission")
    actual fun getLastKnownLocation(): Deferred<Pair<Double, Double>> = async {
        suspendCoroutine<Pair<Double,Double>> { cont->
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                val location = task.result
                if (location != null) cont.resume(Pair(location.latitude,location.longitude))
                else cont.resumeWithException(task.exception!!)
            }
        }
    }

    actual fun getAddressesFromLocation(
        lat: Double,
        lon: Double,
        maxResult: Int
    ): Deferred<List<Address>> = async {
        geocoder.getFromLocation(lat,lon,maxResult).toEntities()
    }
}

fun MutableList<android.location.Address?>.toEntities(): List<Address>{
    val mutableList = mutableListOf<Address>()
    this.filter { it != null }.forEach { mutableList.add(Address(it!!.locality, it.thoroughfare, it.subThoroughfare)) }
    return mutableList.toList()
}