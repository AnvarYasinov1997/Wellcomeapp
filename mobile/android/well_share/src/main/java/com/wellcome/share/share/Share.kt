package com.wellcome.share.share

import android.annotation.SuppressLint
import android.app.Activity
import android.support.design.widget.CoordinatorLayout
import android.util.DisplayMetrics
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.otaliastudios.cameraview.CameraUtils
import com.wellcome.share.R
import com.wellcome.ui.core.BaseFragment
import com.wellcome.ui.core.BaseFragmentView


/**
 * Created by @mistreckless on 08.10.2017. !
 */
class Share : BaseFragment<SharePresenter>(),
    ShareView {
    override val layoutId: Int
        get() = R.layout.fragment_share

    @InjectPresenter
    override lateinit var presenter : SharePresenter
    @ProvidePresenter
    fun providePresenter() = presenterProvider.get()

    override fun initUi(bytes: ByteArray) {
        initAppBar(bytes)
        txtTillTime.setOnClickListener { TimePickerDialog.newInstance({ h, m->presenter.timePicked(h,m)}).show(childFragmentManager,"timePicker") }
        btnShare.setOnClickListener { if (it.isEnabled) presenter.shareClicked(txtAddress.text.toString(),edtDesc.text.toString(),txtFromTime.text.toString(),txtTillTime.text.toString()) }
    }

    override fun showAddress(line: String) {
        pbAddress.visibility=View.INVISIBLE
        txtAddress.visibility=View.VISIBLE
        txtAddress.text=line
    }

    override fun showAddressProgressBar() {
        txtAddress.visibility= View.INVISIBLE
        pbAddress.visibility=View.VISIBLE
    }

    override fun showFromTime(fromTime: String) {
        txtFromTime.text=fromTime
    }

    override fun showTillTime(tillTime: String) {
        txtTillTime.text=tillTime
    }

    override fun setBtnShareEnabled(isEnabled: Boolean) {
        btnShare.isEnabled=isEnabled
    }

    private fun initAppBar(bytes: ByteArray) {
        @SuppressLint("DrawAllocation")
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val size = if (width > height) height else width
        val lp = appbar.layoutParams as CoordinatorLayout.LayoutParams
        lp.height = size
        lp.width = size

        CameraUtils.decodeBitmap(bytes, {
            imgPicture.setImageBitmap(it)
        })
    }


    companion object {
        const val TAG ="ShareFragment"
        const val ADDRESS_KEY="address"
        const val DESC_KEY="desc"
        const val AGE_NUMBER_KEY="age_number"
        const val AGE_KEY="age"
        const val DRESS_KEY="dress"
        const val PICTURE_PATH_KEY="picture_path"
        const val TIME_SNAPSHOT_KEY="time_snapshot"
    }
}


interface ShareView : BaseFragmentView {
    fun initUi(bytes : ByteArray)
    fun showAddress(line: String)
    fun showAddressProgressBar()
    fun showTillTime(tillTime: String)
    fun showFromTime(fromTime : String)
    fun setBtnShareEnabled(isEnabled : Boolean)
}