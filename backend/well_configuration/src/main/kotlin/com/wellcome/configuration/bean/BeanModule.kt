package com.wellcome.configuration.bean

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.wellcome.configuration.initGoogleMaps
import com.wellcome.configuration.property.DirectProperty
import com.wellcome.configuration.property.SimpleQueueProperty
import com.wellcome.configuration.property.createDirectProperty
import com.wellcome.configuration.property.createSimpleQueueProperty
import org.koin.dsl.module.applicationContext
import org.slf4j.LoggerFactory

fun googleMapsModule() = applicationContext {
    bean { initGoogleMaps(get()) }
}

fun toolsModule(loggerName: String) = applicationContext {
    bean { LoggerFactory.getLogger(loggerName) }
}

fun rabbitMqModule() = applicationContext {
    bean { ConnectionFactory().newConnection() }
}

fun authRabbitMqModule() = applicationContext {
    bean("auth") {
        createSimpleQueueProperty("auth-queue")
    }
    factory("auth") {
        val property = get<SimpleQueueProperty>("auth")
        val connection = get<Connection>()
        connection.createChannel().apply {
            queueDeclare(property.queue, false, false, false, null)
        }
    }
    loggerRabbitMqModule()
}

fun loggerRabbitMqModule() = applicationContext {
    bean("logger") {
        createDirectProperty("logger-exchanger", "logger-routing-key")
    }
    bean("logger") {
        val property = get<DirectProperty>("logger")
        val connection = get<Connection>()
        connection.createChannel().apply {
            exchangeDeclare(property.exchanger, BuiltinExchangeType.FANOUT)
        }
    }
}

