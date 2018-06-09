package com.mistreckless.support.wellcomeapp.app

import android.app.Activity
import android.app.Application
import com.arellomobile.mvp.RegisterMoxyReflectorPackages
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.jakewharton.picasso.OkHttp3Downloader
import com.mistreckless.support.wellcomeapp.data.DataModule
import com.mistreckless.support.wellcomeapp.data.RepositoryModule
import com.mistreckless.support.wellcomeapp.domain.InteractorModule
import com.mistreckless.support.wellcomeapp.navigation.NavigationModule
import com.squareup.picasso.Picasso
import com.wellcome.core.room.buildWellcomeDb
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import javax.inject.Inject

@RegisterMoxyReflectorPackages("com.wellcome.share", "com.wellcome.event")
class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        val db = buildWellcomeDb(this,"wellcomedb")
        val storyDao = db.storyDao()
        DaggerAppComponent.builder()
                .application(this)
                .dataModule(DataModule(storyDao))
                .repositoryModule(RepositoryModule())
                .interactorModule(InteractorModule())
                .navigationModule(NavigationModule())
                .build()
                .inject(this)
        initPicasso()
//        initFirebase()
    }

    override fun activityInjector() = activityDispatchAndroidInjector

    private fun initPicasso() {
        val REWRITE_CACHE_CONTROL_INTERCEPTOR = Interceptor { chain ->
            chain.proceed(chain.request())
                    .newBuilder()
                    .header("Cache-Control", String.format("max-age=%d, only-if-cached, max-stale=%d", 120, 0))
                    .build()
        }
        val okHttpClient = OkHttpClient.Builder()
                .cache(Cache(cacheDir, 50 * 1024 * 1024))
                .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .build()
        val okHttp3Downloader = OkHttp3Downloader(okHttpClient)
        val picasso = Picasso.Builder(this)
                .indicatorsEnabled(true)
                .downloader(okHttp3Downloader)
                .build()
        Picasso.setSingletonInstance(picasso)
    }

    private fun initFirebase(){
        val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false).build()
        FirebaseFirestore.getInstance().firestoreSettings = settings
    }

}