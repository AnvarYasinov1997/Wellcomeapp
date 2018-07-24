package com.wellcome.main

import com.wellcome.main.domain.auth.AuthService
import com.wellcome.main.domain.auth.GoogleAuthService
import com.wellcome.utils.RationaleDialog
import com.wellcome.utils.auth.GoogleAuth
import com.wellcome.utils.permission.AppSettingsPage
import com.wellcome.utils.permission.AsyncPermissions
import dagger.Module
import dagger.Provides
import wellcome.common.core.Cache
import wellcome.common.core.service.ApiService
import wellcome.common.core.service.LocationService

@Module
class MainActivityModule {

    @Provides
    fun providePermissions(activity: MainActivity): AsyncPermissions = AsyncPermissions(activity)

    @Provides
    fun provideGoogleAuth(activity: MainActivity): GoogleAuth = GoogleAuth(activity)

    @Provides
    fun provideRationaleDialog(activity: MainActivity): RationaleDialog =
        RationaleDialog(activity.supportFragmentManager)

    @Provides
    fun provideAppSettingsPage(activity: MainActivity): AppSettingsPage = AppSettingsPage(activity)

    @Provides
    fun provideAuthService(cache: Cache,
                           locationService: LocationService,
                           apiService: ApiService): AuthService =
        GoogleAuthService(cache, locationService, apiService)

}