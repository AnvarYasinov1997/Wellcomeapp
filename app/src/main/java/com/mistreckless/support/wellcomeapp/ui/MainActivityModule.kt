package com.mistreckless.support.wellcomeapp.ui

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.screen.drawer.Drawer
import com.mistreckless.support.wellcomeapp.ui.screen.registry.Registry
import com.mistreckless.support.wellcomeapp.ui.screen.wall.Wall
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
 * Created by @mistreckless on 05.08.2017. !
 */

@Module
abstract class MainActivityFragmentProvider {

    @ContributesAndroidInjector
    @PerFragment
    abstract fun provideRegistryFactory(): Registry

    @ContributesAndroidInjector
    @PerFragment
    abstract fun provideWallFactory() : Wall

    @ContributesAndroidInjector
    @PerFragment
    abstract fun provideDrawer() : Drawer
}

@Module
class MainActivityModule {

    @Provides
    fun provideGoogleApiClient(mainActivity: MainActivity): GoogleApiClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mainActivity.getString(R.string.web_client_id))
                .requestEmail()
                .build()
       return GoogleApiClient.Builder(mainActivity)
                .enableAutoManage(mainActivity, { connectionResult -> Log.e(MainActivity.TAG, connectionResult.errorMessage) })
                .addApi(com.google.android.gms.auth.api.Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    @Provides
    fun provideRxPermissions(mainActivity: MainActivity) : RxPermissions = RxPermissions(mainActivity)
}