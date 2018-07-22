package com.wellcome.ui.core

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView

@Suppress("UNCHECKED_CAST")
abstract class BasePresenter<V : MvpView> : MvpPresenter<V>() {

    override fun destroyView(view: V) {
        onViewDestroyed()
        super.destroyView(view)
    }

    open fun onViewDestroyed(){
    }

}