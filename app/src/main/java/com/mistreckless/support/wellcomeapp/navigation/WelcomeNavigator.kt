package com.mistreckless.support.wellcomeapp.navigation

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.domain.entity.NewUserState
import com.mistreckless.support.wellcomeapp.ui.BaseActivity
import com.mistreckless.support.wellcomeapp.ui.MainActivity
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivity
import com.mistreckless.support.wellcomeapp.ui.screen.camera.picture.PictureSettings
import com.mistreckless.support.wellcomeapp.ui.screen.camera.preview.Preview
import com.mistreckless.support.wellcomeapp.ui.screen.camera.share.Share
import com.mistreckless.support.wellcomeapp.ui.screen.profile.Profile
import com.mistreckless.support.wellcomeapp.ui.screen.registry.Registry
import com.mistreckless.support.wellcomeapp.ui.screen.wall.Wall
import ru.terrakok.cicerone.android.SupportAppNavigator

/**
 * Created by mistreckless on 21.11.17.
 */
class WelcomeNavigator(private val activity: FragmentActivity, manager: FragmentManager, containerId: Int) : SupportAppNavigator(activity, manager, containerId) {

    init {
        if (containerId==0) throw RuntimeException("Container id in activity doesn't exists")
    }

    override fun createActivityIntent(screenKey: String, data: Any?): Intent? = when (screenKey) {
        CameraActivity.TAG -> Intent(activity, CameraActivity::class.java)
        MainActivity.TAG -> Intent(activity, MainActivity::class.java)
        BaseActivity.GOOGLE_AUTH_ACTIVITY_TAG -> Auth.GoogleSignInApi.getSignInIntent(provideGoogleApiClient())
        else -> null
    }

    override fun createFragment(screenKey: String, data: Any?): Fragment? = when (screenKey) {
        Wall.TAG -> Wall()
        Profile.TAG -> Profile()
        Registry.TAG -> {

            if (data != null && data is NewUserState) Registry.newInstance(data)
            else throw RuntimeException("error opening registry data is null or not newUserState " + data)
        }
        Preview.TAG -> Preview()
        PictureSettings.TAG -> PictureSettings()
        Share.TAG -> Share()
        else -> null
    }

    private fun provideGoogleApiClient(): GoogleApiClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.web_client_id))
                .requestEmail()
                .build()
        return GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, { connectionResult -> Log.e(MainActivity.TAG, connectionResult.errorMessage) })
                .addApi(com.google.android.gms.auth.api.Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }
}