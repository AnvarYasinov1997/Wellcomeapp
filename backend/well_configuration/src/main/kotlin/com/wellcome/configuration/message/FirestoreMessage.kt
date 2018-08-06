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
data class CreateLocalityMessage(val firestoreRef: String,
                                 val name: String,
                                 val timezoneId: String) : FirestoreRpcSendMessage()

@Serializable
data class CreateUserMessage(val googleUid: String,
                             val firestoreRef: String,
                             val name: String,
                             val photoUrl: String,
                             val email: String,
                             val localityFirestoreRef: String,
                             val localityName: String) : FirestoreRpcSendMessage()

sealed class FirestoreRpcReturnMessage

@Serializable
object LocalityCreatedMessage : FirestoreRpcReturnMessage()

@Serializable
object LocalityNotCreatedMessage : FirestoreRpcReturnMessage()

@Serializable
object UserCreatedMessage : FirestoreRpcReturnMessage()

@Serializable
object UserNotCreatedMessage : FirestoreRpcReturnMessage()

sealed class FirestoreMessage