package com.mistreckless.support.wellcomeapp.ui

import android.content.Context
import com.mistreckless.support.wellcomeapp.data.auth.RxAuth
import com.mistreckless.support.wellcomeapp.domain.auth.AuthService
import com.mistreckless.support.wellcomeapp.domain.auth.GoogleAuthService
import com.mistreckless.support.wellcomeapp.ui.screen.profile.Profile
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wellcome.event.Wall
import com.wellcome.event.WallModule
import com.wellcome.utils.ui.PerFragment
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import wellcome.common.cache.Cache
import wellcome.common.location.LocationService

@Module
abstract class MainActivityFragmentProvider {

    @ContributesAndroidInjector(modules = [WallModule::class])
    @PerFragment
    abstract fun provideWallFactory() : Wall

    @ContributesAndroidInjector
    @PerFragment
    abstract fun provideProfile() : Profile
}

@Module
class MainActivityModule {

    @Provides
    fun provideRxPermissions(activity: MainActivity) : RxPermissions = RxPermissions(activity)

    @Provides
    fun provideRxAuth(activity: MainActivity): RxAuth = RxAuth(activity)

    @Provides
    fun provideAuthService(context: Context,cache: Cache, locationService: LocationService): AuthService = GoogleAuthService(context,cache, locationService)
}