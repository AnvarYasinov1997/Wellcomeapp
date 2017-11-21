package com.mistreckless.support.wellcomeapp.ui.screen.profile

import com.arellomobile.mvp.InjectViewState
import com.mistreckless.support.wellcomeapp.domain.interactor.ProfileInteractor
import com.mistreckless.support.wellcomeapp.ui.*
import javax.inject.Inject

/**
 * Created by @mistreckless on 02.09.2017. !
 */
@PerFragment
@InjectViewState
class ProfilePresenter @Inject constructor(private val profileInteractor: ProfileInteractor) : BasePresenter<ProfileView, MainActivityRouter>() {
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        profileInteractor.controlCurrentUserData()
                .subscribe { viewState.initUi(it) }
    }

    companion object {
        const val TAG = "ProfilePresenter"
    }
}
