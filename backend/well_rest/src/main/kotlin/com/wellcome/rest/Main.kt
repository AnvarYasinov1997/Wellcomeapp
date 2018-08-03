package com.wellcome.rest

import com.wellcome.configuration.bean.authRabbitMqModule
import com.wellcome.configuration.bean.loggerRabbitMqModule
import com.wellcome.configuration.bean.toolsModule
import com.wellcome.configuration.initFirebaseApp
import com.wellcome.configuration.utils.inject
import com.wellcome.rest.module.handlerModule
import com.wellcome.rest.module.senderModule
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.standalone.StandAloneContext.startKoin
import org.slf4j.Logger

fun main(args: Array<String>) {
    startKoin(listOf(authRabbitMqModule(),
        loggerRabbitMqModule(),
        handlerModule(),
        toolsModule("well_rest"),
        senderModule()))
    val logger by inject<Logger>()
    initFirebaseApp(logger)
    embeddedServer(Netty, commandLineEnvironment(args)).start()
}



