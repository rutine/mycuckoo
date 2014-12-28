package com.mycuckoo.service.impl.platform;

import static com.mycuckoo.common.constant.Common.DELETE_OPT;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_SECOND;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_THIRD;
import static com.mycuckoo.common.constant.Common.MODIFY_OPT;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_AFFICHE;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.common.utils.CommonUtils;
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplAccessory;
import com.mycuckoo.domain.platform.SysplAffiche;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.platform.SysplAfficheRepository;
import com.mycuckoo.service.iface.platform.AccessoryService;
import com.mycuckoo.service.iface.platform.AfficheService;
import com.mycuckoo.service.iface.platform.SystemOptLogService;

/**
 * 功能说明: 公告业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:23:52 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class AfficheServiceImpl implements AfficheService {
	@Value("${mycuckoo.documentUrl}")
	private String documentPath;
	
	private SysplAfficheRepository afficheRepository;
	private AccessoryService accessoryService;
	private SystemOptLogService sysOptLogService;
	
	
	@Transactional(readOnly=false)
	@Override
	public void deleteAffichesByIds(List<Long> afficheIdList, HttpServletRequest request) 
			throws ApplicationException {
		
		if(afficheIdList != null && afficheIdList.size() > 0) {
			for(Long afficheId : afficheIdList) {
				// 根据公告ID查询附件列表
				List<SysplAccessory> sysplAccessoryList = accessoryService.findAccessorysByAfficheId(afficheId);
				List<Long> accessoryIdList = new ArrayList<Long>();
				for (SysplAccessory sysplAccessory : sysplAccessoryList) {
					accessoryIdList.add(sysplAccessory.getAccessoryId()); // 附件ID
					CommonUtils.deleteFile(documentPath, sysplAccessory.getAccessoryName()); // 删除附件
				}
				accessoryService.deleteAccessorysByIds(accessoryIdList, request); // 删除附件数据库记录
			}
			String optContent = "删除的公告ID：" + afficheIdList;
			afficheRepository.deleteAffichesByIds(afficheIdList.toArray(new Long[0]));
			sysOptLogService.saveLog(LOG_LEVEL_THIRD, DELETE_OPT, SYS_AFFICHE, optContent, "", request);
		}
	}

	@Override
	public SysplAffiche getAfficheByAfficheId(Long afficheId) {
		List<SysplAccessory> sysplAccessoryList = accessoryService.findAccessorysByAfficheId(afficheId);
		SysplAffiche sysplAffiche = afficheRepository.get(afficheId);
		sysplAffiche.setAccessoryList(sysplAccessoryList);
		
		return sysplAffiche;
	}

	@Override
	public Page<SysplAffiche> findAffichesByCon(String afficheTitle, Short affichePulish, Pageable page) {
		return afficheRepository.findAffichesByCon(afficheTitle, affichePulish, page);
	}

	@Override
	public List<SysplAffiche> findAffichesBeforeValidate() {
		return afficheRepository.findAffichesBeforeValidate();
	}

	@Transactional(readOnly=false)
	@Override
	public void updateAffiche(SysplAffiche affiche,  HttpServletRequest request) 
			throws ApplicationException {
		
		afficheRepository.update(affiche);
		String optContent = "修改公告ID：" + affiche.getAfficheId() + SPLIT
			+ "修改公告标题：" + affiche.getAfficheTitle() + SPLIT
			+ "有效期：" + affiche.getAfficheInvalidate() + SPLIT
			+ "是否发布：" + affiche.getAffichePulish() + SPLIT;
		sysOptLogService.saveLog(LOG_LEVEL_SECOND, SYS_AFFICHE, MODIFY_OPT, optContent, 
				affiche.getAfficheId() + "", request);
	}

	@Transactional(readOnly=false)
	@Override
	public void saveAffiche(SysplAffiche affiche, List<String> accessoryNameList, HttpServletRequest request) 
			throws ApplicationException {
		
		// 1. 保存公告
		afficheRepository.save(affiche);
		
		// 2. 保存附件信息
		if(accessoryNameList != null) {
			for(String newFileName : accessoryNameList) {
				int index = newFileName.lastIndexOf('.');
				String oldFileName = newFileName.substring(0, index) + "!" + newFileName.substring(index + 1);
				CommonUtils.renameFile(documentPath, newFileName, oldFileName);
				
				SysplAccessory sysplAccessory = new SysplAccessory();
				sysplAccessory.setInfoId(affiche.getAfficheId());
				sysplAccessory.setAccessoryName(newFileName);
				accessoryService.saveAccessory(sysplAccessory, request);
			}
		}
		
		// 3. 保存操作日志
		String optContent = "保存公告标题：" + affiche.getAfficheTitle() + SPLIT
				+ "有效期限:" + affiche.getAfficheInvalidate().toString();
		sysOptLogService.saveLog(LOG_LEVEL_FIRST, SYS_AFFICHE, SAVE_OPT, optContent.toString(), 
				affiche.getAfficheId() + "", request);
	}


	
	

	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setAfficheRepository(SysplAfficheRepository afficheRepository) {
		this.afficheRepository = afficheRepository;
	}
	@Autowired
	public void setAccessoryService(AccessoryService accessoryService) {
		this.accessoryService = accessoryService;
	}
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}

}
