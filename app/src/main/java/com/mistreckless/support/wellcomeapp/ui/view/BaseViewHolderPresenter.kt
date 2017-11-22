package com.mistreckless.support.wellcomeapp.ui.view

import com.arellomobile.mvp.MvpView
import com.google.firebase.firestore.DocumentReference
import com.mistreckless.support.wellcomeapp.ui.BasePresenter

/**
 * Created by @mistreckless on 30.08.2017. !
 */
abstract class BaseViewHolderPresenter<V : MvpView> : BasePresenter<V>() {

    abstract fun onViewBinded(ref : DocumentReference)

    abstract fun onViewUnbinded()
}