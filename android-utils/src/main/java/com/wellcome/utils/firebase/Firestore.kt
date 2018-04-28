package com.wellcome.utils.firebase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.suspendCoroutine

fun <T : Any> DocumentReference.setValue(value: T): Job = launch {
    suspendCoroutine<Unit> { cont ->
        this@setValue.set(value).addOnCompleteListener { task ->
            if (task.isSuccessful) cont.resume(Unit)
            else cont.resumeWithException(task.exception!!)
        }
    }
}

fun <T : Any> DocumentReference.getValue(clazz: Class<T>): Deferred<T> = async {
    suspendCoroutine<T> { cont ->
        this@getValue.get().addOnCompleteListener { task ->
            if (task.isSuccessful) cont.resume(task.result.toObject(clazz)!!)
            else cont.resumeWithException(task.exception!!)
        }
    }
}

fun <T : Any> Query.getValues(clazz: Class<T>): Deferred<List<T>> = async {
    suspendCoroutine<List<T>> { cont ->
        this@getValues.get().addOnCompleteListener { task ->
            val result = task.result
            if (task.isSuccessful) cont.resume(
                if (result.documents.isNotEmpty()) result.toObjects(
                    clazz
                ) else emptyList()
            )
            else cont.resumeWithException(task.exception!!)
        }
    }
}