package wellcome.common.core

import android.annotation.SuppressLint
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import wellcome.common.mpp.entity.Address
import wellcome.common.mpp.entity.LatLon
import kotlin.coroutines.experimental.suspendCoroutine

actual class LocationProvider(private val fusedLocationProviderClient: FusedLocationProviderClient,
                              private val geocoder: Geocoder) {
    @SuppressLint("MissingPermission")
    actual fun getLastKnownLocation(): Deferred<LatLon> = async {
        suspendCoroutine<LatLon> { cont ->
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                val location = task.result
                if (location != null) cont.resume(LatLon(location.latitude, location.longitude))
                else cont.resumeWithException(task.exception!!)
            }
        }
    }

    actual fun getAddressesFromLocation(latLon: LatLon, maxResult: Int): Deferred<List<Address>> =
        async {
            geocoder.getFromLocation(latLon.lat, latLon.lon, maxResult).toEntities()
        }

    fun MutableList<android.location.Address?>.toEntities(): List<Address> {
        val mutableList = mutableListOf<Address>()
        this.filter { it != null }.forEach {
            mutableList.add(Address(it!!.locality,
                it.thoroughfare,
                it.subThoroughfare))
        }
        return mutableList.toList()
    }
}