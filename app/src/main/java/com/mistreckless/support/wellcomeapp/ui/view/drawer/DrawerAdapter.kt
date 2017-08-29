package com.mistreckless.support.wellcomeapp.ui.view.drawer

import android.view.ViewGroup
import com.mistreckless.support.wellcomeapp.ui.PerFragment
import com.mistreckless.support.wellcomeapp.ui.model.DrawerItem
import com.mistreckless.support.wellcomeapp.ui.model.HeaderItem
import com.mistreckless.support.wellcomeapp.ui.view.BaseAdapter
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolder
import javax.inject.Inject

/**
 * Created by @mistreckless on 28.08.2017. !
 */
@PerFragment
class DrawerAdapter @Inject constructor(val headerViewFactory: HeaderViewFactory, val listViewFactory: ListViewFactory) : BaseAdapter<DrawerItem>() {

    override fun getItemViewType(position: Int) = when (items[position]) {
        is HeaderItem -> HEADER_ITEM
        else -> LIST_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        HEADER_ITEM -> headerViewFactory.create(parent)
        else -> listViewFactory.create(parent)
    }

    companion object {
        const val HEADER_ITEM = 0
        const val LIST_ITEM = 1
    }
}
