package com.mistreckless.support.wellcomeapp.ui.screen.wall

import com.arellomobile.mvp.InjectViewState
import com.mistreckless.support.wellcomeapp.domain.interactor.WallInteractor
import com.mistreckless.support.wellcomeapp.ui.*
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by @mistreckless on 27.08.2017. !
 */
@PerFragment
@InjectViewState
class WallPresenter @Inject constructor(private val wallInteractor: WallInteractor) : BasePresenter<WallView, MainActivity>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initUi()
    }

    fun fubClicked() {
        getRouter()?.navigateToCamera()
    }

    companion object {
        const val TAG = "WallPresenter"
    }

    fun controlWall(observeScroll: Observable<Int>) {
       // wallInteractor.controlWall(observeScroll)
    }
}
