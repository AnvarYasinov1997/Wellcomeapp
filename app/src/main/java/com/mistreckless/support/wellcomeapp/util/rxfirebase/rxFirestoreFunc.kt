package com.mistreckless.support.wellcomeapp.util.rxfirebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import io.reactivex.Completable

/**
 * Created by @mistreckless on 05.10.2017. !
 */

fun <T : Any>DocumentReference.setValue(value : T): Completable = Completable.create(RxSetValue(this,value))

fun <T : Any>CollectionReference.addValue(value : T): Completable = Completable.create(RxAddValue(this,value))