package com.mistreckless.support.wellcomeapp.ui.screen.camera.preview

import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivityRouter
import com.otaliastudios.cameraview.CameraListener
import kotlinx.android.synthetic.main.fragment_preview.*

/**
 * Created by @mistreckless on 10.09.2017. !
 */
@Layout(id = R.layout.fragment_preview)
class Preview : BaseFragment<PreviewPresenter,PreviewPresenterProviderFactory>(),PreviewView{

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


    override fun getCurrentToolbar()=toolbar
    override fun getRouter() = activity as CameraActivityRouter

    override fun initUi() {
        imgTakePicture.setOnClickListener { cameraView.capturePicture() }
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