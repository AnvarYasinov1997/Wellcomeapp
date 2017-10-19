package com.mistreckless.support.wellcomeapp.ui.view.drawer

import android.view.ViewGroup
import com.mistreckless.support.wellcomeapp.ui.BaseRouter
import com.mistreckless.support.wellcomeapp.ui.MainActivity
import javax.inject.Inject

/**
 * Created by @mistreckless on 30.08.2017. !
 */

class HeaderViewFactory @Inject constructor(private val drawerHeaderPresenter: DrawerHeaderPresenter,private val baseRouter: BaseRouter) {
    fun create(parent: ViewGroup?) = HeaderViewHolder(drawerHeaderPresenter, parent, baseRouter)
}
class ListViewFactory @Inject constructor(private val listPresenter: DrawerListPresenter, private val baseRouter: BaseRouter) {
    fun create(parent: ViewGroup?) = ListViewHolder(listPresenter, parent, baseRouter)
}