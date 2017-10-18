package com.mistreckless.support.wellcomeapp.ui.screen.camera.share

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.mistreckless.support.wellcomeapp.domain.entity.*
import com.mistreckless.support.wellcomeapp.domain.interactor.ShareInteractor
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.BasePresenterProviderFactory
import com.mistreckless.support.wellcomeapp.ui.PerFragment
import com.mistreckless.support.wellcomeapp.ui.presenterHolder
import com.mistreckless.support.wellcomeapp.ui.screen.camera.CameraActivityRouter
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

@SuppressLint("SimpleDateFormat")
/**
 * Created by @mistreckless on 08.10.2017. !
 */

class SharePresenter(private val shareInteractor: ShareInteractor, private val rxPermission: Provider<RxPermissions>) : BasePresenter<ShareView, CameraActivityRouter>() {

    private val sdf by lazy { SimpleDateFormat("dd MMM HH : mm") }
    private val currentTime by lazy { System.currentTimeMillis() }

    override fun onFirstViewAttached() {
        getView()?.apply {
            initUi(shareInteractor.getPhotoBytes())
            showAddressProgressBar()
            showTillTime("till \n" + sdf.format(Date(currentTime + (2 * 60 * 60 * 1000))))
            showFromTime("from \n " + sdf.format(Date(currentTime)))
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
            else {
                showAddressProgressBar()
                findAddress()
            }
            if (isAgeControl && ageNumberLine != null && ageNumberLine.isNotEmpty())
                showAge(ageNumberLine)
            initUi(shareInteractor.getPhotoBytes())
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
        val deletedTime = calculateDeleteTime(h, m)
        Log.e(TAG, "continue in minutes" + ((deletedTime - currentTime) / 1000 / 60))
        getView()?.showTillTime("till \n" + sdf.format(Date(deletedTime)))
        getView()?.showFromTime("from \n " + sdf.format(Date(currentTime)))
    }

    fun shareClicked(addressLine: String, descLine: String, isDressControl: Boolean, isAgeControl: Boolean, ageLine: String, fromTimeLine: String, tillTimeLine: String) {
        shareInteractor.share(addressLine,descLine,isDressControl,isAgeControl,ageLine,
                sdf.parse(fromTimeLine.substring(fromTimeLine.indexOf("\n")+1)).time,
                sdf.parse(tillTimeLine.substring(tillTimeLine.indexOf("\n")+1)).time)
                .subscribe {
                    when(it){
                        is StateInit ->Log.e(TAG,"init")
                        is StateUpload ->Log.e(TAG,"upload "+it.progress)
                        is StateUploaded -> Log.e(TAG,"uploaded url " + it.url)
                        is StateDone->Log.e(TAG,"done")
                        is StateError -> Log.e(TAG,"error "+it.message)
                    }
                }
    }

    private fun findAddress() {
        rxPermission.get().request(Manifest.permission.ACCESS_FINE_LOCATION)
                .flatMapSingle { granted -> if (granted) shareInteractor.findMyLocation() else Single.just("") }
                .subscribe({
                    if (it.isNotEmpty()) {
                        getView()?.apply {
                            showAddress(it)
                            setBtnShareEnabled(true)
                        }
                    }
                }, {
                    Log.e(TAG, it.message)
                })
    }

    private fun calculateDeleteTime(h: Int, m: Int): Long {
//        val offset = Calendar.getInstance().get(Calendar.ZONE_OFFSET)
//        Log.e(TAG, "offset " + (offset / 1000 / 60 / 60.0))
        val currentCalendar = Calendar.getInstance()
        Log.e(TAG, "day " + currentCalendar.get(Calendar.DAY_OF_MONTH) + " hour " + currentCalendar.get(Calendar.HOUR_OF_DAY) + " minute " + currentCalendar.get(Calendar.MINUTE))

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, h)
            set(Calendar.MINUTE, m)
        }

        val currentTime = currentCalendar.time.time
        val deleteTime = calendar.time.time

        return if (deleteTime > currentTime) deleteTime
        else deleteTime + (24 * 60 * 60 * 1000)
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