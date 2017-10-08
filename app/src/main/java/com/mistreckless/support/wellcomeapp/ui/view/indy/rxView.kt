package com.mistreckless.support.wellcomeapp.ui.view.indy

import android.widget.CheckBox
import io.reactivex.Observable

/**
 * Created by @mistreckless on 08.10.2017. !
 */


fun CheckBox.toObservable(): Observable<Boolean> {
    return Observable.create<Boolean> { e ->
        this.setOnCheckedChangeListener { _, isChecked ->
            if (!e.isDisposed) e.onNext(isChecked)
        }
    }.doOnDispose { this.setOnCheckedChangeListener(null) }
}