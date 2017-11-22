package com.mistreckless.support.wellcomeapp.ui.screen.camera

import com.arellomobile.mvp.InjectViewState
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.PerActivity
import com.mistreckless.support.wellcomeapp.ui.screen.camera.preview.Preview
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by @mistreckless on 03.09.2017. !
 */

@PerActivity
@InjectViewState
class CameraPresenter @Inject constructor(private val router: Router) : BasePresenter<CameraActivityView>() {

    override fun onFirstViewAttach() {
        router.replaceScreen(Preview.TAG)
    }

    companion object {
        const val TAG = "CameraPresenter"
    }
}
