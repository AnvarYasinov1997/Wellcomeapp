package com.wellcomeapp

import com.google.cloud.firestore.DocumentChange
import com.google.cloud.firestore.Query
import com.google.firebase.cloud.FirestoreClient
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.launch
import wellcome.common.core.FirebaseConstants
import wellcome.common.entity.EventData

sealed class Message
data class MessageAdd(val ref: String, val deleteTime: Long) : Message()
data class MessageRemove(val ref: String) : Message()

fun startObserve(message: suspend (state: Message) -> Unit) = launch {
    val job = Job()
    val db = FirestoreClient.getFirestore()
    val producer = db.collection("${FirebaseConstants.CITY}/{cityId}/${FirebaseConstants.EVENT}").listenValues(job)
    producer.consumeEach { message(it) }
}

private suspend inline fun Query.listenValues(parentJob: Job) = produce(parent = parentJob) {
    val channel = Channel<Message>()
    val listener = this@listenValues.addSnapshotListener { snapshot, error ->
        launch {
            if (snapshot != null) when {
                error != null -> {
                }
                else -> snapshot.documentChanges.forEach { change ->
                    when (change.type) {
                        DocumentChange.Type.ADDED -> {
                            val event = change.document.toObject(EventData::class.java)
                            channel.send(MessageAdd(event.ref, event.contents.last().deleteTime))
                        }
                        DocumentChange.Type.REMOVED -> {
                            val event = change.document.toObject(EventData::class.java)
                            channel.send(MessageRemove(event.ref))
                        }
                        DocumentChange.Type.MODIFIED -> {
                        }
                    }
                }
            }
        }
    }
    parentJob.invokeOnCompletion {
        listener.remove()
        channel.close()
    }
    channel.consumeEach { send(it) }
}