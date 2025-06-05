package com.vmenon.mpo.search.api.internal

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val baseModule = module {
    single<HttpClientEngine> { CIO.create() }
    singleOf(::SearchRepository)
    singleOf(::SearchApiDataSource)
    singleOf(::SearchCacheDataSource)
}