package com.vmenon.mpo.search.api

data class SearchResultDetails(
    val episodes: List<Episode>,
    val subscribed: Boolean
)