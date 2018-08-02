package com.wellcome.configuration.dto.log

import com.wellcome.configuration.utils.LogType
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable

@Serializable
class LogDtoWrapper(@Serializable(with = PolymorphicSerializer::class) val logDto: LogDto)

@Serializable
data class LogDto(val message: String, val logType: LogType)

