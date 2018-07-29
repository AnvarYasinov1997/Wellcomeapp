package com.wellcome.rest.configs

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.ConnectionFactory
import com.wellcome.dto.auth.AuthDto
import com.wellcome.rest.handlers.InitCityAuthHandler
import com.wellcome.rest.handlers.InitUserAuthHandler
import com.wellcome.rest.property.createSenderProperty
import com.wellcome.rest.sender.Sender
import com.wellcome.rest.sender.SenderImpl
import org.koin.dsl.module.applicationContext

fun propertyModule() = applicationContext {
    bean("auth") {
        createSenderProperty("microservice.properties", "auth-exchanger", "auth-routing-key")
    }
}

fun handlerModule() = applicationContext {
    bean {
        InitUserAuthHandler(
            get()
        )
    }
    bean {
        InitCityAuthHandler(
            get()
        )
    }
}

fun serializationModule() = applicationContext {
    bean { ObjectMapper() }
}

fun rabbitmqModule() = applicationContext {
    bean { ConnectionFactory() }
}

fun senderModule() = applicationContext {
    bean {
        SenderImpl<AuthDto>(
            objectMapper = get(),
            senderProperty = get("auth"),
            connectionFactory = get()
        ) as Sender<AuthDto>
    }
}