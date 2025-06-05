package com.vmenon.mpo.search.api.internal

import com.vmenon.mpo.search.api.SearchResult

internal class SearchUseCase {
    private val repository: SearchRepository = IsolatedKoinContext.koinApp.koin.get()

    suspend fun search(query: String): List<SearchResult> {
        if (query.isBlank()) {
            return emptyList()
        }
        return repository.searchShows(query)
    }
}