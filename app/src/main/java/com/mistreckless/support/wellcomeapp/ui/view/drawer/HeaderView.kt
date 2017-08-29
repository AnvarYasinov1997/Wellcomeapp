package com.mistreckless.support.wellcomeapp.ui.view.drawer

import android.view.ViewGroup
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.ui.MainActivity
import com.mistreckless.support.wellcomeapp.ui.model.HeaderItem
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolder


/**
 * Created by @mistreckless on 28.08.2017. !
 */

class HeaderViewHolder( override var presenter: DrawerHeaderPresenter, parent: ViewGroup?, val mainActivity: MainActivity)
    : BaseViewHolder<HeaderItem, DrawerHeaderPresenter>(mainActivity, parent, R.layout.view_drawer_header), HeaderView {

}


interface HeaderView {

}



