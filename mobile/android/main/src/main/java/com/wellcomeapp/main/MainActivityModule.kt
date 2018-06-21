package com.wellcomeapp.main

import android.content.Context
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wellcome.core.Cache
import com.wellcomeapp.main.data.auth.RxAuth
import com.wellcomeapp.main.domain.auth.AuthService
import com.wellcomeapp.main.domain.auth.GoogleAuthService
import dagger.Module
import dagger.Provides
import wellcome.common.location.LocationService

@Module
class MainActivityModule {

    @Provides
    fun provideRxPermissions(activity: MainActivity): RxPermissions = RxPermissions(activity)

    @Provides
    fun provideRxAuth(activity: MainActivity): RxAuth = RxAuth(activity)

    @Provides
    fun provideAuthService(context: Context, cache: Cache, locationService: LocationService): AuthService = GoogleAuthService(context, cache, locationService)
}