package com.wellcome.configuration.dto.auth

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable

@Serializable
class AuthDtoWrapper(@Serializable(with = PolymorphicSerializer::class) val authDto: AuthDto)

sealed class AuthDto

@Serializable
data class InitUserDto(val uid: String) : AuthDto()

@Serializable
data class InitCityDto(val uid: String,
                       val lat: Double,
                       val lon: Double) : AuthDto()
