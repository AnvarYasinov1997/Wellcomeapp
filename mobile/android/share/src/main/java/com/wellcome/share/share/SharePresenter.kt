package com.wellcome.share.share

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wellcome.core.ui.BasePresenter
import com.wellcome.core.ui.PerFragment
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.cancelAndJoin
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.launch
import ru.terrakok.cicerone.Router
import wellcome.common.entity.StateError
import wellcome.common.entity.StateProgress
import wellcome.common.entity.StateUploaded
import wellcome.common.interactor.ShareInteractor
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Provider
import kotlin.coroutines.experimental.suspendCoroutine

@SuppressLint("SimpleDateFormat")
/**
 * Created by @mistreckless on 08.10.2017. !
 */
@PerFragment
@InjectViewState
class SharePresenter @Inject constructor(
    private val shareInteractor: ShareInteractor,
    private val rxPermission: Provider<RxPermissions>,
    private val router: Router
) : BasePresenter<ShareView>() {

    private val sdf by lazy { SimpleDateFormat("dd MMM HH : mm") }
    private val currentTime by lazy { System.currentTimeMillis() }

    override fun onFirstViewAttach() {
        launch(UI) {
            super.onFirstViewAttach()
            viewState.apply {
                initUi(shareInteractor.getPhotoBytes().await())
                showAddressProgressBar()
                showTillTime("till \n" + sdf.format(Date(currentTime + (2 * 60 * 60 * 1000))))
                showFromTime("from \n " + sdf.format(Date(currentTime)))
            }
            findAddress()
        }
    }

    fun timePicked(h: Int, m: Int) {
        val deletedTime = calculateDeleteTime(h, m)
        Log.e(TAG, "continue in minutes" + ((deletedTime - currentTime) / 1000 / 60))
        viewState.showTillTime("till \n" + sdf.format(Date(deletedTime)))
        viewState.showFromTime("from \n " + sdf.format(Date(currentTime)))
    }

    fun shareClicked(
        addressLine: String,
        descLine: String,
        fromTimeLine: String,
        tillTimeLine: String
    ) {
        launch(UI) {
            val job = Job()
            val producer = shareInteractor.share(
                addressLine, descLine,
                sdf.parse(fromTimeLine.substring(fromTimeLine.indexOf("\n") + 1)).time,
                sdf.parse(tillTimeLine.substring(tillTimeLine.indexOf("\n") + 1)).time,
                coroutineContext,
                job
            )
            producer.consumeEach {
                Log.e("pres", it.toString())
                when (it) {
                    is StateProgress -> Log.e(TAG, "upload " + it.progress)
                    is StateUploaded -> {
                        Log.e(TAG, "uploaded url " + it.url)
                        job.cancelAndJoin()
                        producer.cancel()
                        router.finishChain()
                    }
                    is StateError -> Log.e(TAG, "error " + it.exception.message, it.exception)
                }
            }
        }
    }

    private fun findAddress() {
        launch(UI) {
            val isGranted = suspendCoroutine<Boolean> { cont ->
                rxPermission.get().request(Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe { cont.resume(it) }
            }
            if (isGranted) {
                val address = shareInteractor.findMyLocation().await()
                viewState.apply {
                    showAddress(address)
                    setBtnShareEnabled(true)
                }
            }
        }
    }

    private fun calculateDeleteTime(h: Int, m: Int): Long {
        val currentCalendar = Calendar.getInstance()
        Log.e(
            TAG,
            "day " + currentCalendar.get(Calendar.DAY_OF_MONTH) + " hour " + currentCalendar.get(
                Calendar.HOUR_OF_DAY
            ) + " minute " + currentCalendar.get(Calendar.MINUTE)
        )

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
