package com.wellcome.rest.sender

import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.ConnectionFactory
import com.wellcome.dto.auth.AuthDto
import com.wellcome.rest.property.SenderProperty

interface Sender<T> {
    fun send(dto: T)
}

class AuthSender(private val objectMapper: ObjectMapper,
                 private val senderProperty: SenderProperty,
                 private val connectionFactory: ConnectionFactory) : Sender<AuthDto> {

    override fun send(dto: AuthDto) {
        val connection = connectionFactory.newConnection()

        val channel = connection.createChannel()

        val payload = objectMapper.writeValueAsString(dto)

        try {
            channel.basicPublish(senderProperty.exchanger, senderProperty.routingKey, null, payload.toByteArray())
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            connection.close()
        }

    }

}