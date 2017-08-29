package com.mistreckless.support.wellcomeapp.ui

import android.os.Bundle

/**
 * Created by @mistreckless on 30.07.2017. !
 */
@Suppress("UNCHECKED_CAST")
abstract class BasePresenter<V, R> {
    private var router: R? = null
    private var view: V? = null

    fun attachRouter(router: Any) {
        this.router = router as R
    }

    fun detachRouter() {
        router = null
    }

    fun getRouter() = router

    fun attachView(view: Any) {
        this.view = view as V
    }

    fun detachView() {
        this.view = null
    }

    fun getView() = view

    abstract fun onFirstViewAttached()

    fun onViewRestored(saveInstanceState: Bundle) {}

}