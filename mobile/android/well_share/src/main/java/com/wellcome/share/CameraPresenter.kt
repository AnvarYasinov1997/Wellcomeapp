package com.wellcome.share

import com.arellomobile.mvp.InjectViewState
import com.wellcome.core.navigation.Screen
import com.wellcome.ui.core.BasePresenter
import com.wellcome.ui.core.PerActivity
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
