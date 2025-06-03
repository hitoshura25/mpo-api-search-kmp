package com.vmenon.mpo.search.api

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class SearchResult(
    val name: String,
    val artworkUrl: String?,
    val genres: List<String>,
    val author: String,
    val feedUrl: String,
)
