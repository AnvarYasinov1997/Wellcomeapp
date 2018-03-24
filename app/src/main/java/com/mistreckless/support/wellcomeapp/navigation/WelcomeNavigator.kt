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
import com.mistreckless.support.wellcomeapp.ui.BaseActivity
import com.mistreckless.support.wellcomeapp.ui.MainActivity
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivity
import com.mistreckless.support.wellcomeapp.ui.screen.camera.picture.PictureSettings
import com.mistreckless.support.wellcomeapp.ui.screen.camera.preview.Preview
import com.mistreckless.support.wellcomeapp.ui.screen.camera.share.Share
import com.mistreckless.support.wellcomeapp.ui.screen.profile.Profile
import com.mistreckless.support.wellcomeapp.ui.screen.wall.Wall
import ru.terrakok.cicerone.android.SupportAppNavigator

class WelcomeNavigator(
    private val activity: FragmentActivity,
    manager: FragmentManager,
    containerId: Int
) : SupportAppNavigator(activity, manager, containerId) {

    init {
        if (containerId == 0) throw RuntimeException("Container id in activity doesn't exists")
    }

    override fun createActivityIntent(screenKey: String, data: Any?): Intent? = when (screenKey) {
        CameraActivity.TAG -> Intent(activity, CameraActivity::class.java)
        MainActivity.TAG -> Intent(activity, MainActivity::class.java)
        else -> null
    }

    override fun createFragment(screenKey: String, data: Any?): Fragment? = when (screenKey) {
        Wall.TAG -> Wall()
        Profile.TAG -> Profile()
        Preview.TAG -> Preview()
        PictureSettings.TAG -> PictureSettings()
        Share.TAG -> Share()
        else -> null
    }
}