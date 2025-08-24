package com.hocheoltech.invoicetalk.invoice.mapper

import com.hocheoltech.invoicetalk.invoice.dto.GetInvoice
import com.hocheoltech.invoicetalk.invoice.dto.PostInvoice
import com.hocheoltech.invoicetalk.invoice.dto.PostInvoiceScan
import com.hocheoltech.invoicetalk.invoice.entity.Invoice
import com.hocheoltech.invoicetalk.user.entity.User
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface InvoiceMapper {
    @Mapping(target = "user", expression = "java(user)")
    fun toEntity(
        request: PostInvoice.Request,
        @Context user: User,
    ): Invoice

    fun toResponse(invoice: Invoice): GetInvoice.Response
    fun toScanResponse(invoice: Invoice): PostInvoiceScan.Response.InvoiceInfo
}
