package com.wellcome.firestore

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.WriteBatch
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlin.coroutines.experimental.CoroutineContext

sealed class FirestoreState
data class FirestoreSuccess(val writeResult: String) : FirestoreState()
data class FirestoreError(val exception: Exception) : FirestoreState()

fun <T : Any> Query.getValues(clazz: Class<T>): Deferred<List<T>> = async {
    val snapshot = this@getValues.get().get()
    if (snapshot.isEmpty) emptyList<T>()
    else snapshot.toObjects(clazz)
}

fun <T : Any> DocumentReference.setValue(context: CoroutineContext, value: T) = async(context) {
    successOrError {
        this@setValue.set(value).get().toString()
    }

}

fun DocumentReference.updateFields(context: CoroutineContext, fields: Map<String, Any>) = async(context) {
    successOrError {
        this@updateFields.update(fields).get().toString()
    }

}

fun WriteBatch.commitOperations(context: CoroutineContext) = async(context) {
    successOrError {
        this@commitOperations.commit().get().toString()
    }
}


private inline fun successOrError(block: () -> String) = try {
    FirestoreSuccess(block())
} catch (e: Exception) {
    FirestoreError(e)
}