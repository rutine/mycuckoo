package com.mycuckoo.service.impl.platform;

import static com.mycuckoo.common.constant.Common.DELETE_OPT;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_THIRD;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_ACCESSORY;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.common.utils.CommonUtils;
import com.mycuckoo.domain.platform.SysplAccessory;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.platform.SysplAccessoryRepository;
import com.mycuckoo.service.iface.platform.AccessoryService;
import com.mycuckoo.service.iface.platform.SystemOptLogService;

/**
 * 功能说明: 附件业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:21:32 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class AccessoryServiceImpl implements AccessoryService {
	
	@Value("${mycuckoo.affiche.picUrl}")
	private String afficheAttachmentPath;
	
	private SysplAccessoryRepository accessoryRepository;
	private SystemOptLogService sysOptLogService;

	@Transactional(readOnly=false)
	@Override
	public void deleteAccessorysByIds(List<Long> accessoryIds, HttpServletRequest request) 
			throws ApplicationException {
		
		if(!accessoryIds.isEmpty()) {
			// 删除文件
			for(long id : accessoryIds) {
				SysplAccessory accessory = this.getAccessoryByAccId(id);
				CommonUtils.deleteFile(afficheAttachmentPath, accessory.getAccessoryName());
			}
			
			accessoryRepository.deleteAccessorysByIds(accessoryIds.toArray(new Long[accessoryIds.size()]));
			String optContent = "删除附件ID：" + accessoryIds.toString();
			sysOptLogService.saveLog(LOG_LEVEL_THIRD, SYS_ACCESSORY, DELETE_OPT, optContent, "", request);
		}
	}

	@Override
	public List<SysplAccessory> findAccessorysByAfficheId(long afficheId) {
		return accessoryRepository.findAccessorysByAfficheId(afficheId);
	}

	@Override
	public SysplAccessory getAccessoryByAccId(long accessoryId) {
		return accessoryRepository.get(accessoryId);
	}

	@Transactional(readOnly=false)
	@Override
	public void saveAccessory(SysplAccessory accessory, HttpServletRequest request) 
			throws ApplicationException {

		String optContent = "附件业务表ID：" + accessory.getInfoId() + SPLIT
				+ "附件名称:" + accessory.getAccessoryName();
		accessoryRepository.save(accessory);
		sysOptLogService.saveLog(LOG_LEVEL_FIRST, SYS_ACCESSORY, SAVE_OPT,
				optContent, accessory.getAccessoryId() + "", request);
	}


	
	

	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setAccessoryRepository(SysplAccessoryRepository accessoryRepository) {
		this.accessoryRepository = accessoryRepository;
	}
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}
	
}
