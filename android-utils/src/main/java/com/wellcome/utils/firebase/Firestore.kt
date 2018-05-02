package com.wellcome.utils.firebase

import com.google.firebase.firestore.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.produce
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

fun <T : Any> DocumentReference.observeValue(options: DocumentListenOptions, clazz: Class<T>, parentContext: CoroutineContext) =
    produce<DocumentState<T>>(parentContext) {
        var registration: ListenerRegistration? = null
        try {
            val listener =
                EventListener<DocumentSnapshot> { documentSnapshot, firebaseFirestoreException ->
                    launch {
                        when {
                            firebaseFirestoreException != null -> send(
                                DocumentError(
                                    firebaseFirestoreException,
                                    this@observeValue
                                )
                            )
                            documentSnapshot != null && !documentSnapshot.exists() -> send(
                                DocumentRemoved(this@observeValue)
                            )
                            documentSnapshot != null -> {
                                val value = documentSnapshot.toObject(clazz)
                                if (value == null) send(
                                    DocumentError(
                                        Exception("cannot parse class " + clazz.canonicalName),
                                        documentSnapshot.reference
                                    )
                                )
                                else send(
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
            registration = this@observeValue.addSnapshotListener(options, listener)
        } finally {
            registration?.remove()
        }
    }

fun<T: Any>Query.observeAddedValues(clazz: Class<T>,options: QueryListenOptions, parentContext: CoroutineContext) = produce<DocumentState<T>>(parentContext) {
    var registration: ListenerRegistration? = null
    try {
        val listener = EventListener<QuerySnapshot> { querySnapshot, exception ->
            launch {
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
        registration = this@observeAddedValues.addSnapshotListener(options,listener)
    }finally {
        registration?.remove()
    }
}