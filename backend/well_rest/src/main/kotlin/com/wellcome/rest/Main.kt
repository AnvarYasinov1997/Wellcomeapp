package com.wellcome.rest

import com.wellcome.configuration.bean.authPropertyModule
import com.wellcome.configuration.bean.rabbitmqModule
import com.wellcome.configuration.bean.serializationModule
import com.wellcome.configuration.initFirebaseApp
import com.wellcome.rest.module.*
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.standalone.StandAloneContext.startKoin
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    initFirebaseApp(LoggerFactory.getLogger("main"))
    startKoin(listOf(authPropertyModule(), handlerModule(), serializationModule(), rabbitmqModule(), senderModule()))
    embeddedServer(Netty, commandLineEnvironment(args)).start()
}



