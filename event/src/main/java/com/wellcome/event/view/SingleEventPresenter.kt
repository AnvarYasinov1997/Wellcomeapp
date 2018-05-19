package com.wellcome.event.view

import android.util.Log
import com.wellcome.core.ui.BaseRealTimePresenterProvider
import com.wellcome.core.ui.RealTimePresenter
import com.wellcome.event.WallViewModel
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.cancelChildren
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import wellcome.common.entity.Event
import wellcome.common.entity.EventModified
import wellcome.common.entity.EventState
import wellcome.common.interactor.EventInteractor
import javax.inject.Inject

class SingleEventPresenter(
    private val view: SingleEventView,
    private val eventInteractor: EventInteractor,
    private val wallViewModel: WallViewModel
) : RealTimePresenter<Event> {
    private lateinit var event: Event
    private val jobs by lazy { mutableListOf<Job>() }

    override fun onViewBinded(item: Event) {
        event = item
        view.initUi(event)
        Log.e(event.data.ref, event.isLiked.toString())
    }

    override fun viewAttached() {
        Log.e("view", "attached $this")
        launch {
            val job = Job()
            jobs.add(job)
            val producer =
                eventInteractor.controlEventChanges(event.data.ref, coroutineContext, job)
            job.invokeOnCompletion {
                producer.cancel()
            }
            producer.consumeEach { state ->
                launch(UI) {
                    wallViewModel.putDocument(state)
                    handleChanges(state)
                }
            }
        }
    }

    override fun viewDetached() {
        Log.e("view", "detached $this")
        jobs.forEach {
            it.cancelChildren()
            it.cancel()
        }
        jobs.clear()
    }

    fun clicked(click: Click) = when (click) {
        Click.LIKE -> {
            launch(UI) {
                eventInteractor.like(event.data.ref, event.isLiked).join()
                event = event.copy(isLiked = !event.isLiked)
                wallViewModel.putDocument(EventModified(event))
                view.animateLike(event.isLiked)
                Log.e("event", event.isLiked.toString())
            }
            Unit
        }
        Click.COMMENT -> {
        }
        Click.WILLCOME -> {
        }
        Click.ADDRESS -> {
        }
    }


    private fun handleChanges(state: EventState) {
        when (state) {
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