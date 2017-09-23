package com.mycuckoo.service.platform;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE;
import static com.mycuckoo.common.constant.ServiceVariable.ENABLE;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_MODOPT_ASSIGN;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_MODOPT_MGR;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_MOD_MGR;
import static com.mycuckoo.common.utils.CommonUtils.getResourcePath;
import static com.mycuckoo.common.utils.CommonUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.ModuleLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.common.utils.XmlOptUtils;
import com.mycuckoo.domain.platform.ModOptRef;
import com.mycuckoo.domain.platform.ModuleMemu;
import com.mycuckoo.domain.platform.Operate;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageImpl;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.ModOptRefMapper;
import com.mycuckoo.repository.platform.ModuleMemuMapper;
import com.mycuckoo.service.facade.UumServiceFacade;
import com.mycuckoo.vo.AssignVo;
import com.mycuckoo.vo.HierarchyModuleVo;
import com.mycuckoo.vo.TreeVo;
import com.mycuckoo.vo.platform.ModuleMemuVo;

/**
 * 功能说明: 系统模块业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:37:40 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class ModuleService {
	
	static Logger logger = LoggerFactory.getLogger(ModuleService.class);
	
	@Autowired
	private ModuleMemuMapper moduleMemuMapper;
	@Autowired
	private ModOptRefMapper modOptRefMapper;
	
	@Autowired
	private SystemOptLogService sysOptLogService;
	@Autowired
	private OperateService operateService;
	@Autowired
	private UumServiceFacade uumServiceFacade;
	
	

	@Transactional(readOnly=false)
	public void deleteModOptRefByOperateId(long operateId) throws ApplicationException {
		
		// 查询当前模块的所有操作
		List<ModOptRef> modOptRefList = modOptRefMapper.findByOperateId(operateId);
		List<String> modOptRefIdList = new ArrayList<String>();
		for (ModOptRef modOptRef : modOptRefList) {
			Long modOptId = modOptRef.getModOptId();
			modOptRefIdList.add(modOptId.toString());
		}
		
		// 删除模块时自动删除权限下的模块
		uumServiceFacade.deletePrivilegeByModOptId(modOptRefIdList.toArray(new String[modOptRefIdList.size()]));
		modOptRefMapper.deleteByOperateId(operateId);
		
		String optContent = "根据操作ID删除模块操作关系,级联删除权限";
		sysOptLogService.saveLog(LogLevelEnum.THIRD, OptNameEnum.DELETE, SYS_MODOPT_MGR, 
				optContent, operateId + "");
	}

	@Transactional(readOnly=false)
	public void delete(Long moduleId) throws ApplicationException {
		logger.debug("will delete module id is {}", moduleId);
		
		ModuleMemu moduleMemu = get(moduleId);
		// 查询当前模块的所有操作
		List<ModOptRef> modOptRefList = modOptRefMapper.findByModuleId(moduleId);
		List<String> modOptRefIdList = new ArrayList<String>();
		for (ModOptRef modOptRef : modOptRefList) {
			Long modOptId = modOptRef.getModOptId();
			modOptRefIdList.add(modOptId.toString());
		}
		
		// 删除模块时自动删除权限下的模块
		uumServiceFacade.deletePrivilegeByModOptId(modOptRefIdList.toArray(new String[modOptRefIdList.size()]));
		// 删除模块时自动删除模块下的操作
		modOptRefMapper.deleteByModuleId(moduleId); 
		moduleMemuMapper.delete(moduleId);
	
		this.writeLog(moduleMemu, LogLevelEnum.THIRD, OptNameEnum.DELETE);
	}

	@Transactional(readOnly=false)
	public boolean disEnable(long moduleId, String disableFlag) throws ApplicationException {
		if(DISABLE.equals(disableFlag)) {
			int count = moduleMemuMapper.countByParentId(moduleId);
			if(count > 0) { // 有下级菜单
				return false;
			} else {
				ModuleMemu moduleMemu = get(moduleId);
				moduleMemu.setStatus(DISABLE);
				// 查询当前模块的所有操作
				List<ModOptRef> modOptRefList = modOptRefMapper.findByModuleId(moduleId);
				List<String> modOptRefIdList = new ArrayList<String>();
				for (ModOptRef modOptRef : modOptRefList) {
					Long modOptId = modOptRef.getModOptId();
					modOptRefIdList.add(modOptId.toString());
				}
				
				// 删除模块时自动删除权限下的模块
				uumServiceFacade.deletePrivilegeByModOptId(modOptRefIdList.toArray(new String[modOptRefIdList.size()]));
				// 停用第三级模块时将自动删除模块下的操作
				modOptRefMapper.deleteByModuleId(moduleId); 
				moduleMemuMapper.update(moduleMemu);
				
				this.writeLog(moduleMemu, LogLevelEnum.SECOND, OptNameEnum.DISABLE);
				return true;
			}
		} else {
			ModuleMemu moduleMemu = get(moduleId);
			moduleMemu.setStatus(ENABLE);
			moduleMemuMapper.update(moduleMemu);
			
			this.writeLog(moduleMemu, LogLevelEnum.SECOND, OptNameEnum.ENABLE);
			return true;
		}
	}

	public HierarchyModuleVo filterModule(List<ModuleMemuVo> list) {
		List<ModuleMemuVo> firstList = Lists.newArrayList();
		List<ModuleMemuVo> secondList = Lists.newArrayList();
		List<ModuleMemuVo> thirdList = Lists.newArrayList();
		
		// 过滤分类一级、二级、三级菜单
		for(ModuleMemuVo vo : list) {
			switch (ModuleLevelEnum.value(vo.getModLevel())) {
			case ONE:
				sortModule(firstList, vo);
				break;
			case TWO:
				secondList.add(vo);
				break;
			case THREE:
				thirdList.add(vo);
				break;
			}
		}
		
		Map<String, List<ModuleMemuVo>> secondMap = Maps.newHashMap(); // 第二级
		Map<String, List<ModuleMemuVo>> thirdMap = Maps.newHashMap();	 // 第三级
		
		// 分类一级包含的二级菜单, 二级包含的三级菜单
		for(ModuleMemuVo firstMod : firstList) { // 1
			List<ModuleMemuVo> firstModChildren = Lists.newArrayList();
			for(ModuleMemuVo secondMod : secondList) { // 2
				if(secondMod.getParentId() == firstMod.getModuleId()) {
					sortModule(firstModChildren, secondMod);
					List<ModuleMemuVo> secondModChildren = Lists.newArrayList();
					for(ModuleMemuVo thirdMod : thirdList) { // 3
						if(thirdMod.getParentId() == secondMod.getModuleId()) {
							sortModule(secondModChildren, thirdMod);
						}
					}
					thirdMap.put(secondMod.getModuleId().toString(), secondModChildren); // 二级包含的三级子菜单 
				}
			}
			secondMap.put(firstMod.getModuleId().toString(), firstModChildren); // 一级包含的二级子菜单
		}
		
		return new HierarchyModuleVo(firstList, secondMap, thirdMap);
	}

	public List<ModuleMemuVo> findAll() {
		Pageable pageRequest = new PageRequest(0, Integer.MAX_VALUE);
		List<ModuleMemu> list = moduleMemuMapper.findByPage(null, pageRequest).getContent();
		List<ModuleMemuVo> vos = Lists.newArrayList();
		list.forEach(moduleMemu -> {
			ModuleMemuVo vo = new ModuleMemuVo();
			BeanUtils.copyProperties(moduleMemu, vo);
			vos.add(vo);
		});
		
		return vos;
	}

	public List<ModOptRef> findAllModOptRefs() {
		Pageable pageRequest = new PageRequest(0, Integer.MAX_VALUE);
		return modOptRefMapper.findByPage(null, pageRequest).getContent();
	}

	public AssignVo<Operate> findAssignedAUnAssignedOperatesByModuleId(long moduleId) {
		List<Operate> allOperateList = operateService.findAll(); // 所有操作
		List<Operate> assignedOperateList = findAssignedOperatesByModuleId(moduleId); // 已经分配的操作
		List<Operate> unassignedOperateList = allOperateList; // 未分配的操作
		if(!allOperateList.isEmpty()) {
			List<Operate> list = Lists.newArrayList(allOperateList);
			list.removeAll(assignedOperateList); // 删除已经分配
			unassignedOperateList = allOperateList; // 未分配的操作
		}
	
		return new AssignVo<>(assignedOperateList, unassignedOperateList);
	}

	/**
	 * <b>注意：</b>返回的Operate对象只有部分属性有值
	 */
	public List<Operate> findAssignedOperatesByModuleId(long moduleId) {
		List<ModOptRef> modOptRefList = modOptRefMapper.findByModuleId(moduleId);
		List<Operate> operationList = new ArrayList<Operate>();
		for(ModOptRef modOptRef : modOptRefList) {
			operationList.add(operateService.get(modOptRef.getOperate().getOperateId()));
		}
		
		return operationList;
	}

	public List<ModuleMemuVo> findChildNodeList(long moduleId, int flag) {
		Pageable pageRequest = new PageRequest(0, Integer.MAX_VALUE);
		List<ModuleMemu> listAll = moduleMemuMapper.findByPage(null, pageRequest).getContent();
		
		List<ModuleMemu> filterList = Lists.newArrayList();
		List<ModuleMemu> listAllTemp = Lists.newArrayList();
		listAllTemp.addAll(listAll);
		
		//删除根元素
		ModuleMemu moduleMemu = new ModuleMemu(0L);
		listAllTemp.remove(moduleMemu);
		//过滤出所有下级元素
		filterList = getFilterList(filterList, listAllTemp, moduleId);	
		if(flag == 1) {
			//本元素
			ModuleMemu moduleMemuOld = new ModuleMemu(moduleId);
			filterList.add(moduleMemuOld);
			listAll.removeAll(filterList);
			filterList = listAll;
		}
		
		List<ModuleMemuVo> vos = Lists.newArrayList();
		filterList.forEach(module -> {
			ModuleMemuVo vo = new ModuleMemuVo();
			BeanUtils.copyProperties(module, vo);
			vos.add(vo);
		});
		
		return vos;
	}

	public List<ModOptRef> findModOptRefsByModOptRefIds(Long[] modOptRefIds) {
		if(modOptRefIds == null || modOptRefIds.length == 0) {
			return new ArrayList<ModOptRef>(0);
		}
		
		return modOptRefMapper.findByIds(modOptRefIds);
	}

	public Page<ModuleMemuVo> findByPage(long treeId, String modName, String modEnId, Pageable page) {
		logger.debug("start={} limit={} treeId={} modName={} modEnId={}", 
				page.getOffset(), page.getPageSize(), treeId, modName, modEnId);
		
		List<Long> idList = new ArrayList<Long>();
		if(treeId > 0) {
			List<ModuleMemuVo> list = findChildNodeList(treeId, 0); // 过滤出所有下级
			for(ModuleMemuVo vo : list) {
				idList.add(vo.getModuleId()); // 所有下级模块ID
			}
		}
		Map<String, Object> params = Maps.newHashMap();
		params.put("array", idList.isEmpty() ? null : idList.toArray(new Long[idList.size()]));
		params.put("modName", isNullOrEmpty(modName) ? null : "%" + modName + "%");
		params.put("modEnId", isNullOrEmpty(modEnId) ? null : "%" + modEnId + "%");
		Page<ModuleMemu> entityPage = moduleMemuMapper.findByPage(params, page);
		
		List<ModuleMemuVo> vos = Lists.newArrayList();
		for(ModuleMemu entity : entityPage.getContent()) {					
			ModuleMemuVo vo = new ModuleMemuVo();
			BeanUtils.copyProperties(entity, vo);
			vos.add(vo);
		}
		
		return new PageImpl<>(vos, page, entityPage.getTotalElements());
	}

	public ModuleMemuVo get(Long moduleId) {
		logger.debug("will find module id is {}", moduleId);
		
		ModuleMemu entity = moduleMemuMapper.get(moduleId);
		ModuleMemuVo vo = new ModuleMemuVo();
		BeanUtils.copyProperties(entity, vo);
		
		return vo;
	}

	public boolean existsByModuleName(String moduleName) {
		int totalProperty = moduleMemuMapper.countByModuleName(moduleName);
		
		logger.debug("find module total is {}", totalProperty);
		
		if(totalProperty > 0) {
			return true;
		}
		return false;
	}

	public List<TreeVo> findByParentIdAndFilterModuleIds(long moduleId, long filterModuleId) {
		List<ModuleMemu> list = moduleMemuMapper.findByParentIdAndFilterModuleIds(moduleId, new long[]{0L, filterModuleId});
		List<TreeVo> treeVoList = new ArrayList<TreeVo>();
		if (list != null && list.size() > 0) {
			for (ModuleMemu mod : list) {
				TreeVo treeVo = new TreeVo();
				treeVo.setId(mod.getModuleId().toString());
				treeVo.setText(mod.getModName());
				treeVo.setIconSkin(mod.getModImgCls());
				if (ModuleLevelEnum.value(mod.getModLevel()) != ModuleLevelEnum.THREE) {
					treeVo.setIsParent(true); // 模块菜单级别为3是叶子
				}
				treeVoList.add(treeVo);
			}
		}
		
		return treeVoList;
	}

	@Transactional(readOnly=false)
	public void update(ModuleMemu moduelMemu) throws ApplicationException {
		moduleMemuMapper.update(moduelMemu);
		
		writeLog(moduelMemu, LogLevelEnum.SECOND, OptNameEnum.MODIFY);
	}

	@Transactional(readOnly=false)
	public void updateLabel(String xmlFile, List<String[]> moduleLabelList) throws SystemException {
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
	public void save(ModuleMemu moduleMemu) throws ApplicationException {
		moduleMemuMapper.save(moduleMemu);
		
		writeLog(moduleMemu, LogLevelEnum.FIRST, OptNameEnum.SAVE);
	}

	
	@Transactional(readOnly=false)
	public void saveModuleOptRefs(long moduleId, List<Long> operateIdList) throws ApplicationException {
//		throw new UnsupportedOperationException();
		
		// 查询当前模块的所有操作
		List<ModOptRef> modOptRefList = modOptRefMapper.findByModuleId(moduleId);
		if (modOptRefList == null || modOptRefList.size() == 0) { // 如果没有任何操作则直接增加
			saveModuleOptRefSingle(moduleId, operateIdList);
		} else {
			/*
			 * 模块操作关系：首先找出重复的操作ID，
			 * 原模块操作列表删除重复的之后删除(并级联删除权限操作) 
			 * 新分配的操作列表删除重复的之后增加
			 */
			List<ModOptRef> repeatModOptRefList = new ArrayList<ModOptRef>();
			List<Long> repeatOperateIdList = new ArrayList<Long>();
			for(ModOptRef modOptRef : modOptRefList) {
				Long oldOperateId = modOptRef.getOperate().getOperateId();
				for(Long newOperateId : operateIdList) {
					if(newOperateId  == oldOperateId) {
						repeatModOptRefList.add(modOptRef);
						repeatOperateIdList.add(newOperateId);
						break;
					}
				}
			}
			modOptRefList.removeAll(repeatModOptRefList); //删除重复的模块操作
			operateIdList.removeAll(repeatOperateIdList); //删除重复的ID
			
			modOptRefList.forEach(modOptRef -> {
				modOptRefMapper.delete(modOptRef.getModOptId()); //进行模块操作关系删除
			});
			
			List<String> modOptRefIdList = new ArrayList<String>();
			for (ModOptRef modOptRef : modOptRefList) {
				Long modOptId = modOptRef.getModOptId();
				modOptRefIdList.add(modOptId.toString());
			}
			uumServiceFacade.deletePrivilegeByModOptId(modOptRefIdList.toArray(new String[modOptRefIdList.size()]));
			
			// 删除权限操作 modOptRefList 模块操作关系
			saveModuleOptRefSingle(moduleId, operateIdList); // 保存新分配的模块操作关系
		}
	}
	
	
	
	// --------------------------- 私有方法 -------------------------------
	/**
	 * 公用模块写日志
	 *
	 * @param moduleMemu 模块对象
	 * @param logLevel
	 * @param opt
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 10, 2012 11:04:50 PM
	 */
	private void writeLog(ModuleMemu moduleMemu, LogLevelEnum logLevel, 
			OptNameEnum opt) throws ApplicationException {
		
		String optContent = moduleMemu.getModName() + SPLIT 
				+ moduleMemu.getModEnId() + SPLIT;
		
		sysOptLogService.saveLog(logLevel, opt, SYS_MOD_MGR, optContent, moduleMemu.getModuleId() + "");
	}
	/**
	 * 对系统菜单进行排序
	 *
	 * @param moduleList 菜单列表
	 * @param moduleMemu 菜单对象
	 * @author rutine
	 * @time Oct 11, 2012 8:58:30 PM
	 */
	private void sortModule(List<ModuleMemuVo> moduleList, ModuleMemuVo moduleMemu) {
		// 根据操作顺序进行排序
		int index = 0; // 元素索引
		boolean isAppend = true; // 是否追加元素
		for (ModuleMemuVo mod : moduleList) {
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
	private List<ModuleMemu> getFilterList(List<ModuleMemu> filterList, 
			List<ModuleMemu> listAll, long moduleId) {
		
		List<ModuleMemu> subList = getSubList(listAll, moduleId);
		if (subList.size() > 0) {
			filterList.addAll(subList);
		}
		for (ModuleMemu module : subList) {
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
	private List<ModuleMemu> getSubList(List<ModuleMemu> listAll, long moduleId) {
		List<ModuleMemu> newList = new ArrayList<ModuleMemu>();
		for (ModuleMemu module : listAll) {
			if (module.getParentId() == moduleId) {
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
	private void saveModuleOptRefSingle(long moduleId, List<Long> operateIdList) 
			throws ApplicationException {
		
		StringBuilder operateIds = new StringBuilder();
		if(operateIdList != null) {
			for(Long operateId : operateIdList) {
				Operate operate = new Operate(operateId, null);
				ModuleMemu moduleMemu = new ModuleMemu(moduleId);
				ModOptRef modOptRef = new ModOptRef(null, operate, moduleMemu);
				modOptRefMapper.save(modOptRef);
				
				operateIds.append(operateIds.length() > 0 ? "," + operateId : operateId);
			}
		}
		
		String optContent = "模块分配操作" + SPLIT + operateIds.toString();
		sysOptLogService.saveLog(LogLevelEnum.FIRST, OptNameEnum.SAVE, SYS_MODOPT_ASSIGN, 
				optContent, moduleId + "");
	}
	
}
