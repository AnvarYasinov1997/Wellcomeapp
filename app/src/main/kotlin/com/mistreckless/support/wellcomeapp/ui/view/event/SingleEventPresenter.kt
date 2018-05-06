package com.mistreckless.support.wellcomeapp.ui.view.event

import android.util.Log
import com.mistreckless.support.wellcomeapp.data.rxfirebase.DocumentModified
import com.mistreckless.support.wellcomeapp.data.rxfirebase.DocumentState
import wellcome.common.entity.EventData
import com.mistreckless.support.wellcomeapp.ui.screen.wall.WallViewModel
import com.mistreckless.support.wellcomeapp.ui.view.RealTimePresenter
import com.mistreckless.support.wellcomeapp.ui.view.BaseRealTimePresenterProvider
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.cancelChildren
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import wellcome.common.entity.EventModified
import wellcome.common.entity.EventState
import wellcome.common.interactor.EventInteractor
import javax.inject.Inject

class SingleEventPresenter(
    private val view: SingleEventView,
    private val eventInteractor: EventInteractor,
    private val wallViewModel: WallViewModel
) : RealTimePresenter<EventData> {
    private lateinit var eventData: EventData
    private val jobs by lazy { mutableListOf<Job>() }

    override fun onViewBinded(item: EventData) {
        eventData = item
        view.initUi(eventData)
    }

    override fun viewAttached() {
        Log.e("view", "attached")
        launch {
            val job = Job()
            jobs.add(job)
            val producer = eventInteractor.controlEventChanges(eventData.ref,coroutineContext,job)
            launch(UI) {
                producer.consumeEach { state ->
                    wallViewModel.putDocument(state)
                    handleChanges(state)
                }
            }
            job.invokeOnCompletion {
                producer.cancel()
            }
        }
    }

    override fun viewDetached() {
        Log.e("view", "detached")
        jobs.forEach {
            it.cancelChildren()
            it.cancel()
        }
    }

    private fun handleChanges(state: EventState){
        when(state){
            is EventModified -> view.updateUi(state.event)
        }
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