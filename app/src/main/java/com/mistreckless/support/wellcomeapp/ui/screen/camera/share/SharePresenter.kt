package com.mistreckless.support.wellcomeapp.ui.screen.camera.share

import android.Manifest
import android.os.Bundle
import android.util.Log
import com.mistreckless.support.wellcomeapp.domain.interactor.ShareInteractor
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.BasePresenterProviderFactory
import com.mistreckless.support.wellcomeapp.ui.PerFragment
import com.mistreckless.support.wellcomeapp.ui.presenterHolder
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivityRouter
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by @mistreckless on 08.10.2017. !
 */

class SharePresenter(private val shareInteractor: ShareInteractor, private val rxPermission: Provider<RxPermissions>) : BasePresenter<ShareView, CameraActivityRouter>() {

    override fun onFirstViewAttached() {
        getView()?.apply {
            initUi()
            showAddressProgressBar()
        }
        findAddress()
    }

    override fun onViewRestored(saveInstanceState: Bundle) {
        val address = saveInstanceState.getString(Share.ADDRESS_KEY)
        val ageNumberLine = saveInstanceState.getString(Share.AGE_NUMBER_KEY)
        val isAgeControl = saveInstanceState.getBoolean(Share.AGE_KEY)
        getView()?.apply {
            if (address != null && address.isNotEmpty())
                showAddress(address)
            else showAddressProgressBar()
            if (isAgeControl && ageNumberLine != null && ageNumberLine.isNotEmpty())
                showAge(ageNumberLine)
            initUi()
        }
    }


    fun controlAge(observable: Observable<Boolean>) {
        viewChangesDisposables.add(observable
                .subscribe { checked ->
                    if (checked) getView()?.showNumberPicker()
                    else getView()?.hideAge()
                })
    }

    fun agePicked(age: Int) {
        getView()?.showAge(age.toString() + "+")
    }

    fun timePicked(h: Int, m: Int) {
        val currentTime = System.currentTimeMillis()
        val pickedTime = calculateTime(h,m)
    }

    private fun findAddress() {
        rxPermission.get().request(Manifest.permission.ACCESS_FINE_LOCATION)
                .flatMapSingle { granted -> if (granted) shareInteractor.findMyLocation() else Single.just("") }
                .subscribe({
                    getView()?.showAddress(it)
                }, {
                    Log.e(TAG, it.message)
                })
    }

    private fun calculateTime(h:Int,m : Int) : Int{

    }

    companion object {
        const val TAG = "SharePresenter"
    }

}

@PerFragment
class SharePresenterProviderFactory @Inject constructor(private val shareInteractor: ShareInteractor,
                                                        private val rxPermission: Provider<RxPermissions>) : BasePresenterProviderFactory<SharePresenter> {
    override fun get(): SharePresenter {
        return if (presenterHolder.contains(SharePresenter.TAG))
            presenterHolder[SharePresenter.TAG] as SharePresenter
        else {
            val presenter = SharePresenter(shareInteractor, rxPermission)
            presenterHolder.put(SharePresenter.TAG, presenter)
            presenter
        }
    }

}