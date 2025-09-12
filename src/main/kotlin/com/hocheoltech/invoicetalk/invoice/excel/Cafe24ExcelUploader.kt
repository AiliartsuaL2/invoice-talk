package com.hocheoltech.invoicetalk.invoice.excel

import com.hocheoltech.invoicetalk.global.utils.AbstractExcelUploader
import org.springframework.stereotype.Component
import kotlin.reflect.KProperty1

@Component
class Cafe24ExcelUploader: AbstractExcelUploader(InvoiceExcelUploadDto::class) {
    override fun getParameters(): Map<String, KProperty1<InvoiceExcelUploadDto, *>> {
        return mapOf(
            // 임시
            "쇼핑몰" to InvoiceExcelUploadDto::courierName,
            "주문상품명" to InvoiceExcelUploadDto::productName,
            "수령인 주소" to InvoiceExcelUploadDto::receiveAddress,
            "수령인" to InvoiceExcelUploadDto::receiverName,
        )
    }

    override fun getHeaderRow(): Int {
        return 0
    }
}