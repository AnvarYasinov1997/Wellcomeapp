package com.wellcome.share

import com.arellomobile.mvp.InjectViewState
import com.wellcomeapp.ui_core.BasePresenter
import com.wellcomeapp.ui_core.PerActivity
import com.wellcomeapp.ui_core.Screen
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
