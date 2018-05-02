package com.mistreckless.support.wellcomeapp.ui.screen.wall

import com.arellomobile.mvp.InjectViewState
import com.mistreckless.support.wellcomeapp.domain.interactor.EventInteractor
import com.wellcome.share.CameraActivity
import com.wellcome.utils.ui.BasePresenter
import com.wellcome.utils.ui.PerFragment
import io.reactivex.Observable
import ru.terrakok.cicerone.Router
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

    fun controlWall(observeScroll: Observable<Int>) {
        viewChangesDisposables.add(
            eventInteractor.controlEvents(observeScroll)
                .doOnNext(viewModel::addItems)
                .map { viewModel.items.firstOrNull()?.timestamp ?: 0 }
                .distinctUntilChanged()
                .switchMap(eventInteractor::controlEventsChanges)
                .doOnNext(viewModel::putDocument)
                .subscribe())
    }

    companion object {
        const val TAG = "WallPresenter"
    }
}
