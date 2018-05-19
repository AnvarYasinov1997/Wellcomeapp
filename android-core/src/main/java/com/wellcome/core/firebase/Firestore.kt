package com.wellcome.core.firebase

import com.google.firebase.firestore.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.suspendCoroutine

fun <T : Any> DocumentReference.setValue(value: T): Job = launch {
    suspendCoroutine<Unit> { cont ->
        this@setValue.set(value).addOnCompleteListener { task ->
            if (task.isSuccessful) cont.resume(Unit)
            else cont.resumeWithException(task.exception!!)
        }
    }
}

fun DocumentReference.removeValue(): Job = launch {
    suspendCoroutine<Unit> { cont ->
        this@removeValue.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) cont.resume(Unit)
            else cont.resumeWithException(task.exception!!)
        }
    }
}

suspend fun <T : Any> DocumentReference.updateFields(fields: Map<String, T>) =
    suspendCoroutine<Unit> { cont ->
        this@updateFields.update(fields).addOnCompleteListener { task ->
            if (task.isSuccessful) cont.resume(Unit)
            else cont.resumeWithException(task.exception!!)
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

fun <T : Any> DocumentReference.observeValue(
    options: DocumentListenOptions,
    clazz: Class<T>,
    parentContext: CoroutineContext,
    job: Job
) = produce(parentContext, parent = job) {
    val channel = Channel<DocumentState<T>>()
    val isFirstListener = AtomicBoolean(true)
    val listener =
        this@observeValue.addSnapshotListener(options) { documentSnapshot, firebaseFirestoreException ->
            if (isFirstListener.get()) {
                isFirstListener.set(false)
                return@addSnapshotListener
            }
            launch(coroutineContext) {
                when {
                    firebaseFirestoreException != null -> channel.send(
                        DocumentError(
                            firebaseFirestoreException,
                            this@observeValue
                        )
                    )
                    documentSnapshot != null && !documentSnapshot.exists() -> channel.send(
                        DocumentRemoved(this@observeValue)
                    )
                    documentSnapshot != null -> {
                        val value = documentSnapshot.toObject(clazz)
                        if (value == null) channel.send(
                            DocumentError(
                                Exception("cannot parse class " + clazz.canonicalName),
                                documentSnapshot.reference
                            )
                        )
                        else channel.send(
                            DocumentModified(
                                value,
                                documentSnapshot.metadata,
                                documentSnapshot.reference
                            )
                        )
                    }
                }
            }
        }
    job.invokeOnCompletion(true, true) {
        listener.remove()
        channel.close()
    }
    channel.consumeEach { send(it) }
}

fun <T : Any> Query.observeAddedValues(
    clazz: Class<T>,
    options: QueryListenOptions,
    parentContext: CoroutineContext,
    job: Job
) = produce(parentContext, parent = job) {
    val channel = Channel<DocumentState<T>>()
    val listener =
        this@observeAddedValues.addSnapshotListener(options) { querySnapshot, exception ->
            launch(coroutineContext) {
                if (querySnapshot != null) when {
                    exception != null -> {
                    }
                    else -> querySnapshot.documentChanges.forEach {
                        when (it.type) {
                            DocumentChange.Type.ADDED -> send(
                                DocumentAdded(
                                    it.document.toObject(
                                        clazz
                                    ), querySnapshot.metadata, it.document.reference
                                )
                            )
                            else -> {
                            } //do nothing
                        }
                    }
                }
            }
        }
    job.invokeOnCompletion(true, true) {
        listener.remove()
        channel.close()
    }
    channel.consumeEach { send(it) }
}

fun <T : Any> CollectionReference.observeValues(
    clazz: Class<T>,
    options: QueryListenOptions,
    parentContext: CoroutineContext,
    job: Job
) = produce(parentContext, parent = job) {
    val channel = Channel<DocumentState<T>>()
    val isFirstListener = AtomicBoolean(true)
    val listener = this@observeValues.addSnapshotListener(options) { querySnapshot, exception ->
        if (isFirstListener.get()) {
            isFirstListener.set(false)
            return@addSnapshotListener
        }
        launch(coroutineContext) {
            if (exception != null || querySnapshot == null) channel.send(DocumentError(exception
                    ?: Exception("null ebat"), null))
            else for (change in querySnapshot.documentChanges) when (change.type) {
                DocumentChange.Type.ADDED -> channel.send(DocumentAdded(change.document.toObject(
                    clazz), querySnapshot.metadata, change.document.reference))
                DocumentChange.Type.MODIFIED -> channel.send(DocumentModified(change.document.toObject(
                    clazz), querySnapshot.metadata, change.document.reference))
                DocumentChange.Type.REMOVED -> channel.send(DocumentRemoved(change.document.reference))
            }
        }
    }
    job.invokeOnCompletion(true, true) {
        listener.remove()
        channel.close()
    }

    channel.consumeEach { send(it) }
}