package com.wellcome.auth

import com.wellcome.auth.repository.AuthRepository
import com.wellcome.auth.repository.AuthRepositoryImpl
import com.wellcome.auth.service.AuthService
import com.wellcome.auth.service.AuthServiceImpl
import org.koin.dsl.module.applicationContext
import org.slf4j.Logger

fun authModule() = applicationContext {
    bean { AuthServiceImpl(get(), get()) as AuthService }
    bean { AuthRepositoryImpl(get(), get()) as AuthRepository }
}