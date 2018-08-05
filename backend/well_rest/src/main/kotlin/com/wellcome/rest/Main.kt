package com.wellcome.rest

import com.wellcome.configuration.bean.loggerRabbitMqModule
import com.wellcome.configuration.bean.mainRabbitMqModule
import com.wellcome.configuration.bean.rabbitMqModule
import com.wellcome.configuration.initFirebaseApp
import com.wellcome.configuration.utils.LoggerHandler
import com.wellcome.configuration.utils.MicroserviceName
import com.wellcome.configuration.utils.inject
import com.wellcome.rest.module.handlerModule
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.standalone.StandAloneContext.startKoin

fun main(args: Array<String>) {
    startKoin(listOf(rabbitMqModule(),
        mainRabbitMqModule(),
        loggerRabbitMqModule(MicroserviceName.REST),
        handlerModule()))
    val logger by inject<LoggerHandler>()
    initFirebaseApp(logger)
    embeddedServer(Netty, commandLineEnvironment(args)).start()
}



