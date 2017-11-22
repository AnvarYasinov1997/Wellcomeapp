package com.mistreckless.support.wellcomeapp.ui.view.post

import com.arellomobile.mvp.InjectViewState
import com.google.firebase.firestore.DocumentReference
import com.mistreckless.support.wellcomeapp.domain.interactor.WallInteractor
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolderPresenter
import javax.inject.Inject

/**
 * Created by mistreckless on 19.10.17.
 */

@InjectViewState
class PostPresenter @Inject constructor(private val wallInteractor : WallInteractor): BaseViewHolderPresenter<PostView>(){

    override fun onViewBinded(ref : DocumentReference) {
     ///   viewState.initUi(model)
    }

    override fun onViewUnbinded() {

    }

}