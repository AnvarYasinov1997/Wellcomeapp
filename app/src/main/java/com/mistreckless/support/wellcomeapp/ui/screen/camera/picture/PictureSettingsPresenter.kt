package com.mistreckless.support.wellcomeapp.ui.screen.camera.picture

import com.mistreckless.support.wellcomeapp.domain.interactor.ShareInteractor
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.BasePresenterProviderFactory
import com.mistreckless.support.wellcomeapp.ui.PerFragment
import com.mistreckless.support.wellcomeapp.ui.presenterHolder
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivityRouter
import javax.inject.Inject

/**
 * Created by @mistreckless on 08.10.2017. !
 */

class PictureSettingsPresenter(private val shareInteractor: ShareInteractor) : BasePresenter<PictureSettingsView,CameraActivityRouter>(){
    override fun onFirstViewAttached() {
        getView()?.initUi(shareInteractor.getPhotoBytes())
    }

    companion object {
        const val TAG="PictureSettingsPresenter"
    }

    fun nextClicked() {
        getRouter()?.navigateToShare()
    }
}

@PerFragment
class PictureSettingPresenterProviderFactory @Inject constructor(private val shareInteractor: ShareInteractor) : BasePresenterProviderFactory<PictureSettingsPresenter>{
    override fun get(): PictureSettingsPresenter {
        if (presenterHolder.contains(PictureSettingsPresenter.TAG))
            return presenterHolder[PictureSettingsPresenter.TAG] as PictureSettingsPresenter
        else{
            val presenter = PictureSettingsPresenter(shareInteractor)
            presenterHolder.put(PictureSettingsPresenter.TAG,presenter)
            return presenter
        }
    }

}