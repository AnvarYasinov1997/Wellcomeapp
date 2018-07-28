package com.wellcome.property

import com.wellcome.property.propertyObjects.TestProperty

interface PropertyLoader<T> {

    fun load(propertyObject: T, path: String): T

}

interface TestPropertyLoader : PropertyLoader<TestProperty>