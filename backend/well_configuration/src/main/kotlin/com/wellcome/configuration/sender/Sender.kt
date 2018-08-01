package com.wellcome.configuration.sender

import com.rabbitmq.client.Channel
import kotlinx.serialization.json.JSON

class Sender(private val channel: Channel,
             private val property: ServiceProperty) {
    inline fun <reified T : Any> send(message: T) = send(JSON.stringify(message))

    fun send(jsonBody: String) {
        try {
            channel.basicPublish(property.exchanger, property.routingKey, null, jsonBody.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}