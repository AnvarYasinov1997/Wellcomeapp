package com.wellcome.maps

import com.wellcome.configuration.message.FindLocalityMessage
import com.wellcome.configuration.message.LocalityNotFinded
import com.wellcome.configuration.message.MapsReturnMessageWrapper
import com.wellcome.configuration.message.MapsSendMessageWrapper
import com.wellcome.configuration.utils.LoggerHandler

class MessageHandler(private val logger: LoggerHandler) {

    fun handleMessage(messageWrapper: MapsSendMessageWrapper): MapsReturnMessageWrapper {
        val message = messageWrapper.message
        logger.info(message.toString())
        return when (message) {
            is FindLocalityMessage -> MapsReturnMessageWrapper(LocalityNotFinded("test"))
        }
    }

    fun handleCast(bytes: ByteArray?) {
        logger.warning("unhandled message ${if (bytes != null) String(bytes) else "null"}")
    }
}