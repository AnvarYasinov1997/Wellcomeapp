package com.wellcome.share

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.wellcome.share.picture.PictureSettings
import com.wellcome.share.preview.Preview
import com.wellcome.share.share.Share
import com.wellcome.utils.ui.Screen
import ru.terrakok.cicerone.android.SupportAppNavigator

class ShareNavigator(private val activity: FragmentActivity,
                     manager: FragmentManager,
                     containerId: Int): SupportAppNavigator(activity, containerId){
    override fun createActivityIntent(context: Context?, screenKey: String?, data: Any?): Intent? = null

    override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
        Screen.PREVIEW -> Preview()
        Screen.PICTURE_SETTINGS -> PictureSettings()
        Screen.SHARE -> Share()
        else -> null
    }

}