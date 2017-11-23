package com.mistreckless.support.wellcomeapp.ui.view.event

import com.arellomobile.mvp.InjectViewState
import com.mistreckless.support.wellcomeapp.domain.entity.EventData
import com.mistreckless.support.wellcomeapp.domain.interactor.EventInteractor
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolderPresenter
import javax.inject.Inject

/**
 * Created by mistreckless on 19.10.17.
 */

class SingleEventPresenter @Inject constructor(private val eventInteractor: EventInteractor): BaseViewHolderPresenter<SingleEventView,EventData>(){

    override fun onViewBinded(item : EventData,view : SingleEventView) {
        view.initUi(item)
    }

    override fun onViewUnbinded() {

    }

}