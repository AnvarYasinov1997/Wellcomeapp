package com.mistreckless.support.wellcomeapp.ui.screen.camera

import android.support.v7.widget.Toolbar
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.BaseActivity
import com.mistreckless.support.wellcomeapp.ui.BaseActivityView
import com.mistreckless.support.wellcomeapp.ui.BaseRouter
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.mistreckless.support.wellcomeapp.ui.screen.camera.picture.PictureSettings
import com.mistreckless.support.wellcomeapp.ui.screen.camera.preview.Preview
import com.mistreckless.support.wellcomeapp.ui.screen.camera.share.Share

/**
 * Created by @mistreckless on 03.09.2017. !
 */

@Layout(id = R.layout.activity_camera)
class CameraActivity : BaseActivity<CameraPresenter,CameraPresenterProviderFactory>(), CameraActivityView, CameraActivityRouter{

    override fun setToolbar(toolbar: Toolbar?, isAddedToBackStack: Boolean, isShowDrawer : Boolean) {}


    override fun navigateToPreview() {
        supportFragmentManager.beginTransaction()
                .add(R.id.container,Preview())
                .commit()
    }

    override fun navigateToPictureSettings() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container,PictureSettings())
                .addToBackStack(null)
                .commit()
    }

    override fun navigateToShare() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container,Share())
                .addToBackStack(null)
                .commit()
    }
}

interface CameraActivityView : BaseActivityView

interface CameraActivityRouter : BaseRouter{
    fun navigateToPreview()
    fun navigateToPictureSettings()
    fun navigateToShare()
}