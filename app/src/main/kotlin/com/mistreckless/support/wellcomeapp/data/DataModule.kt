package com.mistreckless.support.wellcomeapp.data

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.ironz.binaryprefs.BinaryPreferencesBuilder
import com.ironz.binaryprefs.Preferences
import com.mistreckless.support.wellcomeapp.data.location.RxLocation
import com.mistreckless.support.wellcomeapp.data.location.RxLocationImpl
import com.mistreckless.support.wellcomeapp.data.repository.*
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

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

    @Singleton
    @Provides
    fun providePostRepository(cacheData: CacheData) : EventRepository = EventRepositoryImpl(cacheData)
}

@Singleton
@Module
class DataModule {

    @Singleton
    @Provides
    fun providePreferences(context: Context): Preferences = BinaryPreferencesBuilder(context).name("user_data").build()

    @Singleton
    @Provides
    fun provideRxLocation(fusedLocationProviderClient: FusedLocationProviderClient, geocoder: Geocoder): RxLocation =RxLocationImpl(fusedLocationProviderClient, geocoder)
    @Singleton
    @Provides
    fun provideCacheData(preferences: Preferences): CacheData = CacheDataImp(preferences)

    @Singleton
    @Provides
    fun provideFusedLocationClient(context: Context): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @Singleton
    @Provides
    fun provideGeocoder(context: Context, locale : Locale) : Geocoder = Geocoder(context, locale)

}