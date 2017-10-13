package com.mistreckless.support.wellcomeapp.ui.view.drawer

import android.view.ViewGroup
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.MainActivity
import com.mistreckless.support.wellcomeapp.ui.model.ListItem
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_drawer_item.*

/**
 * Created by @mistreckless on 30.08.2017. !
 */

class ListViewHolder(override var presenter: DrawerListPresenter, parent : ViewGroup?, mainActivity: MainActivity)
    : BaseViewHolder<ListItem,DrawerListPresenter>(mainActivity,parent, R.layout.view_drawer_item), ListView{

    override fun initUi(model: ListItem) {
        txtTitle.text=model.title
        Picasso.with(itemView.context).load(model.resId).into(imgIcon)
    }


}

interface ListView {
    fun initUi(model: ListItem)
}