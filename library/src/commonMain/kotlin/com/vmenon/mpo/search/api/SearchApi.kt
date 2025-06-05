package com.vmenon.mpo.search.api

import com.vmenon.mpo.search.api.internal.IsolatedKoinContext

object SearchApi {
    private var initialized = false

    fun init() {
        IsolatedKoinContext.koinApp.koin.loadModules(emptyList())
        initialized = true
    }

    suspend fun search(query: String): List<SearchResult> {
        if (!initialized) {
            throw IllegalStateException("SearchApi is not initialized. Call SearchApi.init() first.")
        }
        val searchUseCase: SearchUseCase = IsolatedKoinContext.koin.get()
        return searchUseCase.search(query)
    }
}