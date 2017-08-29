package com.mistreckless.support.wellcomeapp.ui.screen

import android.support.annotation.LayoutRes

/**
 * Created by @mistreckless on 31.07.2017. !
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Layout(@LayoutRes val id : Int)