

package com.mistreckless.support.wellcomeapp.data.rxfirebase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by @mistreckless on 06.08.2017. !
 */


fun FirebaseAuth.signIn(credential: AuthCredential): Single<FirebaseUser>
        = Single.create(RxSignInWithCredential(this, credential))

fun FirebaseAuth.signOutComplteable() : Completable
        = Completable.create(RxSignOut(this))