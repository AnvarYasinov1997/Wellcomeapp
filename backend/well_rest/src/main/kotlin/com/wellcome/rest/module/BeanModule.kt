package com.wellcome.rest.module

import com.wellcome.rest.handler.InitUserMainHandler
import org.koin.dsl.module.applicationContext

fun handlerModule() = applicationContext {
    factory {
        InitUserMainHandler(
            get("main"), get("main")
        )
    }
}