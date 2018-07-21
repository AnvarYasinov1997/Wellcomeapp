package com.wellcome.share.picture

import com.arellomobile.mvp.InjectViewState
import com.wellcome.core.navigation.Screen
import com.wellcome.ui.core.BasePresenter
import com.wellcome.ui.core.PerFragment
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import ru.terrakok.cicerone.Router
import wellcome.common.share.ShareInteractor
import javax.inject.Inject

/**
 * Created by @mistreckless on 08.10.2017. !
 */
@PerFragment
@InjectViewState
class PictureSettingsPresenter @Inject constructor(private val shareInteractor: ShareInteractor, private val router: Router) : BasePresenter<PictureSettingsView>(){

    override fun onFirstViewAttach() {
        launch(UI) {
            super.onFirstViewAttach()
            viewState.initUi(shareInteractor.getPhotoBytes().await())
        }
    }

    companion object {
        const val TAG="PictureSettingsPresenter"
    }

    fun nextClicked() {
        router.navigateTo(Screen.SHARE)
    }
}
