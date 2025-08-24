package com.hocheoltech.invoicetalk.invoice.controller

import com.hocheoltech.invoicetalk.invoice.dto.GetInvoice
import com.hocheoltech.invoicetalk.invoice.dto.PostInvoice
import com.hocheoltech.invoicetalk.invoice.dto.PostInvoiceScan
import com.hocheoltech.invoicetalk.invoice.service.InvoiceService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class InvoiceController(
    private val invoiceService: InvoiceService,
) {
    @PostMapping("/api/v1/invoices")
    fun createInvoice(
        @RequestHeader
        userId: Long,
        @Valid @RequestBody
        request: PostInvoice.Request,
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(invoiceService.createInvoice(userId, request))
    }

    // TODO 변경 예정
    @PostMapping("/api/v1/invoices/excel")
    fun createInvoice(
        @RequestHeader
        userId: Long,
        @Valid @RequestBody
        request: List<PostInvoice.Request>,
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(invoiceService.createInvoices(userId, request))
    }

    @GetMapping("/api/v1/invoices")
    fun getInvoice(
        @RequestHeader
        userId: Long,
    ): ResponseEntity<List<GetInvoice.Response>> {
        return ResponseEntity.ok(invoiceService.getInvoice(userId))
    }

    @PostMapping("/api/v1/invoices/scan")
    fun scanInvoice(
        @RequestHeader
        userId: Long,
        request: PostInvoiceScan.Request,
    ): ResponseEntity<PostInvoiceScan.Response> {
        return ResponseEntity.ok(invoiceService.scanInvoice(userId, request))
    }
}
