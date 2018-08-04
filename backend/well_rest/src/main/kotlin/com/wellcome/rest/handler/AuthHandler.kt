package com.wellcome.rest.handler

import com.google.firebase.auth.FirebaseToken
import com.rabbitmq.client.Channel
import com.wellcome.configuration.dto.auth.AuthDtoWrapper
import com.wellcome.configuration.dto.auth.InitCityDto
import com.wellcome.configuration.dto.auth.InitUserDto
import com.wellcome.configuration.property.SimpleQueueProperty
import com.wellcome.configuration.utils.sendRpc
import org.slf4j.LoggerFactory

class InitUserAuthHandler(private val channel: Channel,
                          private val property: SimpleQueueProperty) {

    suspend fun handle(param: FirebaseToken) {
        val dto = InitUserDto(
            uid = param.uid
        )
        val result = channel.sendRpc<AuthDtoWrapper, AuthDtoWrapper>(AuthDtoWrapper(dto), property).await()
        LoggerFactory.getLogger("test").info(result.toString())
    }

}

class InitCityAuthHandler(private val channel: Channel,
                          private val property: SimpleQueueProperty) {

    fun handle(param: FirebaseToken, lat: Double, lon: Double) {
        val dto = InitCityDto(
            uid = param.uid,
            lat = lat,
            lon = lon
        )
        channel.sendRpc<AuthDtoWrapper, String>(AuthDtoWrapper(dto), property)
    }

}