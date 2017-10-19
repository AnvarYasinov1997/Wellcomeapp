package com.mistreckless.support.wellcomeapp.ui.screen.wall

import com.mistreckless.support.wellcomeapp.domain.interactor.WallInteractor
import com.mistreckless.support.wellcomeapp.ui.*
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by @mistreckless on 27.08.2017. !
 */

class WallPresenter(private val wallInteractor: WallInteractor) : BasePresenter<WallView, MainActivity>() {
    override fun onFirstViewAttached() {
        getView()?.initUi()
    }

    fun fubClicked() {
        getRouter()?.navigateToCamera()
    }

    companion object {
        const val TAG = "WallPresenter"
    }

    fun controlWall(observeScroll: Observable<Int>) {

    }
}

@PerFragment
class WallPresenterProviderFactory @Inject constructor(private val wallInteractor: WallInteractor) : BasePresenterProviderFactory<WallPresenter>{
    override fun get(): WallPresenter {
        if (presenterHolder.contains(WallPresenter.TAG))
            return presenterHolder[WallPresenter.TAG] as WallPresenter
        else{
            val presenter = WallPresenter(wallInteractor)
            presenterHolder.put(WallPresenter.TAG,presenter)
            return presenter
        }
    }

}