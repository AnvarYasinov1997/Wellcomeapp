package com.wellcome.property

import com.wellcome.property.propertyObjects.TestProperty
import java.io.FileInputStream
import java.io.IOException
import java.util.*

class DefaultTestPropertyLoader : TestPropertyLoader {

    private lateinit var fis: FileInputStream

    private val property = Properties()

    override fun load(propertyObject: TestProperty, path: String): TestProperty {
        try {
            fis = FileInputStream("src/main/resources/$path")

            property.load(fis)

            propertyObject.dataOne = property.getProperty("notification.message-broker.exchanger")

            propertyObject.dataTwo = property.getProperty("notification.message-broker.send-notification-routing-key")

        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return propertyObject
    }

}