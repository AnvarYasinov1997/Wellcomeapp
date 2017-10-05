package com.mistreckless.support.wellcomeapp.ui.screen.camera

import android.support.v7.widget.Toolbar
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.BaseActivity
import com.mistreckless.support.wellcomeapp.ui.BaseActivityView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout

/**
 * Created by @mistreckless on 03.09.2017. !
 */

@Layout(id = R.layout.activity_camera)
class CameraActivity : BaseActivity<CameraPresenter,CameraPresenterProviderFactory>(), CameraActivityView, CameraActivityRouter{

    override fun setToolbar(toolbar: Toolbar?, isAddedToBackStack: Boolean) {}

}

interface CameraActivityView : BaseActivityView

interface CameraActivityRouter