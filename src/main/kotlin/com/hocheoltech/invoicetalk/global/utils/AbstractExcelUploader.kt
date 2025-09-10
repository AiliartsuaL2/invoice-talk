package com.hocheoltech.invoicetalk.global.utils

import com.hocheoltech.invoicetalk.invoice.excel.InvoiceExcelUpload
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.web.multipart.MultipartFile
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.createInstance

abstract class AbstractExcelUploader(
    private val dtoClass: KClass<InvoiceExcelUpload>,
) {
    fun upload(file: MultipartFile): List<InvoiceExcelUpload> {
        val workbook = WorkbookFactory.create(file.inputStream)
        val sheet = workbook.getSheetAt(0)
        val headerRow = sheet.getRow(getHeaderRow())

        // 헤더명 → 컬럼 index 매핑
        val headerMap = (0 until headerRow.physicalNumberOfCells).associateBy {
            headerRow.getCell(it).stringCellValue.trim()
        }

        val results = mutableListOf<InvoiceExcelUpload>()

        for (rowIdx in 1 until sheet.physicalNumberOfRows) {
            val row = sheet.getRow(rowIdx)
            val dto = dtoClass.createInstance()

            for ((excelHeader, dtoFieldName) in getParameters()) {
                val colIndex = headerMap[excelHeader] ?: continue
                val cellValue = row.getCell(colIndex)?.toString()
                if (cellValue == excelHeader) continue // 헤더 값 무시

                if (dtoFieldName is KMutableProperty1<InvoiceExcelUpload, *>) {
                    @Suppress("UNCHECKED_CAST")
                    val mutableProperty = dtoFieldName as KMutableProperty1<InvoiceExcelUpload, Any?>
                    val convertedValue = convertValue(cellValue, dtoFieldName.returnType)
                    mutableProperty.set(dto, convertedValue)
                }
            }
            if (dto.isValid()) {
                results.add(dto)
            }
        }
        return results
    }

    /**
     * 파라미터 등록 함수
     *  엑셀 헤더명, DTO 프로퍼티
     */
    protected abstract fun getParameters(): Map<String, KProperty1<InvoiceExcelUpload, *>>

    protected abstract fun getHeaderRow(): Int

    /**
     * String → DTO 필드 타입 변환
     */
    private fun convertValue(
        value: String?,
        type: KType,
    ): Any? {
        if (value == null) return null
        return try {
            when (type.classifier) {
                Int::class -> value.toDouble().toInt()
                Long::class -> value.toDouble().toLong()
                Double::class -> value.toDouble()
                Boolean::class -> value.equals("true", ignoreCase = true)
                String::class -> value
                else -> value // 필요 시 확장
            }
        } catch (e: Exception) {
            throw IllegalArgumentException(e.message)
        }
    }
}