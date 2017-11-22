package com.mistreckless.support.wellcomeapp.ui.view.post

import android.view.ViewGroup
import com.arellomobile.mvp.MvpView
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.domain.entity.PostData
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolder

/**
 * Created by mistreckless on 19.10.17.
 */

class PostViewHolder(override var presenter: PostPresenter, parent: ViewGroup?) : BaseViewHolder<PostPresenter>(parent, R.layout.view_post), PostView {
    override fun initUi(model: PostData) {
    }
}

interface PostView : MvpView {
    fun initUi(model: PostData)
}