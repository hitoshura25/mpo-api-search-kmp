package com.vmenon.mpo.search.api

import kotlinx.serialization.Serializable

@Serializable
data class Episode(
    val name: String,
    val description: String?,
    val published: Long,
    val type: String,
    val downloadUrl: String,
    val length: Long?,
    val artworkUrl: String?
)