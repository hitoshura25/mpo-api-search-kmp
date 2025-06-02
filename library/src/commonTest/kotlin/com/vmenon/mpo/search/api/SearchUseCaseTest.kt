package com.vmenon.mpo.search.api

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class SearchUseCaseTest {
    @Test
    fun `search returns empty list when query is empty`() = runTest {
        val searchUseCase = SearchUseCase()
        val result = searchUseCase.search("")
        result shouldBe emptyList()
    }

    @Test
    fun `search returns empty list when query is not empty`() = runTest() {
        val searchUseCase = SearchUseCase()
        val result = searchUseCase.search("test")
        val expected = listOf(
            SearchResult(
                name = "Game Scoop!",
                artworkUrl = "https://is1-ssl.mzstatic.com/image/thumb/Features4/v4/d6/d5/98/d6d59873-2ee5-1f88-1e9d-224cc4a67b53/mza_1229187100463163224.jpg/600x600bb.jpg",
                genres = listOf(
                    "Video Games",
                    "Podcasts",
                    "Leisure",
                    "Games"
                ),
                author = "IGN & Geek Media",
                feedUrl = "https://rss.pdrl.fm/817ebc/feeds.megaphone.fm/gamescoop",
            )
        )
        result shouldBe expected

    }
}