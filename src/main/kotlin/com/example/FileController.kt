package com.example

import cn.idev.excel.FastExcel
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/file")
class FileController {

    @PostMapping("/upload")
    fun upload(@RequestPart("file") file: MultipartFile) {
        // 获取完整的图片映射关系
        val imageMapping = WPSExcelUtil.getImageMapping(file.inputStream)
        // 读取Excel数据
        FastExcel.read(file.inputStream, StyleExcel::class.java, ExcelListener())
            .registerConverter(ImageImportConverter(imageMapping))
            .sheet()
            .doRead()
    }
}