package com.mistreckless.support.wellcomeapp.ui.view.event

import android.annotation.SuppressLint
import android.view.ViewGroup
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.domain.entity.EventData
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_single_event.*
import javax.inject.Provider

/**
 * Created by mistreckless on 19.10.17.
 */

class SingleEventViewHolder(override val presenter : SingleEventPresenter, parent: ViewGroup?) : BaseViewHolder<SingleEventView,SingleEventPresenter,EventData>(parent, R.layout.view_single_event), SingleEventView {

    @SuppressLint("SetTextI18n")
    override fun initUi(model: EventData) {
        Picasso.with(itemView.context).load(model.contents[0].content).into(imgContent)
        txtDesc.text=model.contents[0].desc
        txtAddress.text=model.address
        txtTime.text="${model.contents[0].createTime} - ${model.contents[0].deleteTime}"
    }

    override fun updateUi(model: EventData) {

    }
}

interface SingleEventView{
    fun initUi(model: EventData)
    fun updateUi(model: EventData)
}