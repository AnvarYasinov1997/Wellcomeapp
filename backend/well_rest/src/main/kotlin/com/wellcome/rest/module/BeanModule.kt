package com.wellcome.rest.module

import com.wellcome.configuration.sender.Sender
import com.wellcome.rest.handler.InitCityAuthHandler
import com.wellcome.rest.handler.InitUserAuthHandler
import com.wellcome.rest.handler.LoggerHandler
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
    bean {
        LoggerHandler(
            get("logger")
        )
    }
}

fun senderModule() = applicationContext {
    bean("auth") {
        Sender(get("auth"), get("auth"))
    }
    bean("logger") {
        Sender(get("logger"), get("logger"))
    }
}