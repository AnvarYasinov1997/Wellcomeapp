package com.mistreckless.support.wellcomeapp.ui.view.post

import com.mistreckless.support.wellcomeapp.domain.entity.PostData
import com.mistreckless.support.wellcomeapp.domain.interactor.WallInteractor
import com.mistreckless.support.wellcomeapp.ui.MainActivityRouter
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolderPresenter
import javax.inject.Inject

/**
 * Created by mistreckless on 19.10.17.
 */
class PostPresenter @Inject constructor(private val wallInteractor : WallInteractor): BaseViewHolderPresenter<PostView,MainActivityRouter,PostData>(){

    override fun onViewBinded(model: PostData) {
        getView()?.initUi(model)
    }

    override fun onViewUnbinded() {

    }

}