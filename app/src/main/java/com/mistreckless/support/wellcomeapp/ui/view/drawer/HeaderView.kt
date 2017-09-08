package com.mistreckless.support.wellcomeapp.ui.view.drawer

import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.domain.entity.UserData
import com.mistreckless.support.wellcomeapp.ui.MainActivity
import com.mistreckless.support.wellcomeapp.ui.model.HeaderItem
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolder
import com.squareup.picasso.Picasso
import com.subinkrishna.widget.CircularImageView


/**
 * Created by @mistreckless on 28.08.2017. !
 */

class HeaderViewHolder(override var presenter: DrawerHeaderPresenter, parent: ViewGroup?, mainActivity: MainActivity)
    : BaseViewHolder<HeaderItem, DrawerHeaderPresenter>(mainActivity, parent, R.layout.view_drawer_header), HeaderView {

    @BindView(R.id.txt_name)
    lateinit var txtName: TextView
    @BindView(R.id.txt_rating)
    lateinit var txtRating: TextView
    @BindView(R.id.img_user)
    lateinit var imgUser: CircularImageView

    @OnClick(R.id.root_view)
    fun onHeaderClick()= presenter.headerClicked()

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



