import com.vmenon.mpo.search.api.SearchApiConfiguration
import com.vmenon.mpo.search.api.SearchResult
import com.vmenon.mpo.search.api.internal.IsolatedKoinContext
import com.vmenon.mpo.search.api.internal.SearchUseCase
import com.vmenon.mpo.search.api.internal.SqlDriverFactory
import com.vmenon.mpo.search.api.internal.WebWorkerSqlDriverFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.JsClient
import kotlin.js.Promise
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.dsl.module

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("SearchApi")
class SearchApiJs(configuration: SearchApiConfiguration) {
    init {
        IsolatedKoinContext.koinApp.koin.loadModules(
            listOf(
                module {
                    single<HttpClientEngine> { JsClient().create { } }
                    single<SearchApiConfiguration> { configuration }
                    single<SqlDriverFactory> { WebWorkerSqlDriverFactory() }
                }
            ))
    }

    @OptIn(DelicateCoroutinesApi::class)
    @JsName("searchPodcasts")
    fun searchPodcasts(query: String): Promise<Array<SearchResult>> {
        return Promise { resolve, reject ->
            if (query.isBlank()) {
                resolve(emptyArray())
            } else {
                GlobalScope.launch {
                    try {
                        val results = SearchUseCase().search(query)
                        resolve(results.toTypedArray())
                    } catch (e: Exception) {
                        reject(e)
                    }
                }
            }
        }
    }

    fun getPodcastDetails(feedUrl: String, episodesOffset: Int, episodesLimit: Int): Promise<SearchResult> {
        return Promise { resolve, reject ->
            GlobalScope.launch {
                try {
                    val result = SearchUseCase().getPodcastDetails(feedUrl)
                    resolve(result)
                } catch (e: Exception) {
                    reject(e)
                }
            }
        }
    }
}