package com.wellcome.rest.utils

import com.wellcome.rest.utils.Paths.API

object Paths {
    const val API = "/api"
}

object PathsV1 {

    const val APPLICATION_VERSION = "$API/v1"

    const val INIT_USER = "$APPLICATION_VERSION/auth/initUser"
}