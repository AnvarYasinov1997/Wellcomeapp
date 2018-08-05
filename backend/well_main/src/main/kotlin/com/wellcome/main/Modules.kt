package com.wellcome.main

import com.wellcome.configuration.property.DBProperty
import com.wellcome.configuration.property.createMainDbProperty
import com.wellcome.main.repository.AuthRepository
import com.wellcome.main.repository.AuthRepositoryImpl
import com.wellcome.main.service.AuthService
import com.wellcome.main.service.AuthServiceImpl
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module.applicationContext

fun mainModule() = applicationContext {
    bean { AuthServiceImpl(get(), get()) as AuthService }
    bean { AuthRepositoryImpl(get(), get()) as AuthRepository }
    bean { MessageHandler(get()) }
}

fun mainDBConnectionModule() = applicationContext {
    bean("main") {
        createMainDbProperty()
    }
    bean {
        val property = get<DBProperty>("main")
        Database.connect(property.dbUrl, property.driver, property.username, property.password)
    }
}