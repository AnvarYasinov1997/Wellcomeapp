package com.mistreckless.support.wellcomeapp.ui.view.event

import android.util.Log
import com.mistreckless.support.wellcomeapp.domain.entity.EventData
import com.mistreckless.support.wellcomeapp.ui.view.BaseRealTimeView
import com.mistreckless.support.wellcomeapp.ui.view.BaseRealTimeViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_single_event.view.*

class SingleEventViewHolder(val delegate: BaseRealTimeView<SingleEventPresenter,EventData,SingleEventView>) :
    BaseRealTimeViewHolder(delegate.containerView),SingleEventView,
    BaseRealTimeView<SingleEventPresenter,EventData,SingleEventView> by delegate {

    override fun updateUi(data: EventData) {
        Log.e("updated",data.likeCount.toString())
        Picasso.with(containerView.context).load(data.contents[0].content).into(containerView.imgContent)
        containerView.txtLikeCount.text=data.likeCount.toString()
    }
}


interface SingleEventView{
    fun updateUi(data: EventData)
}


