package com.hocheoltech.invoicetalk.invoice.controller

import com.hocheoltech.invoicetalk.invoice.dto.GetInvoice
import com.hocheoltech.invoicetalk.invoice.dto.GetInvoiceCount
import com.hocheoltech.invoicetalk.invoice.dto.PostInvoice
import com.hocheoltech.invoicetalk.invoice.dto.PostInvoiceScan
import com.hocheoltech.invoicetalk.invoice.service.InvoiceService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class InvoiceController(
    private val invoiceService: InvoiceService,
) {
    // 단건등록
    @PostMapping("/api/v1/invoices")
    fun createInvoice(
        @RequestHeader(value = "user-id")
        userId: Long,
        @Valid @RequestBody
        request: PostInvoice.Request,
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(invoiceService.createInvoice(userId, request))
    }

    // 엑셀 등록
    @PostMapping("/api/v1/invoices/excel")
    fun createInvoice(
        @RequestHeader(value = "user-id")
        userId: Long,
        @Valid @RequestBody
        request: List<PostInvoice.Request>,
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(invoiceService.createInvoices(userId, request))
    }

    // 송장 내역 리스트 조회
    @GetMapping("/api/v1/invoices")
    fun getInvoices(
        @RequestHeader(value = "user-id")
        userId: Long,
        @ModelAttribute @Valid
        request: GetInvoice.Request,
    ): ResponseEntity<List<GetInvoice.Response>> {
        return ResponseEntity.ok(invoiceService.getInvoices(userId, request))
    }

    // 스캔
    @PostMapping("/api/v1/invoices/scan")
    fun scanInvoice(
        @RequestHeader(value = "user-id")
        userId: Long,
        @Valid @RequestBody
        request: PostInvoiceScan.Request,
    ): ResponseEntity<PostInvoiceScan.Response> {
        return ResponseEntity.ok(invoiceService.scanInvoice(userId, request))
    }

    // 송장 상세 내역 조회
    @GetMapping("/api/v1/invoices/{id}")
    fun getInvoice(
        @RequestHeader(value = "user-id")
        userId: Long,
        @PathVariable id: Long,
    ): ResponseEntity<GetInvoice.Response> {
        return ResponseEntity.ok(invoiceService.getInvoice(userId, id))
    }

    // 내역 상태별 개수 조회
    @GetMapping("/api/v1/invoices/count")
    fun getInvoiceCount(
        @RequestHeader(value = "user-id")
        userId: Long,
    ): ResponseEntity<GetInvoiceCount.Response> {
        return ResponseEntity.ok(invoiceService.getInvoiceCount(userId))
    }
}
