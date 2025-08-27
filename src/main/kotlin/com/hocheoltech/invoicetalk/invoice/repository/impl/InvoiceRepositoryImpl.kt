package com.hocheoltech.invoicetalk.invoice.repository.impl

import com.hocheoltech.invoicetalk.global.enums.InvoiceStatus
import com.hocheoltech.invoicetalk.invoice.dto.GetInvoiceCount
import com.hocheoltech.invoicetalk.invoice.dto.QGetInvoiceCount_QueryResult
import com.hocheoltech.invoicetalk.invoice.entity.Invoice
import com.hocheoltech.invoicetalk.invoice.entity.QInvoice.invoice
import com.hocheoltech.invoicetalk.invoice.repository.custom.CustomInvoiceRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDateTime

class InvoiceRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : CustomInvoiceRepository {
    override fun findForScan(
        userId: Long,
        status: InvoiceStatus,
        number: String,
        invoiceId: Long?,
    ): List<Invoice> {
        return jpaQueryFactory.selectFrom(
            invoice
        ).where(
            invoice.user.id.eq(userId),
            invoice.status.eq(status),
            invoice.number.eq(number),
            invoiceId?.let { invoice.id.eq(it) },
        ).fetch()
    }

    override fun findInvoiceCountByStatus(userId: Long): List<GetInvoiceCount.QueryResult> {
        return jpaQueryFactory.select(
            QGetInvoiceCount_QueryResult(
                invoice.status,
                invoice.count(),
            )
        ).from(invoice)
            .where(invoice.user.id.eq(userId))
            .groupBy(invoice.status)
            .fetch()
    }

    override fun existsSameNumber(
        invoiceId: Long?,
        userId: Long,
        courierName: String,
        number: String,
    ): Boolean {
        val data = jpaQueryFactory
            .select(invoice)
            .from(invoice)
            .where(
                invoice.user.id.eq(userId),
                invoice.courierName.eq(courierName),
                invoice.number.eq(number),
                invoiceId?.let {
                    invoice.id.ne(it)
                },
            ).fetchFirst()
        return data != null
    }
}
