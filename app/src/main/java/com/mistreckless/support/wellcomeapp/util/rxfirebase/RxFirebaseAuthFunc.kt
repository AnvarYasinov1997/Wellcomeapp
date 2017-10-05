@file:Suppress("NOTHING_TO_INLINE")

package com.mistreckless.support.wellcomeapp.util.rxfirebase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by @mistreckless on 06.08.2017. !
 */


inline fun signInWithCredential(instance: FirebaseAuth, credential: AuthCredential): Single<FirebaseUser>
        = Single.create(RxSignInWithCredential(instance, credential))

inline fun signOut(instance: FirebaseAuth)
        = Completable.create(RxSignOut(instance))