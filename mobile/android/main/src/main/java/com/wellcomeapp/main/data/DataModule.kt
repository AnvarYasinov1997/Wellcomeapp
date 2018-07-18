package com.wellcomeapp.main.data

import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.wellcome.core.Cache
import com.wellcome.core.retrofit.Api
import com.wellcome.core.retrofit.CoroutineCallAdapterFactory
import com.wellcome.core.retrofit.TokenInterceptor
import com.wellcome.core.room.StoryDao
import com.wellcome.core.service.StoryService
import com.wellcome.core.service.WellcomeStoryService
import com.wellcomeapp.main.BuildConfig
import com.wellcomeapp.main.data.location.RxLocation
import com.wellcomeapp.main.data.location.RxLocationImpl
import com.wellcomeapp.main.data.repository.EventRepository
import com.wellcomeapp.main.data.repository.EventRepositoryImpl
import com.wellcomeapp.main.data.repository.UserRepository
import com.wellcomeapp.main.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import wellcome.common.location.CoroutineLocation
import wellcome.common.location.LocationService
import wellcome.common.location.LocationServiceImpl
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Singleton
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideUserRepositoryOld(cacheData: CacheData, context: Context): UserRepository = UserRepositoryImpl(cacheData, context)

    @Singleton
    @Provides
    fun providePostRepository(cacheData: CacheData): EventRepository = EventRepositoryImpl(cacheData)

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
    fun provideCoroutineLocation(fusedLocationProviderClient: FusedLocationProviderClient, geocoder: Geocoder): CoroutineLocation = CoroutineLocation(fusedLocationProviderClient, geocoder)
}

@Singleton
@Module
class DataModule(private val storyDao: StoryDao) {

    @Singleton
    @Provides
    fun providePreferences(context: Context): SharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideRxLocation(fusedLocationProviderClient: FusedLocationProviderClient, geocoder: Geocoder): RxLocation = RxLocationImpl(fusedLocationProviderClient, geocoder)

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
    fun provideGeocoder(context: Context, locale: Locale): Geocoder = Geocoder(context, locale)

    @Singleton
    @Provides
    fun provideStoryDao(): StoryDao = storyDao

    @Singleton
    @Provides
    fun provideStoryService(storyDao: StoryDao, cache: Cache): StoryService = WellcomeStoryService(storyDao, cache)

    @Singleton
    @Provides
    fun provideApi(cache: Cache): Api {
        val builder = OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(TokenInterceptor(cache))
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        }

        val client = builder.build()
        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://10.0.2.2:8080")
                .client(client)
                .build()
        return retrofit.create(Api::class.java)
    }
}