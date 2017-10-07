package com.mistreckless.support.wellcomeapp.ui.screen.profile

import com.mistreckless.support.wellcomeapp.domain.interactor.ProfileInteractor
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.BasePresenterProviderFactory
import com.mistreckless.support.wellcomeapp.ui.MainActivityRouter
import com.mistreckless.support.wellcomeapp.ui.presenterHolder
import javax.inject.Inject

/**
 * Created by @mistreckless on 02.09.2017. !
 */
class ProfilePresenter (private val profileInteractor: ProfileInteractor) : BasePresenter<ProfileView, MainActivityRouter>() {
    override fun onFirstViewAttached() {
        profileInteractor.controlCurrentUserData()
                .subscribe { getView()?.initUi(it) }
    }

    companion object {
        const val TAG ="ProfilePresenter"
    }
}

class ProfilePresenterProviderFactory @Inject constructor(private val profileInteractor: ProfileInteractor) : BasePresenterProviderFactory<ProfilePresenter>{
    override fun get(): ProfilePresenter {
        if (presenterHolder.contains(ProfilePresenter.TAG))
            return presenterHolder[ProfilePresenter.TAG] as ProfilePresenter
        else{
            val presenter = ProfilePresenter(profileInteractor)
            presenterHolder.put(ProfilePresenter.TAG,presenter)
            return presenter
        }
    }

}