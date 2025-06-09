package com.vmenon.mpo.search.api

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class Episode(
    val name: String,
    val description: String?,
    val published: String?,
    val type: String?,
    val downloadUrl: String?,
    val durationInSeconds: Double?,
    val artworkUrl: String?
)