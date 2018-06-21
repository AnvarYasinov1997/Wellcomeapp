package com.mistreckless.support.wellcomeapp.navigation

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.wellcome.core.ui.Screen
import com.wellcome.event.Wall
import com.wellcome.share.CameraActivity
import com.wellcome.share.picture.PictureSettings
import com.wellcome.share.preview.Preview
import com.wellcome.share.share.Share
import ru.terrakok.cicerone.android.SupportAppNavigator

class WelcomeNavigator(
    private val activity: FragmentActivity,
    containerId: Int
) : SupportAppNavigator(activity, containerId) {

    init {
        if (containerId == 0) throw RuntimeException("Container id in activity doesn't exists")
    }

    override fun createActivityIntent(context: Context, screenKey: String, data: Any?): Intent? = when (screenKey) {
        Screen.CAMERA -> Intent(activity, CameraActivity::class.java)
        else -> null
    }

    override fun createFragment(screenKey: String, data: Any?): Fragment? = when (screenKey) {
        Screen.WALL -> Wall()
//        Screen.PROFILE -> Profile()
        else -> null
    }
}

class ShareNavigator(activity: FragmentActivity,
                     containerId: Int) : SupportAppNavigator(activity, containerId) {
    override fun createActivityIntent(context: Context?, screenKey: String?, data: Any?): Intent? = null

    override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
        Screen.PREVIEW -> Preview()
        Screen.PICTURE_SETTINGS -> PictureSettings()
        Screen.SHARE -> Share()
        else -> null
    }

}