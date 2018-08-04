package com.wellcome.configuration.dto.log

import com.wellcome.configuration.utils.LogType
import com.wellcome.configuration.utils.MicroserviceName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class LogDto(val message: String,
                  val logType: LogType,
                  val date: String,
                  val microserviceName: MicroserviceName)

