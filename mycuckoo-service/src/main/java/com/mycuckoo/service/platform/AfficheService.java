package com.mycuckoo.service.platform;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_AFFICHE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.common.utils.CommonUtils;
import com.mycuckoo.domain.platform.Accessory;
import com.mycuckoo.domain.platform.Affiche;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.AfficheMapper;

/**
 * 功能说明: 公告业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:23:52 AM
 * @version 3.0.0
 */
@Service
@Transactional(readOnly=true)
public class AfficheService {
	@Value("${mycuckoo.documentUrl}")
	private String documentPath;
	
	@Autowired
	private AfficheMapper afficheMapper;
	@Autowired
	private AccessoryService accessoryService;
	@Autowired
	private SystemOptLogService sysOptLogService;
	
	
	@Transactional(readOnly=false)
	public void deleteByIds(List<Long> afficheIdList) throws ApplicationException {
		
		if(afficheIdList != null && afficheIdList.size() > 0) {
			for(Long afficheId : afficheIdList) {
				// 根据公告ID查询附件列表
				List<Accessory> accessoryList = accessoryService.findByAfficheId(afficheId);
				List<Long> accessoryIdList = new ArrayList<Long>();
				for (Accessory accessory : accessoryList) {
					accessoryIdList.add(accessory.getAccessoryId()); // 附件ID
					CommonUtils.deleteFile(documentPath, accessory.getAccessoryName()); // 删除附件
				}
				accessoryService.deleteByIds(accessoryIdList); // 删除附件数据库记录
				afficheMapper.delete(afficheId);
			}
			
			String optContent = "删除的公告ID：" + afficheIdList;
			sysOptLogService.saveLog(LogLevelEnum.THIRD, OptNameEnum.DELETE, SYS_AFFICHE, optContent, "");
		}
	}

	public Affiche get(Long afficheId) {
		List<Accessory> accessoryList = accessoryService.findByAfficheId(afficheId);
		Affiche affiche = afficheMapper.get(afficheId);
		affiche.setAccessoryList(accessoryList);
		
		return affiche;
	}

	public Page<Affiche> findByPage(Map<String, Object> params, Pageable page) {
		return afficheMapper.findByPage(params, page);
	}

	public List<Affiche> findBeforeValidate() {
		return afficheMapper.findBeforeValidate(new Date());
	}

	@Transactional(readOnly=false)
	public void updateAffiche(Affiche affiche) throws ApplicationException {
		afficheMapper.update(affiche);

		StringBuilder optContent = new StringBuilder();
		optContent.append("修改公告ID：").append(affiche.getAfficheId()).append(SPLIT);
		optContent.append("修改公告标题：").append(affiche.getAfficheTitle()).append(SPLIT);
		optContent.append("有效期：").append(affiche.getAfficheInvalidate()).append(SPLIT);
		optContent.append("是否发布：").append(affiche.getAffichePulish()).append(SPLIT);
		optContent.append("修改公告ID：").append(affiche.getAfficheId()).append(SPLIT);
		sysOptLogService.saveLog(LogLevelEnum.SECOND, OptNameEnum.MODIFY, SYS_AFFICHE, 
				optContent.toString(), affiche.getAfficheId() + "");
	}

	@Transactional(readOnly=false)
	public void saveAffiche(Affiche affiche, List<String> accessoryNameList) throws ApplicationException {
		
		// 1. 保存公告
		afficheMapper.save(affiche);
		
		// 2. 保存附件信息
		if(accessoryNameList != null) {
			for(String newFileName : accessoryNameList) {
				int index = newFileName.lastIndexOf('.');
				String oldFileName = newFileName.substring(0, index) + "!" + newFileName.substring(index + 1);
				CommonUtils.renameFile(documentPath, newFileName, oldFileName);
				
				Accessory accessory = new Accessory();
				accessory.setInfoId(affiche.getAfficheId());
				accessory.setAccessoryName(newFileName);
				accessoryService.save(accessory);
			}
		}
		
		// 3. 保存操作日志
		StringBuilder optContent = new StringBuilder();
		optContent.append("保存公告标题：").append(affiche.getAfficheTitle()).append(SPLIT);
		optContent.append("有效期限:").append(affiche.getAfficheInvalidate()).append(SPLIT);
		sysOptLogService.saveLog(LogLevelEnum.FIRST, OptNameEnum.SAVE, SYS_AFFICHE, 
				optContent.toString(), affiche.getAfficheId() + "");
	}
}
