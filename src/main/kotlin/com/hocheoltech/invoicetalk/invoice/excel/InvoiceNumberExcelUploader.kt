package com.hocheoltech.invoicetalk.invoice.excel

import com.hocheoltech.invoicetalk.global.utils.AbstractExcelUploader
import org.springframework.stereotype.Component
import kotlin.reflect.KProperty1

// 택배 송장 번호 엑셀 업로드 리스트
@Component
class InvoiceNumberExcelUploader: AbstractExcelUploader(InvoiceExcelUploadDto::class) {
    override fun getParameters(): Map<String, KProperty1<InvoiceExcelUploadDto, *>> {
        return mapOf(
            // isValid 때문에 추가
            "발제" to InvoiceExcelUploadDto::courierName,
            // isValid 때문에 추가
            "운임" to InvoiceExcelUploadDto::productName,

            "운송장번호" to InvoiceExcelUploadDto::number,
            "도착지" to InvoiceExcelUploadDto::receiveAddress,
            "수화주" to InvoiceExcelUploadDto::receiverName,
        )
    }

    override fun getHeaderRow(): Int {
        return 1
    }

}