package com.mistreckless.support.wellcomeapp.ui.screen.camera.picture

import android.graphics.BitmapFactory
import android.support.v7.widget.Toolbar
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivityRouter
import kotlinx.android.synthetic.main.fragment_picture_settings.*

/**
 * Created by @mistreckless on 08.10.2017. !
 */
@Layout(id = R.layout.fragment_picture_settings)
class PictureSettings : BaseFragment<PictureSettingsPresenter, PictureSettingPresenterProviderFactory>(), PictureSettingsView {


    override fun getCurrentToolbar(): Toolbar = toolbar
    override fun getRouter() = activity as CameraActivityRouter

    override fun initUi(bytes: ByteArray) {
        imgPicture.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.size))
        btnNext.setOnClickListener { presenter.nextClicked() }

    }

    companion object {

    }
}


interface PictureSettingsView : BaseFragmentView {
    fun initUi(bytes: ByteArray)
}