package com.hocheoltech.invoicetalk.invoice.excel

open class InvoiceExcelUpload {
    // 택배사명
    var courierName: String? = null

    // 송장번호
    var number: String? = null

    // 상품명
    var productName: String? = null

    // 목적지
    var receiveAddress: String? = null

    // 받는 사람
    var receiverName: String? = null

    fun isValid(): Boolean {
        if (
            this.courierName != null &&
            this.number != null &&
            this.productName != null &&
            this.receiveAddress != null &&
            this.receiverName != null
        ) {
            return true
        }
        return false
    }
}