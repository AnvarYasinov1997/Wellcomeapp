package com.mistreckless.support.wellcomeapp.ui.screen.camera

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.BaseActivity
import com.mistreckless.support.wellcomeapp.ui.BaseActivityView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout

/**
 * Created by @mistreckless on 03.09.2017. !
 */

@Layout(id = R.layout.activity_camera, containerId = R.id.container)
class CameraActivity : BaseActivity<CameraPresenter>(), CameraActivityView{

    @InjectPresenter
    override lateinit var presenter : CameraPresenter
    @ProvidePresenter
    fun providePresenter() : CameraPresenter = presenterProvider.get()

    companion object {
        const val TAG = "CameraActivity"
    }
}

interface CameraActivityView : BaseActivityView
