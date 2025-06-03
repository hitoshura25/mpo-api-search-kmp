package com.vmenon.mpo.search.api

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

class SearchUseCase {
    suspend fun search(query: String): List<SearchResult> {
        if (query.isBlank()) {
            return emptyList()
        }
        return listOf(
            SearchResult(
                name = "Game Scoop!",
                artworkUrl = "https://is1-ssl.mzstatic.com/image/thumb/Features4/v4/d6/d5/98/d6d59873-2ee5-1f88-1e9d-224cc4a67b53/mza_1229187100463163224.jpg/600x600bb.jpg",
                genres = listOf(
                    "Video Games",
                    "Podcasts",
                    "Leisure",
                    "Games"
                ),
                author = "IGN & Geek Media",
                feedUrl = "https://rss.pdrl.fm/817ebc/feeds.megaphone.fm/gamescoop",
            )
        )
    }
}