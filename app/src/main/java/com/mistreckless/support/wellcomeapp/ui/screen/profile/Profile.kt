package com.mistreckless.support.wellcomeapp.ui.screen.profile

import android.support.v7.widget.Toolbar
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.domain.entity.UserData
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * Created by @mistreckless on 02.09.2017. !
 */
@Layout(id = R.layout.fragment_profile)
class Profile : BaseFragment<ProfilePresenter, ProfilePresenterProviderFactory>(), ProfileView {

    override fun getCurrentToolbar(): Toolbar? {
        return toolbar
    }

    override fun initUi(userData: UserData) {
        Picasso.with(context).load(userData.photoUrl).into(imgUser)
    }

    companion object {
        const val TAG ="Profile"
    }
}


interface ProfileView : BaseFragmentView {
    fun initUi(userData: UserData)

}