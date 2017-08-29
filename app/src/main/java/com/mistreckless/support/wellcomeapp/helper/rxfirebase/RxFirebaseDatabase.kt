package com.mistreckless.support.wellcomeapp.helper.rxfirebase

import com.google.firebase.database.*
import io.reactivex.CompletableEmitter
import io.reactivex.CompletableOnSubscribe
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe

/**
 * Created by @mistreckless on 06.08.2017. !
 */

class RxSetValue<out T>(val ref: DatabaseReference, val value: T) : CompletableOnSubscribe {
    override fun subscribe(e: CompletableEmitter) {
        ref.setValue(value)
                .addOnCompleteListener({
                    if (!e.isDisposed) {
                        if (!it.isSuccessful)
                            e.onError(it.exception!!)
                        else e.onComplete()
                    }
                })
    }

}

class RxSingleQuery<T>(val query: Query, val clazz: Class<T>) : SingleOnSubscribe<T> {
    override fun subscribe(e: SingleEmitter<T>) {
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                if (!e.isDisposed) {
                    if (p0 != null && p0.exists()) {
                        val value: T? = p0.getValue(clazz)
                        if (value == null)
                            e.onError(Exception("cannot parse firebase "+clazz.canonicalName))
                        else e.onSuccess(value)
                    }else e.onError(Exception("entity doesn't exists"))
                }
            }

            override fun onCancelled(p0: DatabaseError?) {
                if (!e.isDisposed)
                    e.onError(p0?.toException() ?: Exception("firebase nullable ex"))
            }

        })
    }

}