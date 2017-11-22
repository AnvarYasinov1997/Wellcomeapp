package com.mistreckless.support.wellcomeapp.ui.view.post

import com.arellomobile.mvp.InjectViewState
import com.mistreckless.support.wellcomeapp.domain.entity.EventData
import com.mistreckless.support.wellcomeapp.domain.interactor.EventInteractor
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolderPresenter
import javax.inject.Inject

/**
 * Created by mistreckless on 19.10.17.
 */

@InjectViewState
class SingleEventPresenter @Inject constructor(private val eventInteractor: EventInteractor): BaseViewHolderPresenter<EventView,EventData>(){

    override fun onViewBinded(item : EventData) {
     ///   viewState.initUi(model)
    }

    override fun onViewUnbinded() {

    }

}