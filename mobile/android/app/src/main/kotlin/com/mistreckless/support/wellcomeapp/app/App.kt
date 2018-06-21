package com.mistreckless.support.wellcomeapp.app

import android.app.Activity
import android.app.Application
import android.support.v4.app.FragmentActivity
import com.arellomobile.mvp.RegisterMoxyReflectorPackages
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.jakewharton.picasso.OkHttp3Downloader
import com.mistreckless.support.wellcomeapp.navigation.NavigationModule
import com.mistreckless.support.wellcomeapp.navigation.ShareNavigator
import com.mistreckless.support.wellcomeapp.navigation.WelcomeNavigator
import com.squareup.picasso.Picasso
import com.wellcome.core.navigation.NavigatorProvider
import com.wellcome.core.room.buildWellcomeDb
import com.wellcomeapp.main.data.DataModule
import com.wellcomeapp.main.data.RepositoryModule
import com.wellcomeapp.main.domain.InteractorModule
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import javax.inject.Inject

@RegisterMoxyReflectorPackages("com.wellcome.share", "com.wellcome.event", "com.wellcomeapp.main")
class App : Application(), HasActivityInjector, NavigatorProvider {

    @Inject
    lateinit var activityDispatchAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        val db = buildWellcomeDb(this, "wellcomedb")
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

    private fun initFirebase() {
        val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false).build()
        FirebaseFirestore.getInstance().firestoreSettings = settings
    }

    override fun getMainNavigator(activity: FragmentActivity, containerId: Int) = WelcomeNavigator(activity, containerId)
    override fun getShareNavigator(activity: FragmentActivity, containerId: Int) = ShareNavigator(activity, containerId)
}