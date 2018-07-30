package com.wellcome.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.Channel
import com.wellcome.configuration.bean.authPropertyModule
import com.wellcome.configuration.bean.googleMapsModule
import com.wellcome.configuration.bean.rabbitmqModule
import com.wellcome.configuration.bean.toolsModule
import com.wellcome.configuration.dto.auth.AuthDto
import com.wellcome.configuration.dto.auth.InitCityDto
import com.wellcome.configuration.dto.auth.InitUserDto
import com.wellcome.configuration.initFirebaseApp
import com.wellcome.configuration.property.SenderProperty
import com.wellcome.configuration.utils.DeliveryState
import com.wellcome.configuration.utils.QueueName
import com.wellcome.configuration.utils.consume
import com.wellcome.configuration.utils.inject
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.runBlocking
import org.koin.standalone.StandAloneContext.startKoin
import org.slf4j.Logger

fun main(args: Array<String>) = runBlocking {
    startKoin(listOf(authPropertyModule(),
        toolsModule("well_auth"),
        rabbitmqModule(QueueName.AUTH_QUEUE),
        googleMapsModule(),
        authModule()))
    val logger by inject<Logger>()
    initFirebaseApp(logger)
    val channel by inject<Channel>()
    val property by inject<SenderProperty>("auth")
    channel.queueBind(QueueName.AUTH_QUEUE, property.exchanger, property.routingKey)
    val objectMapper by inject<ObjectMapper>()
    val job = Job()
    val messageProducer = channel.consume(job, objectMapper, AuthDto::class.java, QueueName.AUTH_QUEUE)
    job.invokeOnCompletion {
        messageProducer.cancel()
    }
    messageProducer.consumeEach { messageState ->
        when(messageState){
            is DeliveryState -> {
                val authState = messageState.message
                when(authState){
                    is InitUserDto -> {
                        logger.info(authState.uid)
                    }
                    is InitCityDto -> {
                        logger.info("${authState.uid} ${authState.lat} ${authState.lon}")
                    }
                }
            }
            else -> logger.info(messageState.toString())
        }
    }
}

