package com.wellcome.event.view

import android.support.annotation.LayoutRes
import android.util.Log
import android.view.ViewGroup
import com.wellcome.utils.ui.RealTimeViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_single_event.view.*
import wellcome.common.entity.EventData

class SingleEventViewHolder(@LayoutRes layoutId: Int, parent: ViewGroup, presenterProvider: SingleEventPresenterProvider) :
    RealTimeViewHolder<SingleEventPresenter, EventData, SingleEventView>(parent,layoutId, presenterProvider),
    SingleEventView {
    override val view: SingleEventView
        get() = this


    override fun initUi(data: EventData) = with(itemView) {
        Picasso.with(context).load(data.contents.first().content).into(imgContent)
        txtDesc.text = data.contents.first().desc
        txtAddress.text = data.address
        txtTime.text = data.contents.first().createTime.toString()
        txtLikeCount.text = data.likeCount.toString()
    }

    override fun updateUi(data: EventData) {
        Log.e("updated", data.likeCount.toString())
        itemView.txtLikeCount.text = data.likeCount.toString()
    }
}


interface SingleEventView {
    fun initUi(data: EventData)
    fun updateUi(data: EventData)
}


