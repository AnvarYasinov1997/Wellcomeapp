package com.wellcome.sender

import com.fasterxml.jackson.databind.ObjectMapper
import com.wellcome.v1.dto.TestDto
import com.wellcome.property.TestPropertyLoader

class TestSender(
    private val objectMapper: ObjectMapper,
    private val propertyLoader: TestPropertyLoader
) : Sender<TestDto> {

    override fun send(dto: TestDto) {
        val data = objectMapper.writeValueAsString(dto)

    }
}