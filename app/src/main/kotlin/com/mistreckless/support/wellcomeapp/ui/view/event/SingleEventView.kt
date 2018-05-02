package com.mistreckless.support.wellcomeapp.ui.view.event

import android.util.Log
import wellcome.common.entity.EventData
import com.mistreckless.support.wellcomeapp.ui.view.BaseRealTimeView
import com.mistreckless.support.wellcomeapp.ui.view.BaseRealTimeViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_single_event.view.*

class SingleEventViewHolder(val delegate: BaseRealTimeView<SingleEventPresenter, EventData, SingleEventView>) :
    BaseRealTimeViewHolder(delegate.containerView), SingleEventView,
    BaseRealTimeView<SingleEventPresenter, EventData, SingleEventView> by delegate {

    override fun initUi(data: EventData) = with(containerView) {
        Picasso.with(context).load(data.contents.first().content).into(imgContent)
        txtDesc.text = data.contents.first().desc
        txtAddress.text = data.address
        txtTime.text = data.contents.first().createTime.toString()
        txtLikeCount.text = data.likeCount.toString()
    }

    override fun updateUi(data: EventData) {
        Log.e("updated", data.likeCount.toString())
        containerView.txtLikeCount.text = data.likeCount.toString()
    }
}


interface SingleEventView {
    fun initUi(data: EventData)
    fun updateUi(data: EventData)
}


