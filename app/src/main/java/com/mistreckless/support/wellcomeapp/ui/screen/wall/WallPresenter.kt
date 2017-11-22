package com.mistreckless.support.wellcomeapp.ui.screen.wall

import com.arellomobile.mvp.InjectViewState
import com.mistreckless.support.wellcomeapp.domain.interactor.WallInteractor
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.PerFragment
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivity
import io.reactivex.Observable
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by @mistreckless on 27.08.2017. !
 */
@PerFragment
@InjectViewState
class WallPresenter @Inject constructor(private val wallInteractor: WallInteractor, private val router: Router) : BasePresenter<WallView>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initUi()
    }

    fun fubClicked() {
        router.newScreenChain(CameraActivity.TAG)
    }

    companion object {
        const val TAG = "WallPresenter"
    }

    fun controlWall(observeScroll: Observable<Int>) {
       // wallInteractor.controlWall(observeScroll)
    }
}
