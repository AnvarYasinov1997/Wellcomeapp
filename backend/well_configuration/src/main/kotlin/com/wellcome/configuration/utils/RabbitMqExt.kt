package com.wellcome.configuration.utils

import com.rabbitmq.client.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.serialization.json.JSON

sealed class MessageState<T>
data class DeliveryState<T>(val consumerTag: String,
                            val envelope: Envelope,
                            val properties: AMQP.BasicProperties?,
                            val message: T) : MessageState<T>()

data class RecoverOkState<T>(val consumerTag: String) : MessageState<T>()
data class ConsumeOkState<T>(val consumerTag: String) : MessageState<T>()
data class ShutdownSignalState<T>(val consumerTag: String, val sig: ShutdownSignalException) : MessageState<T>()
data class CancelState<T>(val consumerTag: String) : MessageState<T>()
data class CancelOkState<T>(val consumerTag: String) : MessageState<T>()
data class ErrorState<T>(val exception: Exception) : MessageState<T>()

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