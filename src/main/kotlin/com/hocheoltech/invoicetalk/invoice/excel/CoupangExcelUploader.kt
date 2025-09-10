package com.hocheoltech.invoicetalk.invoice.excel

import com.hocheoltech.invoicetalk.global.utils.AbstractExcelUploader
import org.springframework.stereotype.Component
import kotlin.reflect.KProperty1

@Component
class CoupangExcelUploader: AbstractExcelUploader(InvoiceExcelUpload::class) {
    override fun getParameters(): Map<String, KProperty1<InvoiceExcelUpload, *>> {
        return mapOf(
            "택배사" to InvoiceExcelUpload::courierName,
            "묶음배송번호" to InvoiceExcelUpload::number,
            "등록상품명" to InvoiceExcelUpload::productName,
            "수취인 주소" to InvoiceExcelUpload::receiveAddress,
            "수취인이름" to InvoiceExcelUpload::receiverName,
        )
    }

    override fun getHeaderRow(): Int {
        return 0
    }
}