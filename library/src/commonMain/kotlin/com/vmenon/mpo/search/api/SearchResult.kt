package com.vmenon.mpo.search.api

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@Serializable
@JsExport
data class SearchResult(
    val name: String,
    val artworkUrl: String? = null,
    val genres: List<String>,
    val author: String,
    val feedUrl: String,
)
