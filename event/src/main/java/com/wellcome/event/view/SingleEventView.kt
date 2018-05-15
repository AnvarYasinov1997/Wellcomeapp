package com.wellcome.event.view

import android.annotation.SuppressLint
import android.support.annotation.LayoutRes
import android.util.Log
import android.view.ViewGroup
import com.wellcome.core.ui.RealTimeViewHolder
import com.squareup.picasso.Picasso
import com.wellcome.core.ui.toTime
import kotlinx.android.synthetic.main.view_single_event.view.*
import wellcome.common.entity.Address
import wellcome.common.entity.EventData

class SingleEventViewHolder(@LayoutRes layoutId: Int, parent: ViewGroup, presenterProvider: SingleEventPresenterProvider) :
    RealTimeViewHolder<SingleEventPresenter, EventData, SingleEventView>(parent,layoutId, presenterProvider),
    SingleEventView {
    override val view: SingleEventView
        get() = this


    @SuppressLint("SetTextI18n")
    override fun initUi(data: EventData) = with(itemView) {
        Picasso.with(context).load(data.contents.first().content).into(imgContent)
        txtDesc.text = data.contents.first().desc
        txtTime.text = "until ${data.contents.first().deleteTime.toTime()}"
        txtAddress.text = data.address
        txtLikeCount.text = data.likeCount.toString()
        txtCommentCount.text = data.commentCount.toString()
        txtWillcomeCount.text = data.willcomeCount.toString()

        likeLayout.setOnClickListener { presenter.clicked(Click.LIKE) }
        commentLayout.setOnClickListener { presenter.clicked(Click.COMMENT) }
        willcomeLayout.setOnClickListener { presenter.clicked(Click.COMMENT) }
        addressLayout.setOnClickListener { presenter.clicked(Click.ADDRESS) }
    }

    override fun updateUi(data: EventData) {
        Log.e("updated", data.ref)
        itemView.txtLikeCount.text = data.likeCount.toString()
        itemView.txtCommentCount.text = data.commentCount.toString()
        itemView.txtWillcomeCount.text = data.willcomeCount.toString()
    }
}

interface SingleEventView {
    fun initUi(data: EventData)
    fun updateUi(data: EventData)
}

enum class Click{
    LIKE, COMMENT, WILLCOME, ADDRESS
}


