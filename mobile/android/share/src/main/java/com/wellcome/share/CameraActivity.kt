package com.wellcome.share

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.wellcome.core.navigation.NavigatorProvider
import com.wellcomeapp.ui_core.BaseActivity
import com.wellcomeapp.ui_core.BaseActivityView
import ru.terrakok.cicerone.android.SupportAppNavigator

class CameraActivity : BaseActivity<CameraPresenter>(),
        CameraActivityView {
    override val layoutId: Int
        get() = R.layout.activity_camera
    override val navigator: SupportAppNavigator
        get() = (application as NavigatorProvider).getShareNavigator(this, R.id.cameraContainer)

    @InjectPresenter
    override lateinit var presenter: CameraPresenter

    @ProvidePresenter
    fun providePresenter(): CameraPresenter = presenterProvider.get()

}

interface CameraActivityView : BaseActivityView
