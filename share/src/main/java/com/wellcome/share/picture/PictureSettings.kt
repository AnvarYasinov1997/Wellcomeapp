package com.wellcome.share.picture

import android.graphics.BitmapFactory
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.wellcome.share.R
import com.wellcome.utils.ui.BaseFragment
import com.wellcome.utils.ui.BaseFragmentView
import kotlinx.android.synthetic.main.fragment_picture_settings.*

/**
 * Created by @mistreckless on 08.10.2017. !
 */
class PictureSettings : BaseFragment<PictureSettingsPresenter>(),
    PictureSettingsView {
    override val layoutId: Int
        get() = R.layout.fragment_picture_settings

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