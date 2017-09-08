package com.mistreckless.support.wellcomeapp.ui.screen.profile

import com.mistreckless.support.wellcomeapp.domain.interactor.ProfileInteractor
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.MainActivityRouter
import com.mistreckless.support.wellcomeapp.ui.PerFragment
import javax.inject.Inject

/**
 * Created by @mistreckless on 02.09.2017. !
 */
@PerFragment
class ProfilePresenter @Inject constructor(private val profileInteractor: ProfileInteractor) : BasePresenter<ProfileView, MainActivityRouter>() {
    override fun onFirstViewAttached() {
        profileInteractor.controlCurrentUserData()
                .subscribe { getView()?.initUi(it) }
    }
}