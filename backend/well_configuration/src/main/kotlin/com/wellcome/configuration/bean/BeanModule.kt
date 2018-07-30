package com.wellcome.configuration.bean

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.ConnectionFactory
import com.wellcome.configuration.initGoogleMaps
import com.wellcome.configuration.property.SenderProperty
import com.wellcome.configuration.property.createSenderProperty
import org.koin.dsl.module.applicationContext
import org.slf4j.LoggerFactory

fun authPropertyModule() = applicationContext {
    bean("auth") {
        val senderProperty = createSenderProperty("microservice.properties", "auth-exchanger", "auth-routing-key")
        senderProperty
    }
}

fun googleMapsModule() = applicationContext {
    bean { initGoogleMaps(get()) }
}

fun toolsModule(loggerName: String) = applicationContext {
    bean { ObjectMapper() }
    bean { LoggerFactory.getLogger(loggerName) }
}

fun rabbitmqModule(queueName: String,
                   durable: Boolean = false,
                   exclusive: Boolean = false,
                   autoDelete: Boolean = false,
                   arguments: Map<String, Any>? = null) = applicationContext {
    bean(queueName) {
        initRabbitMq(queueName, durable, exclusive, autoDelete, arguments)
    }
}

private fun initRabbitMq(queueName: String,
                         durable: Boolean,
                         exclusive: Boolean,
                         autoDelete: Boolean,
                         arguments: Map<String, Any>?) =
    ConnectionFactory().newConnection().createChannel().apply {
        queueDeclare(queueName, durable, exclusive, autoDelete, arguments)
    }