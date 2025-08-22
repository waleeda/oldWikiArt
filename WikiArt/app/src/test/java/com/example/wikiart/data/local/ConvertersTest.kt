package com.example.wikiart.data.local

import org.junit.Assert.assertEquals
import org.junit.Test

class ConvertersTest {
    private val converters = Converters()

    @Test
    fun stringListRoundTrip() {
        val list = listOf("a", "b", "c")
        val json = converters.fromStringList(list)
        assertEquals("[\"a\",\"b\",\"c\"]", json)
        assertEquals(list, converters.toStringList(json))
    }

    @Test
    fun emptyListRoundTrip() {
        val list = emptyList<String>()
        val json = converters.fromStringList(list)
        assertEquals("[]", json)
        assertEquals(list, converters.toStringList(json))
    }
}
