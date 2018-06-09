package com.wellcomeapp

import kotlinx.coroutines.experimental.channels.actor

fun timerActor() = actor<Message> {
    for (message in channel) println("actor $message")
}