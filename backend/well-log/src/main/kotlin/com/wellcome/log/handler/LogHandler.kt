package com.wellcome.log.handler

import com.wellcome.configuration.dto.log.LogDto
import com.wellcome.log.utils.logTemplateGenerator
import java.util.logging.Logger

class LogHandler(private val logger: Logger) {

    fun handle(logDto: LogDto) = logger.info(logTemplateGenerator(logDto.message, logDto.logType, logDto.date, logDto.microserviceName))

}