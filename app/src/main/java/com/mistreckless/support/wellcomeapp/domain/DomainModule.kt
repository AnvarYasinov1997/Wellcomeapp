package com.mistreckless.support.wellcomeapp.domain

import android.content.Context
import com.google.android.gms.location.LocationRequest
import com.ironz.binaryprefs.BinaryPreferencesBuilder
import com.ironz.binaryprefs.Preferences
import com.mistreckless.support.wellcomeapp.domain.interactor.*
import com.mistreckless.support.wellcomeapp.domain.repository.LocationRepository
import com.mistreckless.support.wellcomeapp.domain.repository.LocationRepositoryImpl
import com.mistreckless.support.wellcomeapp.domain.repository.UserRepository
import com.mistreckless.support.wellcomeapp.domain.repository.UserRepositoryImpl
import com.mistreckless.support.wellcomeapp.util.rxlocation.RxLocation
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by @mistreckless on 31.07.2017. !
 */

@Singleton
@Module
class DataModule {

    @Singleton
    @Provides
    fun providePreferences(context: Context): Preferences = BinaryPreferencesBuilder(context).name("user_data").build()

    @Singleton
    @Provides
    fun provideRxLocation(context: Context): RxLocation = RxLocation(context)

    @Singleton
    @Provides
    fun provideCacheData(preferences: Preferences):CacheData = CacheDataImp(preferences)

    @Singleton
    @Named("lowLocationRequest")
    fun provideLowLocationRequest(context: Context): LocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_LOW_POWER)
            .setMaxWaitTime(1 * 1000)

    @Singleton
    @Named("accuracyLocationRequest")
    fun provideAccuracyLocationRequest(context: Context): LocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setMaxWaitTime(1 * 9000)

}

@Singleton
@Module
class InteractorModule {

    @Singleton
    @Provides
    fun provideMainInteractor(userRepository: UserRepository): MainInteractor = MainInteractorImpl(userRepository)

    @Singleton
    @Provides
    fun provideRegistryInteractor(userRepository: UserRepository, locationRepository : LocationRepository) : RegistryInteractor = RegistryInteractorImpl(userRepository, locationRepository)

    @Singleton
    @Provides
    fun provideDataInteractor(userRepository: UserRepository) : DataInteractor = DataInteractorImpl(userRepository)

    @Singleton
    @Provides
    fun provideProfileInteractor(dataInteractor: DataInteractor,userRepository: UserRepository) : ProfileInteractor = ProfileInteractorImpl(dataInteractor, userRepository)
}

@Singleton
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideUserRepository(cacheData: CacheData, context: Context): UserRepository = UserRepositoryImpl(cacheData, context)

    @Singleton
    @Provides
    fun provideLocationRepository(rxLocation: RxLocation, cacheData: CacheData) : LocationRepository
            = LocationRepositoryImpl(rxLocation,cacheData)
}