import com.vmenon.mpo.search.api.SearchApiConfiguration
import com.vmenon.mpo.search.api.SearchResult
import com.vmenon.mpo.search.api.internal.DatabaseDriverFactory
import com.vmenon.mpo.search.api.internal.IsolatedKoinContext
import com.vmenon.mpo.search.api.internal.JsDatabaseDriverFactory
import com.vmenon.mpo.search.api.internal.SearchUseCase
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
                    single<DatabaseDriverFactory> { JsDatabaseDriverFactory() }
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
}