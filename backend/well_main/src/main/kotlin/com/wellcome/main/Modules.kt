package com.wellcome.main

import org.koin.dsl.module.applicationContext

fun mainModule() = applicationContext {
    bean { MessageHandler(get()) }
}