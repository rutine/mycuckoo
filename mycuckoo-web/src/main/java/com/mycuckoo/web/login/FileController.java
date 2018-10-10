package com.mycuckoo.web.login;

import com.mycuckoo.common.constant.BusinessType;
import com.mycuckoo.common.utils.CommonUtils;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.web.vo.AjaxResponse;
import com.mycuckoo.web.vo.FileMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @Value("${mycuckoo.basePath}")
    private String basePath;

    /**
     * 功能说明 : 请求文件上传
     * <pre>
     * 	<b>返回数据结构:</b>
     *    {"name" : "app_engine-85x77.png", "size" : "8Kb", "type" : "image/png"}
     * </pre>
     *
     * @param business 业务名称
     * @param file     文件上传请求
     * @return FileMeta as json format
     * @author rutine
     * @time Sept 30, 2017 13:57:41 PM
     */
    @PostMapping
    public AjaxResponse<FileMeta> postFile(@RequestParam BusinessType business,
                                           @RequestParam MultipartFile file) {
        FileMeta fileMeta = new FileMeta();
        try {
            String originalFileName = file.getOriginalFilename();
            int index = originalFileName.lastIndexOf('.');

            StringBuilder nameBuilder = new StringBuilder();
            nameBuilder.append(originalFileName.substring(0, index)).append("_");
            for (int i = 0; i < 6; i++) {
                nameBuilder.append(CommonUtils.getRandomChar());
            }
            nameBuilder.append(".").append(originalFileName.substring(index + 1));
            String fileName = nameBuilder.toString();

            String dirPath = StringUtils.cleanPath(basePath) + business.name();
            String path = CommonUtils.saveFile(dirPath, fileName, file.getInputStream());

            logger.debug("filename : {}, size : {}", fileName, file.getSize());

            fileMeta.setUrl(dirPath + "/" + fileName);
            fileMeta.setName(fileName);
            fileMeta.setSize(file.getSize());
            fileMeta.setType(file.getContentType());
            try {
                fileMeta.setBytes(file.getBytes());
            } catch (IOException e) {
                logger.error("获取附件大小出错!", e);
            }
        } catch (SystemException e) {
            logger.error("获取上传文件流失败!", e);
        } catch (IOException e) {
            logger.error("获取上传文件流失败!", e);
        }

        return AjaxResponse.create(fileMeta);
    }

    /**
     * 功能说明 : 下载文件
     *
     * @param business 业务名称
     * @param fileName 文件名
     * @param isOnline 是否在线打开
     * @param response 响应
     * @author rutine
     * @time Jun 30, 2013 6:22:03 PM
     */
    @GetMapping(value = "/download")
    public void download(
            @RequestParam BusinessType business,
            @RequestParam String fileName,
            @RequestParam(required = false, defaultValue = "N") String isOnline,
            HttpServletResponse response) {

        response.reset(); // 重置

        try {
            String dirPath = StringUtils.cleanPath(basePath) + business.name();
            CommonUtils.downloadFile(dirPath, fileName, "Y".equals(isOnline), response);
        } catch (SystemException e) {
            logger.error("下载文件失败: {}/{}", business.name(), fileName, e);
        }
    }

    /**
     * 功能说明 : 根据文件名删除文件
     *
     * @param business 业务名称
     * @param fileName 文件名
     * @return
     * @author rutine
     * @time Sept 30, 2017 14:37:41 PM
     */
    @DeleteMapping(value = "/delete")
    public AjaxResponse<String> delete(@RequestParam BusinessType business,
                                       @RequestParam String fileName) {
        String dirPath = StringUtils.cleanPath(basePath) + business.name();
        CommonUtils.deleteFile(dirPath, fileName);

        return AjaxResponse.create("附件删除成功");
    }
}
