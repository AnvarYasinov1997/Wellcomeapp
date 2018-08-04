package com.wellcome.rest.module

import com.wellcome.rest.handler.InitCityAuthHandler
import com.wellcome.rest.handler.InitUserAuthHandler
import com.wellcome.configuration.utils.LoggerHandler
import org.koin.dsl.module.applicationContext

fun handlerModule() = applicationContext {
    factory {
        InitUserAuthHandler(
            get("auth"), get("auth")
        )
    }
    factory {
        InitCityAuthHandler(
            get("auth"), get("auth")
        )
    }
}