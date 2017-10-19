package com.mistreckless.support.wellcomeapp.ui.view.post

import android.view.ViewGroup
import com.mistreckless.support.wellcomeapp.domain.interactor.WallInteractor
import com.mistreckless.support.wellcomeapp.ui.BaseRouter
import javax.inject.Inject

/**
 * Created by mistreckless on 19.10.17.
 */


class PostViewFactory @Inject constructor(private val presenter: PostPresenter, private val baseRouter: BaseRouter){

    fun create(parent : ViewGroup?) = PostViewHolder(presenter,parent,baseRouter)
}