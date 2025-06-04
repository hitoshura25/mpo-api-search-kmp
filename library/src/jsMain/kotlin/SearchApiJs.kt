import com.vmenon.mpo.search.api.SearchUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.js.Promise

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("SearchApi")
class SearchApiJs {
    @OptIn(DelicateCoroutinesApi::class)
    @JsName("searchPodcasts")
    fun searchPodcasts(query: String): Promise<Array<SearchResultJs>> {
        return Promise { resolve, reject ->
            if (query.isBlank()) {
                resolve(emptyArray())
            } else {
                GlobalScope.launch {
                    try {
                        val results = SearchUseCase().search(query)
                        resolve(results.map { result ->
                            SearchResultJs(
                                name = result.name,
                                artworkUrl = result.artworkUrl,
                                genres = result.genres,
                                author = result.author,
                                feedUrl = result.feedUrl
                            )
                        }.toTypedArray())
                    } catch (e: Exception) {
                        reject(e)
                    }
                }
            }
        }
    }
}