package com.hocheoltech.invoicetalk.invoice.excel

import com.hocheoltech.invoicetalk.global.utils.AbstractExcelUploader
import org.springframework.stereotype.Component
import kotlin.reflect.KProperty1

@Component
class NaverExcelUploader: AbstractExcelUploader(InvoiceExcelUploadDto::class) {
    override fun getParameters(): Map<String, KProperty1<InvoiceExcelUploadDto, *>> {
        return mapOf(
            "택배사" to InvoiceExcelUploadDto::courierName,
            "상품명" to InvoiceExcelUploadDto::productName,
            "통합배송지" to InvoiceExcelUploadDto::receiveAddress,
            "수취인명" to InvoiceExcelUploadDto::receiverName,
        )
    }

    override fun getHeaderRow(): Int {
        return 1
    }
}