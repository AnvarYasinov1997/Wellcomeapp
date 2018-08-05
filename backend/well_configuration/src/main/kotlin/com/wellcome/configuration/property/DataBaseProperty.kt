package com.wellcome.configuration.property

data class DBProperty(val dbUrl: String,
                      val driver: String,
                      val username: String,
                      val password: String)

fun createMainDbProperty(): DBProperty {
    val prop = getProp("postgres.properties")
    return DBProperty(
        dbUrl = prop.getProperty("db-url"),
        driver = prop.getProperty("driver"),
        username = prop.getProperty("username"),
        password = prop.getProperty("password")
    )
}