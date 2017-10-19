package com.mistreckless.support.wellcomeapp.ui.screen.wall

import android.view.ViewGroup
import com.mistreckless.support.wellcomeapp.domain.entity.PostData
import com.mistreckless.support.wellcomeapp.ui.PerFragment
import com.mistreckless.support.wellcomeapp.ui.view.BaseAdapter
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolder
import com.mistreckless.support.wellcomeapp.ui.view.post.PostViewFactory
import javax.inject.Inject

/**
 * Created by mistreckless on 19.10.17.
 */
@PerFragment
class WallAdapter @Inject constructor(private val postViewFactory: PostViewFactory) : BaseAdapter<PostData>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*, *> = postViewFactory.create(parent)
}