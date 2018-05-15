package com.wellcome.share.preview

import com.arellomobile.mvp.InjectViewState
import com.wellcome.share.picture.PictureSettings
import com.wellcome.core.ui.BasePresenter
import com.wellcome.core.ui.PerFragment
import com.wellcome.core.ui.Screen
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ru.terrakok.cicerone.Router
import wellcome.common.interactor.ShareInteractor
import javax.inject.Inject

/**
 * Created by @mistreckless on 10.09.2017. !
 */
@PerFragment
@InjectViewState
class PreviewPresenter @Inject constructor(
    private val shareInteractor: ShareInteractor,
    private val router: Router
) : BasePresenter<PreviewView>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initUi()
    }

    fun pictureTaken(bytes: ByteArray?) = launch(UI) {
        if (bytes != null) {
            shareInteractor.putPhotoBytes(bytes).join()
            router.navigateTo(Screen.PICTURE_SETTINGS)
        }
    }

    companion object {
        const val TAG = "PreviewPresenter"
    }

}
