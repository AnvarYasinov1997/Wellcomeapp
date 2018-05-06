package com.wellcome.share

import com.arellomobile.mvp.InjectViewState
import com.wellcome.share.preview.Preview
import com.wellcome.utils.ui.BasePresenter
import com.wellcome.utils.ui.PerActivity
import com.wellcome.utils.ui.Screen
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by @mistreckless on 03.09.2017. !
 */

@PerActivity
@InjectViewState
class CameraPresenter @Inject constructor(private val router: Router) : BasePresenter<CameraActivityView>() {

    override fun onFirstViewAttach() {
        router.replaceScreen(Screen.PREVIEW)
    }

    companion object {
        const val TAG = "CameraPresenter"
    }
}
