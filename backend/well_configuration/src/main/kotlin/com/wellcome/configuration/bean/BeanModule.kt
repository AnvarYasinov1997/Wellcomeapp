package com.wellcome.configuration.bean

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.wellcome.configuration.initGoogleMaps
import com.wellcome.configuration.property.FanoutProperty
import com.wellcome.configuration.property.SimpleQueueProperty
import com.wellcome.configuration.property.createFanoutProperty
import com.wellcome.configuration.property.createSimpleQueueProperty
import com.wellcome.configuration.utils.LoggerHandler
import com.wellcome.configuration.utils.MicroserviceName
import org.koin.dsl.module.applicationContext

fun googleMapsModule() = applicationContext {
    bean { initGoogleMaps(get()) }
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

