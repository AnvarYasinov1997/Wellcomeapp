package com.mistreckless.support.wellcomeapp.ui

import com.mistreckless.support.wellcomeapp.data.CacheData
import com.mistreckless.support.wellcomeapp.data.auth.AuthService
import com.mistreckless.support.wellcomeapp.data.auth.GoogleAuthService
import com.mistreckless.support.wellcomeapp.data.auth.RxAuth
import com.mistreckless.support.wellcomeapp.data.repository.LocationRepository
import com.mistreckless.support.wellcomeapp.ui.screen.profile.Profile
import com.mistreckless.support.wellcomeapp.ui.screen.wall.Wall
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityFragmentProvider {

    @ContributesAndroidInjector
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
    fun provideAuthService(cacheData: CacheData,locationRepository: LocationRepository): AuthService = GoogleAuthService(cacheData, locationRepository)
}