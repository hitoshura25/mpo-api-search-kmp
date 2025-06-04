package com.vmenon.mpo.search.api.internal

import com.vmenon.mpo.search.api.SearchResult
import com.vmenon.mpo.search.api.SearchResultDetails

internal class SearchRepository(
    private val apiDataSource: SearchApiDataSource,
    private val cacheDataSource: SearchCacheDataSource
) {
    suspend fun searchShows(keyword: String): List<SearchResult> {
        return emptyList()
    }

    suspend fun getShowDetails(feedUrl: String, maxEpisodes: Int): SearchResultDetails {
        return SearchResultDetails(
            episodes = emptyList(),
            subscribed = false
        )
    }
}