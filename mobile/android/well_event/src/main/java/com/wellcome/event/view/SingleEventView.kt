package com.wellcome.event.view

import android.annotation.SuppressLint
import android.support.annotation.LayoutRes
import android.util.Log
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.wellcome.ui.core.RealTimeViewHolder
import com.wellcome.utils.toTime
import wellcome.common.mpp.entity.Event
import wellcome.common.mpp.entity.UserData

class SingleEventViewHolder(@LayoutRes layoutId: Int, parent: ViewGroup,
                            presenterProvider: SingleEventPresenterProvider) :
    RealTimeViewHolder<SingleEventPresenter, Event, SingleEventView>(parent,
        layoutId,
        presenterProvider),
    SingleEventView {
    override val view: SingleEventView
        get() = this


    @SuppressLint("SetTextI18n")
    override fun initUi(event: Event) = with(itemView) {
        val data = event.data
        Picasso.with(context).load(data.content.content).into(imgContent)
        txtDesc.text = data.content.desc
        txtTime.text = "until ${data.content.deleteTime.toTime()}"
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

    override fun updateUi(event: Event) = with(itemView) {
        val data = event.data
        Log.e("updated", data.ref)
        txtLikeCount.text = data.likeCount.toString()
        txtCommentCount.text = data.commentCount.toString()
        txtWillcomeCount.text = data.willcomeCount.toString()
    }

    override fun updateUser(userData: UserData) = with(itemView) {
        if (userData.photoUrl?.isNotEmpty() == true) Picasso.with(context).load(userData.photoUrl).into(imgUser)
        txtUserName.text = userData.displayedName
    }

    override fun animateLike(isLiked: Boolean) {
        itemView.imgLike.isSelected = isLiked
    }
}

interface SingleEventView {
    fun initUi(event: Event)
    fun updateUi(event: Event)
    fun animateLike(isLiked: Boolean)
    fun updateUser(userData: UserData)
}

enum class Click {
    LIKE, COMMENT, WILLCOME, ADDRESS
}


