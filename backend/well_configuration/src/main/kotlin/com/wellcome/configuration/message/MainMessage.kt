package com.wellcome.configuration.message

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable

@Serializable
data class MainSendMessageWrapper(@Serializable(with = PolymorphicSerializer::class) val mainSendMessage: MainSendMessage)

@Serializable
data class MainReturnMessageWrapper(@Serializable(with = PolymorphicSerializer::class) val mainReturnMessage: MainReturnMessage)

sealed class MainSendMessage
@Serializable
data class InitUserMessage(val googleUid: String,
                           val lat: Double,
                           val lon: Double) : MainSendMessage()


sealed class MainReturnMessage
@Serializable
data class UserInitedMessage(val firestoreRef: String,
                             val cityRef: String,
                             val googleUid: String,
                             val cityName: String) : MainReturnMessage()

@Serializable
data class UserNotInitedMessage(val reason: String) : MainReturnMessage()


