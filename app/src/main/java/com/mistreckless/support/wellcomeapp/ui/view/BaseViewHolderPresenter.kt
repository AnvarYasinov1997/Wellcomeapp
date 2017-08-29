package com.mistreckless.support.wellcomeapp.ui.view

import com.mistreckless.support.wellcomeapp.ui.BasePresenter

/**
 * Created by @mistreckless on 30.08.2017. !
 */
abstract class BaseViewHolderPresenter<V,R,M> : BasePresenter<V,R>() {
    override fun onFirstViewAttached() {}

    abstract fun onViewBinded(model : M)

    abstract fun onViewUnbinded()
}