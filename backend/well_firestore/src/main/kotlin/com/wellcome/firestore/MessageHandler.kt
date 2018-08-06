package com.wellcome.firestore

import com.wellcome.configuration.message.*
import com.wellcome.configuration.utils.LoggerHandler
import com.wellcome.configuration.utils.MessageState
import kotlinx.coroutines.experimental.runBlocking

class RpcMessageHandler(private val logger: LoggerHandler) {

    fun handleMessage(messageWrapper: FirestoreRpcSendMessageWrapper): FirestoreRpcReturnMessageWrapper = runBlocking {
        val message = messageWrapper.message
        return@runBlocking FirestoreRpcReturnMessageWrapper(
            when (message) {
                is CreateUserMessage -> UserNotCreatedMessage("test")
            })
    }

    fun handleCast(bytes: ByteArray?) {
        logger.warning("unhandled message ${if (bytes != null) String(bytes) else "null"}")
    }
}

class MessageHandler(private val logger: LoggerHandler) {

    fun handleMessage(messageWrapper: MessageState<FirestoreMessageWrapper>) {

    }
}