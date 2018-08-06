package com.wellcome.maps

import com.rabbitmq.client.Channel
import com.wellcome.configuration.bean.loggerRabbitMqModule
import com.wellcome.configuration.bean.mapsRabbitMqModule
import com.wellcome.configuration.bean.rabbitMqModule
import com.wellcome.configuration.property.SimpleQueueProperty
import com.wellcome.configuration.utils.MicroserviceName
import com.wellcome.configuration.utils.inject
import com.wellcome.configuration.utils.receiveRpc
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.runBlocking
import org.koin.standalone.StandAloneContext.startKoin


fun main(args: Array<String>) = runBlocking<Unit> {
    startKoin(listOf(mapsModule(),
        rabbitMqModule(),
        loggerRabbitMqModule(MicroserviceName.MAPS),
        mapsRabbitMqModule()))

    val receiveJob = Job()
    val messageHandler by inject<MessageHandler>()
    val receiveChannel by inject<Channel>("maps")
    val property by inject<SimpleQueueProperty>("maps")

    receiveChannel.receiveRpc(receiveJob,
        property,
        messageHandler::handleCast,
        messageHandler::handleMessage).join()
}