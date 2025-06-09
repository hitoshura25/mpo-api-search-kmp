package com.vmenon.mpo.search.api.internal

import com.vmenon.mpo.search.api.SearchApiConfiguration
import io.kotest.matchers.shouldBe
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

internal class SearchCacheDataSourceTest {
    private val testDriverFactory = TestSqlDriverFactoryWrapper()
    private val cacheTimeMs = 500

    private var currentTime = Instant.fromEpochMilliseconds(1000)

    private val configuration =
        SearchApiConfiguration(baseUrl = "http://localhost:8080", cacheTimeMilliseconds = cacheTimeMs)

    private val testClock = object : Clock {
        override fun now(): Instant = currentTime
    }

    @AfterTest
    fun tearDown() {
        testDriverFactory.close()
    }

    @Test
    fun `test cache expiration`() = runTest {
        val dataSource = SearchCacheDataSource(
            databaseDriverFactory = testDriverFactory,
            configuration = configuration,
            clock = testClock
        )

        val testKeyword = "test"

        // Save initial results
        dataSource.saveSearchResults(testKeyword, MockResponses.SEARCH_RESPONSE)

        // Verify fresh results are returned
        var testResults = dataSource.loadSearchResults(testKeyword)
        testResults shouldBe MockResponses.SEARCH_RESPONSE

        // Advance time just before expiration
        currentTime = currentTime.plus(499.milliseconds)
        testResults = dataSource.loadSearchResults(testKeyword)
        testResults shouldBe MockResponses.SEARCH_RESPONSE

        // Advance time past expiration
        currentTime = currentTime.plus(2.milliseconds)
        testResults = dataSource.loadSearchResults(testKeyword)
        testResults shouldBe null
    }

    @Test
    fun `test cleanup expired results`() = runTest {
        val dataSource = SearchCacheDataSource(
            databaseDriverFactory = testDriverFactory,
            configuration = configuration,
            clock = testClock
        )

        val expiredKeyword = "expired"
        val freshKeyword = "fresh"
        val expiredResults = """{"results": "expired data"}"""
        val freshResults = """{"results": "fresh data"}"""

        dataSource.saveSearchResults(expiredKeyword, expiredResults)
        currentTime = currentTime.plus(600.milliseconds) // Advance time past expiration
        dataSource.saveSearchResults(freshKeyword, freshResults)

        dataSource.loadSearchResults(expiredKeyword) shouldBe null
        dataSource.loadSearchResults(freshKeyword) shouldBe freshResults
    }

    @Test
    fun `test cleanup expired details`() = runTest {
        val dataSource = SearchCacheDataSource(
            databaseDriverFactory = testDriverFactory,
            configuration = configuration,
            clock = testClock
        )

        val expiredFeedUrl = "http://example.com/expired"
        val freshFeedUrl = "http://example.com/fresh"
        val expiredDetails = """{"details": "expired details"}"""
        val freshDetails = """{"details": "fresh details"}"""

        dataSource.saveDetails(expiredFeedUrl, 0, 10, expiredDetails)
        currentTime = currentTime.plus(600.milliseconds) // Advance time past expiration
        dataSource.saveDetails(freshFeedUrl, 0, 10, freshDetails)

        dataSource.loadDetails(expiredFeedUrl, 0, 10) shouldBe null
        dataSource.loadDetails(freshFeedUrl, 0, 10) shouldBe freshDetails
    }
}