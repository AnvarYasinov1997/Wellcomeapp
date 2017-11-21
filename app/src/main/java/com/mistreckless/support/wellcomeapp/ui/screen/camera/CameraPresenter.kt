package com.mistreckless.support.wellcomeapp.ui.screen.camera

import com.arellomobile.mvp.InjectViewState
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.PerActivity
import javax.inject.Inject

/**
 * Created by @mistreckless on 03.09.2017. !
 */

@PerActivity
@InjectViewState
class CameraPresenter  @Inject constructor(): BasePresenter<CameraActivityView, CameraActivityRouter>() {

    override fun onFirstViewAttach() {
        getRouter()?.navigateToPreview()
    }

    companion object {
        const val TAG = "CameraPresenter"
    }
}
