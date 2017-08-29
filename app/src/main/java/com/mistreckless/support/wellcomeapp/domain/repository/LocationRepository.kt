package com.mistreckless.support.wellcomeapp.domain.repository

import android.annotation.SuppressLint
import android.location.Address
import android.location.Location
import android.support.annotation.RequiresPermission
import com.mistreckless.support.wellcomeapp.domain.CacheData
import com.patloew.rxlocation.RxLocation
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Created by @mistreckless on 10.08.2017. !
 */

interface LocationRepository {

    fun getCurrentCity(): Single<String>

    fun getCurrentAddress(): Single<String>
}

class LocationRepositoryImpl (private val rxLocation: RxLocation,
                                                 private val cacheData: CacheData ) : LocationRepository {
    override fun getCurrentAddress(): Single<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @SuppressLint("SupportAnnotationUsage", "MissingPermission")
    @RequiresPermission(allOf = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION))
    override fun getCurrentCity(): Single<String> {
        return rxLocation.location().lastLocation()
                .toSingle()
                .flatMap { getAddressFromLocation(it) }
                .map { it.locality }
                .doOnSuccess { cacheData.cacheString(CacheData.USER_CITY, it) }
                .subscribeOn(Schedulers.io())

    }


    private fun getAddressFromLocation(location: Location): Single<Address> {
        return rxLocation.geocoding().fromLocation(location).toSingle()
    }
}