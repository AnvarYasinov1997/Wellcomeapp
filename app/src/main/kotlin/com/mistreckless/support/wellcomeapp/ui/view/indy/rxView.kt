package com.mistreckless.support.wellcomeapp.ui.view.indy

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.CheckBox
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.launch

/**
 * Created by @mistreckless on 08.10.2017. !
 */


fun CheckBox.toObservable(): Observable<Boolean> {
    return Observable.create<Boolean> { e ->
        this.setOnCheckedChangeListener { _, isChecked ->
            if (!e.isDisposed) e.onNext(isChecked)
        }
        e.setDisposable(Disposables.fromAction { setOnCheckedChangeListener(null) })
    }
}

fun RecyclerView.observeScroll(initialValue: Int = 0): Observable<Int> =
    Observable.create<Int> { e ->
        if (!e.isDisposed) e.onNext(initialValue)
        val listener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (!e.isDisposed) {
                    val position =
                        (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val needToEmit =
                        layoutManager.itemCount != 0 && position * 100 / layoutManager.itemCount > 70
                    if (needToEmit) e.onNext(layoutManager.itemCount)
                }
            }
        }
        addOnScrollListener(listener)
        e.setDisposable(Disposables.fromAction { removeOnScrollListener(listener) })
    }

fun RecyclerView.observeScroll(job: Job, initialValue: Int = 0) =
    produce(context = UI, parent = job) {
        val channel = Channel<Int>()
        val listener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                launch(UI) {
                    val position =
                        (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val needToEmit =
                        layoutManager.itemCount != 0 && position * 100 / layoutManager.itemCount > 70
                    if (needToEmit) channel.send(layoutManager.itemCount)
                }
            }
        }
        addOnScrollListener(listener)
        job.invokeOnCompletion {
            Log.e("recycler","canceled")
            removeOnScrollListener(listener)
            channel.close()
        }
        launch(context = UI) {
            channel.send(initialValue)
        }
        channel.consumeEach { Log.e("recycler","send $it");send(it) }
    }
