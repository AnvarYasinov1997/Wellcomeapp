package com.wellcome.configuration.utils

interface Dictionary {
    fun getId(): Int
}

enum class LogType(private val id: Int) : Dictionary {

    INFO(0),
    WARN(1),
    ERROR(2);

    override fun getId(): Int = id
}