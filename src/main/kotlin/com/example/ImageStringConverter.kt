package com.example

import cn.idev.excel.converters.Converter
import cn.idev.excel.converters.ReadConverterContext
import cn.idev.excel.enums.CellDataTypeEnum


class ImageImportConverter(
    private val imageMapping: Map<String, ByteArray>
) : Converter<String> {
    override fun supportJavaTypeKey(): Class<*> {
        return String::class.java
    }

    override fun supportExcelTypeKey(): CellDataTypeEnum {
        return CellDataTypeEnum.STRING
    }

    override fun convertToJavaData(context: ReadConverterContext<*>): String {
        val stringValue = context.getReadCellData().stringValue
        val id = stringValue.split("\"").getOrNull(1)
        if(id == null) {
            return stringValue
        }else{
            val bytes = imageMapping[id]
            return bytes?.toString() ?: context.getReadCellData().stringValue
        }
    }

}