package com.wellcome.configuration.dto.log

import com.wellcome.configuration.utils.LogType
import com.wellcome.configuration.utils.MicroserviceName
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
class LogDtoWrapper(@Serializable(with = PolymorphicSerializer::class) val logDto: LogDto)

@Serializable
data class LogDto(val message: String,
                  val logType: LogType,
                  val date: ZonedDateTime,
                  val microserviceName: MicroserviceName)

