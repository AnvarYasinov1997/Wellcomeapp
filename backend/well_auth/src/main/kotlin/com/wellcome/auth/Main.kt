package com.wellcome.auth

import com.rabbitmq.client.Channel
import com.wellcome.configuration.bean.authRabbitMqModule
import com.wellcome.configuration.bean.googleMapsModule
import com.wellcome.configuration.bean.rabbitMqModule
import com.wellcome.configuration.bean.toolsModule
import com.wellcome.configuration.dto.auth.AuthDtoWrapper
import com.wellcome.configuration.dto.auth.InitUserDto
import com.wellcome.configuration.initFirebaseApp
import com.wellcome.configuration.property.SimpleQueueProperty
import com.wellcome.configuration.utils.inject
import com.wellcome.configuration.utils.receiveRpc
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.runBlocking
import org.koin.standalone.StandAloneContext.startKoin
import org.slf4j.Logger

fun main(args: Array<String>) = runBlocking<Unit> {
    startKoin(listOf(rabbitMqModule(),
        authRabbitMqModule(),
        toolsModule("well_auth"),
        googleMapsModule(),
        authModule()))
    val logger by inject<Logger>()
    initFirebaseApp(logger)
    val channel by inject<Channel>("auth")
    val property by inject<SimpleQueueProperty>("auth")

    val job = Job()
    channel.receiveRpc<AuthDtoWrapper, AuthDtoWrapper>(job, property) {
        logger.info("Yeah")
        AuthDtoWrapper(InitUserDto("Нихера себе"))
    }.join()
}

