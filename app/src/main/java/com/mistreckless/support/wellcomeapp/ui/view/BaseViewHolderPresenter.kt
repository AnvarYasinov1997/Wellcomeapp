package com.mistreckless.support.wellcomeapp.ui.view

import com.arellomobile.mvp.MvpView
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by @mistreckless on 30.08.2017. !
 */
abstract class BaseViewHolderPresenter<V,T> {

    val viewDisposable by lazy { CompositeDisposable() }

    abstract fun onViewBinded(item : T,view : V)

    open fun onViewUnbinded(){
        viewDisposable.clear()
    }
}