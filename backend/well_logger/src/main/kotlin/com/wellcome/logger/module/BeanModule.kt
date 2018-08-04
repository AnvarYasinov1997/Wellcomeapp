package com.wellcome.logger.module

import com.wellcome.logger.service.LogService
import org.koin.dsl.module.applicationContext

fun loggerModule() = applicationContext {
    bean {
        LogService(get())
    }
}