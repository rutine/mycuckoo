package com.mycuckoo.web.platform;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.collect.Maps;
import com.mycuckoo.common.utils.CommonUtils;
import com.mycuckoo.domain.platform.SysplAccessory;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.domain.vo.FileMeta;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.platform.AccessoryService;

/**
 * 功能说明: 附件Controller
 * 
 * @author rutine
 * @time Oct 16, 2014 4:47:41 PM
 * @version 2.0.0
 */
@Controller
@RequestMapping("/platform/accessoryMgr")
public class AccessoryController {
	private static Logger logger = LoggerFactory.getLogger(AccessoryController.class);

	@Value("${mycuckoo.documentUrl}")
	private String documentPath;
	
	@Autowired
	private AccessoryService accessoryService;

	/**
	 * 功能说明 : 根据附件ID删除附件
	 * 
	 * @param id
	 * @param request
	 * @return
	 * @author rutine
	 * @time Jul 1, 2013 8:58:27 PM
	 */
	@RequestMapping(value = "/delete/{fileNameOrId}", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult delete(
			@PathVariable String fileNameOrId,
			HttpServletRequest request) {
		
		
		AjaxResult ajaxResult = new AjaxResult(true, "附件删除成功");
		try {
			long id = NumberUtils.toLong(fileNameOrId, -1L);
			if(id == -1L) {
				CommonUtils.deleteFile(documentPath, fileNameOrId);
			} else {
				accessoryService.deleteAccessorysByIds(Arrays.asList(id), request);
			}
		} catch (ApplicationException e) {
			logger.error("1. 附件删除失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("附件删除失败!");
		} catch (SystemException e) {
			logger.error("2. 附件删除失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("附件删除失败!");
		}

		return ajaxResult;
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
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, LinkedList<FileMeta>> postUpload(
			@RequestParam(required = false) String afficheId,
			MultipartHttpServletRequest request) {
		
		long afficheIdLong = NumberUtils.toLong(afficheId, -1L);
		LinkedList<FileMeta> files = new LinkedList<FileMeta>();
		Iterator<String> it = request.getFileNames();
		while (it.hasNext()) {
			MultipartFile mpf = request.getFile(it.next());
			String originalFileName = mpf.getOriginalFilename();
			if (mpf != null && !mpf.isEmpty()) {
				try {
					StringBuilder fileNameBuilder = new StringBuilder();
					for (int i = 0; i < 32; i++) {
						fileNameBuilder.append(CommonUtils.getRandomChar());
					}
					int index = originalFileName.lastIndexOf('.');
					fileNameBuilder.append("_");
					fileNameBuilder.append(originalFileName.substring(0, index));
					fileNameBuilder.append(afficheIdLong > 0 ? "." : "!");
					fileNameBuilder.append(originalFileName.substring(index + 1));
					String fileName = fileNameBuilder.toString();
					
					CommonUtils.saveFile(documentPath, fileName, mpf.getInputStream());
					
					// 更新公告时执行如下代码
					if(afficheIdLong > 0) {
						SysplAccessory sysplAccessory = new SysplAccessory();
						sysplAccessory.setInfoId(afficheIdLong);
						sysplAccessory.setAccessoryName(fileName);
						accessoryService.saveAccessory(sysplAccessory, request);
						fileName = sysplAccessory.getAccessoryId() + "";
					}
					
					logger.debug("filename : {}, size : {}", fileName, files.size());

					if (files.size() >= 5) {
						files.pop();
					}

					FileMeta fileMeta = new FileMeta();
					fileMeta.setUrl("/platform/accessoryMgr/download/" + fileName);
					fileMeta.setName(originalFileName);
					fileMeta.setSize(mpf.getSize());
					fileMeta.setType(mpf.getContentType());
					fileMeta.setDelete_type("get");
					fileMeta.setDelete_url("/platform/accessoryMgr/delete/" + fileName);
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
	 * @param accessoryId 附件<code>ID</code>
	 * @param isOnline 是否在线打开
	 * @param response 响应
	 * @author rutine
	 * @time Jun 30, 2013 6:22:03 PM
	 */
	@RequestMapping(value = "/download/{fileNameOrId}", method = RequestMethod.GET)
	public void download(
			@PathVariable String fileNameOrId,
			@RequestParam(required = false, defaultValue = "N") String isOnline,
			HttpServletResponse response) {
		
		response.reset(); // 重置
		
		try {
			long id = NumberUtils.toLong(fileNameOrId, -1L);
			if(id == -1L) {
				CommonUtils.downloadFile(documentPath, fileNameOrId, "Y".equals(isOnline), response);
			} else {
				SysplAccessory accessory = accessoryService.getAccessoryByAccId(id);
				CommonUtils.downloadFile(documentPath, accessory.getAccessoryName(), "Y".equals(isOnline), response);
			}
		} catch (ApplicationException e) {
			logger.error("下载附件出错!", e);
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
