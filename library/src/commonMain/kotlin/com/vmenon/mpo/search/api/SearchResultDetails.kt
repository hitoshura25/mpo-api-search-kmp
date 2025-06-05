package com.vmenon.mpo.search.api

import kotlinx.serialization.Serializable

@Serializable
data class SearchResultDetails(
    val episodes: List<Episode>,
    val subscribed: Boolean
)