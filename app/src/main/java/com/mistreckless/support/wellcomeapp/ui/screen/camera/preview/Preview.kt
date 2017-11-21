package com.mistreckless.support.wellcomeapp.ui.screen.camera.preview

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
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
class Preview : BaseFragment<PreviewPresenter>(),PreviewView{

    @InjectPresenter
    override lateinit var presenter : PreviewPresenter
    @ProvidePresenter
    fun providePresenter() = presenterProvider.get()

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

    companion object {
        const val TAG ="PreviewFragment"
    }
}

interface PreviewView : BaseFragmentView {
    fun initUi()
}