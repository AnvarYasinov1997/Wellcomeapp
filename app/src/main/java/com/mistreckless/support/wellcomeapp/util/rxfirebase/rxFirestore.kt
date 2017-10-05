package com.mistreckless.support.wellcomeapp.util.rxfirebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import io.reactivex.CompletableEmitter
import io.reactivex.CompletableOnSubscribe

/**
 * Created by @mistreckless on 05.10.2017. !
 */

class RxSetValue<T : Any>(private val ref : DocumentReference, private val value : T) : CompletableOnSubscribe{
    override fun subscribe(e: CompletableEmitter) {
        ref.set(value).addOnCompleteListener {
            if (!e.isDisposed){
                if (it.isSuccessful) e.onComplete() else e.onError(it.exception!!)
            }
        }
    }
}

class RxAddValue<T:Any>(private val ref : CollectionReference, private val value : T) : CompletableOnSubscribe{
    override fun subscribe(e: CompletableEmitter) {
        ref.add(value).addOnCompleteListener {
            if (!e.isDisposed)
                if (it.isSuccessful) e.onComplete() else e.onError(it.exception!!)
        }
    }

}