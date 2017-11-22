package com.mistreckless.support.wellcomeapp.ui.screen.camera.picture

import android.graphics.BitmapFactory
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import kotlinx.android.synthetic.main.fragment_picture_settings.*

/**
 * Created by @mistreckless on 08.10.2017. !
 */
@Layout(id = R.layout.fragment_picture_settings)
class PictureSettings : BaseFragment<PictureSettingsPresenter>(), PictureSettingsView {

    @InjectPresenter
    override lateinit var presenter : PictureSettingsPresenter
    @ProvidePresenter
    fun providePresenter()=presenterProvider.get()

    override fun initUi(bytes: ByteArray) {
        imgPicture.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.size))
        btnNext.setOnClickListener { presenter.nextClicked() }

    }

    companion object {
        const val TAG = "PictureSettingsFragment"
    }
}


interface PictureSettingsView : BaseFragmentView {
    fun initUi(bytes: ByteArray)
}