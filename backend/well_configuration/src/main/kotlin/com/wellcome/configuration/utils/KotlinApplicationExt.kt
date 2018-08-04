package com.wellcome.configuration.utils

import org.koin.KoinContext
import org.koin.core.parameter.Parameters
import org.koin.standalone.StandAloneContext

inline fun <reified T> inject(name: String = "", noinline parameters: Parameters = { emptyMap() }) =
    lazy { (StandAloneContext.koinContext as KoinContext).get<T>(name, parameters) }

inline fun <reified T> property(key: String) =
    lazy { (StandAloneContext.koinContext as KoinContext).getProperty<T>(key) }

inline fun <reified T> property(key: String, defaultValue: T) =
    lazy { (StandAloneContext.koinContext as KoinContext).getProperty(key, defaultValue) }

inline fun <reified T> get(name: String = "", noinline parameters: Parameters = { emptyMap() }) =
    (StandAloneContext.koinContext as KoinContext).get<T>(name, parameters)

inline fun <reified T> getProperty(key: String) =
    (StandAloneContext.koinContext as KoinContext).getProperty<T>(key)

inline fun <reified T> getProperty(key: String, defaultValue: T) =
    (StandAloneContext.koinContext as KoinContext).getProperty(key, defaultValue)

private fun context() = (StandAloneContext.koinContext as KoinContext)

fun setProperty(key: String, value: Any) = context().setProperty(key, value)