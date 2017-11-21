package com.mistreckless.support.wellcomeapp.ui.screen.camera.preview

import com.arellomobile.mvp.InjectViewState
import com.mistreckless.support.wellcomeapp.domain.interactor.ShareInteractor
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.PerFragment
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivityRouter
import javax.inject.Inject

/**
 * Created by @mistreckless on 10.09.2017. !
 */
@PerFragment
@InjectViewState
class PreviewPresenter @Inject constructor(private val shareInteractor: ShareInteractor) : BasePresenter<PreviewView, CameraActivityRouter>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initUi()
    }

    fun pictureTaken(bytes: ByteArray?) {
        if (bytes != null){
            shareInteractor.putPhotoBytes(bytes)
            getRouter()?.navigateToPictureSettings()
        }
    }


    companion object {
        const val TAG = "PreviewPresenter"
    }

}
