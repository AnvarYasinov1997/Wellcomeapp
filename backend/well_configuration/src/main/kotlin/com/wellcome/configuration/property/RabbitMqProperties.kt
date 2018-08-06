package com.wellcome.configuration.property

import io.ktor.application.Application
import java.util.*

data class DirectProperty(val exchanger: String, val routingKey: String)

data class FanoutProperty(val exchanger: String)

data class SimpleQueueProperty(val queue: String)

data class DurableFanoutProperty(val queue: String, val exchanger: String)


fun createDirectProperty(exchangerName: String, routingKeyName: String): DirectProperty {
    val prop = getProp()
    return DirectProperty(prop.getProperty(exchangerName), prop.getProperty(routingKeyName))
}

fun createSimpleQueueProperty(queueName: String): SimpleQueueProperty {
    val prop = getProp()
    return SimpleQueueProperty(prop.getProperty(queueName))
}

fun createFanoutProperty(exchangerName: String): FanoutProperty {
    val prop = getProp()
    return FanoutProperty(prop.getProperty(exchangerName))
}

fun createDurableFanoutProperty(queueName: String, exchangerName: String): DurableFanoutProperty {
    val prop = getProp()
    return DurableFanoutProperty(prop.getProperty(queueName), prop.getProperty(exchangerName))
}

private fun getProp(): Properties {
    val stream = Application::class.java.classLoader.getResourceAsStream("rabbit.properties")
    return Properties().apply {
        load(stream)
    }
}