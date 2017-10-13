package com.mistreckless.support.wellcomeapp.ui.screen.camera.preview

import android.os.Bundle
import com.mistreckless.support.wellcomeapp.domain.interactor.ShareInteractor
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.BasePresenterProviderFactory
import com.mistreckless.support.wellcomeapp.ui.presenterHolder
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivityRouter
import javax.inject.Inject

/**
 * Created by @mistreckless on 10.09.2017. !
 */
class PreviewPresenter(private val shareInteractor: ShareInteractor) : BasePresenter<PreviewView, CameraActivityRouter>() {
    override fun onFirstViewAttached() {
        getView()?.initUi()
    }

    override fun onViewRestored(saveInstanceState: Bundle) {
        getView()?.initUi()
    }


    companion object {
        const val TAG = "PreviewPresenter"
    }

    fun pictureTaken(bytes: ByteArray?) {
        if (bytes != null){
            shareInteractor.putPhotoBytes(bytes)
            getRouter()?.navigateToPictureSettings()
        }
    }
}

class PreviewPresenterProviderFactory @Inject constructor(private val shareInteractor: ShareInteractor) : BasePresenterProviderFactory<PreviewPresenter> {
    override fun get(): PreviewPresenter {
        return if (presenterHolder.containsKey(PreviewPresenter.TAG))
            presenterHolder[PreviewPresenter.TAG] as PreviewPresenter
        else {
            val presenter = PreviewPresenter(shareInteractor)
            presenterHolder.put(PreviewPresenter.TAG, presenter)
            presenter
        }
    }

}