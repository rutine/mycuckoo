package com.mycuckoo.common.utils;

import com.mycuckoo.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static com.mycuckoo.common.constant.Common.MYCUCKOO_SYSTEM_FILE_DIR;
import static com.mycuckoo.common.constant.Common.WEB_APP_ROOT_KEY;

/**
 * 功能说明: 常用工具类
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 22, 2014 9:36:10 PM
 */
public class CommonUtils {
    private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    /**
     * 加密操作
     *
     * @param data
     * @return
     * @author rutine
     * @time Oct 4, 2012 1:08:18 PM
     */
    public static String encrypt(String data) {
        return PwdCrypt.getInstance().encrypt(data);
    }

    /**
     * 解密操作
     *
     * @param data
     * @return
     * @author rutine
     * @time Oct 4, 2012 1:08:36 PM
     */
    public static String decrypt(String data) {
        return PwdCrypt.getInstance().decrypt(data);
    }

    /**
     * 获得一句话的首字母
     *
     * @param words
     * @return
     * @author rutine
     * @time Oct 6, 2012 2:38:11 PM
     */
    public static String getFirstLetters(String words) {
        return FirstLetter.getFirstLetters(words);
    }

    /**
     * 随机生成字符，含大写、小写、数字
     *
     * @return
     * @author rutine
     * @time Oct 6, 2012 10:43:40 AM
     */
    public static String getRandomChar() {
        int index = (int) Math.round(Math.random() * 2);
        String randChar = "";
        switch (index) {
            case 0:// 大写字符
                randChar = String.valueOf((char) Math.round(Math.random() * 25 + 65));
                break;
            case 1:// 小写字符
                randChar = String.valueOf((char) Math.round(Math.random() * 25 + 97));
                break;
            default:// 数字
                randChar = String.valueOf(Math.round(Math.random() * 9));
                break;
        }

        return randChar;
    }

    /**
     * 检查是否为null
     *
     * @param obj 被检查对象
     * @return
     * @author rutine
     * @time Oct 4, 2012 1:08:52 PM
     */
    public static boolean isNull(Object obj) {
        if (obj == null) return true;
        return false;
    }

    /**
     * 检查字符串是否为空
     *
     * @param str
     * @return
     * @author rutine
     * @time Oct 4, 2012 1:09:16 PM
     */
    public static boolean isEmpty(String str) {
        if ("".equals(str.trim())) return true;
        return false;
    }

    /**
     * 检查字符串是否为空或null
     *
     * @param str 被检查字符串
     * @return
     * @author rutine
     * @time Oct 4, 2012 1:10:16 PM
     */
    public static boolean isNullOrEmpty(String str) {
        if (isNull(str) || isEmpty(str)) return true;
        return false;
    }

    /**
     * 获得web根路径
     *
     * @return
     * @author rutine
     * @time Oct 6, 2012 10:29:18 AM
     */
    public static String getResourcePath() {
        String resPath = System.getProperty(WEB_APP_ROOT_KEY);
        if (resPath == null) {
            resPath = System.getProperty("catalina.home");
        }
        if (resPath == null) {
            resPath = System.getProperty("user.dir");
        }

        logger.info("webAppRootKey path ----> {}", resPath);

        return resPath == null ? "" : resPath;
    }

    /**
     * 系统集群时获得存放文件路径 为系统配置附件所用
     *
     * @param fileName 文件名称 可能为空
     * @return
     * @author rutine
     * @time Oct 6, 2012 10:30:53 AM
     */
    public static String getClusterResourcePath(String fileName) {
        String separate = File.separator;
        String path = getResourcePath();
        String rootDir = separate;// 默认为分隔符 linux unix
        if ("\\".equals(separate)) {// windows系统
            rootDir = path.substring(0, 2);
        }
        String sysConfigPath = rootDir + separate + MYCUCKOO_SYSTEM_FILE_DIR + separate + fileName;
        File sysConfigFile = new File(sysConfigPath);
        String resourcePath = (sysConfigFile.exists() ? sysConfigPath : (path + fileName));

        logger.info("mycuckoo cluster resource path --> {}", resourcePath);

        return resourcePath;
    }

    /**
     * 保存文件
     *
     * @param dirPath  文件路径
     * @param fileName 文件名
     * @param is       文件输入流
     * @author rutine
     * @time Oct 6, 2012 11:05:02 AM
     */
    public static String saveFile(String dirPath, String fileName, InputStream is) throws SystemException {
        if (is == null) {
            return null;
        }

        // 文件上传路径
        String absolutePath = getClusterResourcePath("") + dirPath;
        // 如果路径不存在则自动创建
        File dirFile = new File(absolutePath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        // 文件上传
        String filePath = absolutePath + File.separator + fileName;
        logger.info("save file path --> {}", filePath);
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            byte[] buffer = new byte[10240];
            int len = 0;
            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
                fos.flush();
            }
            is.close();
            fos.close();
        } catch (IOException e) {
            logger.error("write file error", e);

            throw new SystemException("", e);
        }

        return filePath;
    }

    /**
     * 下载文件
     *
     * @param dirPath  文件路径
     * @param fileName 文件名
     * @param isOnline 是否在线打开
     * @param response
     * @return
     * @throws SystemException
     * @author rutine
     * @time Oct 6, 2012 2:22:06 PM
     */
    public static void downloadFile(String dirPath, String fileName,
                                    boolean isOnline, HttpServletResponse response) throws SystemException {

        // 文件下载
        // String filePath = request.getRealPath(File.separator) + dirPath + File.separator + fileName;
        String filePath = getClusterResourcePath("") + dirPath + File.separator + fileName;
        BufferedInputStream bufInStream = null;
        BufferedOutputStream bufOutStream = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new SystemException("对不起，找不到" + fileName + "文件!");
            }

            String displayFileName = fileName.substring(fileName.indexOf("_") + 1);
            // 下载文件名需要转换为ISO8859-1编码才能正常显示
            displayFileName = new String(displayFileName.getBytes("utf8"), "ISO8859-1") + "\"";

            if (isOnline) { // 在线打开方式
                URL url = new URL("file:///" + CommonUtils.getClusterResourcePath("") + dirPath + File.separator + fileName);
                response.setContentType(url.openConnection().getContentType());
                response.setHeader("Content-Disposition", "inline; filename=" + displayFileName);
            } else {// 纯下载方式
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition", "attachment; filename=" + displayFileName);
            }

            bufInStream = new BufferedInputStream(new FileInputStream(file));
            bufOutStream = new BufferedOutputStream(response.getOutputStream());
            byte[] buffer = new byte[10240];
            int len = 0;
            while ((len = bufInStream.read(buffer)) > 0) {
                bufOutStream.write(buffer, 0, len);
                bufOutStream.flush();
            }
        } catch (IOException e) {
            throw new SystemException("", e);
        } finally {
            try {
                if (bufInStream != null) {
                    bufInStream.close();
                }
                if (bufOutStream != null) {
                    bufOutStream.close();
                }
            } catch (IOException e) {
                logger.error("close file error", e);
            }
        }
    }

    /**
     * 替换文件
     *
     * @param dirPath     文件路径
     * @param newFileName 新文件名
     * @param oldFileName 旧文件名
     * @param is          新文件流
     * @return
     * @author rutine
     * @time Oct 6, 2012 2:32:27 PM
     */
    public static void replaceFile(String dirPath, String newFileName, String oldFileName, InputStream is) throws SystemException {
        // 文件上传目录
        // String absolutePath = request.getRealPath(File.separator) + dirPath;
        String absolutePath = getClusterResourcePath("") + dirPath;
        // 如果目录不存在则自动创建
        File dirFile = new File(absolutePath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        if ((oldFileName != null || !"".equals(oldFileName)) && is != null) {
            File[] files = dirFile.listFiles();
            for (File file : files) {
                if (file.getName().equals(oldFileName)) {
                    file.delete();
                    break;
                }
            }
            saveFile(dirPath, newFileName, is);
        }
    }

    /**
     * 重命名文件, 只能重命名同一路径的文件
     *
     * @param dirPath     路径
     * @param newFileName 新文件名
     * @param oldFileName 旧文件名
     */
    public static void renameFile(String dirPath, String newFileName, String oldFileName) {
        File newFile = new File(getClusterResourcePath("") + dirPath + File.separator + newFileName);
        File oldFile = new File(getClusterResourcePath("") + dirPath + File.separator + oldFileName);

        oldFile.renameTo(newFile);
    }

    /**
     * 删除目录下的文件
     *
     * @param dirPath  目录路径
     * @param fileName 文件名称
     * @author rutine
     * @time Oct 6, 2012 2:34:45 PM
     */
    public static void deleteFile(String dirPath, String fileName) {
        // 文件存放目录
        // String absolutePath = request.getRealPath(File.separator) + dirPath;
        String absolutePath = getClusterResourcePath("") + dirPath;

        File file = new File(absolutePath + File.separator + fileName);
        boolean success = file.delete();

        logger.info("delete file --> {} {}", file.getAbsolutePath(), success);
    }

    /**
     * 删除目录, 目录必须为空才能删除
     *
     * @param dirPath 目录
     * @author rutine
     * @time Oct 6, 2012 2:35:48 PM
     */
    public static void deleteDir(String dirPath) {
        // 文件上传目录
        // String absolutePath = request.getRealPath(File.separator) + dirPath;
        String absolutePath = getClusterResourcePath("") + dirPath;
        File dirFile = new File(absolutePath);
        if (dirFile.exists()) {
            dirFile.delete();
        }
    }


    public static void main(String[] args) {
        String fileSeparator = System.getProperties().getProperty("file.separator");
        CommonUtils.class.getResourceAsStream(fileSeparator);
        System.out.println(CommonUtils.getResourcePath());
        System.out.println(System.getProperty("mycuckoo.root"));
        File[] drive = File.listRoots();
        for (int i = 0; i < drive.length; i++) {
            System.out.println("\t" + drive[i]);
        }
    }
}
