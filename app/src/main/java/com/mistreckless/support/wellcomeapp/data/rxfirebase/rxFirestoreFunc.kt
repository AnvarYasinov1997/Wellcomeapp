package com.mistreckless.support.wellcomeapp.data.rxfirebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentListenOptions
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

fun <T : Any>DocumentReference.setValue(value : T): Completable = Completable.create(RxSetValue(this,value))

fun <T>DocumentReference.getValue(clazz: Class<T>) : Single<T> = Single.create(RxGetValue(this,clazz))

fun <T : Any>CollectionReference.addValue(value : T): Completable = Completable.create(RxAddValue(this,value))

fun <T> Query.getValues(clazz: Class<T>) : Single<List<T>> = Single.create(RxQuery(this,clazz))

fun<T>DocumentReference.observeValue(options: DocumentListenOptions,clazz: Class<T>) : Observable<T> = Observable.create(RxDocumentObserver(this,options,clazz))

