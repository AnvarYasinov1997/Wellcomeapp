package com.mistreckless.support.wellcomeapp.ui.view

import com.arellomobile.mvp.MvpView
import com.google.firebase.firestore.DocumentReference
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.BaseRouter

/**
 * Created by @mistreckless on 30.08.2017. !
 */
abstract class BaseViewHolderPresenter<V : MvpView, out R : BaseRouter> : BasePresenter<V,R>() {

    abstract fun onViewBinded(ref : DocumentReference)

    abstract fun onViewUnbinded()
}