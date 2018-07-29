package com.wellcome.configuration.bean

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.ConnectionFactory
import com.wellcome.configuration.property.createSenderProperty
import org.koin.dsl.module.applicationContext

fun authPropertyModule() = applicationContext {
    bean("auth") {
        createSenderProperty("microservice.properties", "auth-exchanger", "auth-routing-key")
    }
}

fun serializationModule() = applicationContext {
    bean { ObjectMapper() }
}

fun rabbitmqModule() = applicationContext {
    bean { ConnectionFactory() }
}