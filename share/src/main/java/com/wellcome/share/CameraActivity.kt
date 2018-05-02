package com.wellcome.share

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.wellcome.utils.ui.BaseActivity
import com.wellcome.utils.ui.BaseActivityView
import ru.terrakok.cicerone.android.SupportAppNavigator

/**
 * Created by @mistreckless on 03.09.2017. !
 */

class CameraActivity : BaseActivity<CameraPresenter>(),
    CameraActivityView {
    override val layoutId: Int
        get() = R.layout.activity_camera
    override val navigator: SupportAppNavigator
        get() = ShareNavigator(this,supportFragmentManager,R.id.cameraContainer)

    @InjectPresenter
    override lateinit var presenter : CameraPresenter
    @ProvidePresenter
    fun providePresenter() : CameraPresenter = presenterProvider.get()

    companion object {
        const val TAG = "CameraActivity"
    }
}

interface CameraActivityView : BaseActivityView
