package com.mistreckless.support.wellcomeapp.ui.screen.camera.share

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.OnClick
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivityRouter
import com.mistreckless.support.wellcomeapp.ui.view.camera.AgePickerDialog
import com.mistreckless.support.wellcomeapp.ui.view.camera.TimePickerDialog
import com.mistreckless.support.wellcomeapp.ui.view.indy.toObservable
import com.otaliastudios.cameraview.CameraUtils


/**
 * Created by @mistreckless on 08.10.2017. !
 */
@Layout(id = R.layout.fragment_share)
class Share : BaseFragment<SharePresenter, SharePresenterProviderFactory>(), ShareView {
    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar
    @BindView(R.id.img_picture)
    lateinit var imgPicture: ImageView
    @BindView(R.id.appbar)
    lateinit var appbarLayout: AppBarLayout
    @BindView(R.id.txt_address)
    lateinit var txtAddress: TextView
    @BindView(R.id.address_progress_bar)
    lateinit var pbAddress: ProgressBar
    @BindView(R.id.cb_age)
    lateinit var cbAge : CheckBox
    @BindView(R.id.cb_dress)
    lateinit var cbDress : CheckBox
    @BindView(R.id.txt_age)
    lateinit var txtAge : TextView
    @BindView(R.id.edt_desc)
    lateinit var edtDesc : EditText

    override fun getCurrentToolbar() = toolbar
    override fun getRouter() = activity as CameraActivityRouter

    override fun initUi() {
        initAppBar()
        presenter.controlAge(cbAge.toObservable())
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

    @OnClick(R.id.btn_share)
    fun onShareClick(){
        TimePickerDialog.newInstance({h,m->presenter.timePicked(h,m)}).show(childFragmentManager,"timePicker")
    }
    fun log(h : Int, m : Int){
        Log.e("time", "h $h m $m")
    }

    private fun initAppBar() {
        @SuppressLint("DrawAllocation")
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val size = if (width > height) height else width
        val lp = appbarLayout.layoutParams as CoordinatorLayout.LayoutParams
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


        lateinit var bytes: ByteArray

        fun newInstance(bytes: ByteArray): Share {
            this.bytes = bytes
            return Share()
        }
    }
}


interface ShareView : BaseFragmentView {
    fun initUi()
    fun showAddress(line: String)
    fun showAddressProgressBar()
    fun showNumberPicker()
    fun showAge(ageLine: String)
    fun hideAge()
}