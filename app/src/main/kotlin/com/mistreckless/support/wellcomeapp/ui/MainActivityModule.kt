package com.mistreckless.support.wellcomeapp.ui

import android.content.Context
import com.mistreckless.support.wellcomeapp.data.auth.RxAuth
import com.mistreckless.support.wellcomeapp.ui.screen.profile.Profile
import com.mistreckless.support.wellcomeapp.ui.screen.wall.Wall
import com.mistreckless.support.wellcomeapp.ui.screen.wall.WallModule
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import wellcome.common.cache.Cache
import wellcome.common.location.LocationService
import com.mistreckless.support.wellcomeapp.domain.auth.AuthService
import com.mistreckless.support.wellcomeapp.domain.auth.GoogleAuthService
import com.wellcome.utils.ui.PerFragment

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