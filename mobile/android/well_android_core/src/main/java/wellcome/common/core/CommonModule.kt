package wellcome.common.core

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import wellcome.common.core.custom.CoroutineCallAdapterFactory
import wellcome.common.core.custom.TokenInterceptor
import wellcome.common.core.service.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class CommonModule(private val storyDao: StoryDao) {

    @Singleton
    @Provides
    fun provideCache(context: Context): Cache =
        Cache(context.getSharedPreferences("user_data", Context.MODE_PRIVATE))

    @Singleton
    @Provides
    fun provideStoryService(cache: Cache): StoryService =
        StoryServiceImpl(cache, storyDao, StoryProvider())

    @Singleton
    @Provides
    fun provideLocationService(context: Context, locale: Locale, cache: Cache): LocationService =
        LocationServiceImpl(LocationProvider(LocationServices.getFusedLocationProviderClient(context),
            Geocoder(context, locale)), cache)

    @Singleton
    @Provides
    fun provideApiService(cache: Cache): ApiService {
        val builder = OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(TokenInterceptor(cache))
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        val client = builder.build()
        val retrofit = Retrofit.Builder().addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create()).baseUrl("http://10.0.2.2:8080")
                .client(client).build()
        return retrofit.create(ApiService::class.java)
    }

}