package com.mycuckoo.core.util;

import com.mycuckoo.core.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/7/7 9:33
 */
public abstract class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static final String MYCUCKOO_CONFIG_LOCATION = "config";
    private static final int BUFFER_SIZE = 10240;

    private FileUtils() {}


    /**
     * 系统集群时获得存放文件路径 为系统配置附件所用
     *
     * @param filename 文件名称 可能为空
     * @return
     * @author rutine
     * @time Oct 6, 2012 10:30:53 AM
     */
    public static String getResourcePath(String filename) {
        String path = System.getProperty("mycuckoo.config.location");
        if (path == null) {
            path = Paths.get(System.getProperty("user.dir"), MYCUCKOO_CONFIG_LOCATION).toString();
        }
        logger.info("mycuckoo config path ----> {}", path);

        String separate = File.separator;
        String rootDir = separate;
        if ("\\".equals(separate)) {
            rootDir = path.substring(0, 2);
        }
        String filePath = Paths.get(rootDir + separate, filename).toString();
        File file = new File(filePath);
        String resourcePath = file.exists() ? filePath : Paths.get(path, filename).toString();

        logger.info("mycuckoo resource path --> {}", resourcePath);

        return resourcePath;
    }

    /**
     * 保存文件
     *
     * @param dirPath  文件路径
     * @param filename 文件名
     * @param in       文件输入流
     * @author rutine
     * @time Oct 6, 2012 11:05:02 AM
     */
    public static String save(String dirPath, String filename, InputStream in) throws SystemException {
        if (in == null) {
            return null;
        }

        Path directory = Paths.get(dirPath);
        Path targetFile = directory.resolve(filename);
        String filePath = targetFile.toString();
        logger.info("save file: {}", filePath);
        try {
            Files.createDirectories(directory);
            if (!Files.isDirectory(directory)) {
                throw new SystemException("文件目录无效: " + dirPath);
            }
        } catch (IOException e) {
            throw new SystemException("创建目录失败: " + dirPath, e);
        }

        try (InputStream input = in; OutputStream out = Files.newOutputStream(targetFile)) {
            copy(input, out);
        } catch (IOException e) {
            logger.error("save file error", e);

            throw new SystemException("", e);
        }

        return filePath;
    }

    /**
     * 下载文件
     *
     * @param filePath  文件路径
     * @param filename 文件名
     * @param isOnline 是否在线打开
     * @param response
     * @return
     * @throws SystemException
     * @author rutine
     * @time Oct 6, 2012 2:22:06 PM
     */
    public static void download(String filePath, String filename, boolean isOnline,
                                HttpServletResponse response) throws SystemException {

        Path file = Paths.get(filePath);
        if (!Files.exists(file)) {
            throw new SystemException("对不起，找不到[" + filePath + "]文件!");
        }

        try (BufferedInputStream bufIn = new BufferedInputStream(Files.newInputStream(file));
             BufferedOutputStream bufOut = new BufferedOutputStream(response.getOutputStream())) {
            String displayFilename = encodeDownloadFilename(resolveDisplayFilename(filename));

            if (isOnline) { // 在线打开方式
                String contentType = Files.probeContentType(file);
                response.setContentType(contentType == null ? "application/octet-stream" : contentType);
                response.setHeader("Content-Disposition", "inline; filename=" + displayFilename);
            } else {// 纯下载方式
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition", "attachment; filename=" + displayFilename);
            }

            copy(bufIn, bufOut);
            bufOut.flush();
        } catch (IOException e) {
            throw new SystemException("", e);
        }
    }

    /**
     * 替换文件
     *
     * @param dirPath     文件路径
     * @param newFilename 新文件名
     * @param oldFilename 旧文件名
     * @param in          新文件流
     * @return
     * @author rutine
     * @time Oct 6, 2012 2:32:27 PM
     */
    public static void replace(String dirPath, String newFilename, String oldFilename, InputStream in) throws SystemException {
        if (in == null) {
            return;
        }

        Path directory = Paths.get(dirPath);
        try {
            Files.createDirectories(directory);
            if (oldFilename != null && !oldFilename.isEmpty()) {
                Files.deleteIfExists(directory.resolve(oldFilename));
            }
        } catch (IOException e) {
            throw new SystemException("处理文件目录失败: " + dirPath, e);
        }

        save(dirPath, newFilename, in);
    }

    /**
     * 重命名文件, 只能重命名同一路径的文件
     *
     * @param dirPath     路径
     * @param newFilename 新文件名
     * @param oldFilename 旧文件名
     */
    public static void rename(String dirPath, String newFilename, String oldFilename) {
        Path directory = Paths.get(dirPath);
        Path oldFile = directory.resolve(oldFilename);
        Path newFile = directory.resolve(newFilename);
        try {
            Files.move(oldFile, newFile);
        } catch (IOException e) {
            logger.error("rename file error: {} -> {}", oldFile, newFile, e);
        }
    }

    /**
     * 删除目录下的文件
     *
     * @param filePath  目录路径
     * @param filename 文件名称
     * @author rutine
     * @time Oct 6, 2012 2:34:45 PM
     */
    public static void delete(String filePath, String filename) {
        if (filename == null || filename.isEmpty()) {
            return;
        }

        Path file = Paths.get(filePath);
        try {
            boolean success = Files.deleteIfExists(file);
            logger.info("delete file --> {} {}", file.toAbsolutePath(), success);
        } catch (IOException e) {
            logger.error("delete file error: {}", file.toAbsolutePath(), e);
        }
    }

    /**
     * 删除目录, 目录必须为空才能删除
     *
     * @param dirPath 目录
     * @author rutine
     * @time Oct 6, 2012 2:35:48 PM
     */
    public static void deleteDir(String dirPath) {
        Path dir = Paths.get(dirPath);
        try {
            if (Files.isDirectory(dir)) {
                Files.deleteIfExists(dir);
            }
        } catch (IOException e) {
            logger.error("delete directory error: {}", dir.toAbsolutePath(), e);
        }
    }

    private static void copy(InputStream input, OutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int len;
        while ((len = input.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }

    private static String resolveDisplayFilename(String filename) {
        int index = filename.indexOf("_");
        return index >= 0 && index < filename.length() - 1 ? filename.substring(index + 1) : filename;
    }

    private static String encodeDownloadFilename(String filename) {
        return new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
    }
}
