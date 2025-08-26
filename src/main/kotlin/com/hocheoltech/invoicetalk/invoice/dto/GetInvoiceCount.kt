package com.hocheoltech.invoicetalk.invoice.dto

import com.hocheoltech.invoicetalk.global.enums.InvoiceStatus
import com.querydsl.core.annotations.QueryProjection

class GetInvoiceCount {
    data class Response (
        val todayProcessCount: Long,
        val allCount: Long,
        val pendingCount: Long,
        val successCount: Long,
        val errorCount: Long,
    )
    data class QueryResult @QueryProjection constructor(
        val status: InvoiceStatus,
        val count: Long,
    )
}
