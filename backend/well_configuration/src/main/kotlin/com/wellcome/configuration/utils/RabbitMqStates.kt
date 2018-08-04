package com.wellcome.configuration.utils

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Envelope
import com.rabbitmq.client.ShutdownSignalException

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


sealed class RPCState<T>
data class RPCResultState<T>(val result: T) : RPCState<T>()
data class RPCErrorState<T>(val exception: Exception) : RPCState<T>()