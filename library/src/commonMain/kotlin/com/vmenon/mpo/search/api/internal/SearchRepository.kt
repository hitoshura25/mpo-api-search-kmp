package com.vmenon.mpo.search.api.internal

import com.vmenon.mpo.search.api.SearchResult
import com.vmenon.mpo.search.api.SearchResultDetails
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class SearchResults(
    val results: List<SearchResult>
)

internal class SearchRepository(
    private val apiDataSource: SearchApiDataSource,
    private val cacheDataSource: SearchCacheDataSource
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun searchShows(keyword: String): List<SearchResult> {
        val response = apiDataSource.search(keyword)
        return json.decodeFromString<SearchResults>(response).results
    }

    suspend fun getShowDetails(feedUrl: String, maxEpisodes: Int): SearchResultDetails {
        return SearchResultDetails(
            episodes = emptyList(),
            subscribed = false
        )
    }
}