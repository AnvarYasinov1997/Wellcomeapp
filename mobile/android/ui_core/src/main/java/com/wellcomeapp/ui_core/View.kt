package com.wellcomeapp.ui_core

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.text.SimpleDateFormat
import java.util.*


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
                Log.e("recycler", "canceled")
                removeOnScrollListener(listener)
                channel.close()
            }
            launch(context = UI) {
                channel.send(initialValue)
            }
            channel.consumeEach { Log.e("recycler", "send $it");send(it) }
        }

inline fun View.setDelayedClickListener(millis: Long, crossinline block: () -> Unit) {
    var isEnabled = true
    this@setDelayedClickListener.setOnClickListener {
        launch {
            if (isEnabled) {
                val job = launch(UI) {
                    block.invoke()
                }
                isEnabled = false
                job.join()
                delay(millis)
                isEnabled = true
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun Long.toTime() = SimpleDateFormat("HH:mm").format(Date(this))

