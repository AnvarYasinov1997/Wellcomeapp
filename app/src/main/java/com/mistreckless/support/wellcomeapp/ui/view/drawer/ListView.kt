package com.mistreckless.support.wellcomeapp.ui.view.drawer

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.MainActivity
import com.mistreckless.support.wellcomeapp.ui.model.ListItem
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolder
import com.squareup.picasso.Picasso

/**
 * Created by @mistreckless on 30.08.2017. !
 */

class ListViewHolder(override var presenter: DrawerListPresenter, parent : ViewGroup?, mainActivity: MainActivity)
    : BaseViewHolder<ListItem,DrawerListPresenter>(mainActivity,parent, R.layout.view_drawer_item), ListView{
    @BindView(R.id.txt_title)
    lateinit var txtTitle : TextView
    @BindView(R.id.img_icon)
    lateinit var imgIcon : ImageView

    override fun initUi(model: ListItem) {
        txtTitle.text=model.title
        Picasso.with(itemView.context).load(model.resId).into(imgIcon)
    }
    @OnClick(R.id.root_view)
    fun onViewClicked(){
    }

}

interface ListView {
    fun initUi(model: ListItem)
}