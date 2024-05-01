package com.mycuckoo.utils;

import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    protected static Logger logger = LoggerFactory.getLogger(XmlOptUtils.class);


    /**
     * 将字符串转为Document
     *
     * @param str xml格式的字符串
     * @return
     * @author rutine
     * @time Oct 3, 2012 10:19:50 AM
     */
    public static Document readString(String str) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(str);
        } catch (Exception e) {
            logger.error("readString occur error : ", e);
            throw new ApplicationException("readString occur error : ", e);
        }

        return doc;
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
        String str = "";
        try {
            // 使用输出流来进行转化
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 使用UTF-8编码
            OutputFormat format = new OutputFormat("  ", true, "UTF-8");
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(document);
            str = out.toString("UTF-8");
        } catch (Exception e) {
            logger.error("writeString occur error : ", e);
            throw new ApplicationException("writeString occur error : ", e);
        }

        return str;
    }


    /**
     * 载入一个xml文档
     *
     * @param fileName uri 文件路径
     * @return 成功返回Document对象，失败返回null
     * @throws ApplicationException
     * @author rutine
     * @time Oct 3, 2012 11:14:43 AM
     */
    public static Document readXML(String fileName) throws SystemException {
        Document document = null;
        SAXReader saxReader = new SAXReader();
        File file = new File(fileName);
        if (!file.exists()) {
            throw new ApplicationException("对不起，文件" + fileName + "找不到.");
        }

        try {
            document = saxReader.read(file);
        } catch (DocumentException e) {
            logger.error("load xml occur error : ", e);
            throw new SystemException("load xml occur error : ", e);
        }

        return document;
    }

    /**
     * 将Document对象保存为一个xml文件到本地
     *
     * @param document 需要保存的document对象
     * @param fileName 保存的文件名
     * @return true : 保存成功  flase : 失败
     * @throws ApplicationException
     * @author rutine
     * @time Oct 3, 2012 11:32:33 AM
     */
    public static boolean writeXML(Document document, String fileName) throws SystemException {
        boolean flag = true;
        /* 将document中的内容写入文件中 */
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        File file = new File(fileName);
        if (!file.exists()) {
            throw new ApplicationException("对不起，文件" + fileName + "找不到.");
        }

        try {
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            writer.write(document);
            writer.close();
        } catch (UnsupportedEncodingException e) {
            logger.error("write " + fileName + " UTF-8 error : ", e);
            throw new SystemException("write " + fileName + " UTF error : ", e);
        } catch (IOException e) {
            logger.error("write " + fileName + " IO occur error : ", e);
            throw new SystemException("write " + fileName + " IO occur error : ", e);
        }
        return flag;
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
        Element el = (Element) doc.selectSingleNode(xmlPath);
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
    public static List<Element> selectNodes(Document doc, String xmlPath) {
        List<Element> elList = (List<Element>) doc.selectNodes(xmlPath);
        return elList;
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
        List<String> textList = new ArrayList<String>();
        List<Element> elements = (List<Element>) doc.selectNodes(xmlPath);
        if (elements != null && elements.size() > 0) {
            for (Element element : elements) {
                textList.add(element.getText());
            }
        }

        return textList;
    }


    @SuppressWarnings("unchecked")
    public static void main(String args[]) {
        String filePath = "WebContent/SystemConfig.xml";
        Document doc = null;
        try {
            doc = readXML(filePath);
        } catch (SystemException e) {
            e.printStackTrace();
        }

        List<Element> elList = doc.selectNodes("//JMX/tomcat");
        for (Element e : elList) {
            String val = e.attributeValue("default");
            System.out.println("-------->  " + val);
        }
    }
}
