package com.mistreckless.support.wellcomeapp.ui.screen.camera

import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.BasePresenterProviderFactory
import com.mistreckless.support.wellcomeapp.ui.presenterHolder
import javax.inject.Inject

/**
 * Created by @mistreckless on 03.09.2017. !
 */
class CameraPresenter : BasePresenter<CameraActivityView, CameraActivityRouter>() {
    override fun onFirstViewAttached() {
        getRouter()?.navigateToPreview()
    }

    companion object {
        const val TAG = "CameraPresenter"
    }
}

class CameraPresenterProviderFactory @Inject constructor(): BasePresenterProviderFactory<CameraPresenter>{
    override fun get(): CameraPresenter {
        if (presenterHolder.containsKey(CameraPresenter.TAG))
            return presenterHolder[CameraPresenter.TAG] as CameraPresenter
        else{
            val presenter = CameraPresenter()
            presenterHolder.put(CameraPresenter.TAG,presenter)
            return presenter
        }
    }

}