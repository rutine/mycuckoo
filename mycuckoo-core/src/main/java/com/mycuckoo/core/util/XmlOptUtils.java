package com.mycuckoo.core.util;

import com.mycuckoo.core.exception.MyCuckooException;
import com.mycuckoo.core.exception.SystemException;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能说明: xml文件工具类
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 22, 2014 9:43:01 PM
 */
public class XmlOptUtils {
    private static final Logger logger = LoggerFactory.getLogger(XmlOptUtils.class);

    private XmlOptUtils() {
    }


    /**
     * 将字符串转为Document
     *
     * @param str xml格式的字符串
     * @return
     * @author rutine
     * @time Oct 3, 2012 10:19:50 AM
     */
    public static Document readString(String str) {
        try {
            return DocumentHelper.parseText(str);
        } catch (Exception e) {
            logger.error("readString occur error : ", e);
            throw new MyCuckooException("readString occur error : ", e);
        }
    }

    /**
     * 将xml文档内容转为String
     *
     * @param document
     * @return 字符串
     * @author rutine
     * @time Oct 3, 2012 10:06:47 AM
     */
    public static String writeString(Document document) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            OutputFormat format = new OutputFormat("  ", true, "UTF-8");
            XMLWriter writer = null;
            try {
                writer = new XMLWriter(out, format);
                writer.write(document);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
            return out.toString(StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            logger.error("writeString occur error : ", e);
            throw new MyCuckooException("writeString occur error : ", e);
        }
    }


    /**
     * 载入一个xml文档
     *
     * @param fileName uri 文件路径
     * @return 成功返回Document对象，失败返回null
     * @throws MyCuckooException
     * @author rutine
     * @time Oct 3, 2012 11:14:43 AM
     */
    public static Document readXML(String fileName) throws SystemException {
        SAXReader saxReader = new SAXReader();
        File file = requireExists(fileName);

        try {
            return saxReader.read(file);
        } catch (DocumentException e) {
            logger.error("load xml occur error : ", e);
            throw new SystemException("load xml occur error : ", e);
        }
    }

    /**
     * 将Document对象保存为一个xml文件到本地
     *
     * @param document 需要保存的document对象
     * @param fileName 保存的文件名
     * @return true : 保存成功  flase : 失败
     * @throws MyCuckooException
     * @author rutine
     * @time Oct 3, 2012 11:32:33 AM
     */
    public static boolean writeXML(Document document, String fileName) throws SystemException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        File file = requireExists(fileName);

        XMLWriter writer = null;
        try {
            writer = new XMLWriter(new FileOutputStream(file), format);
            writer.write(document);
        } catch (IOException e) {
            logger.error("write " + fileName + " IO occur error : ", e);
            throw new SystemException("write " + fileName + " IO occur error : ", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error("close " + fileName + " IO occur error : ", e);
                }
            }
        }
        return true;
    }

    /**
     * 获得xml一个节点
     *
     * @param doc
     * @param xmlPath
     * @return
     * @author rutine
     * @time Oct 3, 2012 1:28:00 PM
     */
    public static Element selectSingleNode(Document doc, String xmlPath) {
        return (Element) doc.selectSingleNode(xmlPath);
    }

    /**
     * 获得xml一个节点的文本值
     *
     * @param doc
     * @param xmlPath
     * @return
     * @author rutine
     * @time Oct 3, 2012 1:31:22 PM
     */
    public static String selectSingleText(Document doc, String xmlPath) {
        Element el = selectSingleNode(doc, xmlPath);
        return el.getText();
    }

    /**
     * 获得XML一个节点的属性值
     *
     * @param doc
     * @param xmlPath
     * @param attributeName
     * @return
     * @author rutine
     * @time Oct 3, 2012 1:35:07 PM
     */
    public static String selectSingleAttribute(Document doc, String xmlPath, String attributeName) {
        Element el = selectSingleNode(doc, xmlPath);
        return el.attributeValue(attributeName);
    }

    /**
     * 获得xml节点
     *
     * @param doc
     * @param xmlPath
     * @return
     * @author rutine
     * @time Oct 3, 2012 1:37:36 PM
     */
    @SuppressWarnings("unchecked")
    public static List<Node> selectNodes(Document doc, String xmlPath) {
        return (List<Node>) doc.selectNodes(xmlPath);
    }

    /**
     * 获得xml节点的文本
     *
     * @param doc
     * @param xmlPath
     * @return
     * @author rutine
     * @time Oct 3, 2012 1:41:12 PM
     */
    @SuppressWarnings("unchecked")
    public static List<String> selectNodesText(Document doc, String xmlPath) {
        List<String> textList = new ArrayList<>();
        List<Node> elements = (List<Node>) doc.selectNodes(xmlPath);
        if (elements != null && !elements.isEmpty()) {
            for (Node element : elements) {
                textList.add(element.getText());
            }
        }

        return textList;
    }

    private static File requireExists(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new MyCuckooException("对不起，文件" + fileName + "找不到.");
        }
        return file;
    }
}
