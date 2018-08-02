package com.wellcome.configuration.sender

import io.ktor.application.Application
import java.util.*

data class ServiceProperty(val exchanger: String, val routingKey: String)

fun createServiceProperty(exchangerName: String, routingKeyName: String): ServiceProperty {
    val stream = Application::class.java.classLoader.getResourceAsStream("microservice.properties")
    val prop = Properties()
    prop.load(stream)
    return ServiceProperty(prop.getProperty(exchangerName), prop.getProperty(routingKeyName))
}