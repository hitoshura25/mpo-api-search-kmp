package com.vmenon.mpo.search.api.internal

import co.touchlab.kermit.Logger
import com.vmenon.mpo.search.api.SearchResult
import com.vmenon.mpo.search.api.SearchResultDetails

internal class SearchUseCase {
    private val repository: SearchRepository = IsolatedKoinContext.koinApp.koin.get()

    suspend fun search(query: String): List<SearchResult> {
        if (query.isBlank()) {
            throw IllegalArgumentException("Query cannot be blank")
        }
        Logger.d("SearchUseCase") {
            "Searching for shows with query: $query"
        }
        return repository.searchShows(query)
    }

    suspend fun details(feedUrl: String, episodesOffset: Long, episodesLimit: Long): SearchResultDetails {
        if (feedUrl.isBlank()) {
            throw IllegalArgumentException("Feed URL cannot be blank")
        }

        if (episodesOffset < 0) {
            throw IllegalArgumentException("Episodes offset is negative: $episodesOffset")
        }

        if (episodesLimit <= 0) {
            throw IllegalArgumentException("Episodes limit is not positive: $episodesLimit")
        }

        Logger.d("SearchUseCase") {
            "Fetching details for feedUrl: $feedUrl, offset: $episodesOffset, limit: $episodesLimit"
        }
        return repository.getShowDetails(feedUrl, episodesOffset, episodesLimit)
    }
}