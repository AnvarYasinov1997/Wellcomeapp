package com.mistreckless.support.wellcomeapp.ui.screen.camera.picture

import com.arellomobile.mvp.InjectViewState
import com.mistreckless.support.wellcomeapp.domain.interactor.ShareInteractor
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.PerFragment
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivityRouter
import javax.inject.Inject

/**
 * Created by @mistreckless on 08.10.2017. !
 */
@PerFragment
@InjectViewState
class PictureSettingsPresenter @Inject constructor(private val shareInteractor: ShareInteractor) : BasePresenter<PictureSettingsView,CameraActivityRouter>(){

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initUi(shareInteractor.getPhotoBytes())
    }

    companion object {
        const val TAG="PictureSettingsPresenter"
    }

    fun nextClicked() {
        getRouter()?.navigateToShare()
    }
}
