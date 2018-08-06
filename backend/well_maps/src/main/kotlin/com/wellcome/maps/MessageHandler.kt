package com.wellcome.maps

import com.wellcome.configuration.message.FindLocalityMessage
import com.wellcome.configuration.message.MapsReturnMessageWrapper
import com.wellcome.configuration.message.MapsSendMessageWrapper
import com.wellcome.configuration.utils.LoggerHandler
import kotlinx.coroutines.experimental.runBlocking

class MessageHandler(private val logger: LoggerHandler,
                     private val mapsService: MapsService) {

    fun handleMessage(messageWrapper: MapsSendMessageWrapper): MapsReturnMessageWrapper = runBlocking {
        val message = messageWrapper.message
        logger.info(message.toString())
        return@runBlocking MapsReturnMessageWrapper(
            when (message) {
                is FindLocalityMessage -> mapsService.findLocality(message).await()
            })
    }

    fun handleCast(bytes: ByteArray?) {
        logger.warning("unhandled message ${if (bytes != null) String(bytes) else "null"}")
    }
}