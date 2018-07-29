package com.wellcome.configuration.property

import io.ktor.application.Application
import java.util.*

data class SenderProperty(val exchanger: String, val routingKey: String)

fun createSenderProperty(name: String, exchangerName: String, routingKeyName: String): SenderProperty {
    val stream = Application::class.java.classLoader.getResourceAsStream(name)
    val prop = Properties()
    prop.load(stream)
    return SenderProperty(prop.getProperty(exchangerName), prop.getProperty(routingKeyName))
}