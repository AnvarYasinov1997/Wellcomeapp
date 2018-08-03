package com.wellcome.rest.handler

import com.wellcome.configuration.dto.log.LogDto
import com.wellcome.configuration.sender.Sender
import com.wellcome.configuration.utils.LogType
import com.wellcome.configuration.utils.MicroserviceName
import java.time.ZonedDateTime

class LoggerHandler(private val sender: Sender) {

    fun sendInfo(message: String) {
        sender.send(
            LogDto(message,
                LogType.INFO,
                ZonedDateTime.now().toString(),
                MicroserviceName.REST
            )
        )
    }

}