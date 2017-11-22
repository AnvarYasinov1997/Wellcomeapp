package com.mistreckless.support.wellcomeapp.ui.view.post

import android.view.ViewGroup
import javax.inject.Inject

/**
 * Created by mistreckless on 19.10.17.
 */


class PostViewFactory @Inject constructor(private val presenter: PostPresenter){

    fun create(parent : ViewGroup?) = PostViewHolder(presenter,parent)
}