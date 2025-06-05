package com.vmenon.mpo.search.api

import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    val name: String,
    val artworkUrl: String?,
    val genres: List<String>,
    val author: String,
    val feedUrl: String,
)
