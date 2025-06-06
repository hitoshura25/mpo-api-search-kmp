package com.vmenon.mpo.search.api.internal

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import app.cash.sqldelight.driver.worker.expected.Worker

internal actual fun TestDatabaseDriverFactory(): DatabaseDriverFactory = object : DatabaseDriverFactory {
    override suspend fun provideDbDriver(
        schema: SqlSchema<QueryResult.AsyncValue<Unit>>
    ): SqlDriver {
        return WebWorkerDriver(
            Worker(
                js("""new URL("./in-memory.worker.js", import.meta.url)""")
            )
        ).also { schema.create(it).await() }
    }
}