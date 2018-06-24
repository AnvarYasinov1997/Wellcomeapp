package com.wellcomeapp.main

import android.content.Context
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wellcome.core.Cache
import com.wellcome.core.retrofit.Api
import com.wellcome.core.retrofit.CoroutineCallAdapterFactory
import com.wellcomeapp.main.data.auth.RxAuth
import com.wellcomeapp.main.domain.auth.AuthService
import com.wellcomeapp.main.domain.auth.GoogleAuthService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import wellcome.common.location.LocationService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class MainActivityModule {

    @Provides
    fun provideRxPermissions(activity: MainActivity): RxPermissions = RxPermissions(activity)

    @Provides
    fun provideRxAuth(activity: MainActivity): RxAuth = RxAuth(activity)

    @Provides
    fun provideAuthService(context: Context, cache: Cache, locationService: LocationService): AuthService = GoogleAuthService(context, cache, locationService)

    @Provides
    fun provideApiTest(): Api {
        val client = OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .build()
        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://us-central1-wellcomeapp-cc11e.cloudfunctions.net")
                .client(client)
                .build()
        return retrofit.create(Api::class.java)
    }
}