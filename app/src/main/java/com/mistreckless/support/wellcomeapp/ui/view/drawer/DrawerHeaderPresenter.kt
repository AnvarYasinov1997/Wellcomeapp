package com.mistreckless.support.wellcomeapp.ui.view.drawer

import com.mistreckless.support.wellcomeapp.domain.interactor.MainInteractor
import com.mistreckless.support.wellcomeapp.ui.MainActivityRouter
import com.mistreckless.support.wellcomeapp.ui.model.HeaderItem
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolderPresenter
import javax.inject.Inject

/**
 * Created by @mistreckless on 30.08.2017. !
 */

class DrawerHeaderPresenter @Inject constructor(private val mainInteractor: MainInteractor): BaseViewHolderPresenter<HeaderView,MainActivityRouter, HeaderItem>() {
    override fun onViewBinded(model: HeaderItem) {

    }

    override fun onViewUnbinded() {
    }

}