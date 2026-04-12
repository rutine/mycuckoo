package com.mycuckoo.web.login;

import com.mycuckoo.constant.enums.AttachmentType;
import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.FileMeta;
import com.mycuckoo.core.exception.SystemException;
import com.mycuckoo.core.util.FileUtils;
import com.mycuckoo.core.util.IdGenerator;
import com.mycuckoo.core.util.StrUtils;
import com.mycuckoo.domain.platform.CloudFile;
import com.mycuckoo.service.platform.CloudFileService;
import com.mycuckoo.web.config.WebProperties;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 功能说明: 文件服务Controller
 *
 * @author rutine
 * @version 3.0.0
 * @time Sept 30, 2017 13:57:41 PM
 */
@RestController
@RequestMapping("/file")
public class FileController {
    private static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private WebProperties properties;

    @Autowired
    private CloudFileService cloudFileService;

    /**
     * 功能说明 : 请求文件上传
     * <pre>
     * 	<b>返回数据结构:</b>
     *    {"name" : "app_engine-85x77.png", "size" : "8Kb", "type" : "image/png"}
     * </pre>
     *
     * @param type 业务名称
     * @param file     文件上传请求
     * @return FileMeta as json format
     * @author rutine
     * @time Sept 30, 2017 13:57:41 PM
     */
    @PostMapping
    public AjaxResponse<FileMeta> upload(@RequestParam AttachmentType type,
                                         @RequestParam MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return AjaxResponse.create(1, "上传文件不能为空");
        }
        String originFilename = file.getOriginalFilename();
        if (StrUtils.isEmpty(originFilename)) {
            return AjaxResponse.create(1, "上传文件名不能为空");
        }
        String safeFilename = sanitizeFilename(originFilename);
        if (StrUtils.isEmpty(safeFilename)) {
            return AjaxResponse.create(1, "上传文件名不合法");
        }

        String id = IdGenerator.uuid();
        String month = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        FileMeta fileMeta = new FileMeta();
        try {
            String extension = StringUtils.getFilenameExtension(safeFilename);
            StringBuilder nameBuilder = new StringBuilder()
                    .append(id)
                    .append(StrUtils.isEmpty(extension) ? "" : "." + extension);
            String fileName = nameBuilder.toString();

            String dirPath = buildDirPath(type) + File.separator + month;
            FileUtils.save(dirPath, fileName, file.getInputStream());

            logger.debug("filename : {}, size : {}", fileName, file.getSize());

            String uri = "/download/" + type.name + "/" + month + "/" + fileName;
            fileMeta.setId(id);
            fileMeta.setUrl(properties.getHost() + uri);
            fileMeta.setName(safeFilename);
            fileMeta.setSize(file.getSize());
            fileMeta.setType(file.getContentType());
            try {
                fileMeta.setBytes(file.getBytes());
            } catch (IOException e) {
                logger.error("获取附件大小出错!", e);
            }

            CloudFile entity = new CloudFile();
            entity.setId(id);
            entity.setPath("/" + type.name + "/" + month + "/" + fileName);
            entity.setName(safeFilename);
            entity.setSize(file.getSize());
            entity.setType(extension);
            cloudFileService.save(entity);
        } catch (IllegalArgumentException e) {
            return AjaxResponse.create(1, e.getMessage());
        } catch (SystemException | IOException e) {
            logger.error("上传文件失败!", e);
            return AjaxResponse.create(1, "上传文件失败");
        }

        return AjaxResponse.create(fileMeta);
    }

    /**
     * 功能说明 : 下载文件
     *
     * @param fileId 文件ID
     * @param isOnline 是否在线打开
     * @param response 响应
     * @author rutine
     * @time Jun 30, 2013 6:22:03 PM
     */
    @GetMapping
    public void download(
            @RequestParam String fileId,
            @RequestParam(required = false, defaultValue = "N") String isOnline,
            HttpServletResponse response) {

        response.reset(); // 重置

        try {
            CloudFile file = cloudFileService.get(fileId);
            String filePath = StringUtils.cleanPath(properties.getUploadPath()) + file.getPath();
            FileUtils.download(filePath, file.getName(), "Y".equals(isOnline), response);
        } catch (IllegalArgumentException | SystemException e) {
            logger.error("下载文件失败: {}", fileId, e);
        }
    }

    /**
     * 功能说明 : 根据文件名删除文件
     *
     * @param fileId 文件ID
     * @return
     * @author rutine
     * @time Sept 30, 2017 14:37:41 PM
     */
    @DeleteMapping
    public AjaxResponse<String> delete(@RequestParam String fileId) {
        try {
            CloudFile file = cloudFileService.get(fileId);
            String filePath = StringUtils.cleanPath(properties.getUploadPath()) + file.getPath();
            FileUtils.delete(filePath, file.getName());
        } catch (IllegalArgumentException e) {
            return AjaxResponse.create(1, e.getMessage());
        }

        return AjaxResponse.create("附件删除成功");
    }

    private String buildDirPath(AttachmentType type) {
        return StringUtils.cleanPath(properties.getUploadPath()) + "/"+ type.name;
    }

    private String sanitizeFilename(String filename) {
        String cleanFilename = StringUtils.cleanPath(filename);
        String safeFilename = new File(cleanFilename).getName();
        if (StrUtils.isEmpty(safeFilename) || safeFilename.contains("..")) {
            throw new IllegalArgumentException("文件名不合法");
        }

        return safeFilename;
    }
}
