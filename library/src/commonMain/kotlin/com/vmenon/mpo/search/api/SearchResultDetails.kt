package com.vmenon.mpo.search.api

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@Serializable
@JsExport
data class SearchResultDetails(
    val episodes: List<Episode>,
    val name: String,
    val description: String,
    val imageUrl: String
)