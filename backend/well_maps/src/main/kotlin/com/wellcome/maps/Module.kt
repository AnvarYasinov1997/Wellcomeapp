package com.wellcome.maps

import com.google.maps.GeoApiContext
import com.wellcome.configuration.utils.LoggerHandler
import org.koin.dsl.module.applicationContext
import java.util.*

fun mapsModule() = applicationContext {
    bean { initGoogleMaps(get()) }
    bean { MessageHandler(get()) }
}

fun initGoogleMaps(logger: LoggerHandler): GeoApiContext {
    val props = Properties()
    props.load(GeoApiContext::class.java.classLoader.getResourceAsStream("wellcome.property"))
    val key = props.getProperty("googleMapsKey")
    val context = GeoApiContext.Builder().apiKey(key).build()
    logger.info("google maps initialized")
    return context
}