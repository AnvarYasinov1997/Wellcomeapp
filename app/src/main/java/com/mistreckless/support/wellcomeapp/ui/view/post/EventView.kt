package com.mistreckless.support.wellcomeapp.ui.view.post

import android.view.ViewGroup
import com.arellomobile.mvp.MvpView
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.domain.entity.EventData
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolder

/**
 * Created by mistreckless on 19.10.17.
 */

class EventViewHolder(override var presenter: SingleEventPresenter, parent: ViewGroup?) : BaseViewHolder<SingleEventPresenter,EventData>(parent, R.layout.view_post), EventView {
    override fun initUi(model: EventData) {
    }
}

interface EventView : MvpView {
    fun initUi(model: EventData)
}