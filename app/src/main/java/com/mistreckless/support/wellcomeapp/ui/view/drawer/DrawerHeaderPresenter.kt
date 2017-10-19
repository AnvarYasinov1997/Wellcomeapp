package com.mistreckless.support.wellcomeapp.ui.view.drawer

import android.util.Log
import com.mistreckless.support.wellcomeapp.domain.interactor.ProfileInteractor
import com.mistreckless.support.wellcomeapp.ui.MainActivityRouter
import com.mistreckless.support.wellcomeapp.ui.model.HeaderItem
import com.mistreckless.support.wellcomeapp.ui.screen.BaseFragmentView
import com.mistreckless.support.wellcomeapp.ui.view.BaseViewHolderPresenter
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by @mistreckless on 30.08.2017. !
 */

class DrawerHeaderPresenter @Inject constructor(private val fragmentView: BaseFragmentView, private val profileInteractor: ProfileInteractor) : BaseViewHolderPresenter<HeaderView, MainActivityRouter, HeaderItem>() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onViewBinded(model: HeaderItem) {
        compositeDisposable.add(profileInteractor.controlCurrentUserData()
                .compose(fragmentView.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe({
                    getView()?.updateUser(it)
                }, {
                    Log.e(TAG, it.message)
                }))
    }

    override fun onViewUnbinded() {
        compositeDisposable.clear()
    }

    fun  headerClicked() {
        getRouter()?.navigateToProfile()
    }

    companion object {
        const val TAG = "DrawerHeaderPresenter"
    }

}