package com.wellcome.rest.handlers

import com.google.firebase.auth.FirebaseToken
import com.wellcome.configuration.dto.auth.AuthDtoWrapper
import com.wellcome.configuration.dto.auth.InitCityDto
import com.wellcome.configuration.dto.auth.InitUserDto
import com.wellcome.configuration.sender.Sender

class InitUserAuthHandler(private val sender: Sender) {

    fun handle(param: FirebaseToken) {
        val dto = InitUserDto(
            uid = param.uid
        )
        sender.send(AuthDtoWrapper(dto))
    }

}

class InitCityAuthHandler(private val sender: Sender) {

    fun handle(param: FirebaseToken, lat: Double, lon: Double) {
        val dto = InitCityDto(
            uid = param.uid,
            lat = lat,
            lon = lon
        )
        sender.send(AuthDtoWrapper(dto))
    }

}