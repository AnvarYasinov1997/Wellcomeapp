package com.wellcome.rest

import com.wellcome.configuration.bean.authRabbitMqModule
import com.wellcome.configuration.bean.toolsModule
import com.wellcome.configuration.initFirebaseApp
import com.wellcome.rest.module.handlerModule
import com.wellcome.rest.module.senderModule
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.standalone.StandAloneContext.startKoin
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    initFirebaseApp(LoggerFactory.getLogger("main"))
    startKoin(listOf(authRabbitMqModule(),
        handlerModule(),
        toolsModule("well_rest"),
        senderModule()))
    embeddedServer(Netty, commandLineEnvironment(args)).start()
}



