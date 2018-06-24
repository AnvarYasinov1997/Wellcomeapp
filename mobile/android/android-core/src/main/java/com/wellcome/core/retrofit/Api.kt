package com.wellcome.core.retrofit

import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import wellcome.common.entity.CityData

interface Api {

    @FormUrlEncoded
    @POST("/helloWorld")
    fun helloWorld(@Field("location") location: String, @Field("name") name: String): Deferred<CityData>
}