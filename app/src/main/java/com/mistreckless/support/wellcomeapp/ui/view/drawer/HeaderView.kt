package com.mistreckless.support.wellcomeapp.ui.view.drawer

import android.view.View
import android.view.ViewGroup
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.domain.entity.UserData
import com.mistreckless.support.wellcomeapp.ui.BaseRouter
import com.mistreckless.support.wellcomeapp.ui.MainActivity
import com.mistreckless.support.wellcomeapp.ui.model.HeaderItem
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_drawer_header.*



/**
 * Created by @mistreckless on 28.08.2017. !
 */

class HeaderViewHolder(override var presenter: DrawerHeaderPresenter, parent: ViewGroup?, baseRouter: BaseRouter)
    : BaseViewHolder<HeaderItem, DrawerHeaderPresenter>(baseRouter, parent, R.layout.view_drawer_header), HeaderView {

    override fun onViewBinded(item: Any) {
        super.onViewBinded(item)
        rootView.setOnClickListener { presenter.headerClicked() }
    }

    override fun updateUser(userData: UserData) {
        txtName.text = userData.displayedName
        Picasso.with(itemView.context).load(userData.photoUrl).into(imgUser)
    }

    override fun updateRating(rating: String) {
        txtRating.text = rating
    }

}


interface HeaderView {

    fun updateUser(userData: UserData)

    fun updateRating(rating: String)
}



