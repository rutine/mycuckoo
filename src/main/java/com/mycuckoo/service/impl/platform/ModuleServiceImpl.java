package com.mycuckoo.service.impl.platform;

import static com.mycuckoo.common.constant.Common.DELETE_OPT;
import static com.mycuckoo.common.constant.Common.DISABLE;
import static com.mycuckoo.common.constant.Common.ENABLE;
import static com.mycuckoo.common.constant.Common.FIRST;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_SECOND;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_THIRD;
import static com.mycuckoo.common.constant.Common.MODIFY_OPT;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SECOND;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.Common.THIRD;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE_DIS;
import static com.mycuckoo.common.constant.ServiceVariable.ENABLE_DIS;
import static com.mycuckoo.common.constant.ServiceVariable.ONE;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_MODOPT_ASSIGN;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_MODOPT_MGR;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_MOD_MGR;
import static com.mycuckoo.common.constant.ServiceVariable.THREE;
import static com.mycuckoo.common.constant.ServiceVariable.TWO;
import static com.mycuckoo.common.utils.CommonUtils.getResourcePath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.common.utils.XmlOptUtils;
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplModOptRef;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.domain.platform.SysplOperate;
import com.mycuckoo.domain.vo.TreeVo;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.platform.SysplModuleMemuRepository;
import com.mycuckoo.service.iface.commonused.UumCommonService;
import com.mycuckoo.service.iface.platform.OperateService;
import com.mycuckoo.service.iface.platform.ModuleService;
import com.mycuckoo.service.iface.platform.SystemOptLogService;

/**
 * 功能说明: 系统模块业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:37:40 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class ModuleServiceImpl implements ModuleService {
	
	static Logger logger = LoggerFactory.getLogger(ModuleServiceImpl.class);
	
	private SysplModuleMemuRepository moduleRepository;
	
	private SystemOptLogService sysOptLogService;
	private OperateService moduleOptService;
	private UumCommonService uumCommonService;

	@Transactional(readOnly=false)
	@Override
	public void deleteModOptRefByOperateId(long operateId, HttpServletRequest request) 
			throws ApplicationException {
		
		// 查询当前模块的所有操作
		List<SysplModOptRef> sysplModOptRefList = moduleRepository.findModOptRefsByOperateId(operateId);
		List<String> modOptRefIdList = new ArrayList<String>();
		for (SysplModOptRef sysplModOptRef : sysplModOptRefList) {
			Long modOptId = sysplModOptRef.getModOptId();
			modOptRefIdList.add(modOptId.toString());
		}
		
		// 删除模块时自动删除权限下的模块
		uumCommonService.deletePrivilegeByModOptId(modOptRefIdList.toArray(new String[modOptRefIdList.size()]), request);
		moduleRepository.deleteModOptRefByOperateId(operateId);
		
		String optContent = "根据操作ID删除模块操作关系,级联删除权限";
		sysOptLogService.saveLog(LOG_LEVEL_THIRD, SYS_MODOPT_MGR, DELETE_OPT, optContent, operateId + "", request);
	}

	@Transactional(readOnly=false)
	@Override
	public void deleteModuleByModuleId(Long moduleId, HttpServletRequest request) throws ApplicationException {
		logger.debug("will delete module id is " + moduleId);
		
		SysplModuleMemu sysplModuleMemu = getModuleByModuleId(moduleId);
		// 查询当前模块的所有操作
		List<SysplModOptRef> sysplModOptRefList = moduleRepository.findModOptRefsByModuleId(moduleId);
		List<String> modOptRefIdList = new ArrayList<String>();
		for (SysplModOptRef sysplModOptRef : sysplModOptRefList) {
			Long modOptId = sysplModOptRef.getModOptId();
			modOptRefIdList.add(modOptId.toString());
		}
		
		// 删除模块时自动删除权限下的模块
		uumCommonService.deletePrivilegeByModOptId(modOptRefIdList.toArray(new String[modOptRefIdList.size()]), request);
		// 删除模块时自动删除模块下的操作
		moduleRepository.deleteModOptRefByModuleId(moduleId); 
		moduleRepository.delete(sysplModuleMemu);
	
		this.writeLog(sysplModuleMemu, LOG_LEVEL_THIRD, DELETE_OPT, request);
	}

	@Transactional(readOnly=false)
	@Override
	public boolean disEnableModule(long moduleId, String disableFlag, 
			HttpServletRequest request) throws ApplicationException {
		
		if(DISABLE.equals(disableFlag)) {
			int count = moduleRepository.countModulesByUpModId(moduleId);
			if(count > 0) { // 有下级菜单
				return false;
			} else {
				SysplModuleMemu sysplModuleMemu = getModuleByModuleId(moduleId);
				sysplModuleMemu.setStatus(DISABLE);
				// 查询当前模块的所有操作
				List<SysplModOptRef> sysplModOptRefList = moduleRepository.findModOptRefsByModuleId(moduleId);
				List<String> modOptRefIdList = new ArrayList<String>();
				for (SysplModOptRef sysplModOptRef : sysplModOptRefList) {
					Long modOptId = sysplModOptRef.getModOptId();
					modOptRefIdList.add(modOptId.toString());
				}
				
				// 删除模块时自动删除权限下的模块
				uumCommonService.deletePrivilegeByModOptId(modOptRefIdList.toArray(new String[modOptRefIdList.size()]), request);
				// 停用第三级模块时将自动删除模块下的操作
				moduleRepository.deleteModOptRefByModuleId(moduleId); 
				moduleRepository.update(sysplModuleMemu);
				
				this.writeLog(sysplModuleMemu, LOG_LEVEL_SECOND, DISABLE_DIS, request);
				return true;
			}
		} else {
			SysplModuleMemu sysplModuleMemu = getModuleByModuleId(moduleId);
			sysplModuleMemu.setStatus(ENABLE);
			moduleRepository.update(sysplModuleMemu);
			
			this.writeLog(sysplModuleMemu, LOG_LEVEL_SECOND, ENABLE_DIS, request);
			return true;
		}
	}

	@Override
	public Map<String, Object> filterModule(List<SysplModuleMemu> list) {
		List<SysplModuleMemu> firstList = new ArrayList<SysplModuleMemu>();
		List<SysplModuleMemu> secondList = new ArrayList<SysplModuleMemu>();
		List<SysplModuleMemu> thirdList = new ArrayList<SysplModuleMemu>();
		
		// 过滤分类一级、二级、三级菜单
		for(SysplModuleMemu module : list) {
			if(ONE.equals(module.getModLevel())) {
				sortModule(firstList, module);
			} else if(TWO.equals(module.getModLevel())) {
				secondList.add(module);
			} else if(THREE.equals(module.getModLevel())) {
				thirdList.add(module);
			}
		}
		
		Map<String, List<SysplModuleMemu>> secondMap = new HashMap<String, List<SysplModuleMemu>>(); // 第二级
		Map<String, List<SysplModuleMemu>> thirdMap = new HashMap<String, List<SysplModuleMemu>>();	 // 第三级
		
		// 分类一级包含的二级菜单, 二级包含的三级菜单
		for(SysplModuleMemu firstMod : firstList) { // 1
			List<SysplModuleMemu> firstModChildren = new ArrayList<SysplModuleMemu>();
			for(SysplModuleMemu secondMod : secondList) { // 2
				if(secondMod.getUpModId() == firstMod.getModuleId()) {
					sortModule(firstModChildren, secondMod);
					List<SysplModuleMemu> secondModChildren = new ArrayList<SysplModuleMemu>();
					for(SysplModuleMemu thirdMod : thirdList) { // 3
						if(thirdMod.getUpModId() == secondMod.getModuleId()) {
							sortModule(secondModChildren, thirdMod);
						}
					}
					thirdMap.put(secondMod.getModuleId().toString(), secondModChildren); // 二级包含的三级子菜单 
				}
			}
			secondMap.put(firstMod.getModuleId().toString(), firstModChildren); // 一级包含的二级子菜单
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(FIRST, firstList);
		map.put(SECOND, secondMap);
		map.put(THIRD, thirdMap);
		
		return map;
	}

	@Override
	public List<SysplModuleMemu> findAllModules() {
		List<SysplModuleMemu> list = moduleRepository.findAllModules();
		if(list != null && !list.isEmpty()) {
			for(SysplModuleMemu mod : list) {
				mod.setUpModId(mod.getSysplModuleMemu().getModuleId()); // 上级模块ID
			}
		}
		
		return list;
	}

	@Override
	public List<SysplModOptRef> findAllModOptRefs() {
		return moduleRepository.findAllModOptRefs();
	}

	@Override
	public Map<String, List<SysplOperate>> findAssignedAUnAssignedOperatesByModuleId(long moduleId) {
		List<SysplOperate> allOperateList = moduleOptService.findAllOperates(); // 所有操作
		List<SysplOperate> assignedOperateList = findAssignedOperatesByModuleId(moduleId); // 已经分配的操作
		allOperateList.removeAll(assignedOperateList); // 删除已经分配
		List<SysplOperate> unassignedOperateList = allOperateList; // 未分配的操作
		
		Map<String, List<SysplOperate>> map = new HashMap<String, List<SysplOperate>>();
		map.put("assignedModOpts", assignedOperateList);
		map.put("unassignedModOpts", unassignedOperateList);
	
		return map;
	}

	/**
	 * <b>注意：</b>返回的SysplOperate对象只有部分属性有值
	 */
	@Override
	public List<SysplOperate> findAssignedOperatesByModuleId(long moduleId) {
		List<SysplModOptRef> modOptRefList = moduleRepository.findModOptRefsByModuleId(moduleId);
		List<SysplOperate> operationList = new ArrayList<SysplOperate>();
		if(modOptRefList != null) {
			for(SysplModOptRef modOptRef : modOptRefList) {
				operationList.add(moduleOptService.findOperatesByOperateId(modOptRef.getSysplOperate().getOperateId()));
			}
		}
		
		return operationList;
	}

	@Override
	public List<SysplModuleMemu> findChildNodeList(long moduleId, int flag) {
		List<SysplModuleMemu> filterList = new ArrayList<SysplModuleMemu>();
		List<SysplModuleMemu> listAll = moduleRepository.findAllModules();
		List<SysplModuleMemu> listAllTemp = new ArrayList<SysplModuleMemu>();
		listAllTemp.addAll(listAll);
		//删除根元素
		SysplModuleMemu sysplModuleMemu = new SysplModuleMemu();
		sysplModuleMemu.setModuleId(0L);
		listAllTemp.remove(sysplModuleMemu);
		//过滤出所有下级元素
		filterList = getFilterList(filterList, listAllTemp, moduleId);	
		if(flag == 1){
			//本元素
			SysplModuleMemu sysplModuleMemuOld = new SysplModuleMemu();
			sysplModuleMemuOld.setModuleId(moduleId);
			filterList.add(sysplModuleMemuOld);
			listAll.removeAll(filterList);
			filterList = listAll;
			if(filterList != null && filterList.size()>0){
				for(SysplModuleMemu mod : filterList){
					mod.setUpModId(mod.getSysplModuleMemu().getModuleId());
				}
			}
		}
		
		return filterList;
	}

	@Override
	public List<SysplModOptRef> findModOptRefsByModOptRefIds(Long[] modOptRefIds) {
		if(modOptRefIds == null || modOptRefIds.length == 0) return new ArrayList<SysplModOptRef>(0);
		return moduleRepository.findModOptRefsByModOptRefIds(modOptRefIds);
	}

	@Override
	public Page<SysplModuleMemu> findModulesByCon(long treeId, String modName, String modEnId, Pageable page) {
		logger.debug("start=" + page.getOffset() + " limit=" + page.getPageSize() 
				+ " treeId= " + treeId + " modName" + modName + " modEnId" + modEnId);
		
		List<Long> idList = new ArrayList<Long>();
		if(treeId > 0) {
			List<SysplModuleMemu> list = findChildNodeList(treeId, 0); // 过滤出所有下级
			for(SysplModuleMemu sysplModuleMemu : list) {
				idList.add(sysplModuleMemu.getModuleId()); // 所有下级模块ID
			}
		}
		
		return moduleRepository.findModulesByCon(idList.toArray(new Long[idList.size()]), modName, modEnId, page);
	}

	@Override
	public SysplModuleMemu getModuleByModuleId(Long moduleId) {
		logger.debug("will find module id is " + moduleId);
		return moduleRepository.get(moduleId);
	}

	@Override
	public boolean existsModuleByModuleName(String moduleName) {
		int totalProperty = moduleRepository.countModulesByModuleName(moduleName);
		
		logger.debug("find module total is " + totalProperty);
		
		if(totalProperty > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<TreeVo> findModulesByUpModId(long moduleId, long filterModuleId) {
		List<SysplModuleMemu> list = moduleRepository.findModulesByUpModId(moduleId, filterModuleId);
		List<TreeVo> treeVoList = new ArrayList<TreeVo>();
		if (list != null && list.size() > 0) {
			for (SysplModuleMemu mod : list) {
				TreeVo treeVo = new TreeVo();
				treeVo.setId(mod.getModuleId().toString());
				treeVo.setText(mod.getModName());
				treeVo.setIconSkin(mod.getModImgCls());
				if (!THREE.equals(mod.getModLevel())) {
					treeVo.setIsParent(true); // 模块菜单级别为3是叶子
				}
				treeVoList.add(treeVo);
			}
		}
		
		return treeVoList;
	}

	@Transactional(readOnly=false)
	@Override
	public void updateModule(SysplModuleMemu moduelMemu, HttpServletRequest request) 
			throws ApplicationException {
		
		moduleRepository.update(moduelMemu);
		writeLog(moduelMemu, LOG_LEVEL_SECOND, MODIFY_OPT, request);
	}

	@Transactional(readOnly=false)
	@Override
	public void updateModuleLabel(String xmlFile, List<String[]> moduleLabelList, 
			HttpServletRequest request)  throws ApplicationException {
		
		Document document = XmlOptUtils.loadXMLFile(getResourcePath() + xmlFile);
		Element moduleEl = (Element) document.selectSingleNode("/module");
		Element fieldNamesEl = (Element) document.selectSingleNode("/module/fieldNames");
		moduleEl.remove(fieldNamesEl); // 删除fieldNames结点并重新添加
		fieldNamesEl = moduleEl.addElement("fieldNames");
		for (String[] moduleLabelArr : moduleLabelList) {
			Element funNameEl = fieldNamesEl.addElement("field");
			funNameEl.addAttribute("name", moduleLabelArr[0]);
			funNameEl.addText(moduleLabelArr[1]);
		}
		XmlOptUtils.doc2XMLFile(document, getResourcePath() + xmlFile);
	}

	@Transactional(readOnly=false)
	@Override
	public void saveModule(SysplModuleMemu moduleMemu, HttpServletRequest request) 
			throws ApplicationException {
		
		moduleRepository.save(moduleMemu);
		
		writeLog(moduleMemu, LOG_LEVEL_FIRST, SAVE_OPT, request);
	}

	
	@Transactional(readOnly=false)
	@Override
	public void saveModuleOptRefs(long moduleId, List<Long> operateIdList, 
			HttpServletRequest request) throws ApplicationException {
		
//		throw new UnsupportedOperationException();
		// 查询当前模块的所有操作
		List<SysplModOptRef> sysplModOptRefList = moduleRepository.findModOptRefsByModuleId(moduleId);
		if (sysplModOptRefList == null || sysplModOptRefList.size() == 0) { // 如果没有任何操作则直接增加
			saveModuleOptRefSingle(moduleId, operateIdList, request);
		} else {
			/*
			 * 模块操作关系：首先找出重复的操作ID，
			 * 原模块操作列表删除重复的之后删除(并级联删除权限操作) 
			 * 新分配的操作列表删除重复的之后增加
			 */
			List<SysplModOptRef> repeateModOptRefList = new ArrayList<SysplModOptRef>();
			List<Long> repeateOperateIdList = new ArrayList<Long>();
			for(SysplModOptRef modOptRef : sysplModOptRefList) {
				Long oldOperateId = modOptRef.getSysplOperate().getOperateId();
				for(Long newOperateId : operateIdList) {
					if(newOperateId  == oldOperateId) {
						repeateModOptRefList.add(modOptRef);
						repeateOperateIdList.add(newOperateId);
						break;
					}
				}
			}
			sysplModOptRefList.removeAll(repeateModOptRefList); //删除重复的模块操作
			operateIdList.removeAll(repeateOperateIdList); //删除重复的ID
			
			moduleRepository.deleteModOptRefs(sysplModOptRefList); //进行模块操作关系删除
			List<String> modOptRefIdList = new ArrayList<String>();
			for (SysplModOptRef sysplModOptRef : sysplModOptRefList) {
				Long modOptId = sysplModOptRef.getModOptId();
				modOptRefIdList.add(modOptId.toString());
			}
			uumCommonService.deletePrivilegeByModOptId(modOptRefIdList.toArray(new String[modOptRefIdList.size()]), request);
			// 删除权限操作 sysplModOptRefList 模块操作关系
			saveModuleOptRefSingle(moduleId, operateIdList, request); // 保存新分配的模块操作关系
		}
	}
	
	
	
	// --------------------------- 私有方法 -------------------------------
	/**
	 * 公用模块写日志
	 *
	 * @param moduleMemu 模块对象
	 * @param logLevel
	 * @param opt
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 10, 2012 11:04:50 PM
	 */
	private void writeLog(SysplModuleMemu moduleMemu, String logLevel, String opt, 
			HttpServletRequest request) throws ApplicationException {
		
		String optContent = moduleMemu.getModName() + SPLIT + moduleMemu.getModEnId() + SPLIT;
		sysOptLogService.saveLog(logLevel, SYS_MOD_MGR, opt, optContent, moduleMemu.getModuleId() + "", request);
	}
	/**
	 * 对系统菜单进行排序
	 *
	 * @param moduleList 菜单列表
	 * @param moduleMemu 菜单对象
	 * @author rutine
	 * @time Oct 11, 2012 8:58:30 PM
	 */
	private void sortModule(List<SysplModuleMemu> moduleList, SysplModuleMemu moduleMemu) {
		// 根据操作顺序进行排序
		int index = 0; // 元素索引
		boolean isAppend = true; // 是否追加元素
		for (SysplModuleMemu mod : moduleList) {
			int listModOrder = mod.getModOrder(); // 已经有的操作顺序
			int currModOrder = moduleMemu.getModOrder(); // 当前操作顺序
			if (listModOrder > currModOrder) {
				moduleList.add(index, moduleMemu); // 顺序小的插在前
				isAppend = false;
				break;
			}
			index++;
		}
		if (isAppend) {
			moduleList.add(moduleMemu); //加到操作list中
		}
	}
	/**
	 * 根据上级模块id递归过滤结点
	 *
	 * @param filterList 过滤得到的子节点
	 * @param listAll 所有模块结果集
	 * @param moduleId 上级模块id
	 * @return  所有子结点
	 * @author rutine
	 * @time Oct 13, 2012 10:08:25 AM
	 */
	private List<SysplModuleMemu> getFilterList(List<SysplModuleMemu> filterList, 
			List<SysplModuleMemu> listAll, long moduleId) {
		
		List<SysplModuleMemu> subList = getSubList(listAll, moduleId);
		if (subList.size() > 0) {
			filterList.addAll(subList);
		}
		for (SysplModuleMemu module : subList) {
			getFilterList(filterList, listAll, module.getModuleId());
		}
		
		return filterList;
	}
	/**
	 * 根据模块id获得所有子结点
	 *
	 * @param listAll 所有模块结果集
	 * @param moduleId 上级模块id
	 * @return  所有子结点
	 * @author rutine
	 * @time Oct 13, 2012 10:04:42 AM
	 */
	private List<SysplModuleMemu> getSubList(List<SysplModuleMemu> listAll, long moduleId) {
		List<SysplModuleMemu> newList = new ArrayList<SysplModuleMemu>();
		for (SysplModuleMemu module : listAll) {
			if (module.getSysplModuleMemu().getModuleId() == moduleId) {
				newList.add(module);
			}
		}
		
		return newList;
	}

	/**
	 * 只单独保存模块操作关系
	 * 
	 * @param moduleId
	 * @param operateIdList
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 14, 2012 9:14:00 AM
	 */
	private void saveModuleOptRefSingle(long moduleId, List<Long> operateIdList, 
			HttpServletRequest request) throws ApplicationException {
		
		StringBuilder operateIds = new StringBuilder();
		if(operateIdList != null) {
			List<SysplModOptRef> modOptRefList = new ArrayList<SysplModOptRef>();
			for(Long operateId : operateIdList) {
				SysplOperate sysplOperate = new SysplOperate(operateId, null);
				SysplModuleMemu sysplModuleMemu = new SysplModuleMemu(moduleId);
				SysplModOptRef sysplModOptRef = new SysplModOptRef(null, sysplOperate, sysplModuleMemu);
				
				modOptRefList.add(sysplModOptRef);
				operateIds.append(operateIds.length() > 0 ? "," + operateId : operateId);
			}
			
			moduleRepository.saveModOptRef(modOptRefList);
		}
		
		String optContent = "模块分配操作" + SPLIT + operateIds.toString();
		sysOptLogService.saveLog(LOG_LEVEL_FIRST, SYS_MODOPT_ASSIGN, SAVE_OPT, optContent, moduleId + "", request);
	}
	
	
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setModuleRepository(SysplModuleMemuRepository moduleRepository) {
		this.moduleRepository = moduleRepository;
	}
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}
	@Autowired
	public void setModuleOptService(OperateService moduleOptService) {
		this.moduleOptService = moduleOptService;
	}
	@Autowired
	public void setUumCommonService(UumCommonService uumCommonService) {
		this.uumCommonService = uumCommonService;
	}
	
}
