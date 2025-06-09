package com.vmenon.mpo.search.api

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class Episode(
    val name: String,
    val description: String? = null,
    val published: String? = null,
    val type: String? = null,
    val downloadUrl: String? = null,
    val durationInSeconds: Double? = null,
    val artworkUrl: String? = null,
)