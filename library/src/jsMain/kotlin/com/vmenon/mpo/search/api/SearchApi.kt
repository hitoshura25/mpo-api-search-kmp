package com.vmenon.mpo.search.api

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalJsExport::class)
@JsExport
class SearchApi {
    @OptIn(DelicateCoroutinesApi::class)
    @JsName("searchPodcasts")
    fun searchPodcasts(query: String, callback: (Array<SearchResult>) -> Unit) {
        GlobalScope.launch {
            val results = SearchUseCase().search(query)
            callback(results.toTypedArray())
        }
    }
}