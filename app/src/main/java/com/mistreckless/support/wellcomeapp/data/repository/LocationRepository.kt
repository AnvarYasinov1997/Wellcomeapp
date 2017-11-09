package com.mistreckless.support.wellcomeapp.data.repository

import android.annotation.SuppressLint
import android.location.Address
import android.support.annotation.RequiresPermission
import com.mistreckless.support.wellcomeapp.data.CacheData
import com.mistreckless.support.wellcomeapp.data.location.LocationException
import com.mistreckless.support.wellcomeapp.data.location.RxLocation
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Created by @mistreckless on 10.08.2017. !
 */

interface LocationRepository {

    fun getCurrentCity(): Single<String>

    fun getCurrentAddress(): Single<Address>
}

class LocationRepositoryImpl (private val rxLocation: RxLocation,
                              private val cacheData: CacheData) : LocationRepository {


    @SuppressLint("SupportAnnotationUsage", "MissingPermission")
    @RequiresPermission(allOf = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION))
    override fun getCurrentAddress(): Single<Address> {
        return rxLocation.getLastKnownLocation()
                .doOnSuccess{
                    cacheData.cacheDouble(CacheData.TMP_LAT,it.first)
                    cacheData.cacheDouble(CacheData.TMP_LON,it.second)
                }
                .flatMap { getAddressFromLocation(it) }
                .subscribeOn(Schedulers.io())
    }

    @SuppressLint("SupportAnnotationUsage", "MissingPermission")
    @RequiresPermission(allOf = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION))
    override fun getCurrentCity(): Single<String> {
        return rxLocation.getLastKnownLocation()
                .flatMap { getAddressFromLocation(it) }
                .map { it.locality }
                .doOnSuccess { cacheData.cacheString(CacheData.USER_CITY, it) }
                .subscribeOn(Schedulers.io())

    }


    private fun getAddressFromLocation(latLon : Pair<Double,Double>): Single<Address> {
        return rxLocation.getAddressesFromLocation(latLon.first,latLon.second,1)
                .map { if (it.isNotEmpty()) it[0] else throw LocationException("cannot determune address by ${latLon.first} and ${latLon.second}")  }
    }
}