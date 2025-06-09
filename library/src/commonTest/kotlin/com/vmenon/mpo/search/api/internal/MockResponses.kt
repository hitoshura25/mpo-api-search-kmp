package com.vmenon.mpo.search.api.internal

internal object MockResponses {
    val SEARCH_RESPONSE = """
{
  "resultCount": 1,
  "results": [
    {
      "name": "Game Scoop!",
      "author": "IGN & Geek Media",
      "feedUrl": "https://rss.pdrl.fm/817ebc/feeds.megaphone.fm/gamescoop",
      "genres": [
        "Video Games",
        "Podcasts",
        "Leisure",
        "Games"
      ],
      "artworkUrl": "https://is1-ssl.mzstatic.com/image/thumb/Features4/v4/d6/d5/98/d6d59873-2ee5-1f88-1e9d-224cc4a67b53/mza_1229187100463163224.jpg/600x600bb.jpg"
    }
  ]
}
    """.trimIndent()

    val DETAILS_RESPONSE = """
{
    "name": "Test Podcast",
    "description": "Test Description",
    "imageUrl": "https://example.com/default.jpg",
    "episodes": [
      {
        "name": "Minimal Episode",
        "description": "This is a minimal episode description.",
        "published": "2025-03-22T01:02:00",
        "downloadUrl": "https://example.com/episode2.mp3",
        "artworkUrl": "https://example.com/default.jpg",
        "durationInSeconds": 3600,
        "type": "audio/mp3"
      }
    ],
    "pagination": {
        "total": 4,
        "limit": 1,
        "offset": 1,
        "next_page": "/details/?feed_url=https://rss.pdrl.fm/817ebc/feeds.megaphone.fm/gamescoop&episode_limit=1&episode_offset=2",
        "previous_page": "/details/?feed_url=https://rss.pdrl.fm/817ebc/feeds.megaphone.fm/gamescoop&episode_limit=1&episode_offset=0"
      }
  }
""".trimIndent()
}