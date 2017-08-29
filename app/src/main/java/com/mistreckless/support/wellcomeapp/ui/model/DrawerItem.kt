package com.mistreckless.support.wellcomeapp.ui.model

import android.support.annotation.IntegerRes

/**
 * Created by @mistreckless on 28.08.2017. !
 */

sealed class DrawerItem

data class ListItem(val title : String, @IntegerRes val resId : Int) : DrawerItem()

class HeaderItem(): DrawerItem()