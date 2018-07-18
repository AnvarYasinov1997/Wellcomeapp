package com.wellcome.core.retrofit

import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.*
import wellcome.common.entity.CityData
import wellcome.common.entity.UserData

interface Api {

    @GET("/auth/initUser")
    fun initUser(): Deferred<UserData>

    @GET("/auth/initCity")
    fun initCity( @Query("lat") lat: Double, @Query("lon") lon: Double): Deferred<CityData>
}