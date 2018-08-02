package com.wellcome.logger.service

import com.wellcome.configuration.dto.log.LogDto
import com.wellcome.configuration.utils.LogType
import com.wellcome.logger.utils.logTemplateGenerator
import org.slf4j.Logger


class LogService(private val logger: Logger) {

    fun handle(logDto: LogDto) {
        when (logDto.logType) {
            LogType.INFO -> logger.info(logTemplateGenerator(logDto))
            LogType.WARN -> logger.warn(logTemplateGenerator(logDto))
            LogType.ERROR -> logger.error(logTemplateGenerator(logDto))
        }
    }

}