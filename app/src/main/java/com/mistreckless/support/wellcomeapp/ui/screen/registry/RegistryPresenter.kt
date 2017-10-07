package com.mistreckless.support.wellcomeapp.ui.screen.registry

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.util.Log
import com.jakewharton.rxbinding2.InitialValueObservable
import com.mistreckless.support.wellcomeapp.domain.entity.NewUserState
import com.mistreckless.support.wellcomeapp.domain.interactor.RegistryInteractor
import com.mistreckless.support.wellcomeapp.ui.*
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by @mistreckless on 31.07.2017. !
 */
class RegistryPresenter (private val rxPermissions: Provider<RxPermissions>, private val registryInteractor: RegistryInteractor) : BasePresenter<RegistryView, MainActivityRouter>() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onFirstViewAttached() {
        getView()?.initUi()
        compositeDisposable.add(rxPermissions.get().request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .flatMap { granted -> if (granted) registryInteractor.findMyCity().toObservable() else error("no city") }
                .subscribe({ city: String ->
                    getView()?.showCity(city)
                    getView()?.setBtnNextEnabled(true)
                }, {
                    Log.e(Registry.TAG, it.message)
                    getView()?.setBtnNextEnabled(false)
                }))
    }

    fun finishClicked(name: String, newUserState: NewUserState) {
        compositeDisposable.add(registryInteractor.regUser(name, newUserState)
                .subscribe({
                    getRouter()?.navigateToWall()
                    compositeDisposable.dispose()
                }, { Log.e(Registry.TAG, it.message) }))
    }

    fun controlName(nameChanges: InitialValueObservable<CharSequence>) {
        compositeDisposable.add(nameChanges
                .map { it.toString() }
                .subscribe { getView()?.showName(it) })
    }

    fun photoClicked() {
        rxPermissions.get()
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted)
                        getView()?.sendIntentToGallery()
                }
    }

    fun resultFromIntent(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Registry.GALLERY_INTENT_CODE && resultCode == Activity.RESULT_OK && data != null)
            registryInteractor.choosePhoto(data)
                    .subscribe({ getView()?.setPhoto(it) }, {
                        Log.e(Registry.TAG, it.message)
                    })
    }

    companion object {
        const val TAG="RegistryPresenter"
    }
}

@PerFragment
class RegistryPresenterProviderFactory @Inject constructor(private val rxPermissions: Provider<RxPermissions>, private val registryInteractor: RegistryInteractor) : BasePresenterProviderFactory<RegistryPresenter>{
    override fun get(): RegistryPresenter {
        if (presenterHolder.contains(RegistryPresenter.TAG))
            return presenterHolder[RegistryPresenter.TAG] as RegistryPresenter
        else{
            val presenter = RegistryPresenter(rxPermissions, registryInteractor)
            presenterHolder.put(RegistryPresenter.TAG,presenter)
            return presenter
        }
    }

}