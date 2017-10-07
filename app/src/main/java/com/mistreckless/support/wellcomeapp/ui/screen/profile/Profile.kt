package com.mistreckless.support.wellcomeapp.ui.screen.profile

import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import butterknife.BindView
import com.mistreckless.support.wellcomeapp.R
import com.mistreckless.support.wellcomeapp.domain.entity.UserData
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragment
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.screen.Layout
import com.mistreckless.support.wellcomeapp.ui.view.indy.SlidingTabLayout
import com.squareup.picasso.Picasso

/**
 * Created by @mistreckless on 02.09.2017. !
 */
@Layout(id = R.layout.fragment_profile)
class Profile : BaseFragment<ProfilePresenter, ProfilePresenterProviderFactory>(), ProfileView {
    @BindView(R.id.img_user)
    lateinit var imgUser: ImageView
    @BindView(R.id.view_pager)
    lateinit var viewPager: ViewPager
    @BindView(R.id.sliding_tab)
    lateinit var slidingTab: SlidingTabLayout
    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

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