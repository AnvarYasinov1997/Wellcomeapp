package com.mistreckless.support.wellcomeapp.ui.screen.camera.preview

import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.OnClick
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivityRouter
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView

/**
 * Created by @mistreckless on 10.09.2017. !
 */
@Layout(id = R.layout.fragment_preview)
class Preview : BaseFragment<PreviewPresenter,PreviewPresenterProviderFactory>(),PreviewView{
    @BindView(R.id.toolbar)
    lateinit var toolbar : Toolbar
    @BindView(R.id.camera_view)
    lateinit var cameraView : CameraView

    override fun onResume() {
        super.onResume()
        cameraView.start()
    }

    override fun onPause() {
        super.onPause()
        cameraView.stop()
    }

    override fun onDestroyView() {
        cameraView.destroy()
        super.onDestroyView()
    }

    @OnClick(R.id.img_take_picture)
    fun onTakePictureClick(){
        cameraView.capturePicture()
    }

    override fun getCurrentToolbar()=toolbar
    override fun getRouter() = activity as CameraActivityRouter

    override fun initUi() {
        cameraView.addCameraListener(object : CameraListener(){
            override fun onPictureTaken(jpeg: ByteArray?) {
                super.onPictureTaken(jpeg)
                cameraView.postDelayed({presenter.pictureTaken(jpeg)},50)
            }
        })
    }

}

interface PreviewView : BaseFragmentView {
    fun initUi()
}