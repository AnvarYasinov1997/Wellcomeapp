package com.mistreckless.support.wellcomeapp.ui.screen.wall

import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.MainActivity
import com.mistreckless.support.wellcomeapp.ui.PerFragment
import javax.inject.Inject

/**
 * Created by @mistreckless on 27.08.2017. !
 */

@PerFragment
class WallPresenter @Inject constructor(): BasePresenter<WallView, MainActivity>() {
    override fun onFirstViewAttached() {
    }

    fun fubClicked() {
        getRouter()?.navigateToCamera()
    }
}