package com.wellcome.rest.handlers

import com.google.firebase.auth.FirebaseToken
import com.wellcome.dto.auth.AuthDto
import com.wellcome.dto.auth.InitCityDto
import com.wellcome.dto.auth.InitUserDto
import com.wellcome.rest.sender.Sender

class InitUserAuthHandler(private val sender: Sender<AuthDto>) {

    fun handle(param: FirebaseToken) {
        val dto = InitUserDto(
            uid = param.uid
        )
        sender.send(dto)
    }

}

class InitCityAuthHandler(private val sender: Sender<AuthDto>) {

    fun handle(param: FirebaseToken, lat: Double, lon: Double) {
        val dto = InitCityDto(
            uid = param.uid,
            lat = lat,
            lon = lon
        )
        sender.send(dto)
    }

}