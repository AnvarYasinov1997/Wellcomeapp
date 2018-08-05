package com.wellcome.configuration.property

data class FanoutProperty(val exchanger: String)

data class SimpleQueueProperty(val queue: String)

data class DirectProperty(val exchanger: String, val routingKey: String)

fun createDirectProperty(exchangerName: String, routingKeyName: String): DirectProperty {
    val prop = getProp("rabbit.properties")
    return DirectProperty(prop.getProperty(exchangerName), prop.getProperty(routingKeyName))
}

fun createSimpleQueueProperty(queueName: String): SimpleQueueProperty {
    val prop = getProp("rabbit.properties")
    return SimpleQueueProperty(prop.getProperty(queueName))
}

fun createFanoutProperty(exchangerName: String): FanoutProperty {
    val prop = getProp("rabbit.properties")
    return FanoutProperty(prop.getProperty(exchangerName))
}