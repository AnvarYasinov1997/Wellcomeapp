package com.mistreckless.support.wellcomeapp.ui.screen.camera.picture

import android.support.v7.widget.Toolbar
import android.widget.ImageView
import butterknife.BindView
import butterknife.OnClick
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivityRouter
import com.otaliastudios.cameraview.CameraUtils

/**
 * Created by @mistreckless on 08.10.2017. !
 */
@Layout(id = R.layout.fragment_picture_settings)
class PictureSettings : BaseFragment<PictureSettingsPresenter,PictureSettingPresenterProviderFactory>(), PictureSettingsView{
    @BindView(R.id.toolbar)
    lateinit var toolbar : Toolbar
    @BindView(R.id.img_picture)
    lateinit var imgPicture : ImageView

    override fun getCurrentToolbar()=toolbar
    override fun getRouter()=activity as CameraActivityRouter

    @OnClick(R.id.btn_next)
    fun onNextClick(){
        presenter.nextClicked(bytes)
    }

    override fun initUi() {
        CameraUtils.decodeBitmap(bytes,{
            imgPicture.setImageBitmap(it)
        })
    }

    companion object {
        const val BYTE_KEY="bytes"
        lateinit var bytes : ByteArray
        fun newInstance(bytes : ByteArray) : PictureSettings{
            val instance = PictureSettings()
            this.bytes=bytes
            return instance
        }
    }
}


interface PictureSettingsView : BaseFragmentView {
    fun initUi()
}