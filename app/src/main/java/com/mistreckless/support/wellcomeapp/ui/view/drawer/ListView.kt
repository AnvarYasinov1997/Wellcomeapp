package com.mistreckless.support.wellcomeapp.ui.view.drawer

import android.view.ViewGroup
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.MainActivity
import com.mistreckless.support.wellcomeapp.ui.model.ListItem
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolder

/**
 * Created by @mistreckless on 30.08.2017. !
 */

class ListViewHolder(override var presenter: DrawerListPresenter,parent : ViewGroup?,val mainActivity: MainActivity)
    : BaseViewHolder<ListItem,DrawerListPresenter>(mainActivity,parent, R.layout.view_drawer_item), ListView{

}

interface ListView