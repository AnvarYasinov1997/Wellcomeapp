package com.wellcome.rest.handler

import com.google.firebase.auth.FirebaseToken
import com.rabbitmq.client.Channel
import com.wellcome.configuration.message.*
import com.wellcome.configuration.property.SimpleQueueProperty
import com.wellcome.configuration.utils.RPCErrorState
import com.wellcome.configuration.utils.RPCResultState
import com.wellcome.configuration.utils.sendRpc

class InitUserMainHandler(private val channel: Channel,
                          private val property: SimpleQueueProperty) {

    suspend fun handle(param: FirebaseToken, lat: Double, lon: Double): MainReturnMessage {
        val message = InitUserMessage(
            googleUid = param.uid,
            lat = lat,
            lon = lon
        )
        val rpcResult =
            channel.sendRpc<MainSendMessageWrapper, MainReturnMessageWrapper>(MainSendMessageWrapper(message), property)
                .await()
        return when (rpcResult) {
            is RPCResultState -> rpcResult.result.mainReturnMessage
            is RPCErrorState  -> UserNotInitedMessage("RPC error ${rpcResult.exception.message}")
        }
    }

}