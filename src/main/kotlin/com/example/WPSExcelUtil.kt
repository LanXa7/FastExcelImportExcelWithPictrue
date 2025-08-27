package com.example

import org.dom4j.Document
import org.dom4j.Element
import org.dom4j.Namespace
import org.dom4j.QName
import org.dom4j.io.SAXReader
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.zip.ZipInputStream
import kotlin.collections.set
import kotlin.text.substring

object WPSExcelUtil {

    /**
     * @param inputStream excel输入流
     * 遍历zip文件获取所需要的xml文件
     */
    private fun getExcelFiles(inputStream: InputStream): Triple<String?, String?, Map<String, ByteArray>> {
        val zipInputStream = ZipInputStream(inputStream)
        var cellImageXml: String? = null
        var cellImageRelsXml: String? = null
        val imageMap = mutableMapOf<String, ByteArray>()

        zipInputStream.use { zis ->
            generateSequence { zis.nextEntry }
                .forEach { entry ->
                    when {
                        entry.name == "xl/cellimages.xml" -> {
                            cellImageXml = zis.readBytes().toString(StandardCharsets.UTF_8)
                        }

                        entry.name == "xl/_rels/cellimages.xml.rels" -> {
                            cellImageRelsXml = zis.readBytes().toString(StandardCharsets.UTF_8)
                        }

                        entry.name.startsWith("xl/media/") -> {
                            val imageName = entry.name.substring("xl/media/".length)
                            if (imageName.isNotBlank()) {
                                imageMap[imageName] = zis.readBytes()
                            }
                        }
                    }
                    zis.closeEntry()
                }
        }

        return Triple(cellImageXml, cellImageRelsXml, imageMap)
    }

    /**
     * @param cellImagesXml cellImage.xml文件 字符串
     * 分析cellImages.xml文件 获取id和rId的映射关系
     */
    private fun parseCellImagesXml(cellImagesXml: String?): Map<String, String> {
        if (cellImagesXml.isNullOrBlank()) {
            return emptyMap()
        }
        return try {
            val sr = SAXReader()
            val doc: Document = sr.read(cellImagesXml.byteInputStream())

            // 注册命名空间前缀
            val etcNs = Namespace("etc", "http://www.wps.cn/officeDocument/2017/etCustomData")
            val xdrNs = Namespace("xdr", "http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing")
            val aNs = Namespace("a", "http://schemas.openxmlformats.org/drawingml/2006/main")
            val rNs = Namespace("r", "http://schemas.openxmlformats.org/officeDocument/2006/relationships")

            doc.rootElement.add(etcNs)
            doc.rootElement.add(xdrNs)
            doc.rootElement.add(aNs)
            doc.rootElement.add(rNs)

            val idToRidMap = mutableMapOf<String, String>()

            // 使用带前缀的XPath
            val cellImages = doc.selectNodes("//etc:cellImage")
            for (cellImage in cellImages) {
                val cellImageElement = cellImage as Element

                val id = (cellImageElement.selectSingleNode(".//xdr:cNvPr") as? Element)
                    ?.attributeValue("name") ?: continue

                val rId = (cellImageElement.selectSingleNode(".//a:blip") as? Element)
                    ?.attributeValue(QName("embed", rNs)) ?: continue

                idToRidMap[id] = rId
            }
            idToRidMap
        } catch (e: Exception) {
            println("解析cellImages.xml文件出错: ${e.message}")
            emptyMap()
        }
    }

    /**
     * @param cellImagesXmlRels cellImages.xml.rels 字符串
     * 分析cellImages.xml.rels 文件 获取rid和target的映射关系
     */
    private fun parseCellImagesXmlRels(cellImagesXmlRels: String?): Map<String, String> {
        if (cellImagesXmlRels.isNullOrBlank()) {
            return emptyMap()
        }

        return try {
            val rIdToTargetMap = HashMap<String, String>()
            val sr = SAXReader()
            val doc = sr.read(cellImagesXmlRels.byteInputStream())

            /* 1. 给默认命名空间注册一个前缀（例如 ns）*/
            val nsUri = "http://schemas.openxmlformats.org/package/2006/relationships"
            doc.rootElement.add(Namespace("ns", nsUri))

            /* 2. 使用带前缀的 XPath */
            val rels = doc.selectNodes("//ns:Relationship")
            for (rel in rels) {
                val relElement = rel as Element
                val id = relElement.attributeValue("Id")
                val target = relElement.attributeValue("Target")
                    ?.substringAfterLast("/")          // 只保留文件名
                    ?: continue
                rIdToTargetMap[id] = target
            }
            rIdToTargetMap
        } catch (e: Exception) {
            println("解析cellImages.xml.rels文件出错: ${e.message}")
            emptyMap()
        }
    }

    /**
     * 获取完整的图片映射关系
     * @param inputStream Excel文件输入流
     * @return 图片Id到字节数组的映射 后续需要手动遍历上传到oss获得新的图片Id到url的映射
     */
    fun getImageMapping(
        inputStream: InputStream
    ): Map<String, ByteArray> {
        val (cellImagesXml, cellImagesXmlRels, imageMap) = getExcelFiles(inputStream)

        val idToRIdMap = parseCellImagesXml(cellImagesXml)
        val rIdToTargetMap = parseCellImagesXmlRels(cellImagesXmlRels)

        val result = mutableMapOf<String, ByteArray>()

        idToRIdMap.forEach { (id, rId) ->
            val target = rIdToTargetMap[rId]
            val imageBytes = target?.let { imageMap[it] }
            if (imageBytes != null) {
                result[id] = imageBytes
            }
        }

        return result
    }
}