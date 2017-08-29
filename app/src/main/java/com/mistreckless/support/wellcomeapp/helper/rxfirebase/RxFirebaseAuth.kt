package com.mistreckless.support.wellcomeapp.helper.rxfirebase

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.CompletableEmitter
import io.reactivex.CompletableOnSubscribe
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe


/**
 * Created by @mistreckless on 06.08.2017. !
 */
class RxSignInWithCredential(private val instance: FirebaseAuth, private val credential: AuthCredential) : SingleOnSubscribe<FirebaseUser> {

    override fun subscribe(emitter: SingleEmitter<FirebaseUser>) {
        val listener = OnCompleteListener<AuthResult> { task ->
            if (!task.isSuccessful) {
                if (!emitter.isDisposed) {
                    emitter.onError(task.exception!!)
                }
                return@OnCompleteListener
            }

            if (!emitter.isDisposed) {
                emitter.onSuccess(task.result.user)
            }
        }

        instance.signInWithCredential(credential)
                .addOnCompleteListener(listener)
    }
}

class RxSignOut(private val instance : FirebaseAuth) : CompletableOnSubscribe{
    override fun subscribe(e: CompletableEmitter) {
        if (!e.isDisposed){
            instance.signOut()

        }
    }

}