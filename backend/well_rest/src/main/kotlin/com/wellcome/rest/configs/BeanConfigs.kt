package com.wellcome.rest.configs

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.ConnectionFactory
import com.wellcome.rest.handlers.AuthHandler
import com.wellcome.rest.property.createSenderProperty
import com.wellcome.rest.sender.AuthSender
import org.koin.dsl.module.applicationContext

fun propertyModule() = applicationContext {
    bean("auth") {
        createSenderProperty("microservice.properties", "auth-exchanger", "auth-routing-key")
    }
}

fun handlerModule() = applicationContext {
    bean("auth") {
        AuthHandler(AuthSender(
            objectMapper = ObjectMapper(),
            senderProperty = createSenderProperty("microservice.properties", "auth-exchanger", "auth-routing-key"),
            connectionFactory = ConnectionFactory()))
    }
}