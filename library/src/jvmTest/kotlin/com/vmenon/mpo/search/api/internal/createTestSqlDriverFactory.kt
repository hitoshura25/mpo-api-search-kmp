package com.vmenon.mpo.search.api.internal

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.util.Properties

internal actual fun createTestSqlDriverFactory(): SqlDriverFactory = object : SqlDriverFactory {
    override suspend fun provideDbDriver(
        schema: SqlSchema<QueryResult.AsyncValue<Unit>>
    ): SqlDriver {
        return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY, Properties())
            .also { schema.create(it).await() }
    }
}