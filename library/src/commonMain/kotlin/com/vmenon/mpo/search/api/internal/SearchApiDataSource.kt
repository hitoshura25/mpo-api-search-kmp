package com.vmenon.mpo.search.api.internal


import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class SearchApiDataSource(clientEngine: HttpClientEngine) {
    private val httpClient = HttpClient(clientEngine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    suspend fun search(keyword: String): String {
        return httpClient.get("http://localhost:8000/search") {
            parameter("term", keyword)
        }.body()
    }
}