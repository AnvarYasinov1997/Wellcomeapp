package com.wellcome.log.module

import com.wellcome.log.handler.LogHandler
import org.koin.dsl.module.applicationContext

fun handlerModule() = applicationContext {
    bean {
        LogHandler(get())
    }
}