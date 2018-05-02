package com.wellcome.share.preview

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.otaliastudios.cameraview.CameraListener
import com.wellcome.share.R
import com.wellcome.utils.ui.BaseFragment
import com.wellcome.utils.ui.BaseFragmentView
import kotlinx.android.synthetic.main.fragment_preview.*

/**
 * Created by @mistreckless on 10.09.2017. !
 */
class Preview : BaseFragment<PreviewPresenter>(),
    PreviewView {
    override val layoutId: Int
        get() = R.layout.fragment_preview

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