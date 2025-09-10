package com.hocheoltech.invoicetalk.invoice.excel

import com.hocheoltech.invoicetalk.global.utils.AbstractExcelUploader
import org.springframework.stereotype.Component
import kotlin.reflect.KProperty1

@Component
class NaverExcelUploader: AbstractExcelUploader(InvoiceExcelUpload::class) {
    override fun getParameters(): Map<String, KProperty1<InvoiceExcelUpload, *>> {
        return mapOf(
            "택배사" to InvoiceExcelUpload::courierName,
            "상품번호" to InvoiceExcelUpload::number,
            "상품명" to InvoiceExcelUpload::productName,
            "통합배송지" to InvoiceExcelUpload::receiveAddress,
            "수취인명" to InvoiceExcelUpload::receiverName,
        )
    }

    override fun getHeaderRow(): Int {
        return 1
    }
}