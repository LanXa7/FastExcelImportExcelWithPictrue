package com.example

import cn.idev.excel.annotation.ExcelProperty
import java.math.BigDecimal

class StyleExcel {
    @ExcelProperty(value = ["店铺"])
    var store: String? = null

    @ExcelProperty(value = ["买手"])
    var buyer: String? = null

    @ExcelProperty(value = ["供给方式"])
    var modeOfSupply: String? = null

    @ExcelProperty(value = ["国家"])
    var countryCode: String? = null

    @ExcelProperty(value = ["灵感图来源"])
    var inspirationImageSource: String? = null

    @ExcelProperty(value = ["商品类型"])
    var productType: String? = null

    @ExcelProperty(value = ["元素"])
    var elements: String? = null

    @ExcelProperty(value = ["主题"])
    var theme: String? = null

    @ExcelProperty(value = ["市场"])
    var market: String? = null

    @ExcelProperty(value = ["系列"])
    var series: String? = null

    @ExcelProperty(value = ["货盘类型"])
    var palletType: String? = null

    @ExcelProperty(value = ["内部品类"])
    var innerType: String? = null

    @ExcelProperty(value = ["灵感图"])
    var inspirationImage: String? = null

    @ExcelProperty(value = ["AIGC图"])
    var aigcImage: String? = null

    @ExcelProperty(value = ["设计图"])
    var planImage: String? = null

    @ExcelProperty(value = ["期望成本"])
    var expectedCost: BigDecimal? = null

    @ExcelProperty(value = ["拆板备注"])
    var dismantlingRemark: String? = null

    @ExcelProperty(value = ["视觉需求备注"])
    var visualRequirementNote: String? = null

    @ExcelProperty(value = ["视觉需求图"])
    var visualRequirementImage: String? = null

    @ExcelProperty(value = ["视觉需求类型"])
    var visualRequirementType: String? = null

    @ExcelProperty(value = ["分配人"])
    var assigner: String? = null

    @ExcelProperty(value = ["设计师"])
    var designer: String? = null

    @ExcelProperty(value = ["波段"])
    var band: String? = null

    @ExcelProperty(value = ["tryon背景图"])
    var backgroundImage: String? = null

    @ExcelProperty(value = ["tryon模特图"])
    var modelImage: String? = null

    @ExcelProperty(value = ["tryon姿势图参考图"])
    var poseImage1: String? = null

    @ExcelProperty(value = ["tryon姿势图参考图"])
    var poseImage2: String? = null

    @ExcelProperty(value = ["tryon姿势图参考图"])
    var poseImage3: String? = null

    @ExcelProperty(value = ["tryon姿势图参考图"])
    var poseImage4: String? = null
}