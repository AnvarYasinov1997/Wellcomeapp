package com.wellcome.logger.utils

import com.wellcome.configuration.dto.log.LogDto

fun logTemplateGenerator(logDto: LogDto): String = StringBuilder()
    .append("${logDto.microserviceName.name}  ")
    .append("${logDto.date}  ")
    .append("[${logDto.logType}]  ")
    .append(logDto.message)
    .toString()