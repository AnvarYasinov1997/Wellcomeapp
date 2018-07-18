package com.wellcome.auth

import com.google.maps.GeoApiContext
import com.wellcome.auth.repository.AuthRepository
import com.wellcome.auth.repository.AuthRepositoryImpl
import com.wellcome.auth.service.AuthService
import com.wellcome.auth.service.AuthServiceImpl
import org.koin.dsl.module.applicationContext
import org.slf4j.Logger

fun authModule(logger: Logger, geoContext: GeoApiContext) = applicationContext {
    bean { AuthServiceImpl(logger, get()) as AuthService }
    bean { AuthRepositoryImpl(logger, geoContext) as AuthRepository }
}