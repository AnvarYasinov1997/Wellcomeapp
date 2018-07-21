package com.wellcome.utils.firebase

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.suspendCoroutine

suspend fun FirebaseAuth.signIn(credential: AuthCredential) : Deferred<FirebaseUser> = async {
    suspendCoroutine<FirebaseUser> {
        val listener = OnCompleteListener<AuthResult> { task ->
            if (task.isSuccessful) it.resume(task.result.user)
            else it.resumeWithException(task.exception!!)
        }
        this@signIn.signInWithCredential(credential).addOnCompleteListener(listener)
    }
}

fun FirebaseAuth.signOutJob() : Job = launch {
    this@signOutJob.signOut()
}

fun FirebaseUser.getFirebaseToken(): Deferred<GetTokenResult> = async {
    suspendCoroutine<GetTokenResult> { cont ->
        this@getFirebaseToken.getIdToken(true).addOnSuccessListener { token ->
            cont.resume(token)
        }.addOnFailureListener { err ->
            cont.resumeWithException(err)
        }
    }
}