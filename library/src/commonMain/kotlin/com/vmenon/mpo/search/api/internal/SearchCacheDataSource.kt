package com.vmenon.mpo.search.api.internal

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import co.touchlab.kermit.Logger
import com.vmenon.mpo.cache.MpoDatabase
import com.vmenon.mpo.cache.SearchDetails
import com.vmenon.mpo.cache.SearchResults
import com.vmenon.mpo.search.api.SearchApiConfiguration
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.minus

internal class SearchCacheDataSource(
    private val databaseDriverFactory: SqlDriverFactory,
    private val configuration: SearchApiConfiguration,
    private val clock: Clock = Clock.System,
) {

    private val createdAtAdapter = object : ColumnAdapter<Instant, Long> {
        override fun decode(databaseValue: Long): Instant {
            return Instant.fromEpochMilliseconds(databaseValue)
        }

        override fun encode(value: Instant): Long {
            return value.toEpochMilliseconds()
        }
    }
    private var database: MpoDatabase? = null

    private suspend fun <T> setupDatabase(block: suspend (MpoDatabase) -> T): T {
        val db = database ?: run {
            MpoDatabase(
                databaseDriverFactory.provideDbDriver(MpoDatabase.Schema),
                SearchDetails.Adapter(created_atAdapter = createdAtAdapter),
                SearchResults.Adapter(created_atAdapter = createdAtAdapter)
            ).also { database = it }
        }
        return block(db)
    }

    suspend fun loadSearchResults(keyword: String): String? {
        Logger.d("SearchCacheDataSource") {
            "Loading search results for keyword: $keyword"
        }
        return setupDatabase { database ->
            val now = clock.now()
            val results = database.mpoDatabaseQueries.selectSearchResults(
                keyword,
                now.minus(
                    configuration.cacheTimeMilliseconds,
                    DateTimeUnit.MILLISECOND
                ),
                now
            ).awaitAsOneOrNull()
            if (results != null) {
                Logger.d("SearchCacheDataSource") {
                    "Loaded search results for keyword: ${results.keyword}"
                }
            }

            results?.results
        }
    }

    suspend fun saveSearchResults(keyword: String, results: String) {
        setupDatabase { database ->
            val expirationThreshold = clock.now().minus(
                configuration.cacheTimeMilliseconds,
                DateTimeUnit.MILLISECOND
            )
            val deletedCount = database.mpoDatabaseQueries.deleteExpiredResults(expirationThreshold)
            Logger.d("SearchCacheDataSource") { "Cleaned up $deletedCount expired search results" }

            database.mpoDatabaseQueries.insertSearchResults(
                keyword,
                results,
                clock.now()
            )
            Logger.d("SearchCacheDataSource") {
                "Saved search results for keyword: $keyword"
            }
        }
    }

    suspend fun loadDetails(feedUrl: String, episodesOffset: Long, episodesLimit: Long): String? {
        Logger.d("SearchCacheDataSource") {
            "Loading details for feed URL: $feedUrl"
        }
        return setupDatabase { database ->
            val now = clock.now()
            val details = database.mpoDatabaseQueries.selectDetails(
                feedUrl,
                episodesOffset,
                episodesLimit,
                now.minus(
                    configuration.cacheTimeMilliseconds,
                    DateTimeUnit.MILLISECOND
                ),
                now
            ).awaitAsOneOrNull()
            if (details != null) {
                Logger.d("SearchCacheDataSource") {
                    "Loaded details for feed URL: ${details.feedUrl}"
                }
            }

            details?.details
        }
    }

    suspend fun saveDetails(feedUrl: String, episodesOffset: Long, episodesLimit: Long, details: String) {
        setupDatabase { database ->
            val expirationThreshold = clock.now().minus(
                configuration.cacheTimeMilliseconds,
                DateTimeUnit.MILLISECOND
            )
            val deletedCount = database.mpoDatabaseQueries.deleteExpiredDetails(expirationThreshold)
            Logger.d("SearchCacheDataSource") { "Cleaned up $deletedCount expired details" }

            database.mpoDatabaseQueries.insertDetails(
                feedUrl,
                episodesOffset,
                episodesLimit,
                details,
                clock.now()
            )
            Logger.d("SearchCacheDataSource") {
                "Saved details for feed URL: $feedUrl"
            }
        }
    }
}