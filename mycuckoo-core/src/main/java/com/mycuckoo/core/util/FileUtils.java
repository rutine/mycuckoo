package com.mycuckoo.core.util;

import com.mycuckoo.core.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/7/7 9:33
 */
public abstract class FileUtils {
    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public final static String MYCUCKOO_CONFIG_LOCATION = "config";


    public static void main(String[] args) {
        String fileSeparator = System.getProperties().getProperty("file.separator");
        FileUtils.class.getResourceAsStream(fileSeparator);
        System.out.println(getResourcePath());
        System.out.println(System.getProperty("mycuckoo.root"));
        File[] drive = File.listRoots();
        for (int i = 0; i < drive.length; i++) {
            System.out.println("\t" + drive[i]);
        }
    }

    private FileUtils() {}

    /**
     * 获得web根路径
     *
     * @return
     * @author rutine
     * @time Oct 6, 2012 10:29:18 AM
     */
    public static String getResourcePath() {
        String confPath = System.getProperty("mycuckoo.config.location");
        if (confPath == null) {
            String separate = File.separator;
            confPath = System.getProperty("user.dir") + separate + MYCUCKOO_CONFIG_LOCATION ;
        }

        logger.info("mycuckoo config path ----> {}", confPath);

        return confPath == null ? "" : confPath;
    }

    /**
     * 系统集群时获得存放文件路径 为系统配置附件所用
     *
     * @param filename 文件名称 可能为空
     * @return
     * @author rutine
     * @time Oct 6, 2012 10:30:53 AM
     */
    public static String getClusterResourcePath(String filename) {
        String separate = File.separator;
        String path = getResourcePath();
        String rootDir = separate;// 默认为分隔符 linux unix
        if ("\\".equals(separate)) {// windows系统
            rootDir = path.substring(0, 2);
        }
        String filePath = rootDir + separate + filename;
        File file = new File(filePath);
        String resourcePath = (file.exists() ? filePath : (path + separate + filename));

        logger.info("mycuckoo cluster resource path --> {}", resourcePath);

        return resourcePath;
    }

    /**
     * 保存文件
     *
     * @param dirPath  文件路径
     * @param filename 文件名
     * @param is       文件输入流
     * @author rutine
     * @time Oct 6, 2012 11:05:02 AM
     */
    public static String saveFile(String dirPath, String filename, InputStream is) throws SystemException {
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
        String filePath = absolutePath + File.separator + filename;
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
     * @param filename 文件名
     * @param isOnline 是否在线打开
     * @param response
     * @return
     * @throws SystemException
     * @author rutine
     * @time Oct 6, 2012 2:22:06 PM
     */
    public static void downloadFile(String dirPath, String filename,
                                    boolean isOnline, HttpServletResponse response) throws SystemException {

        // 文件下载
        // String filePath = request.getRealPath(File.separator) + dirPath + File.separator + filename;
        String filePath = getClusterResourcePath("") + dirPath + File.separator + filename;
        BufferedInputStream bufInStream = null;
        BufferedOutputStream bufOutStream = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new SystemException("对不起，找不到" + filename + "文件!");
            }

            String displayFilename = filename.substring(filename.indexOf("_") + 1);
            // 下载文件名需要转换为ISO8859-1编码才能正常显示
            displayFilename = new String(displayFilename.getBytes("utf8"), "ISO8859-1") + "\"";

            if (isOnline) { // 在线打开方式
                URL url = new URL("file:///" + getClusterResourcePath("") + dirPath + File.separator + filename);
                response.setContentType(url.openConnection().getContentType());
                response.setHeader("Content-Disposition", "inline; filename=" + displayFilename);
            } else {// 纯下载方式
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition", "attachment; filename=" + displayFilename);
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
     * @param newFilename 新文件名
     * @param oldFilename 旧文件名
     * @param is          新文件流
     * @return
     * @author rutine
     * @time Oct 6, 2012 2:32:27 PM
     */
    public static void replaceFile(String dirPath, String newFilename, String oldFilename, InputStream is) throws SystemException {
        // 文件上传目录
        // String absolutePath = request.getRealPath(File.separator) + dirPath;
        String absolutePath = getClusterResourcePath("") + dirPath;
        // 如果目录不存在则自动创建
        File dirFile = new File(absolutePath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        if ((oldFilename != null || !"".equals(oldFilename)) && is != null) {
            File[] files = dirFile.listFiles();
            for (File file : files) {
                if (file.getName().equals(oldFilename)) {
                    file.delete();
                    break;
                }
            }
            saveFile(dirPath, newFilename, is);
        }
    }

    /**
     * 重命名文件, 只能重命名同一路径的文件
     *
     * @param dirPath     路径
     * @param newFilename 新文件名
     * @param oldFilename 旧文件名
     */
    public static void renameFile(String dirPath, String newFilename, String oldFilename) {
        File newFile = new File(getClusterResourcePath("") + dirPath + File.separator + newFilename);
        File oldFile = new File(getClusterResourcePath("") + dirPath + File.separator + oldFilename);

        oldFile.renameTo(newFile);
    }

    /**
     * 删除目录下的文件
     *
     * @param dirPath  目录路径
     * @param filename 文件名称
     * @author rutine
     * @time Oct 6, 2012 2:34:45 PM
     */
    public static void deleteFile(String dirPath, String filename) {
        // 文件存放目录
        // String absolutePath = request.getRealPath(File.separator) + dirPath;
        String absolutePath = getClusterResourcePath("") + dirPath;

        File file = new File(absolutePath + File.separator + filename);
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
}
