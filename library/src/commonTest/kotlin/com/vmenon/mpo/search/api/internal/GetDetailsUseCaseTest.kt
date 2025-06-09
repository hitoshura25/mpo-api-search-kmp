package com.vmenon.mpo.search.api.internal

import com.vmenon.mpo.search.api.Episode
import com.vmenon.mpo.search.api.SearchApiConfiguration
import com.vmenon.mpo.search.api.SearchResultDetails
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.HttpClientEngine
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.koin.core.Koin
import org.koin.dsl.module
import org.koin.test.KoinTest

internal class GetDetailsUseCaseTest : KoinTest {
    private val testDriverFactory = TestSqlDriverFactoryWrapper()
    private val modules = listOf(
        module {
            single<HttpClientEngine> { mockEngine }
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
    fun `details throws exception when feedUrl is blank`() = runTest {
        val detailsUseCase = GetDetailsUseCase()
        shouldThrow<IllegalArgumentException> {
            detailsUseCase.details("", 0, 10)
        }
    }

    @Test
    fun `details throws exception when episodesOffset is negative`() = runTest {
        val detailsUseCase = GetDetailsUseCase()
        shouldThrow<IllegalArgumentException> {
            detailsUseCase.details("http://example.com/feed", -1, 10)
        }
    }

    @Test
    fun `details throws exception when episodesLimit is not positive`() = runTest {
        val detailsUseCase = GetDetailsUseCase()
        shouldThrow<IllegalArgumentException> {
            detailsUseCase.details("http://example.com/feed", 0, 0)
        }
    }

    @Test
    fun `details returns list of search results and cached result on second call`() = runTest {
        val detailsUseCase = GetDetailsUseCase()
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
        var result = detailsUseCase.details("http://example.com/feed", 0, 10)
        result shouldBe expected

        result = detailsUseCase.details("http://example.com/feed", 0, 10)
        result shouldBe expected
    }
}