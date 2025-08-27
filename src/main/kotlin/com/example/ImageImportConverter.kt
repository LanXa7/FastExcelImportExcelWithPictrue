package com.example

import cn.idev.excel.converters.Converter
import cn.idev.excel.converters.ReadConverterContext
import cn.idev.excel.enums.CellDataTypeEnum

/**
 * @param imageMapping id和图片url的映射
 * @see WPSExcelUtil.getImageMapping
 * WPS 带有嵌入式图片的Excel导入
 */
class ImageImportConverter(
    private val imageMapping: Map<String, String>
) : Converter<String> {
    override fun supportJavaTypeKey(): Class<*> {
        return String::class.java
    }

    override fun supportExcelTypeKey(): CellDataTypeEnum {
        return CellDataTypeEnum.STRING
    }

    override fun convertToJavaData(context: ReadConverterContext<*>): String {
        val stringValue = context.getReadCellData().stringValue
        // Excel图片输入 为 =DISPIMG("ID_68162A0B91D04C6BA0C96F1CD3F5A1B5",1) 获取ID_xxx
        val id = stringValue.split("\"").getOrNull(1)
        return if(id == null) {
            stringValue
        }else{
            imageMapping[id]?:stringValue
        }
    }

}