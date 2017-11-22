package com.mistreckless.support.wellcomeapp.ui.screen.registry

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.jakewharton.rxbinding2.InitialValueObservable
import com.mistreckless.support.wellcomeapp.domain.entity.NewUserState
import com.mistreckless.support.wellcomeapp.domain.interactor.RegistryInteractor
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.PerFragment
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by @mistreckless on 31.07.2017. !
 */

@PerFragment
@InjectViewState
class RegistryPresenter @Inject constructor(private val rxPermissions: Provider<RxPermissions>, private val registryInteractor: RegistryInteractor, private val router: Router) : BasePresenter<RegistryView>() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        viewState.initUi()
        compositeDisposable.add(rxPermissions.get().request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .flatMap { granted -> if (granted) registryInteractor.findMyCity().toObservable() else error("no city") }
                .subscribe({ city: String ->
                    viewState.showCity(city)
                    viewState.setBtnNextEnabled(true)
                }, {
                    Log.e(Registry.TAG, it.message)
                    viewState.setBtnNextEnabled(false)
                }))
    }

    fun finishClicked(name: String, newUserState: NewUserState) {
        compositeDisposable.add(registryInteractor.regUser(name, newUserState)
                .subscribe({
                    router.exitWithResult(Registry.RESULT_OK,null)
                }, { Log.e(Registry.TAG, it.message) }))
    }

    fun controlName(nameChanges: InitialValueObservable<CharSequence>) {
        compositeDisposable.add(nameChanges
                .map { it.toString() }
                .subscribe { viewState.showName(it) })
    }

    fun photoClicked() {
        rxPermissions.get()
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted)
                        viewState.sendIntentToGallery()
                }
    }

    fun resultFromIntent(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Registry.GALLERY_INTENT_CODE && resultCode == Activity.RESULT_OK && data != null)
            registryInteractor.choosePhoto(data)
                    .subscribe({ viewState.setPhoto(it) }, {
                        Log.e(Registry.TAG, it.message)
                    })
    }

    companion object {
        const val TAG = "RegistryPresenter"
    }
}