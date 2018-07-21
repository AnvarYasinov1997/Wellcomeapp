package com.wellcome.event

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.wellcome.core.navigation.Screen
import com.wellcome.ui.core.BasePresenter
import com.wellcome.ui.core.PerFragment
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.cancelChildren
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import ru.terrakok.cicerone.Router
import wellcome.common.event.EventAdded
import wellcome.common.event.EventInteractor
import wellcome.common.event.EventState
import wellcome.common.event.EventsPaginated
import javax.inject.Inject
import kotlin.coroutines.experimental.coroutineContext

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

        eventsPaginator.consumeEach { state ->
            val size = viewModel.items.size
            launch(UI) {
                when (state) {
                    is EventsPaginated -> {
                        viewModel.putState(state)
                        if (size == 0) controlAddedEvents()
                    }
                    else               -> Log.e("unexpected state", state.toString())
                }
            }
        }
    }

    private fun controlAddedEvents() = launch {
        var job = Job()
        var producer: ReceiveChannel<EventState>? = null

        var timestamp = -1L
        var firstTimestamp = viewModel.items.firstOrNull()?.data?.timestamp ?: 0L

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
                producer.consumeEach { state ->
                    when (state) {
                        is EventAdded -> {
                            Log.e("addedProducer", "works + $state")
                            launch(UI) {
                                viewModel.putState(state)
                                firstTimestamp = viewModel.items.first().data.timestamp
                            }
                        }
                        else          -> Log.e("unexpected state", state.toString())
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
