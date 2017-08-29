package com.mistreckless.support.wellcomeapp.ui.view.drawer

import android.view.ViewGroup
import com.mistreckless.support.wellcomeapp.ui.MainActivity
import javax.inject.Inject

/**
 * Created by @mistreckless on 30.08.2017. !
 */

class HeaderViewFactory @Inject constructor(val drawerHeaderPresenter: DrawerHeaderPresenter, val mainActivity: MainActivity) {
    fun create(parent: ViewGroup?) = HeaderViewHolder(drawerHeaderPresenter, parent, mainActivity)
}
class ListViewFactory @Inject constructor(val listPresenter: DrawerListPresenter, val mainActivity: MainActivity) {
    fun create(parent: ViewGroup?) = ListViewHolder(listPresenter, parent, mainActivity)
}