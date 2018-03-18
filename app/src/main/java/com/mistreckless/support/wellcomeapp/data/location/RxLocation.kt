package com.mistreckless.support.wellcomeapp.data.location

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import io.reactivex.Single


interface RxLocation {

    fun getLastKnownLocation(): Single<Pair<Double, Double>>

    fun getAddressesFromLocation(lat : Double, lon : Double, maxResult : Int) : Single<List<Address>>
}

class RxLocationImpl(private val fusedLocationProviderClient: FusedLocationProviderClient, private val geocoder: Geocoder) : RxLocation {

    @SuppressLint("MissingPermission")
    override fun getLastKnownLocation(): Single<Pair<Double, Double>> {
        return Single.create { e ->
            fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener {
                        if (!e.isDisposed) {
                            if (it != null)
                                e.onSuccess(Pair(it.latitude, it.longitude))
                            else {
                                e.onError(LocationException("null location"))
                            }
                        }
                    }
                    .addOnFailureListener {
                        if (!e.isDisposed)
                            e.onError(LocationException(it.message?:""))
                    }
        }
    }

    override fun getAddressesFromLocation(lat: Double, lon: Double, maxResult: Int): Single<List<Address>> {
        return Single.fromCallable { geocoder.getFromLocation(lat,lon,maxResult) }
    }


}