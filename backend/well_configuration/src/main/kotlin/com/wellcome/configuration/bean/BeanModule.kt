package com.wellcome.configuration.bean

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.ConnectionFactory
import com.wellcome.configuration.initGoogleMaps
import com.wellcome.configuration.sender.ServiceProperty
import com.wellcome.configuration.sender.createServiceProperty
import org.koin.dsl.module.applicationContext
import org.slf4j.LoggerFactory

fun googleMapsModule() = applicationContext {
    bean { initGoogleMaps(get()) }
}

fun toolsModule(loggerName: String) = applicationContext {
    bean { LoggerFactory.getLogger(loggerName) }
}

fun authRabbitMqModule() = applicationContext {
    bean("auth") {
        createServiceProperty("auth-exchanger", "auth-routing-key")
    }
    bean("auth") {
        val property = get<ServiceProperty>("auth")
        ConnectionFactory().newConnection().createChannel().apply {
            exchangeDeclare(property.exchanger, BuiltinExchangeType.FANOUT)
        }
    }
}
fun loggerRabbitMqModule() = applicationContext {
    bean("logger") {
        createServiceProperty("logger-exchanger", "logger-routing-key")
    }
    bean("logger") {
        val property = get<ServiceProperty>("logger")
        ConnectionFactory().newConnection().createChannel().apply {
            exchangeDeclare(property.exchanger, BuiltinExchangeType.FANOUT)
        }
    }
}

