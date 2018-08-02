package com.wellcome.logger

import com.rabbitmq.client.Channel
import com.wellcome.configuration.bean.loggerRabbitMqModule
import com.wellcome.configuration.bean.toolsModule
import com.wellcome.configuration.dto.log.LogDto
import com.wellcome.configuration.sender.ServiceProperty
import com.wellcome.configuration.utils.DeliveryState
import com.wellcome.configuration.utils.consume
import com.wellcome.configuration.utils.inject
import com.wellcome.logger.module.loggerModule
import com.wellcome.logger.service.LogService
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.runBlocking
import org.koin.standalone.StandAloneContext.startKoin
import org.slf4j.Logger

fun main(args: Array<String>) = runBlocking {
    startKoin(listOf(loggerRabbitMqModule(),
        toolsModule("well_log"),
        loggerModule()))
    val logger by inject<Logger>()
    val channel by inject<Channel>()
    val property by inject<ServiceProperty>("logger")
    val logHandler by inject<LogService>()
    val queue = channel.queueDeclare().queue
    channel.queueBind(queue, property.exchanger, property.routingKey)

    val job = Job()
    val messageProducer = channel.consume<LogDto>(job, queue)
    job.invokeOnCompletion {
        messageProducer.cancel()
    }
    messageProducer.consumeEach { messageState ->
        when (messageState) {
            is DeliveryState -> {
                val authState = messageState.message
                logHandler.handle(authState)
            }
            else -> logger.info(messageState.toString())
        }
    }
}