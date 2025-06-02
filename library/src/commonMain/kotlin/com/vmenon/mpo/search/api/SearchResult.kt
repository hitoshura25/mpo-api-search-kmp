package com.vmenon.mpo.search.api

data class SearchResult(
    val name: String,
    val artworkUrl: String?,
    val genres: List<String>,
    val author: String,
    val feedUrl: String,
)
