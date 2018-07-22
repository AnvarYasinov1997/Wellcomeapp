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
import wellcome.common.event.EventsPaginated
import javax.inject.Inject

@PerFragment
@InjectViewState
class WallPresenter @Inject constructor(private val eventInteractor: EventInteractor,
                                        private val router: Router,
                                        private val viewModel: WallViewModel) :
    BasePresenter<WallView>() {

    private val jobs by lazy { mutableListOf<Job>() }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initUi()
    }

    fun fubClicked() {
        router.newScreenChain(Screen.CAMERA)
    }

    fun controlWall(scrollChannel: ReceiveChannel<Int>) = launch {
        val job = Job()
        jobs.add(job)
        val eventsController = eventInteractor.controlEvents(job, scrollChannel)
        job.invokeOnCompletion {
            eventsController.cancel()
        }
        eventsController.consumeEach { state ->
            launch(UI) {
                when (state) {
                    is EventsPaginated -> {
                        Log.e("paginated", "size ${state.events.size}")
                        viewModel.putState(state)
                    }
                    is EventAdded      -> {
                        Log.e("event added", "event ${state.event}")
                        viewModel.putState(state)
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
