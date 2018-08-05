package com.wellcome.logger.module

import com.wellcome.logger.service.LogService
import org.koin.dsl.module.applicationContext
import org.slf4j.LoggerFactory

fun loggerModule() = applicationContext {
    bean { LoggerFactory.getLogger("logger") }
    bean {
        LogService(get())
    }
}