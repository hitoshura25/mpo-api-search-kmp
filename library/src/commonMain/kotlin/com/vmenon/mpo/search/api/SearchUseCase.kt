package com.vmenon.mpo.search.api

import com.vmenon.mpo.search.api.internal.IsolatedKoinContext
import com.vmenon.mpo.search.api.internal.SearchRepository

class SearchUseCase {
    private val repository: SearchRepository = IsolatedKoinContext.koinApp.koin.get()

    suspend fun search(query: String): List<SearchResult> {
        if (query.isBlank()) {
            return emptyList()
        }
        return repository.searchShows(query)
    }
}