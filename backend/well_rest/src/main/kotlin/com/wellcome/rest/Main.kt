package com.wellcome.rest

import com.wellcome.rest.configs.*
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.standalone.StandAloneContext.startKoin
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    initFirebaseApp(LoggerFactory.getLogger("main"))
    startKoin(listOf(propertyModule(), handlerModule(), serializationModule(), rabbitmqModule(), senderModule()))
    embeddedServer(Netty, commandLineEnvironment(args)).start()
}



