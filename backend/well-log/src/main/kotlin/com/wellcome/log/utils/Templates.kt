package com.wellcome.log.utils

import com.wellcome.configuration.utils.LogType
import com.wellcome.configuration.utils.MicroserviceName
import java.time.ZonedDateTime

fun logTemplateGenerator(message: String,
                         logType: LogType,
                         date: ZonedDateTime,
                         microserviceName: MicroserviceName): String = StringBuilder()
    .append("${microserviceName.name}  ")
    .append("$date  ")
    .append("[$logType]  ")
    .append(message)
    .toString()