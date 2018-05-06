package com.mistreckless.support.wellcomeapp.navigation

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.mistreckless.support.wellcomeapp.ui.screen.profile.Profile
import com.wellcome.event.Wall
import com.wellcome.share.CameraActivity
import com.wellcome.utils.ui.Screen
import ru.terrakok.cicerone.android.SupportAppNavigator

class WelcomeNavigator(
    private val activity: FragmentActivity,
    manager: FragmentManager,
    containerId: Int
) : SupportAppNavigator(activity, manager, containerId) {

    init {
        if (containerId == 0) throw RuntimeException("Container id in activity doesn't exists")
    }

    override fun createActivityIntent(context: Context, screenKey: String, data: Any?): Intent? = when (screenKey) {
        Screen.CAMERA -> Intent(activity, CameraActivity::class.java)
        else -> null
    }

    override fun createFragment(screenKey: String, data: Any?): Fragment? = when (screenKey) {
        Screen.WALL -> Wall()
        Screen.PROFILE -> Profile()
        else -> null
    }
}