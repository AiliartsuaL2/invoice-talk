package com.hocheoltech.invoicetalk.global.config

import com.p6spy.engine.logging.Category
import com.p6spy.engine.spy.P6SpyOptions
import com.p6spy.engine.spy.appender.MessageFormattingStrategy
import jakarta.annotation.PostConstruct
import org.hibernate.engine.jdbc.internal.FormatStyle
import org.springframework.context.annotation.Configuration


@Configuration
class P6SpyConfig : MessageFormattingStrategy {
    @PostConstruct
    fun setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().logMessageFormat = this::class.java.name
    }

    override fun formatMessage(
        connectionId: Int,
        now: String,
        elapsed: Long,
        category: String,
        prepared: String,
        sql: String?,
        url: String,
    ): String {
        val formattedSql = formatSql(category, sql)?.replace(Regex("\\s+"), " ")
        return formatLog(url, elapsed, category, formattedSql)
    }

    private fun formatSql(
        category: String,
        sql: String?,
    ): String? {
        return if (!sql.isNullOrBlank() && isStatement(category)) {
            val trimmedSQL = trim(sql)
            if (isDdl(trimmedSQL)) {
                FormatStyle.DDL.formatter.format(sql)
            } else {
                FormatStyle.HIGHLIGHT.formatter.format(sql)
            }
        } else {
            sql
        }
    }

    private fun formatLog(
        url: String,
        elapsed: Long,
        category: String,
        formattedSql: String?,
    ): String {
        return buildString {
            append("[${if ("master" in url) "master" else "replica"}] ")
            append(category)
            append(" | ")
            append("$elapsed ms")

            if (!formattedSql.isNullOrBlank()) {
                append(" | $formattedSql")
            }
        }
    }

    private fun isDdl(trimmedSQL: String): Boolean {
        return trimmedSQL.startsWith("create") ||
                trimmedSQL.startsWith("alter") ||
                trimmedSQL.startsWith("comment")
    }

    private fun trim(sql: String): String = sql.trim().lowercase()

    private fun isStatement(category: String): Boolean {
        return Category.STATEMENT.name == category
    }
}

