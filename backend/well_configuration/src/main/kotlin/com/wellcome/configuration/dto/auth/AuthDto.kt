package com.wellcome.configuration.dto.auth

sealed class AuthDto
data class InitUserDto(val uid: String) : AuthDto()
data class InitCityDto(val uid: String,
                       val lat: Double,
                       val lon: Double) : AuthDto()