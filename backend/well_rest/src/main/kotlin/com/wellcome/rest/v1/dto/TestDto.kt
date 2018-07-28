package com.wellcome.rest.v1.dto

import java.io.Serializable

data class TestDto(
    var dataOne: String? = null,
    var dataTwo: String? = null
) : Serializable