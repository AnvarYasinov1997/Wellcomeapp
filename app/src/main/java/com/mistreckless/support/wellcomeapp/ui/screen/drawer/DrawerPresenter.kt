package com.mistreckless.support.wellcomeapp.ui.screen.drawer

import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.MainActivity
import com.mistreckless.support.wellcomeapp.ui.PerFragment
import com.mistreckless.support.wellcomeapp.ui.model.HeaderItem
import com.mistreckless.support.wellcomeapp.ui.model.ListItem
import javax.inject.Inject

/**
 * Created by @mistreckless on 27.08.2017. !
 */
@PerFragment
class DrawerPresenter @Inject constructor(): BasePresenter<DrawerView, MainActivity>() {
    override fun onFirstViewAttached() {
        val drawerItems = List(2,{
            if (it==0) HeaderItem()
            else ListItem("Wall", android.R.drawable.ic_menu_send)
        })
        getView()?.initUi(drawerItems)
    }
}