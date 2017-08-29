package com.mistreckless.support.wellcomeapp.ui.screen.registry

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.jakewharton.rxbinding2.widget.RxTextView
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.domain.entity.NewUserState
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.squareup.picasso.Picasso
import com.subinkrishna.widget.CircularImageView
import java.io.File

/**
 * Created by @mistreckless on 31.07.2017. !
 */
@Layout(id = R.layout.fragment_registry)
class Registry : BaseFragment<RegistryPresenter>(), RegistryView {
    @BindView(R.id.txt_header)
    lateinit var txtHeader: TextView
    @BindView(R.id.txt_name)
    lateinit var txtName: TextView
    @BindView(R.id.edt_name)
    lateinit var edtName: EditText
    @BindView(R.id.btn_finish)
    lateinit var btnFinish: Button
    @BindView(R.id.img_user)
    lateinit var imgUser: CircularImageView

    override fun getCurrentToolbar() = null

    override fun onStart() {
        super.onStart()
        presenter.controlName(RxTextView.textChanges(edtName))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.resultFromIntent(requestCode, resultCode, data)
    }

    override fun initUi() {
        val user = arguments.getSerializable("user_state") as NewUserState
        edtName.hint = user.fullName
    }

    override fun showCity(city: String) {
        txtHeader.text = "Welcome to " + city
    }

    override fun showName(name: String) {
        txtName.text = name
    }

    override fun setBtnNextEnabled(enabled: Boolean) {
        btnFinish.isEnabled = enabled
    }

    override fun sendIntentToGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_INTENT_CODE)
    }

    override fun setPhoto(path: String) {
        Picasso.with(context).load(File(path)).placeholder(R.drawable.common_google_signin_btn_icon_light).into(imgUser)
    }

    @OnClick(R.id.btn_finish)
    fun onFinishClick(view: Button) {
        if (view.isEnabled) presenter.finishClicked(txtName.text.toString(), arguments.getSerializable("user_state") as NewUserState)
    }

    @OnClick(R.id.img_user)
    fun onPhotoClick() {
        presenter.photoClicked()
    }

    companion object {
        const val TAG = "Auth"
        const val GALLERY_INTENT_CODE = 8

        fun newInstance(newUserState: NewUserState): Registry {
            val fragment = Registry()
            val args = Bundle()
            args.putSerializable("user_state", newUserState)
            fragment.arguments = args
            return fragment
        }
    }
}

interface RegistryView : BaseFragmentView {
    fun showCity(city: String)
    fun showName(name: String)
    fun setBtnNextEnabled(enabled: Boolean)
    fun sendIntentToGallery()
    fun setPhoto(path: String)
    fun initUi()
}

