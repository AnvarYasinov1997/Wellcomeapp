package com.mistreckless.support.wellcomeapp.ui

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by @mistreckless on 30.07.2017. !
 */
@Suppress("UNCHECKED_CAST")
abstract class BasePresenter<V : MvpView> : MvpPresenter<V>() {
    protected val viewChangesDisposables by lazy { CompositeDisposable() }

    override fun destroyView(view: V) {
        onViewDestroyed()
        super.destroyView(view)
    }

    open fun onViewDestroyed(){
        viewChangesDisposables.clear()
    }

}