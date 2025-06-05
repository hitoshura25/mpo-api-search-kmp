package com.vmenon.mpo.search.api.internal

import org.koin.dsl.koinApplication

internal object IsolatedKoinContext {
    val koinApp = koinApplication {
        modules(baseModule)
    }

    val koin = koinApp.koin
}