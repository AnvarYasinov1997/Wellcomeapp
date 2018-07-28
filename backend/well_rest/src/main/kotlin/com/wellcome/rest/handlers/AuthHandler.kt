package com.wellcome.rest.handlers

import com.google.firebase.auth.FirebaseToken
import com.wellcome.dto.auth.AuthDto
import com.wellcome.dto.auth.InitUserDto
import com.wellcome.rest.sender.Sender

class AuthHandler(private val sender: Sender<AuthDto>) {

    fun handle(token: FirebaseToken) {
        val dto = InitUserDto(
            uid = token.uid
        )
        sender.send(dto)
    }

}