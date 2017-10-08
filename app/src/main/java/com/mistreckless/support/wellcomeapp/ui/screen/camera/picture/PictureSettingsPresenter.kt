package com.mistreckless.support.wellcomeapp.ui.screen.camera.picture

import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.BasePresenterProviderFactory
import com.mistreckless.support.wellcomeapp.ui.PerFragment
import com.mistreckless.support.wellcomeapp.ui.presenterHolder
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivityRouter
import javax.inject.Inject

/**
 * Created by @mistreckless on 08.10.2017. !
 */

class PictureSettingsPresenter : BasePresenter<PictureSettingsView,CameraActivityRouter>(){
    override fun onFirstViewAttached() {
        getView()?.initUi()
    }

    companion object {
        const val TAG="PictureSettingsPresenter"
    }

    fun nextClicked(bytes: ByteArray) {
        getRouter()?.navigateToShare(bytes)
    }
}

@PerFragment
class PictureSettingPresenterProviderFactory @Inject constructor() : BasePresenterProviderFactory<PictureSettingsPresenter>{
    override fun get(): PictureSettingsPresenter {
        if (presenterHolder.contains(PictureSettingsPresenter.TAG))
            return presenterHolder[PictureSettingsPresenter.TAG] as PictureSettingsPresenter
        else{
            val presenter = PictureSettingsPresenter()
            presenterHolder.put(PictureSettingsPresenter.TAG,presenter)
            return presenter
        }
    }

}