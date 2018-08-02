package com.wellcome.auth

import com.rabbitmq.client.Channel
import com.wellcome.configuration.bean.authRabbitMqModule
import com.wellcome.configuration.bean.googleMapsModule
import com.wellcome.configuration.bean.toolsModule
import com.wellcome.configuration.dto.auth.AuthDtoWrapper
import com.wellcome.configuration.dto.auth.InitCityDto
import com.wellcome.configuration.dto.auth.InitUserDto
import com.wellcome.configuration.initFirebaseApp
import com.wellcome.configuration.sender.ServiceProperty
import com.wellcome.configuration.utils.DeliveryState
import com.wellcome.configuration.utils.consume
import com.wellcome.configuration.utils.inject
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.runBlocking
import org.koin.standalone.StandAloneContext.startKoin
import org.slf4j.Logger

fun main(args: Array<String>) = runBlocking {
    startKoin(listOf(authRabbitMqModule(),
        toolsModule("well_auth"),
        googleMapsModule(),
        authModule()))
    val logger by inject<Logger>()
    initFirebaseApp(logger)
    val channel by inject<Channel>()
    val property by inject<ServiceProperty>("auth")
    val queue = channel.queueDeclare().queue
    channel.queueBind(queue, property.exchanger, property.routingKey)

    val job = Job()
    val messageProducer = channel.consume<AuthDtoWrapper>(job, queue)
    job.invokeOnCompletion {
        messageProducer.cancel()
    }
    messageProducer.consumeEach { messageState ->
        when(messageState){
            is DeliveryState -> {
                val authState = messageState.message.authDto
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

