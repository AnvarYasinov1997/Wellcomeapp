package com.wellcome.core.navigation

import android.support.v4.app.FragmentActivity
import ru.terrakok.cicerone.android.SupportAppNavigator

interface NavigatorProvider {

    fun getMainNavigator(activity: FragmentActivity, containerId: Int): SupportAppNavigator
    fun getShareNavigator(activity: FragmentActivity, containerId: Int): SupportAppNavigator
}