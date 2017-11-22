package com.mistreckless.support.wellcomeapp.ui.view.post

import android.view.ViewGroup
import javax.inject.Inject

/**
 * Created by mistreckless on 19.10.17.
 */


class EventViewFactory @Inject constructor(private val presenter: SingleEventPresenter){

    fun create(parent : ViewGroup?) = EventViewHolder(presenter,parent)
}