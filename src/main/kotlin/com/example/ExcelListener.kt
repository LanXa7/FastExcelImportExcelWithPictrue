package com.example

import cn.idev.excel.context.AnalysisContext
import cn.idev.excel.event.AnalysisEventListener
import cn.idev.excel.util.ListUtils
import org.dom4j.Element
import org.dom4j.io.SAXReader
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class ExcelListener(
) : AnalysisEventListener<StyleExcel>() {

    private val caches: MutableList<StyleExcel> =
        ListUtils.newArrayListWithExpectedSize(BATCH_COUNT)

    override fun invoke(data: StyleExcel, context: AnalysisContext) {

        caches.add(data)
    }

    override fun doAfterAllAnalysed(context: AnalysisContext) {

    }





    companion object {
        private const val BATCH_COUNT: Int = 50
    }

}