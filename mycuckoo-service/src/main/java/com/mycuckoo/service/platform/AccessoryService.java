package com.mycuckoo.service.platform;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_ACCESSORY;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.common.utils.CommonUtils;
import com.mycuckoo.domain.platform.Accessory;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.platform.AccessoryMapper;

/**
 * 功能说明: 附件业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:21:32 AM
 * @version 3.0.0
 */
@Service
@Transactional(readOnly=true)
public class AccessoryService {
	
	@Value("${mycuckoo.affiche.picUrl}")
	private String afficheAttachmentPath;
	
	@Autowired
	private AccessoryMapper accessoryMapper;
	@Autowired
	private SystemOptLogService systemOptLogService;
	

	@Transactional(readOnly=false)
	public void deleteByIds(List<Long> accessoryIds) 
			throws ApplicationException {
		
		if(!accessoryIds.isEmpty()) {
			// 删除文件
			for(long id : accessoryIds) {
				Accessory accessory = this.get(id);
				CommonUtils.deleteFile(afficheAttachmentPath, accessory.getAccessoryName());
				
				accessoryMapper.delete(id);
			}
			
			
			String optContent = "删除附件ID：" + accessoryIds.toString();
			systemOptLogService.saveLog(LogLevelEnum.THIRD, OptNameEnum.DELETE, SYS_ACCESSORY, optContent, "");
		}
	}
	
	public List<Accessory> findByAfficheId(long afficheId) {
		return accessoryMapper.findByAfficheId(afficheId);
	}

	public Accessory get(long accessoryId) {
		return accessoryMapper.get(accessoryId);
	}

	@Transactional(readOnly=false)
	public void save(Accessory accessory) throws ApplicationException {
		accessoryMapper.save(accessory);
		
		String optContent = "附件业务表ID：" + accessory.getInfoId() + SPLIT + "附件名称:" + accessory.getAccessoryName();
		systemOptLogService.saveLog(LogLevelEnum.FIRST, OptNameEnum.SAVE, SYS_ACCESSORY, optContent, accessory.getAccessoryId() + "");
	}
}