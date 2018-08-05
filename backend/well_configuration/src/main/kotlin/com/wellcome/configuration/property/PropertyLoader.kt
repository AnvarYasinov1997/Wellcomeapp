package com.wellcome.configuration.property

import io.ktor.application.Application
import java.util.*

fun getProp(fileName: String): Properties {
    val stream = Application::class.java.classLoader.getResourceAsStream(fileName)
    return Properties().apply {
        load(stream)
    }
}