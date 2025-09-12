package com.hocheoltech.invoicetalk.invoice.excel

import com.hocheoltech.invoicetalk.global.utils.AbstractExcelUploader
import org.springframework.stereotype.Component
import kotlin.reflect.KProperty1

@Component
class AuctionExcelUploader: AbstractExcelUploader(InvoiceExcelUploadDto::class) {
    override fun getParameters(): Map<String, KProperty1<InvoiceExcelUploadDto, *>> {
        return mapOf(
            // 임시
            "택배사명(발송방법)" to InvoiceExcelUploadDto::courierName,
            "상품명" to InvoiceExcelUploadDto::productName,
            "주소" to InvoiceExcelUploadDto::receiveAddress,
            "수령인명" to InvoiceExcelUploadDto::receiverName,
        )
    }

    override fun getHeaderRow(): Int {
        return 0
    }
}