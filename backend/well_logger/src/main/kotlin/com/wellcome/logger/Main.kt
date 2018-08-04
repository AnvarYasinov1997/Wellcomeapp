package com.wellcome.logger

import com.rabbitmq.client.Channel
import com.wellcome.configuration.bean.loggerRabbitMqModule
import com.wellcome.configuration.bean.rabbitMqModule
import com.wellcome.configuration.dto.log.LogDto
import com.wellcome.configuration.property.FanoutProperty
import com.wellcome.configuration.utils.*
import com.wellcome.logger.module.loggerModule
import com.wellcome.logger.service.LogService
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.runBlocking
import org.koin.standalone.StandAloneContext.startKoin
import java.time.ZonedDateTime

fun main(args: Array<String>) = runBlocking {
    startKoin(listOf(rabbitMqModule(),
        loggerRabbitMqModule(MicroserviceName.LOGGER),
        loggerModule()))
    val channel by inject<Channel>()
    val property by inject<FanoutProperty>("logger")
    val logService by inject<LogService>()
    val queue = channel.queueDeclare().queue
    channel.queueBind(queue, property.exchanger, "", null)

    val job = Job()
    val messageProducer = channel.consume<LogDto>(job, queue)
    job.invokeOnCompletion {
        messageProducer.cancel()
    }
    messageProducer.consumeEach { messageState ->
        when (messageState) {
            is DeliveryState -> {
                val authState = messageState.message
                logService.handle(authState)
            }
            else             -> logService.handle(LogDto(messageState.toString(),
                LogType.WARN,
                ZonedDateTime.now().toString(),
                MicroserviceName.LOGGER))
        }
    }
}