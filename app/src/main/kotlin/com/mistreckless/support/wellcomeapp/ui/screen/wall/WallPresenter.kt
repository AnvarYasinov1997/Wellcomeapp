package com.mistreckless.support.wellcomeapp.ui.screen.wall

import com.arellomobile.mvp.InjectViewState
import com.wellcome.share.CameraActivity
import com.wellcome.utils.ui.BasePresenter
import com.wellcome.utils.ui.PerFragment
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.cancelAndJoin
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
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initUi()
    }

    fun fubClicked() {
        router.newScreenChain(CameraActivity.TAG)
    }

    fun controlWall(scrollChannel: ReceiveChannel<Int>) = launch {
        val paginatorJob = Job()
        val addedJob = Job()
        var timestamp = -1L
        eventInteractor.controlPagination(scrollChannel,paginatorJob).consumeEach { launch(UI) {   viewModel.addItems(it) } }
        val eventsPaginator = eventInteractor.controlPagination(scrollChannel, paginatorJob)
        var addedProducer: ReceiveChannel<EventState>? = null

        fun controlAddedEvents(addedJob: Job) = launch(coroutineContext) {
            val firstTimestamp = viewModel.items.firstOrNull()?.timestamp ?: 0L
            if (timestamp < firstTimestamp) {
                timestamp = firstTimestamp
                addedJob.cancelAndJoin()
                addedProducer?.cancel()
                addedJob.start()
                addedProducer =
                        eventInteractor.controlAddedEvents(timestamp, coroutineContext, addedJob)
                addedProducer?.consumeEach { event ->
                    viewModel.putDocument(event)
                }
            }
        }

        eventsPaginator.consumeEach { events ->
            viewModel.addItems(events)
            controlAddedEvents(addedJob)
        }

        paginatorJob.invokeOnCompletion {
            eventsPaginator.cancel()
            addedProducer?.cancel()
        }
    }


    companion object {
        const val TAG = "WallPresenter"
    }
}
