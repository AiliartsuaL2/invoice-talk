package com.hocheoltech.invoicetalk.invoice.repository.impl

import com.hocheoltech.invoicetalk.global.enums.InvoiceStatus
import com.hocheoltech.invoicetalk.invoice.entity.Invoice
import com.hocheoltech.invoicetalk.invoice.entity.QInvoice.invoice
import com.hocheoltech.invoicetalk.invoice.repository.custom.CustomInvoiceRepository
import com.querydsl.jpa.impl.JPAQueryFactory

class InvoiceRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
): CustomInvoiceRepository {
    override fun findForScan(userId: Long, status: InvoiceStatus, number: String, invoiceId: Long?): List<Invoice> {
        return jpaQueryFactory.selectFrom(
            invoice
        ).where(
            invoice.user.id.eq(userId),
            invoice.status.eq(status),
            invoice.number.eq(number),
            invoiceId?.let { invoice.id.eq(it) },
        ).fetch()
    }
}
