package com.vmenon.mpo.search.api.internal

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel

internal val mockEngine = MockEngine { request ->
    when (request.url.encodedPath) {
        "/details" -> respond(
            content = ByteReadChannel(MockResponses.DETAILS_RESPONSE),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )

        "/search" -> respond(
            content = ByteReadChannel(MockResponses.SEARCH_RESPONSE),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )

        else -> error("Unhandled request: ${request.url}")
    }
}