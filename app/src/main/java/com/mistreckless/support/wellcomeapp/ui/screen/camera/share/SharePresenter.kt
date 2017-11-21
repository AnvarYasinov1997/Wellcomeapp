package com.mistreckless.support.wellcomeapp.ui.screen.camera.share

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.mistreckless.support.wellcomeapp.domain.entity.*
import com.mistreckless.support.wellcomeapp.domain.interactor.ShareInteractor
import com.mistreckless.support.wellcomeapp.ui.BasePresenter
import com.mistreckless.support.wellcomeapp.ui.PerFragment
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
@PerFragment
@InjectViewState
class SharePresenter @Inject constructor(private val shareInteractor: ShareInteractor, private val rxPermission: Provider<RxPermissions>) : BasePresenter<ShareView, CameraActivityRouter>() {

    private val sdf by lazy { SimpleDateFormat("dd MMM HH : mm") }
    private val currentTime by lazy { System.currentTimeMillis() }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.apply {
            initUi(shareInteractor.getPhotoBytes())
            showAddressProgressBar()
            showTillTime("till \n" + sdf.format(Date(currentTime + (2 * 60 * 60 * 1000))))
            showFromTime("from \n " + sdf.format(Date(currentTime)))
        }
        findAddress()
    }


    fun timePicked(h: Int, m: Int) {
        val deletedTime = calculateDeleteTime(h, m)
        Log.e(TAG, "continue in minutes" + ((deletedTime - currentTime) / 1000 / 60))
        viewState.showTillTime("till \n" + sdf.format(Date(deletedTime)))
        viewState.showFromTime("from \n " + sdf.format(Date(currentTime)))
    }

    fun shareClicked(addressLine: String, descLine: String, fromTimeLine: String, tillTimeLine: String) {
        shareInteractor.share(addressLine, descLine,
                sdf.parse(fromTimeLine.substring(fromTimeLine.indexOf("\n") + 1)).time,
                sdf.parse(tillTimeLine.substring(tillTimeLine.indexOf("\n") + 1)).time)
                .subscribe {
                    when (it) {
                        is StateInit -> Log.e(TAG, "init")
                        is StateUpload -> Log.e(TAG, "upload " + it.progress)
                        is StateUploaded -> Log.e(TAG, "uploaded url " + it.url)
                        is StateDone -> Log.e(TAG, "done")
                        is StateError -> Log.e(TAG, "error " + it.message)
                    }
                }
    }

    private fun findAddress() {
        rxPermission.get().request(Manifest.permission.ACCESS_FINE_LOCATION)
                .flatMapSingle { granted -> if (granted) shareInteractor.findMyLocation() else Single.just("") }
                .subscribe({
                    if (it.isNotEmpty()) {
                        viewState.apply {
                            showAddress(it)
                            setBtnShareEnabled(true)
                        }
                    }
                }, {
                    Log.e(TAG, it.message)
                })
    }

    private fun calculateDeleteTime(h: Int, m: Int): Long {
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
