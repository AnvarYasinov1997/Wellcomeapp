package com.mistreckless.support.wellcomeapp.ui.screen.camera.share

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.util.DisplayMetrics
import android.view.View
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivityRouter
import com.mistreckless.support.wellcomeapp.ui.view.camera.AgePickerDialog
import com.mistreckless.support.wellcomeapp.ui.view.camera.TimePickerDialog
import com.mistreckless.support.wellcomeapp.ui.view.indy.toObservable
import com.otaliastudios.cameraview.CameraUtils
import kotlinx.android.synthetic.main.fragment_share.*


/**
 * Created by @mistreckless on 08.10.2017. !
 */
@Layout(id = R.layout.fragment_share)
class Share : BaseFragment<SharePresenter, SharePresenterProviderFactory>(), ShareView {


    override fun getCurrentToolbar() = toolbar
    override fun getRouter() = activity as CameraActivityRouter

    override fun initUi(bytes: ByteArray) {
        initAppBar(bytes)
        presenter.controlAge(cbAge.toObservable())
        txtTillTime.setOnClickListener { TimePickerDialog.newInstance({h,m->presenter.timePicked(h,m)}).show(childFragmentManager,"timePicker") }
        btnShare.setOnClickListener { if (it.isEnabled) presenter.shareClicked(txtAddress.text.toString(),edtDesc.text.toString(),cbDress.isChecked,cbAge.isChecked,txtAge.text.toString(),txtFromTime.text.toString(),txtTillTime.text.toString()) }
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

    override fun showNumberPicker() {
        AgePickerDialog.newInstance({presenter.agePicked(it)},{cbAge.isChecked=false}).show(childFragmentManager,"agePicker")
    }

    override fun showAge(ageLine: String) {
        txtAge.visibility=View.VISIBLE
        txtAge.text=ageLine
    }

    override fun hideAge() {
        txtAge.visibility=View.GONE
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

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.apply {
            putString(ADDRESS_KEY,txtAddress.text.toString())
            putString(DESC_KEY,edtDesc.text.toString())
            putString(AGE_NUMBER_KEY,txtAge.text.toString())
            putBoolean(AGE_KEY,cbAge.isChecked)
            putBoolean(DRESS_KEY,cbDress.isChecked)
        }
        super.onSaveInstanceState(outState)
    }

    companion object {
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
    fun showNumberPicker()
    fun showAge(ageLine: String)
    fun hideAge()
    fun showTillTime(tillTime: String)
    fun showFromTime(fromTime : String)
    fun setBtnShareEnabled(isEnabled : Boolean)
}