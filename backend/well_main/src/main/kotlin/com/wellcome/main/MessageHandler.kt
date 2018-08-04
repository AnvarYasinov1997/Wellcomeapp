package com.wellcome.main

import com.wellcome.configuration.message.InitUserMessage
import com.wellcome.configuration.message.MainReturnMessageWrapper
import com.wellcome.configuration.message.MainSendMessageWrapper
import com.wellcome.configuration.message.UserNotInitedMessage
import com.wellcome.configuration.utils.LoggerHandler

class MessageHandler(private val logger: LoggerHandler) {

    fun handleMessage(messageWrapper: MainSendMessageWrapper): MainReturnMessageWrapper {
        val message = messageWrapper.mainSendMessage
        logger.info(message.toString())
        return when (message) {
            is InitUserMessage -> MainReturnMessageWrapper(UserNotInitedMessage("test"))
        }
    }

    fun handleCast(bytes: ByteArray?) {
        logger.warning("unhandled message ${if (bytes != null) String(bytes) else "null"}")
    }
}