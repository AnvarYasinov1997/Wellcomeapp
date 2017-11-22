package com.mistreckless.support.wellcomeapp.ui.screen.camera.preview

import com.arellomobile.mvp.InjectViewState
import com.mistreckless.support.wellcomeapp.domain.interactor.ShareInteractor
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.PerFragment
import com.mistreckless.support.wellcomeapp.ui.screen.camera.picture.PictureSettings
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by @mistreckless on 10.09.2017. !
 */
@PerFragment
@InjectViewState
class PreviewPresenter @Inject constructor(private val shareInteractor: ShareInteractor, private val router: Router) : BasePresenter<PreviewView>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initUi()
    }

    fun pictureTaken(bytes: ByteArray?) {
        if (bytes != null){
            shareInteractor.putPhotoBytes(bytes)
            router.navigateTo(PictureSettings.TAG)
        }
    }

    companion object {
        const val TAG = "PreviewPresenter"
    }

}
