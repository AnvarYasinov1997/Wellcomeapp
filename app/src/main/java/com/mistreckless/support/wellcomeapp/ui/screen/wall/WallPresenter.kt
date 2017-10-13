package com.mistreckless.support.wellcomeapp.ui.screen.wall

import com.mistreckless.support.wellcomeapp.ui.*
import javax.inject.Inject

/**
 * Created by @mistreckless on 27.08.2017. !
 */

class WallPresenter : BasePresenter<WallView, MainActivity>() {
    override fun onFirstViewAttached() {
        getView()?.initUi()
    }

    fun fubClicked() {
        getRouter()?.navigateToCamera()
    }

    companion object {
        const val TAG = "WallPresenter"
    }
}

@PerFragment
class WallPresenterProviderFactory @Inject constructor() : BasePresenterProviderFactory<WallPresenter>{
    override fun get(): WallPresenter {
        if (presenterHolder.contains(WallPresenter.TAG))
            return presenterHolder[WallPresenter.TAG] as WallPresenter
        else{
            val presenter = WallPresenter()
            presenterHolder.put(WallPresenter.TAG,presenter)
            return presenter
        }
    }

}