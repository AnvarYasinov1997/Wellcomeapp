package com.mistreckless.support.wellcomeapp.navigation

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
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
class WelcomeNavigator(private val activity: FragmentActivity, private val manager: FragmentManager, containerId: Int) : SupportAppNavigator(activity, manager, containerId) {

    override fun createActivityIntent(screenKey: String, data: Any?): Intent? = when (screenKey) {
        CameraActivity.TAG -> Intent(activity, CameraActivity::class.java)
        MainActivity.TAG -> Intent(activity, MainActivity::class.java)
        BaseActivity.GOOGLE_AUTH_ACTIVITY_TAG-> {
            if (data != null && data is GoogleApiClient){
                 Auth.GoogleSignInApi.getSignInIntent(data)
              //  activity.startActivityForResult(intent,BaseActivity.RC_SIGN_IN)
            }else throw RuntimeException("error opening google auth "+data)
        }
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
}