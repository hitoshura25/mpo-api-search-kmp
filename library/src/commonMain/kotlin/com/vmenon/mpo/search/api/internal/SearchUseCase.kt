package com.vmenon.mpo.search.api.internal

import co.touchlab.kermit.Logger
import com.vmenon.mpo.search.api.SearchResult

internal class SearchUseCase {
    private val repository: SearchRepository = IsolatedKoinContext.koinApp.koin.get()

    suspend fun search(query: String): List<SearchResult> {
        if (query.isBlank()) {
            return emptyList()
        }
        Logger.d("SearchUseCase") {
            "Searching for shows with query: $query"
        }
        return repository.searchShows(query)
    }
}