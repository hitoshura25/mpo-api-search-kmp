package com.vmenon.mpo.search.api

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SearchUseCaseTest {
    @Test
    fun `search returns empty list when query is empty`() = runTest {
        val searchUseCase = SearchUseCase()
        val result = searchUseCase.search("")
        assertEquals(emptyList(), result)
    }

    @Test
    fun `search returns empty list when query is not empty`() = runTest() {
        val searchUseCase = SearchUseCase()
        val result = searchUseCase.search("test")
        assertEquals(listOf(), result)
    }
}