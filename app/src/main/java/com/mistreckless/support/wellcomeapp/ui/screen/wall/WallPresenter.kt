package com.mistreckless.support.wellcomeapp.ui.screen.wall

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.mistreckless.support.wellcomeapp.domain.interactor.EventInteractor
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
class WallPresenter @Inject constructor(private val eventInteractor: EventInteractor, private val router: Router) : BasePresenter<WallView>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initUi()
    }

    fun fubClicked() {
        router.newScreenChain(CameraActivity.TAG)
    }


    fun controlWall(observeScroll: Observable<Int>) {
       viewChangesDisposables.add(eventInteractor.controlEvents(observeScroll)
               .subscribe({
                   viewState.addEvents(it)
               },{
                   Log.e(TAG,it.message,it)
               }))
    }

    companion object {
        const val TAG = "WallPresenter"
    }
}
