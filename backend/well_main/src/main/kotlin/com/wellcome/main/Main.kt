package com.wellcome.main

import com.rabbitmq.client.Channel
import com.wellcome.configuration.bean.loggerRabbitMqModule
import com.wellcome.configuration.bean.mainRabbitMqModule
import com.wellcome.configuration.bean.rabbitMqModule
import com.wellcome.configuration.property.SimpleQueueProperty
import com.wellcome.configuration.utils.MicroserviceName
import com.wellcome.configuration.utils.inject
import com.wellcome.configuration.utils.receiveRpc
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.runBlocking
import org.koin.standalone.StandAloneContext.startKoin

fun main(args: Array<String>) = runBlocking {
    //    Database.connect("jdbc:postgresql://localhost:5432/well_main_db","org.postgresql.Driver", "root", "root")
//    transaction {
//        addLogger(StdOutSqlLogger)
//        SchemaUtils.create(Test)
//
//        for (i in 0..10){
//            Test.insert {
//                it[name] = "Anvar $i"
//            }
//        }
//    }
    startKoin(listOf(rabbitMqModule(),
        loggerRabbitMqModule(MicroserviceName.MAIN),
        mainRabbitMqModule(),
        mainModule()))

    val messageHandler by inject<MessageHandler>()
    val receiveChannel by inject<Channel>("main")
    val property by inject<SimpleQueueProperty>("main")
    val receiveJob = Job()

    receiveChannel
        .receiveRpc(receiveJob,
            property,
            messageHandler::handleCast,
            messageHandler::handleMessage).join()
}
//
//object Test : Table(){
//    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
//    val name: Column<String> = varchar("name", 10)
//}

