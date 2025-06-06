package com.vmenon.mpo.search.api

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class SearchApiConfiguration(val baseUrl: String, val cacheTimeMilliseconds: Int)
