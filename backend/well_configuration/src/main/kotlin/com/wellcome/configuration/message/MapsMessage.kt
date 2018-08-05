package com.wellcome.configuration.message

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable

@Serializable
data class MapsSendMessageWrapper(@Serializable(with = PolymorphicSerializer::class) val message: MapsSendMessage)

@Serializable
data class MapsReturnMessageWrapper(@Serializable(with = PolymorphicSerializer::class) val message: MapsReturnMessage)

sealed class MapsSendMessage
@Serializable
data class FindLocalityMessage(val lat: Double, val lon: Double) : MapsSendMessage()


sealed class MapsReturnMessage
@Serializable
data class LocalityFindedMessage(val locality: String,
                                 val timezoneId: String) : MapsReturnMessage()

@Serializable
data class LocalityNotFinded(val reason: String) : MapsReturnMessage()