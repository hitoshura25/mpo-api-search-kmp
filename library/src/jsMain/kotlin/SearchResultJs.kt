@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("SearchResult")
data class SearchResultJs(
    val name: String,
    val artworkUrl: String?,
    val genres: List<String>,
    val author: String,
    val feedUrl: String,
)
