package com.wellcome.event.view

import android.annotation.SuppressLint
import android.support.annotation.LayoutRes
import android.util.Log
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.wellcome.core.ui.RealTimeViewHolder
import com.wellcome.core.ui.setDelayedClickListener
import com.wellcome.core.ui.toTime
import kotlinx.android.synthetic.main.view_single_event.view.*
import wellcome.common.entity.Event

class SingleEventViewHolder(@LayoutRes layoutId: Int, parent: ViewGroup, presenterProvider: SingleEventPresenterProvider) :
    RealTimeViewHolder<SingleEventPresenter, Event, SingleEventView>(parent,layoutId, presenterProvider),
    SingleEventView {
    override val view: SingleEventView
        get() = this


    @SuppressLint("SetTextI18n")
    override fun initUi(event: Event) = with(itemView) {
        val data = event.data
        Picasso.with(context).load(data.contents.first().content).into(imgContent)
        txtDesc.text = data.contents.first().desc
        txtTime.text = "until ${data.contents.first().deleteTime.toTime()}"
        txtAddress.text = data.address
        txtLikeCount.text = data.likeCount.toString()
        txtCommentCount.text = data.commentCount.toString()
        txtWillcomeCount.text = data.willcomeCount.toString()

        likeLayout.setDelayedClickListener(1000) { presenter.clicked(Click.LIKE) }
        commentLayout.setDelayedClickListener(300) { presenter.clicked(Click.COMMENT) }
        willcomeLayout.setDelayedClickListener(1000) { presenter.clicked(Click.COMMENT) }
        addressLayout.setDelayedClickListener(300) { presenter.clicked(Click.ADDRESS) }

        itemView.imgLike.isSelected = event.isLiked
    }

    override fun updateUi(event: Event) {
        val data = event.data
        Log.e("updated", data.ref)
        itemView.txtLikeCount.text = data.likeCount.toString()
        itemView.txtCommentCount.text = data.commentCount.toString()
        itemView.txtWillcomeCount.text = data.willcomeCount.toString()
    }

    override fun animateLike(isLiked: Boolean) {
        itemView.imgLike.isSelected = isLiked
    }
}

interface SingleEventView {
    fun initUi(event: Event)
    fun updateUi(event: Event)
    fun animateLike(isLiked: Boolean)
}

enum class Click{
    LIKE, COMMENT, WILLCOME, ADDRESS
}


