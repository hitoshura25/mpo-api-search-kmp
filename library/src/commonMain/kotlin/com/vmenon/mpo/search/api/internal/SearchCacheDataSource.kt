package com.vmenon.mpo.search.api.internal

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import com.vmenon.mpo.cache.MpoDatabase
import com.vmenon.mpo.cache.SearchResults.Adapter
import com.vmenon.mpo.search.api.SearchApiConfiguration
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.minus

internal class SearchCacheDataSource(
    private val databaseDriverFactory: SqlDriverFactory,
    private val configuration: SearchApiConfiguration
) {
    private val searchResultsAdapter = object : ColumnAdapter<Instant, Long> {
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
                Adapter(created_atAdapter = searchResultsAdapter)
            ).also { database = it }
        }
        return block(db)
    }

    suspend fun loadSearchResults(keyword: String): String? {
        return setupDatabase { database ->
            val now = Clock.System.now()
            val results = database.mpoDatabaseQueries.selectSearchResults(
                keyword,
                now.minus(
                    configuration.cacheTimeMilliseconds,
                    DateTimeUnit.MILLISECOND
                ),
                now
            ).awaitAsOneOrNull()
            results?.results
        }
    }

    suspend fun saveSearchResults(keyword: String, results: String) {
        setupDatabase { database ->
            database.mpoDatabaseQueries.insertSearchResults(
                keyword,
                results,
                Clock.System.now()
            )
        }
    }
}