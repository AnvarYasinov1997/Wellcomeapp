package com.mistreckless.support.wellcomeapp.ui.view

import com.arellomobile.mvp.MvpView
import com.mistreckless.support.wellcomeapp.ui.BasePresenter

/**
 * Created by @mistreckless on 30.08.2017. !
 */
abstract class BaseViewHolderPresenter<V : MvpView,T> : BasePresenter<V>() {

    abstract fun onViewBinded(item : T)

    abstract fun onViewUnbinded()
}