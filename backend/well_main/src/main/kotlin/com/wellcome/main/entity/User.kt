package com.wellcome.main.entity

import org.jetbrains.exposed.sql.Table

class User(
    val id: Long,
    val googleId: Long,
    val city: City
) : Table()