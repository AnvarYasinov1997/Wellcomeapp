package com.wellcome.rest.module

import com.wellcome.configuration.sender.Sender
import com.wellcome.rest.handler.InitCityAuthHandler
import com.wellcome.rest.handler.InitUserAuthHandler
import org.koin.dsl.module.applicationContext

fun handlerModule() = applicationContext {
    bean {
        InitUserAuthHandler(
            get("auth")
        )
    }
    bean {
        InitCityAuthHandler(
            get("auth")
        )
    }
}

fun senderModule() = applicationContext {
    bean("auth") {
        Sender(get("auth"), get("auth"))
    }
}