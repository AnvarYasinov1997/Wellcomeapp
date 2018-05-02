package com.mistreckless.support.wellcomeapp.ui.screen.profile

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mistreckless.support.wellcomeapp.R
import wellcome.common.entity.UserData
import com.wellcome.utils.ui.BaseFragment
import com.wellcome.utils.ui.BaseFragmentView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * Created by @mistreckless on 02.09.2017. !
 */
class Profile : BaseFragment<ProfilePresenter>(), ProfileView {
    override val layoutId: Int
        get() = R.layout.fragment_profile

    @InjectPresenter
    override lateinit var presenter : ProfilePresenter
    @ProvidePresenter fun providePresenter()=presenterProvider.get()

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