package com.vmenon.mpo.search.api.internal

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.util.Properties

class JdbcDatabaseDriverFactory : SqlDriverFactory {
    override suspend fun provideDbDriver(
        schema: SqlSchema<QueryResult.AsyncValue<Unit>>
    ): SqlDriver {
        return JdbcSqliteDriver("jdbc:sqlite:mpo.db", Properties())
            .also { schema.create(it).await() }
    }
}