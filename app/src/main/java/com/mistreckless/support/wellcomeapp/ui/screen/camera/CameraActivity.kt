package com.mistreckless.support.wellcomeapp.ui.screen.camera

import android.support.v7.widget.Toolbar
import butterknife.BindView
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.BaseActivity
import com.mistreckless.support.wellcomeapp.ui.BaseActivityView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.otaliastudios.cameraview.CameraView

/**
 * Created by @mistreckless on 03.09.2017. !
 */

@Layout(id = R.layout.activity_camera)
class CameraActivity : BaseActivity<CameraPresenter>(), CameraActivityView, CameraActivityRouter{
    @BindView(R.id.camera_view)
    lateinit var cameraView : CameraView

    override fun setToolbar(toolbar: Toolbar?, isAddedToBackStack: Boolean) {}

    override fun onResume() {
        super.onResume()
        cameraView.start()
    }

    override fun onPause() {
        super.onPause()
        cameraView.stop()
    }

    override fun onDestroy() {
        cameraView.destroy()
        super.onDestroy()
    }
}

interface CameraActivityView : BaseActivityView

interface CameraActivityRouter