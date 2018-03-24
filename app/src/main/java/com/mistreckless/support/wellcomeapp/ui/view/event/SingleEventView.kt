package com.mistreckless.support.wellcomeapp.ui.view.event

import android.util.Log
import com.mistreckless.support.wellcomeapp.domain.entity.EventData
import com.mistreckless.support.wellcomeapp.ui.view.BaseRealTimeView
import com.mistreckless.support.wellcomeapp.ui.view.BaseRealTimeViewHolder

class SingleEventViewHolder(val delegate: BaseRealTimeView<SingleEventPresenter,EventData,SingleEventView>) :
    BaseRealTimeViewHolder(delegate.containerView),SingleEventView,
    BaseRealTimeView<SingleEventPresenter,EventData,SingleEventView> by delegate {

    override fun test() {
        Log.e("view","test")
    }
}


interface SingleEventView{
    fun test()
}


