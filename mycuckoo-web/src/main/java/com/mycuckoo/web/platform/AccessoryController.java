package com.mycuckoo.web.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.common.utils.CommonUtils;
import com.mycuckoo.domain.platform.Accessory;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.service.platform.AccessoryService;
import com.mycuckoo.web.vo.AjaxResponse;
import com.mycuckoo.web.vo.FileMeta;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * 功能说明: 附件Controller
 * 
 * @author rutine
 * @time Oct 16, 2014 4:47:41 PM
 * @version 3.0.0
 */
@RestController
@RequestMapping("/platform/accessory/mgr")
public class AccessoryController {
	private static Logger logger = LoggerFactory.getLogger(AccessoryController.class);

	@Value("${mycuckoo.affiche.documentUrl}")
	private String documentPath;
	
	@Autowired
	private AccessoryService accessoryService;


	/**
	 * 功能说明 : 根据附件ID删除附件
	 * 
	 * @param fileNameOrId
	 * @return
	 * @author rutine
	 * @time Jul 1, 2013 8:58:27 PM
	 */
	@DeleteMapping(value = "/delete/{fileNameOrId}")
	public AjaxResponse<String> delete(@PathVariable String fileNameOrId) {
		long id = NumberUtils.toLong(fileNameOrId, -1L);
		if(id == -1L) {
			CommonUtils.deleteFile(documentPath, fileNameOrId);
		} else {
			accessoryService.deleteByIds(Arrays.asList(id));
		}

		return AjaxResponse.create("附件删除成功");
	}

	/**
	 * 功能说明 : 请求文件上传
	 * <pre>
	 * 	<b>返回数据结构:</b> 
	 * 	{"files" : [{"name" : "app_engine-85x77.png", "size" : "8Kb", "type" : "image/png"}, ...]}
	 * </pre>
	 * 
	 * @param request 文件上传请求
	 * @return LinkedList<FileMeta> as json format
	 * @author rutine
	 * @time Jun 30, 2013 6:21:23 PM
	 */
	@PostMapping(value = "/upload")
	public Map<String, LinkedList<FileMeta>> postUpload(MultipartHttpServletRequest request) {
		LinkedList<FileMeta> files = new LinkedList<FileMeta>();
		Iterator<String> it = request.getFileNames();
		while (it.hasNext()) {
			MultipartFile mpf = request.getFile(it.next());
			String originalFileName = mpf.getOriginalFilename();
			if (mpf != null && !mpf.isEmpty()) {
				try {
					int index = originalFileName.lastIndexOf('.');

					StringBuilder fileNameBuilder = new StringBuilder();
					fileNameBuilder.append(originalFileName.substring(0, index));
					fileNameBuilder.append("_");
					for (int i = 0; i < 6; i++) {
						fileNameBuilder.append(CommonUtils.getRandomChar());
					}
					fileNameBuilder.append("!");
					fileNameBuilder.append(originalFileName.substring(index + 1));
					String fileName = fileNameBuilder.toString();
					
					CommonUtils.saveFile(documentPath, fileName, mpf.getInputStream());
					
					logger.debug("filename : {}, size : {}", fileName, files.size());

					if (files.size() >= 5) {
						files.pop();
					}

					FileMeta fileMeta = new FileMeta();
					fileMeta.setUrl(fileName);
//					fileMeta.setUrl("/platform/accessory/mgr/download/" + fileName);
					fileMeta.setName(originalFileName);
					fileMeta.setSize(mpf.getSize());
					fileMeta.setType(mpf.getContentType());
					fileMeta.setDelete_type("get");
					fileMeta.setDelete_url("/platform/accessory/mgr/delete/" + fileName);
					try {
						fileMeta.setBytes(mpf.getBytes());
					} catch (IOException e) {
						logger.error("获取附件大小出错!", e);
					}
					files.add(fileMeta);
				} catch(ApplicationException e) {
					logger.error("保存附件信息失败!", e);
				} catch (IOException e) {
					logger.error("获取上传文件流失败!", e);
				}
			}
		}
		Map<String, LinkedList<FileMeta>> result = Maps.newHashMap();
		result.put("files", files);
		
		return result;
	}

	/**
	 * 功能说明 : 下载文件
	 * 
	 * @param fileNameOrId 附件<code>ID</code>
	 * @param isOnline 是否在线打开
	 * @param response 响应
	 * @author rutine
	 * @time Jun 30, 2013 6:22:03 PM
	 */
	@GetMapping(value = "/download/{fileNameOrId}")
	public void download(
			@PathVariable String fileNameOrId,
			@RequestParam(required = false, defaultValue = "N") String isOnline,
			HttpServletResponse response) {
		
		response.reset(); // 重置

		long id = NumberUtils.toLong(fileNameOrId, -1L);
		if(id == -1L) {
			CommonUtils.downloadFile(documentPath, fileNameOrId, "Y".equals(isOnline), response);
		} else {
			Accessory accessory = accessoryService.get(id);
			CommonUtils.downloadFile(documentPath, accessory.getAccessoryName(), "Y".equals(isOnline), response);
		}

//		FileMeta file = files.get(Integer.parseInt(accessoryId));
//		try {
//			response.setContentType(file.getType());
//			response.setHeader("Content-disposition", "attachment; filename=\"" + file.getName() + "\"");
//			FileCopyUtils.copy(file.getBytes(), response.getOutputStream());
//		} catch (IOException e) {
//			logger.error("下载附件出错!", e);
//		}
	}
	
}
