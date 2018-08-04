package com.wellcome.configuration.utils

import com.rabbitmq.client.Channel
import com.wellcome.configuration.dto.log.LogDto
import com.wellcome.configuration.property.FanoutProperty
import java.time.ZonedDateTime

class LoggerHandler(private val channel: Channel,
                    private val property: FanoutProperty,
                    private val microserviceName: MicroserviceName) {

    fun info(message: String) {
        val dto = LogDto(message, LogType.INFO, ZonedDateTime.now().toString(), microserviceName)
        channel.send(dto, property)
    }

    fun warning(message: String) {
        val dto = LogDto(message, LogType.WARN, ZonedDateTime.now().toString(), microserviceName)
        channel.send(dto, property)
    }

    fun error(message: String) {
        val dto = LogDto(message, LogType.ERROR, ZonedDateTime.now().toString(), microserviceName)
        channel.send(dto, property)
    }

}