package com.wellcome.configuration.bean

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.wellcome.configuration.property.*
import com.wellcome.configuration.utils.LoggerHandler
import com.wellcome.configuration.utils.MicroserviceName
import com.wellcome.configuration.utils.inject
import io.ktor.application.Application
import org.koin.dsl.module.applicationContext

fun rabbitMqModule() = applicationContext {
    bean { ConnectionFactory().newConnection() }
}

fun firebaseAppModule() = applicationContext {
    bean {
        val logger by inject<LoggerHandler>()
        val databaseUrl = "https://wellcomeapp-cc11e.firebaseio.com"
        val serviceAccount = "wellcomeapp-cc11e-firebase-adminsdk-qoxy4-310b8e400c.json"
        val options = FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(Application::class.java.classLoader.getResourceAsStream(
                serviceAccount)))
            .setDatabaseUrl(databaseUrl)
            .build()
        FirebaseApp.initializeApp(options).also { _ ->
            logger.info("firebase app initialized")
        }
    }
}

fun mainRabbitMqModule() = applicationContext {
    bean("main") {
        createSimpleQueueProperty("main-queue")
    }
    factory("main") {
        val property = get<SimpleQueueProperty>("main")
        val connection = get<Connection>()
        connection.createChannel().apply {
            queueDeclare(property.queue, false, false, false, null)
        }
    }
}

fun loggerRabbitMqModule(microserviceName: MicroserviceName) = applicationContext {
    bean("logger") {
        createFanoutProperty("logger-exchanger")
    }
    bean("logger") {
        val property = get<FanoutProperty>("logger")
        val connection = get<Connection>()
        connection.createChannel().apply {
            exchangeDeclare(property.exchanger, BuiltinExchangeType.FANOUT)
        }
    }
    bean {
        LoggerHandler(get("logger"), get("logger"), microserviceName)
    }
}

fun mapsRabbitMqModule() = applicationContext {
    bean("maps") {
        createSimpleQueueProperty("maps-queue")
    }
    factory("maps") {
        val property = get<SimpleQueueProperty>("maps")
        val connection = get<Connection>()
        connection.createChannel().apply {
            queueDeclare(property.queue, false, false, false, null)
        }
    }
}

fun firestoreRabbitMqModule() = applicationContext {
    bean("firestore-rpc") {
        createSimpleQueueProperty("firestore-rpc-queue")
    }
    factory("firestore-rpc") {
        val property = get<SimpleQueueProperty>("firestore-rpc")
        val connection = get<Connection>()
        connection.createChannel().apply {
            queueDeclare(property.queue, false, false, false, null)
        }
    }
    bean("firestore") {
        createDurableFanoutProperty("firestore-queue", "firestore-exchanger")
    }
    bean("firestore") {
        val property = get<DurableFanoutProperty>("firestore")
        val connection = get<Connection>()
        connection.createChannel().apply {
            queueDeclare(property.queue, true, false, false, null)
            exchangeDeclare(property.exchanger, BuiltinExchangeType.FANOUT, true)
        }
    }
}

