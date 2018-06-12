package com.wellcomeapp

import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.DocumentChange
import com.google.firebase.cloud.FirestoreClient
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.launch
import wellcome.common.core.FirebaseConstants
import wellcome.common.entity.CityData
import wellcome.common.entity.EventData

private sealed class EventState
private data class EventAdded(val refPath: String, val deleteTime: Long): EventState()
private data class EventRemoved(val refPath: String): EventState()

fun startObserve(message: suspend (state: Message) -> Unit) = launch {
    val job = Job()
    val db = FirestoreClient.getFirestore()

    val cityProducer = db.collection(FirebaseConstants.CITY).listenCities(job)
    job.invokeOnCompletion {
        println("cityProducer close")
        cityProducer.cancel()
    }

    cityProducer.consumeEach { city ->
        launch {
            val eventProducer = db
                .collection(FirebaseConstants.CITY)
                .document(city.ref)
                .collection(FirebaseConstants.EVENT)
                .listenEvents(job)

            job.invokeOnCompletion {
                println("eventProducer close")
                eventProducer.cancel()
            }
            eventProducer.consumeEach {state ->
                println("producer $state")
                when(state){
                    is EventAdded -> message(MessageAdd(state.refPath, state.deleteTime, city.zoneId))
                    is EventRemoved -> message(MessageRemove(state.refPath))
                }
            }
        }
    }
}

private suspend fun CollectionReference.listenCities(parentJob: Job) = produce(parent = parentJob) {
    val channel = Channel<CityData>()
    val listener = this@listenCities.addSnapshotListener { snapshot, error ->
        launch {
            when {
                error != null -> println("ERROR listenCities ${error.message}")
                snapshot != null && snapshot.documentChanges.isNotEmpty() -> snapshot.documentChanges.forEach { change ->
                    when (change.type) {
                        DocumentChange.Type.ADDED -> {
                            val city = change.document.toObject(CityData::class.java)
                            channel.send(city)
                        }
                        DocumentChange.Type.MODIFIED -> println("MODIFIED CITY ${change.document.reference}")
                        DocumentChange.Type.REMOVED -> println("REMOVE CITY ${change.document.reference}")
                    }
                }
                else -> println("CITY snapshot null!")
            }
        }
    }

    parentJob.invokeOnCompletion {
        println("listenCities close")
        listener.remove()
        channel.close()
    }

    channel.consumeEach {
        println("channel $it")
        send(it)
    }
}

private suspend fun CollectionReference.listenEvents(parentJob: Job) = produce(parent = parentJob) {
    val channel = Channel<EventState>()
    val listener = this@listenEvents.addSnapshotListener { snapshot, error ->
        launch {
            if (snapshot != null) when {
                error != null -> {
                    println("ERROR listenEvents ${error.message}")
                }
                else -> snapshot.documentChanges.forEach { change ->
                    when (change.type) {
                        DocumentChange.Type.ADDED -> {
                            val event = change.document.toObject(EventData::class.java)
                            println("event $event")
                            channel.send(EventAdded(change.document.reference.path, event.contents.last().deleteTime))
                        }
                        DocumentChange.Type.REMOVED -> {
                            val event = change.document.toObject(EventData::class.java)
                            println("event $event")
                            channel.send(EventRemoved(change.document.reference.path))
                        }
                        DocumentChange.Type.MODIFIED -> {
                        }
                    }
                }
            } else println("snapshot null")
        }
    }
    parentJob.invokeOnCompletion {
        println("listenEvents close")
        listener.remove()
        channel.close()
    }
    channel.consumeEach {
        println("channel $it")
        send(it)
    }
}