package com.vmenon.mpo.search.api.internal

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema

internal class TestSqlDriverFactoryWrapper : SqlDriverFactory {
    private val factory = createTestSqlDriverFactory()
    private var driver: SqlDriver? = null

    override suspend fun provideDbDriver(schema: SqlSchema<QueryResult.AsyncValue<Unit>>): SqlDriver {
        if (driver == null) {
            driver = factory.provideDbDriver(schema)
        }
        return driver!!
    }

    fun close() {
        driver?.close()
        driver = null
    }
}