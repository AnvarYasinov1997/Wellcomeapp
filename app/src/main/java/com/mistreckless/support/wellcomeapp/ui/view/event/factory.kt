package com.mistreckless.support.wellcomeapp.ui.view.event

import android.view.ViewGroup
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by mistreckless on 19.10.17.
 */


class SingleEventViewFactory @Inject constructor(private val presenter: SingleEventPresenter){

    fun create(parent : ViewGroup?) = SingleEventViewHolder(presenter,parent)
}