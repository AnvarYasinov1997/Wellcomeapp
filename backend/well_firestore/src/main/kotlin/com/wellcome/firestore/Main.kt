package com.wellcome.firestore

import com.google.firebase.cloud.FirestoreClient
import com.rabbitmq.client.Channel
import com.wellcome.configuration.bean.firebaseAppModule
import com.wellcome.configuration.bean.firestoreRabbitMqModule
import com.wellcome.configuration.bean.loggerRabbitMqModule
import com.wellcome.configuration.bean.rabbitMqModule
import com.wellcome.configuration.message.FirestoreMessageWrapper
import com.wellcome.configuration.property.DurableFanoutProperty
import com.wellcome.configuration.property.SimpleQueueProperty
import com.wellcome.configuration.utils.MicroserviceName
import com.wellcome.configuration.utils.inject
import com.wellcome.configuration.utils.receive
import com.wellcome.configuration.utils.receiveRpc
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.joinAll
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.koin.dsl.module.applicationContext
import org.koin.standalone.StandAloneContext.startKoin

fun main(args: Array<String>) = runBlocking<Unit> {
    startKoin(listOf(firestoreModule(),
        firebaseAppModule(),
        rabbitMqModule(),
        firestoreRabbitMqModule(),
        loggerRabbitMqModule(MicroserviceName.FIRESTORE)))

    val parentRpcJob = Job()
    val rpcMessageHandler by inject<RpcMessageHandler>()
    val rpcChannel by inject<Channel>("firestore-rpc")
    val rpcProperty by inject<SimpleQueueProperty>("firestore-rpc")

    val rpcJob = rpcChannel
        .receiveRpc(parentRpcJob, rpcProperty, rpcMessageHandler::handleCast, rpcMessageHandler::handleMessage)

    val parentJob = Job()
    val messageHandler by inject<MessageHandler>()
    val channel by inject<Channel>("firestore")
    val property by inject<DurableFanoutProperty>("firestore")

    val messageProducer = channel.receive<FirestoreMessageWrapper>(parentJob, property.queue, true)
    parentJob.invokeOnCompletion {
        messageProducer.cancel()
    }

    val messageJob = launch {
        messageProducer.consumeEach(messageHandler::handleMessage)
    }

    joinAll(rpcJob, messageJob)
}

fun firestoreModule() = applicationContext {
    bean { FirestoreClient.getFirestore(get()) }
    bean { FirestoreServiceImpl(get(), get()) as FirestoreService }
    bean { RpcMessageHandler(get(), get()) }
    bean { MessageHandler(get()) }
}