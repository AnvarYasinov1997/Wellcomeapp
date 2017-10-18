package com.mistreckless.support.wellcomeapp.ui

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.mistreckless.support.wellcomeapp.domain.entity.AlreadyRegisteredState
import com.mistreckless.support.wellcomeapp.domain.entity.ErrorState
import com.mistreckless.support.wellcomeapp.domain.entity.NewUserState
import com.mistreckless.support.wellcomeapp.domain.interactor.MainInteractor
import dagger.Lazy
import javax.inject.Inject


/**
 * Created by @mistreckless on 30.07.2017. !
 */
class MainActivityPresenter (val mainInteractor: MainInteractor, val googleApiClient: Lazy<GoogleApiClient>) : BasePresenter<MainActivityView, MainActivityRouter>() {

    override fun onFirstViewAttached() {
        if (mainInteractor.isUserAuthenticated()) {
            getRouter()?.navigateToWall()
        } else getRouter()?.navigateToGoogleAuthDialog(googleApiClient.get())
    }

    fun authResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MainActivity.RC_SIGN_IN
                && resultCode == Activity.RESULT_OK) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            mainInteractor.signUpWithGoogle(result)
                    .subscribe({
                        when (it) {
                            is AlreadyRegisteredState -> {
                                Log.e(TAG, "already reg")
                                getRouter()?.navigateToWall()
                            }
                            is NewUserState -> getRouter()?.navigateToRegistry(it)
                            is ErrorState -> Log.e(MainActivity.TAG, it.message)
                        }

                    }, { Log.e(MainActivity.TAG, it.message) })
        }
    }

    fun destroyed() {
//        if (!mainInteractor.isUserAuthenticated()) {
//            FirebaseAuth.getInstance().signOut()
//            Auth.GoogleSignInApi.signOut(googleApiClient.get())
//        }
    }

    companion object {
        const val TAG = "MainActivityPresenter"
    }

}

class MainActivityPresenterProviderFactory @Inject constructor(val mainInteractor: MainInteractor, val googleApiClient: Lazy<GoogleApiClient>) : BasePresenterProviderFactory<MainActivityPresenter> {
    override fun get(): MainActivityPresenter {
        if (presenterHolder.contains(MainActivityPresenter.TAG))
            return presenterHolder[MainActivityPresenter.TAG] as MainActivityPresenter
        else {
            val presenter = MainActivityPresenter(mainInteractor, googleApiClient)
            presenterHolder.put(MainActivityPresenter.TAG, presenter)
            return presenter
        }
    }
}

