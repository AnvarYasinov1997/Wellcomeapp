package com.wellcome.main

import com.tbruyelle.rxpermissions2.RxPermissions
import com.wellcome.main.data.auth.RxAuth
import com.wellcome.main.domain.auth.AuthService
import com.wellcome.main.domain.auth.GoogleAuthService
import dagger.Module
import dagger.Provides
import wellcome.common.core.Cache
import wellcome.common.core.service.ApiService
import wellcome.common.core.service.LocationService

@Module
class MainActivityModule {

    @Provides
    fun provideRxPermissions(activity: MainActivity): RxPermissions = RxPermissions(activity)

    @Provides
    fun provideRxAuth(activity: MainActivity): RxAuth = RxAuth(activity)

    @Provides
    fun provideAuthService(cache: Cache,
                           locationService: LocationService,
                           apiService: ApiService): AuthService =
        GoogleAuthService(cache, locationService, apiService)

}