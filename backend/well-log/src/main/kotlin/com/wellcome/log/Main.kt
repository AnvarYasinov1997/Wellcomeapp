package com.wellcome.log

import com.rabbitmq.client.Channel
import com.wellcome.configuration.bean.authRabbitMqModule
import com.wellcome.configuration.bean.toolsModule
import com.wellcome.configuration.dto.log.LogDtoWrapper
import com.wellcome.configuration.sender.ServiceProperty
import com.wellcome.configuration.utils.DeliveryState
import com.wellcome.configuration.utils.consume
import com.wellcome.configuration.utils.inject
import com.wellcome.log.handler.LogHandler
import com.wellcome.log.module.handlerModule
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.runBlocking
import org.koin.standalone.StandAloneContext.startKoin
import org.slf4j.Logger

fun main(args: Array<String>) = runBlocking {
    startKoin(listOf(authRabbitMqModule(),
        toolsModule("well_log"),
        handlerModule()))
    val logger by inject<Logger>()
    val channel by inject<Channel>()
    val property by inject<ServiceProperty>("auth")
    val logHandler by inject<LogHandler>()
    val queue = channel.queueDeclare().queue
    channel.queueBind(queue, property.exchanger, property.routingKey)

    val job = Job()
    val messageProducer = channel.consume<LogDtoWrapper>(job, queue)
    job.invokeOnCompletion {
        messageProducer.cancel()
    }
    messageProducer.consumeEach { messageState ->
        when (messageState) {
            is DeliveryState -> {
                val authState = messageState.message.logDto
                logHandler.handle(authState)
            }
            else -> logger.info(messageState.toString())
        }
    }
}