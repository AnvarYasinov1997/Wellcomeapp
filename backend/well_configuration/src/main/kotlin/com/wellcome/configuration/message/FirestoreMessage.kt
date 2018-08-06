package com.wellcome.configuration.message

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable

@Serializable
data class FirestoreRpcSendMessageWrapper(@Serializable(with = PolymorphicSerializer::class) val message: FirestoreRpcSendMessage)

@Serializable
data class FirestoreRpcReturnMessageWrapper(@Serializable(with = PolymorphicSerializer::class) val message: FirestoreRpcReturnMessage)

@Serializable
data class FirestoreMessageWrapper(@Serializable(with = PolymorphicSerializer::class) val message: FirestoreMessage)

sealed class FirestoreRpcSendMessage

@Serializable
data class CreateUserMessage(val googleUid: String) : FirestoreRpcSendMessage() //todo

sealed class FirestoreRpcReturnMessage

@Serializable
object UserCreatedMessage : FirestoreRpcReturnMessage() //todo

@Serializable
data class UserNotCreatedMessage(val reason: String) : FirestoreRpcReturnMessage() //todo

sealed class FirestoreMessage