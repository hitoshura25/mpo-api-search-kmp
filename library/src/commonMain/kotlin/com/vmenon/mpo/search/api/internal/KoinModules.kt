package com.vmenon.mpo.search.api.internal

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val baseModule = module {
    singleOf(::SearchRepository)
    singleOf(::SearchApiDataSource)
    singleOf(::SearchCacheDataSource)
    singleOf(::SearchUseCase)
}