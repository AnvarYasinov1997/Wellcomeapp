package com.wellcome.main

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.WriteBatch
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

fun <T : Any> Query.getValues(clazz: Class<T>): Deferred<List<T>> = async {
    val snapshot = this@getValues.get().get()
    if (snapshot.isEmpty) emptyList<T>()
    else snapshot.toObjects(clazz)
}

fun <T : Any> DocumentReference.setValue(value: T): Job = launch {
    this@setValue.set(value).get()
}

fun DocumentReference.updateFields(fields: Map<String, Any>): Job = launch {
    this@updateFields.update(fields).get()
}

fun WriteBatch.commitOperations(): Job = launch {
    this@commitOperations.commit().get()
}