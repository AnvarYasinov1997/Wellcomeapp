package com.wellcome.rest

import com.google.firebase.FirebaseApp
import com.wellcome.configuration.bean.firebaseAppModule
import com.wellcome.configuration.bean.loggerRabbitMqModule
import com.wellcome.configuration.bean.mainRabbitMqModule
import com.wellcome.configuration.bean.rabbitMqModule
import com.wellcome.configuration.utils.MicroserviceName
import com.wellcome.configuration.utils.inject
import com.wellcome.rest.module.handlerModule
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.standalone.StandAloneContext.startKoin

fun main(args: Array<String>) {
    startKoin(listOf(rabbitMqModule(),
        firebaseAppModule(),
        mainRabbitMqModule(),
        loggerRabbitMqModule(MicroserviceName.REST),
        handlerModule()))
    inject<FirebaseApp>()
    embeddedServer(Netty, commandLineEnvironment(args)).start()
}



