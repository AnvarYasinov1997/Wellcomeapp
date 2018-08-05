package com.wellcome.main

import com.wellcome.main.repository.AuthRepository
import com.wellcome.main.repository.AuthRepositoryImpl
import com.wellcome.main.service.AuthService
import com.wellcome.main.service.AuthServiceImpl
import org.koin.dsl.module.applicationContext

fun mainModule() = applicationContext {
    bean { AuthServiceImpl(get(), get()) as AuthService }
    bean { AuthRepositoryImpl(get(), get()) as AuthRepository }
    bean { MessageHandler(get()) }
}