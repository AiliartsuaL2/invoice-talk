package com.hocheoltech.invoicetalk.invoice.dto

import com.hocheoltech.invoicetalk.global.anotation.NotNullEnum
import com.hocheoltech.invoicetalk.global.enums.InvoiceStatus
import com.hocheoltech.invoicetalk.invoice.entity.Invoice
import jakarta.validation.constraints.Min
import org.jetbrains.annotations.NotNull
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

class GetInvoice {
    data class Request(
        @field:NotNullEnum
        val status: StatusType?,
        @field:NotNull
        @field:Min(value = 1)
        val pageNumber: Int?,
        @field:NotNull
        @field:Min(value = 1)
        val size: Int?,
    ) {
        fun toPageable(): Pageable {
            return PageRequest.of(pageNumber!! - 1, size!!)
        }

        fun toInvoiceStatus(): Set<InvoiceStatus> {
            return when (this.status!!) {
                StatusType.ALL, StatusType.PROCESSED -> {
                    setOf(
                        InvoiceStatus.PENDING,
                        InvoiceStatus.SUCCESS,
                        InvoiceStatus.ERROR,
                    )
                }

                StatusType.PENDING -> setOf(InvoiceStatus.PENDING)
                StatusType.SUCCESS -> setOf(InvoiceStatus.SUCCESS)
                StatusType.ERROR -> setOf(InvoiceStatus.ERROR)
            }
        }

        enum class StatusType {
            ALL,
            PROCESSED,
            PENDING,
            SUCCESS,
            ERROR,
        }
    }

    data class Response(
        val id: Long,
        val status: ResponseStatus,
        val courierName: String,
        val number: String,
        val productName: String,
        val sendAddress: String,
        val receiveAddress: String,
        val receiverName: String,
        val scannedAt: LocalDateTime?,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime?,
    )

    enum class ResponseStatus {
        PENDING,
        SUCCESS,
        CANCELED,
        ERROR;

        companion object {
            @JvmStatic
            fun from(invoice: Invoice, statusType: Request.StatusType): ResponseStatus {
                // PROCESSED 로 요청하고, 대기중인데 히스토리가 있는 경우 취소된 건
                return if (
                    statusType == Request.StatusType.PROCESSED &&
                    invoice.status == InvoiceStatus.PENDING &&
                    invoice.histories.isNotEmpty()
                ) {
                    CANCELED
                } else {
                    ResponseStatus.valueOf(invoice.status.name)
                }
            }

            @JvmStatic
            fun from(invoice: Invoice): ResponseStatus {
                return ResponseStatus.valueOf(invoice.status.name)
            }
        }
    }
}
