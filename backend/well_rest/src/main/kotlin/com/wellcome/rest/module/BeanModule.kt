package com.wellcome.rest.module

import com.wellcome.configuration.dto.auth.AuthDto
import com.wellcome.rest.handlers.InitCityAuthHandler
import com.wellcome.rest.handlers.InitUserAuthHandler
import com.wellcome.rest.sender.Sender
import com.wellcome.rest.sender.SenderImpl
import org.koin.dsl.module.applicationContext

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

fun senderModule() = applicationContext {
    bean {
        SenderImpl<AuthDto>(
            objectMapper = get(),
            senderProperty = get("auth"),
            connectionFactory = get()
        ) as Sender<AuthDto>
    }
}