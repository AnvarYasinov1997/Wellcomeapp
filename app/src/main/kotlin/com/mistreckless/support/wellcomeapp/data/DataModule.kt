package com.mistreckless.support.wellcomeapp.data

import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mistreckless.support.wellcomeapp.data.location.RxLocation
import com.mistreckless.support.wellcomeapp.data.location.RxLocationImpl
import com.mistreckless.support.wellcomeapp.data.repository.*
import com.wellcome.core.room.StoryDao
import dagger.Module
import dagger.Provides
import com.wellcome.core.Cache
import com.wellcome.core.service.StoryService
import com.wellcome.core.service.WellcomeStoryService
import wellcome.common.location.CoroutineLocation
import wellcome.common.location.LocationService
import wellcome.common.location.LocationServiceImpl
import java.util.*
import javax.inject.Singleton

@Singleton
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideUserRepositoryOld(cacheData: CacheData, context: Context): UserRepository = UserRepositoryImpl(cacheData, context)

    @Singleton
    @Provides
    fun providePostRepository(cacheData: CacheData) : EventRepository = EventRepositoryImpl(cacheData)

    @Singleton
    @Provides
    fun provideUserRepository(cache: Cache): wellcome.common.repository.UserRepository = wellcome.common.repository.UserRepository(cache)

    @Singleton
    @Provides
    fun provideEventRepository(cache: Cache): wellcome.common.repository.EventRepository = wellcome.common.repository.EventRepository(cache)

    @Singleton
    @Provides
    fun provideLocationService(coroutineLocation: CoroutineLocation, cache: Cache): LocationService =
        LocationServiceImpl(coroutineLocation, cache)

    @Singleton
    @Provides
    fun provideCoroutineLocation(fusedLocationProviderClient: FusedLocationProviderClient, geocoder: Geocoder): CoroutineLocation = CoroutineLocation(fusedLocationProviderClient,geocoder)
}

@Singleton
@Module
class DataModule(private val storyDao: StoryDao) {

    @Singleton
    @Provides
    fun providePreferences(context: Context): SharedPreferences = context.getSharedPreferences("user_data",Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideRxLocation(fusedLocationProviderClient: FusedLocationProviderClient, geocoder: Geocoder): RxLocation =RxLocationImpl(fusedLocationProviderClient, geocoder)
    @Singleton
    @Provides
    fun provideCacheData(preferences: SharedPreferences): CacheData = CacheDataImp(preferences)

    @Singleton
    @Provides
    fun provideCache(preferences: SharedPreferences): Cache =
        Cache(preferences)

    @Singleton
    @Provides
    fun provideFusedLocationClient(context: Context): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @Singleton
    @Provides
    fun provideGeocoder(context: Context, locale : Locale) : Geocoder = Geocoder(context, locale)

    @Singleton
    @Provides
    fun provideStoryDao(): StoryDao = storyDao

    @Singleton
    @Provides
    fun provideStoryService(storyDao: StoryDao, cache: Cache): StoryService = WellcomeStoryService(storyDao, cache)

}