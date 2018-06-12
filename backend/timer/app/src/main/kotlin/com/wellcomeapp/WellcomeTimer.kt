package com.wellcomeapp

import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.timer

sealed class Message
data class MessageAdd(val refPath: String, val deleteTime: Long, val zoneId: String) : Message()
data class MessageRemove(val refPath: String) : Message()

class WellcomeTimer(private val remover: EventRemover) {
    init {
        startTimer()
    }
    private val eventMap: ConcurrentHashMap<String, Pair<Long, ZoneId>> = ConcurrentHashMap()

    fun timerActor() = actor<Message> {
        for (message in channel) when (message) {
            is MessageAdd -> eventMap[message.refPath] = Pair(message.deleteTime, ZoneId.of(message.zoneId))
            is MessageRemove -> eventMap.remove(message.refPath)
        }
    }

    private fun startTimer() = launch {
        timer("timer", period = 1000 * 60) {
            runBlocking {
                val instant = Instant.now()
                println("eventMapSize ${eventMap.size}")
                eventMap.forEach { refPath, pair ->
                    val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(pair.first),pair.second)
                    val zonedDateTime = ZonedDateTime.of(localDateTime,pair.second)
                    if (instant >= zonedDateTime.toInstant()){
                        remover.remove(refPath)
                    }
                }
            }
        }
    }
}