package com.wellcome.event.view

import android.util.Log
import com.wellcome.event.WallViewModel
import com.wellcome.ui.core.BaseRealTimePresenterProvider
import com.wellcome.ui.core.RealTimePresenter
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.cancelChildren
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import wellcome.common.event.EventInteractor
import wellcome.common.event.EventModified
import wellcome.common.event.EventState
import wellcome.common.event.UserModified
import wellcome.common.mpp.entity.Event
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.experimental.coroutineContext

class SingleEventPresenter(private val view: SingleEventView,
                           private val eventInteractor: EventInteractor,
                           private val wallViewModel: WallViewModel) : RealTimePresenter<Event> {
    private lateinit var event: Event
    private val jobs by lazy { mutableListOf<Job>() }

    override fun onViewBinded(item: Event) {
        event = item
        view.initUi(event)
        Log.e(event.data.ref, event.isLiked.toString())
        Log.e("TIME", Date(event.data.content.createTime).toString())
    }

    override fun viewAttached() {
        Log.e("view", "attached $this")

        launch {
            val job = Job()
            jobs.add(job)
            val producer = eventInteractor.controlEventChanges(event, coroutineContext, job)
            job.invokeOnCompletion {
                producer.cancel()
            }
            producer.consumeEach { state ->
                launch(UI) {
                    wallViewModel.putState(state)
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
        Click.LIKE     -> {
        }
        Click.COMMENT  -> {
        }
        Click.WILLCOME -> {
        }
        Click.ADDRESS  -> {
        }
    }


    private fun handleChanges(state: EventState) {
        when (state) {
            is EventModified -> view.updateUi(state.event)
            is UserModified  -> view.updateUser(state.userData)
        }
    }
}

class SingleEventPresenterProvider @Inject constructor(private val eventInteractor: EventInteractor,
                                                       private val wallViewModel: WallViewModel) :
    BaseRealTimePresenterProvider<SingleEventPresenter, SingleEventView> {
    override fun providePresenter(view: SingleEventView): SingleEventPresenter =
        SingleEventPresenter(view, eventInteractor, wallViewModel)
}