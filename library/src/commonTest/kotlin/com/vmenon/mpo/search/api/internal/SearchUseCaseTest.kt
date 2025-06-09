package com.vmenon.mpo.search.api.internal

import com.vmenon.mpo.search.api.Episode
import com.vmenon.mpo.search.api.SearchApiConfiguration
import com.vmenon.mpo.search.api.SearchResult
import com.vmenon.mpo.search.api.SearchResultDetails
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.koin.core.Koin
import org.koin.dsl.module
import org.koin.test.KoinTest

internal class SearchUseCaseTest : KoinTest {
    private val testDriverFactory = TestSqlDriverFactoryWrapper()
    private val modules = listOf(
        module {
            single<HttpClientEngine> {
                MockEngine { request ->
                    when (request.url.encodedPath) {
                        "/search" -> respond(
                            content = ByteReadChannel(MockResponses.SEARCH_RESPONSE),
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType, "application/json")
                        )

                        "/details" -> respond(
                            content = ByteReadChannel(MockResponses.DETAILS_RESPONSE),
                            status = HttpStatusCode.OK,
                            headers = headersOf(HttpHeaders.ContentType, "application/json")
                        )

                        else -> error("Unhandled request: ${request.url}")
                    }

                }
            }
            single<SearchApiConfiguration> {
                SearchApiConfiguration(baseUrl = "http://localhost:8080", cacheTimeMilliseconds = 5 * 60 * 1000)
            }
            single<SqlDriverFactory> {
                testDriverFactory
            }
        }
    )

    override fun getKoin(): Koin = IsolatedKoinContext.koin

    @BeforeTest
    fun setup() {
        getKoin().loadModules(modules)
    }

    @AfterTest
    fun tearDown() {
        testDriverFactory.close()
        getKoin().unloadModules(modules)
    }

    @Test
    fun `search throws exception when query is empty`() = runTest {
        val searchUseCase = SearchUseCase()
        shouldThrow<IllegalArgumentException> {
            searchUseCase.search("")
        }
    }

    @Test
    fun `search returns list when query is not empty and cached result on second call`() = runTest() {
        val expectedResult = SearchResult(
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
        val searchUseCase = SearchUseCase()
        val result = searchUseCase.search("test")
        result shouldBe listOf(expectedResult)

        val cachedResult = searchUseCase.search("test")
        cachedResult shouldBe listOf(expectedResult)
    }

    @Test
    fun `details throws exception when feedUrl is blank`() = runTest {
        val searchUseCase = SearchUseCase()
        shouldThrow<IllegalArgumentException> {
            searchUseCase.details("", 0, 10)
        }
    }

    @Test
    fun `details throws exception when episodesOffset is negative`() = runTest {
        val searchUseCase = SearchUseCase()
        shouldThrow<IllegalArgumentException> {
            searchUseCase.details("http://example.com/feed", -1, 10)
        }
    }

    @Test
    fun `details throws exception when episodesLimit is not positive`() = runTest {
        val searchUseCase = SearchUseCase()
        shouldThrow<IllegalArgumentException> {
            searchUseCase.details("http://example.com/feed", 0, 0)
        }
    }

    @Test
    fun `details returns list of search results and cached result on second call`() = runTest {
        val searchUseCase = SearchUseCase()
        val expected = SearchResultDetails(
            name = "Test Podcast",
            description = "Test Description",
            imageUrl = "https://example.com/default.jpg",
            episodes = listOf(
                Episode(
                    name = "Minimal Episode",
                    published = "2025-03-22T01:02:00",
                    downloadUrl = "https://example.com/episode2.mp3",
                    artworkUrl = "https://example.com/default.jpg",
                    description = "This is a minimal episode description.",
                    type = "audio/mp3",
                    durationInSeconds = 3600.toDouble()
                )
            )
        )
        var result = searchUseCase.details("http://example.com/feed", 0, 10)
        result shouldBe expected

        result = searchUseCase.details("http://example.com/feed", 0, 10)
        result shouldBe expected
    }
}