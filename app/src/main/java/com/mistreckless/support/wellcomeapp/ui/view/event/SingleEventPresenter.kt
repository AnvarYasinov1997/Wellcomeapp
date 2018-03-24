package com.mistreckless.support.wellcomeapp.ui.view.event

import android.util.Log
import com.mistreckless.support.wellcomeapp.domain.entity.EventData
import com.mistreckless.support.wellcomeapp.ui.view.BaseRealTimePresenter
import com.mistreckless.support.wellcomeapp.ui.view.BaseRealTimePresenterProvider
import javax.inject.Inject

class SingleEventPresenter(private val view: SingleEventView) : BaseRealTimePresenter<EventData>() {
    override fun onViewBinded(item: EventData) {
        Log.e("binded", item.ref)
    }

    override fun viewAttached() {
        Log.e("view", "attached")
        view.test()
    }

    override fun viewDetached() {
        super.viewDetached()
        Log.e("view", "detached")
    }
}

class SingleEventPresenterProvider @Inject constructor() :
    BaseRealTimePresenterProvider<SingleEventPresenter, SingleEventView> {
    override fun providePresenter(view: SingleEventView): SingleEventPresenter =
        SingleEventPresenter(view)
}