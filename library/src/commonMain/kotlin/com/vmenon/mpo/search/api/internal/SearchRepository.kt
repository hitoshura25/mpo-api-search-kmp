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
        val cachedResponse = cacheDataSource.loadSearchResults(keyword)
        if (cachedResponse != null) {
            return parseSearchResponse(cachedResponse)
        }
        val response = apiDataSource.search(keyword)
        cacheDataSource.saveSearchResults(keyword, response)
        return parseSearchResponse(response)
    }

    suspend fun getShowDetails(feedUrl: String, episodesOffset: Long, episodesLimit: Long): SearchResultDetails {
        val cachedResponse = cacheDataSource.loadDetails(feedUrl, episodesOffset, episodesLimit)
        if (cachedResponse != null) {
            return json.decodeFromString(cachedResponse)
        }
        val response = apiDataSource.details(feedUrl, episodesOffset, episodesLimit)
        cacheDataSource.saveDetails(feedUrl, episodesOffset, episodesLimit, response)
        return parseShowDetailsResponse(response)
    }

    private fun parseSearchResponse(response: String): List<SearchResult> {
        return json.decodeFromString<SearchResults>(response).results
    }

    private fun parseShowDetailsResponse(response: String): SearchResultDetails {
        return json.decodeFromString<SearchResultDetails>(response)
    }
}