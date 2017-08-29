@file:Suppress("NOTHING_TO_INLINE")

package com.mistreckless.support.wellcomeapp.helper.rxfirebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by @mistreckless on 06.08.2017. !
 */


inline fun <T> setValue(ref: DatabaseReference, value: T): Completable = Completable.create(RxSetValue(ref, value))

inline fun <T>singleQuery(query: Query, clazz: Class<T>) : Single<T> = Single.create(RxSingleQuery(query,clazz))