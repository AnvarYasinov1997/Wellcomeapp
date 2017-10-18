package com.mistreckless.support.wellcomeapp.util.rxfirebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by @mistreckless on 05.10.2017. !
 */

fun <T : Any>DocumentReference.setValue(value : T): Completable = Completable.create(RxSetValue(this,value))

fun <T : Any>CollectionReference.addValue(value : T): Completable = Completable.create(RxAddValue(this,value))

fun <T> Query.getValues(clazz: Class<T>) : Single<MutableList<T>> = Single.create(RxQuery(this,clazz))

fun<T>DocumentReference.observeValue(clazz: Class<T>) : Observable<T> = Observable.create(RxDocumentObserver(this,clazz))