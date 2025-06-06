package com.vmenon.mpo.search.api.internal

import com.vmenon.mpo.search.api.SearchApiConfiguration
import com.vmenon.mpo.search.api.SearchResult
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.koin.core.Koin
import org.koin.dsl.module
import org.koin.test.KoinTest

class SearchUseCaseTest : KoinTest {
    override fun getKoin(): Koin = IsolatedKoinContext.koin

    @BeforeTest
    fun setup() {
        getKoin().loadModules(
            listOf(
                module {
                    single<HttpClientEngine> {
                        MockEngine { request ->
                            respond(
                                content = ByteReadChannel(MockResponses.SEARCH_RESPONSE),
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json")
                            )
                        }
                    }
                    single<SearchApiConfiguration> {
                        SearchApiConfiguration(baseUrl = "http://localhost:8080", cacheTimeMilliseconds = 5 * 60 * 1000)
                    }
                    single<DatabaseDriverFactory> {
                        TestDatabaseDriverFactory()
                    }
                }
            )
        )
    }

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