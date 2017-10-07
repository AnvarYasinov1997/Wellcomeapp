package com.mistreckless.support.wellcomeapp.ui.screen.camera.preview

import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.BasePresenterProviderFactory
import com.mistreckless.support.wellcomeapp.ui.presenterHolder
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivityRouter
import javax.inject.Inject

/**
 * Created by @mistreckless on 10.09.2017. !
 */
class PreviewPresenter : BasePresenter<PreviewView,CameraActivityRouter>() {
    override fun onFirstViewAttached() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        const val TAG = "PreviewPresenter"
    }
}

class PreviewPresenterProviderFactory @Inject constructor() : BasePresenterProviderFactory<PreviewPresenter>{
    override fun get(): PreviewPresenter {
        if (presenterHolder.containsKey(PreviewPresenter.TAG))
            return presenterHolder[PreviewPresenter.TAG] as PreviewPresenter
        else{
            val presenter = PreviewPresenter()
            presenterHolder.put(PreviewPresenter.TAG,presenter)
            return presenter
        }
    }

}