package com.hocheoltech.invoicetalk.invoice.excel

import com.hocheoltech.invoicetalk.global.utils.AbstractExcelUploader
import org.springframework.stereotype.Component
import kotlin.reflect.KProperty1

@Component
class Cafe24ExcelUploader: AbstractExcelUploader(InvoiceExcelUpload::class) {
    override fun getParameters(): Map<String, KProperty1<InvoiceExcelUpload, *>> {
        return mapOf(
            // 임시
            "쇼핑몰" to InvoiceExcelUpload::courierName,
            "상품번호" to InvoiceExcelUpload::number,
            "주문상품명" to InvoiceExcelUpload::productName,
            "수령인 주소" to InvoiceExcelUpload::receiveAddress,
            "수령인" to InvoiceExcelUpload::receiverName,
        )
    }

    override fun getHeaderRow(): Int {
        return 0
    }
}