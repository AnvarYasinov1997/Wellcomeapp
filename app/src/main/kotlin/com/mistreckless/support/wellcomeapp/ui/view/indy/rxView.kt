package com.mistreckless.support.wellcomeapp.ui.view.indy

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.CheckBox
import io.reactivex.Observable
import io.reactivex.disposables.Disposables

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

fun RecyclerView.observeScroll(initialValue : Int =0): Observable<Int> = Observable.create<Int> { e ->
    if (!e.isDisposed)
        e.onNext(initialValue)
    val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            if (!e.isDisposed) {
                val position = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                val needToEmit = layoutManager.itemCount!=0 && position * 100 / layoutManager.itemCount > 70
                if (needToEmit) e.onNext(layoutManager.itemCount)
            }
        }
    }
    addOnScrollListener(listener)
    e.setDisposable(Disposables.fromAction { removeOnScrollListener(listener) })
}
