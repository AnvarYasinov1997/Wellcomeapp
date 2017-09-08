package com.mistreckless.support.wellcomeapp.ui.view.drawer

import com.mistreckless.support.wellcomeapp.ui.MainActivityRouter
import com.mistreckless.support.wellcomeapp.ui.model.ListItem
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolderPresenter
import javax.inject.Inject

/**
 * Created by @mistreckless on 30.08.2017. !
 */
class DrawerListPresenter @Inject constructor() : BaseViewHolderPresenter<ListView,MainActivityRouter,ListItem>() {
    override fun onViewBinded(model: ListItem) {
        getView()?.initUi(model)
    }

    override fun onViewUnbinded() {

    }

    fun test() {
        getRouter()?.navigateToCamera()
    }
}