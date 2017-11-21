package com.mistreckless.support.wellcomeapp.ui.screen.wall

import android.view.ViewGroup
import com.mistreckless.support.wellcomeapp.ui.PerFragment
import com.mistreckless.support.wellcomeapp.ui.view.BaseRealTimeAdapter
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolder
import com.mistreckless.support.wellcomeapp.ui.view.post.PostViewFactory
import javax.inject.Inject

/**
 * Created by mistreckless on 19.10.17.
 */
@PerFragment
class WallAdapter @Inject constructor(private val postViewFactory: PostViewFactory) : BaseRealTimeAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*> = postViewFactory.create(parent)
}