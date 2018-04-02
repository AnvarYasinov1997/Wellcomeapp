package com.mistreckless.support.wellcomeapp.ui.view.event

import android.util.Log
import com.mistreckless.support.wellcomeapp.data.rxfirebase.DocumentState
import com.mistreckless.support.wellcomeapp.domain.entity.EventData
import com.mistreckless.support.wellcomeapp.domain.interactor.EventInteractor
import com.mistreckless.support.wellcomeapp.ui.screen.wall.WallViewModel
import com.mistreckless.support.wellcomeapp.ui.view.BaseRealTimePresenter
import com.mistreckless.support.wellcomeapp.ui.view.BaseRealTimePresenterProvider
import io.reactivex.subjects.Subject
import javax.inject.Inject

class SingleEventPresenter(
    private val view: SingleEventView,
    private val eventInteractor: EventInteractor,
    private val wallViewModel: WallViewModel
) : BaseRealTimePresenter<EventData>() {
    private lateinit var eventData: EventData

    override fun onViewBinded(item: EventData) {
        eventData = item
    }

    override fun viewAttached() {
        Log.e("attached", eventData.contents.first().desc)
        viewDisposable.add(
            eventInteractor.controlEventChanges(eventData.ref)
                .doOnNext(wallViewModel::putDocument)
                .subscribe()
        )
    }

    override fun viewDetached() {
        super.viewDetached()
        Log.e("view", "detached")
    }

}

class SingleEventPresenterProvider @Inject constructor(
    private val eventInteractor: EventInteractor,
    private val wallViewModel: WallViewModel
) :
    BaseRealTimePresenterProvider<SingleEventPresenter, SingleEventView> {
    override fun providePresenter(view: SingleEventView): SingleEventPresenter =
        SingleEventPresenter(view, eventInteractor, wallViewModel)
}