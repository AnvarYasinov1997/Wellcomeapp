package com.wellcome.event

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.wellcome.utils.ui.BasePresenter
import com.wellcome.utils.ui.PerFragment
import com.wellcome.utils.ui.Screen
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.cancelChildren
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import ru.terrakok.cicerone.Router
import wellcome.common.entity.EventState
import wellcome.common.interactor.EventInteractor
import javax.inject.Inject

@PerFragment
@InjectViewState
class WallPresenter @Inject constructor(
    private val eventInteractor: EventInteractor,
    private val router: Router,
    private val viewModel: WallViewModel
) : BasePresenter<WallView>() {

    private val jobs by lazy { mutableListOf<Job>() }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initUi()
    }

    fun fubClicked() {
        router.newScreenChain(Screen.CAMERA)
    }

    fun controlWall(scrollChannel: ReceiveChannel<Int>) = launch {
        val paginatorJob = Job()
        val eventsPaginator = eventInteractor.controlPagination(scrollChannel, paginatorJob)
        jobs.add(paginatorJob)

        paginatorJob.invokeOnCompletion {
            eventsPaginator.cancel()
        }

        eventsPaginator.consumeEach { events ->
            val size = viewModel.items.size
            launch(UI) {
                viewModel.addItems(events)
            }
            if (size == 0) controlAddedEvents()
        }
    }

    private fun controlAddedEvents() = launch {
        var job = Job()
        var producer: ReceiveChannel<EventState>? = null

        var timestamp = -1L
        var firstTimestamp = viewModel.items.firstOrNull()?.timestamp ?: 0L

        job.invokeOnCompletion {
            producer?.cancel()
        }
        while (isActive) {
            if (timestamp < firstTimestamp) {
                timestamp = firstTimestamp
                job.cancelChildren()
                job.cancel()
                producer?.cancel()
                job = Job()
                jobs.add(job)
                producer = eventInteractor.controlAddedEvents(timestamp, coroutineContext, job)
                producer.consumeEach { event ->
                    Log.e("addedProducer", "works + $event")
                    launch(UI) {
                        viewModel.putDocument(event)
                        firstTimestamp = viewModel.items.first().timestamp
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach { it.cancelChildren(); it.cancel() }
    }
}
