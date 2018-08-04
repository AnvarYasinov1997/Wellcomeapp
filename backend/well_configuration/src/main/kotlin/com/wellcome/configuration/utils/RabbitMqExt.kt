package com.wellcome.configuration.utils

import com.rabbitmq.client.*
import com.wellcome.configuration.property.DirectProperty
import com.wellcome.configuration.property.FanoutProperty
import com.wellcome.configuration.property.SimpleQueueProperty
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.serialization.json.JSON
import kotlin.coroutines.experimental.suspendCoroutine

inline fun <reified T : Any> Channel.consume(job: Job, queueName: String, autoAck: Boolean = false) = produce {
    val coroutineChannel = kotlinx.coroutines.experimental.channels.Channel<MessageState<T>>()
    job.invokeOnCompletion {
        coroutineChannel.close()
    }

    val queueConsumer = object : DefaultConsumer(this@consume) {
        override fun handleDelivery(consumerTag: String,
                                    envelope: Envelope,
                                    properties: AMQP.BasicProperties?,
                                    body: ByteArray) = runBlocking {
            try {
                val message = JSON.parse<T>(String(body))
                coroutineChannel.send(DeliveryState(consumerTag, envelope, properties, message))
            } catch (e: Exception) {
                coroutineChannel.send(ErrorState(e))
            }
        }

        override fun handleRecoverOk(consumerTag: String) = runBlocking {
            coroutineChannel.send(RecoverOkState(consumerTag))
        }

        override fun handleConsumeOk(consumerTag: String) = runBlocking {
            coroutineChannel.send(ConsumeOkState(consumerTag))
        }

        override fun handleShutdownSignal(consumerTag: String, sig: ShutdownSignalException) = runBlocking {
            coroutineChannel.send(ShutdownSignalState(consumerTag, sig))
        }

        override fun handleCancel(consumerTag: String) = runBlocking {
            coroutineChannel.send(CancelState(consumerTag))
        }

        override fun handleCancelOk(consumerTag: String) = runBlocking {
            coroutineChannel.send(CancelOkState(consumerTag))
        }
    }

    basicConsume(queueName, autoAck, queueConsumer)

    coroutineChannel.consumeEach {
        send(it)
    }
}

inline fun <reified T : Any> Channel.send(message: T, property: DirectProperty) =
    basicPublish(property.exchanger, property.routingKey, null, JSON.stringify(message).toByteArray())

inline fun <reified T : Any> Channel.send(message: T, property: FanoutProperty) =
    basicPublish(property.exchanger, "", null, JSON.stringify(message).toByteArray())

inline fun <reified T : Any, reified R : Any> Channel.sendRpc(message: T, property: SimpleQueueProperty) = async {
    val rpcClient = RpcClient(this@sendRpc, "", property.queue)
    val result = suspendCoroutine<RPCState<R>> { cont ->
        try {
            val bytes = rpcClient.primitiveCall(JSON.stringify(message).toByteArray())
            val result = JSON.parse<R>(String(bytes))
            cont.resume(RPCResultState(result))
        } catch (e: Exception) {
            cont.resume(RPCErrorState(e))
        }
    }
    rpcClient.close()
    rpcClient.channel.close()
    return@async result
}

inline fun <reified T : Any, reified R : Any> Channel.receiveRpc(job: Job,
                                                                 property: SimpleQueueProperty,
                                                                 crossinline handleCast: (bytes: ByteArray?) -> Unit = {},
                                                                 crossinline handleMessage: (message: T) -> R) =
    launch(job) {
        val rpcServer = object : RpcServer(this@receiveRpc, property.queue) {
            override fun handleCall(requestBody: ByteArray, replyProperties: AMQP.BasicProperties): ByteArray {
                val request = JSON.parse<T>(String(requestBody))
                return JSON.stringify(handleMessage(request)).toByteArray()
            }

            override fun handleCast(requestBody: ByteArray?) {
                handleCast(requestBody)
            }
        }
        job.invokeOnCompletion {
            rpcServer.terminateMainloop()
            rpcServer.channel.close()
        }
        rpcServer.mainloop()
    }