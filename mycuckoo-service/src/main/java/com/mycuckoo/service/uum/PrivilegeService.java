package com.mycuckoo.service.uum;

import static com.mycuckoo.common.constant.Common.ORGAN_ID;
import static com.mycuckoo.common.constant.Common.OWNER_TYPE_ARR;
import static com.mycuckoo.common.constant.Common.OWNER_TYPE_ROL;
import static com.mycuckoo.common.constant.Common.OWNER_TYPE_USR;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_SCOPE;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_TYPE_ARR;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_TYPE_OPT;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_TYPE_ROW;
import static com.mycuckoo.common.constant.Common.ROLE_ID;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.Common.USER;
import static com.mycuckoo.common.constant.Common.USER_ID;
import static com.mycuckoo.common.constant.ServiceVariable.MOD_ASSIGN_OPT;
import static com.mycuckoo.common.utils.CommonUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.common.constant.PrivilegeScopeEnum;
import com.mycuckoo.common.utils.SystemConfigXmlParse;
import com.mycuckoo.domain.platform.ModOptRef;
import com.mycuckoo.domain.platform.ModuleMemu;
import com.mycuckoo.domain.platform.Operate;
import com.mycuckoo.domain.uum.OrgRoleRef;
import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.domain.uum.Privilege;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.uum.PrivilegeMapper;
import com.mycuckoo.service.facade.PlatformServiceFacade;
import com.mycuckoo.service.platform.SystemOptLogService;
import com.mycuckoo.vo.HierarchyModuleVo;
import com.mycuckoo.vo.ModuleOperationVo;
import com.mycuckoo.vo.SystemConfigBean;
import com.mycuckoo.vo.TreeVoExtend;

/**
 * 功能说明: 权限业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:21:42 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class PrivilegeService {
	static Logger logger = LoggerFactory.getLogger(PrivilegeService.class);
	
	@Autowired
	private PrivilegeMapper privilegeMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private UserAgentService userAgentService;
	@Autowired
	private OrganService organService;
	@Autowired
	private RoleOrganService roleOrganService;
	@Autowired
	private SystemOptLogService sysOptLogService;
	@Autowired
	private PlatformServiceFacade platformServiceFacade;
	

	public void delete(long ownerId, String ownerType, String privilegeType) 
			throws ApplicationException {
		
		privilegeMapper.deleteByOwnerIdAndPrivilegeType(ownerId, ownerType, privilegeType);
	}

	public void deletePrivilegeByModOptId(String[] modOptRefIds) {
		if(modOptRefIds == null || modOptRefIds.length == 0) return;
		
		privilegeMapper.deleteByModOptId(modOptRefIds, PRIVILEGE_TYPE_OPT);
	}

	public void deleteByOwnerIdAndOwnerType(long ownerId, String ownerType) throws ApplicationException {
		privilegeMapper.deleteByOwnerIdAndOwnerType(ownerId, ownerType);
	}

	public void deleteRowPriByResourceId(String resourceId) throws ApplicationException {
		privilegeMapper.deleteRowPrivilegeByOrgId(resourceId, PRIVILEGE_TYPE_ROW, 
				PrivilegeScopeEnum.ORGAN.value());
	}

	public List<User> findUsersByUserName(String userName) {
		return userService.findByUserName(userName);
	}

	public ModuleOperationVo filterModOpt(List<ModOptRef> modOptRefList, boolean isTreeFlag) {
		Map<Long, List<ModuleMemu>> modOptMap = Maps.newHashMap(); // 四级模块操作
		List<ModuleMemu> moduleMemuList = Lists.newArrayList(); // 模块菜单list
		
		for(ModOptRef modOptRef : modOptRefList) {
			ModuleMemu moduleMemu3 = modOptRef.getModuleMemu(); // 第三级菜单
			Operate operate = modOptRef.getOperate();
			// 操作按钮
			ModuleMemu moduleMemuOpt = new ModuleMemu();
			moduleMemuOpt.setModuleId(modOptRef.getModOptId() + 1000); // 将模块操作关系的id加上1000,防id重复
			moduleMemuOpt.setModName(operate.getOperateName());
			moduleMemuOpt.setModImgCls(operate.getOptImgLink());
			moduleMemuOpt.setOptFunLink(operate.getOptFunLink()); // 为操作准备功能链接
			moduleMemuOpt.setModOrder(operate.getOptOrder()); // 操作按钮的顺序
			moduleMemuOpt.setUpModId(moduleMemu3.getModuleId()); // 将第三级菜单设置为操作
			moduleMemuOpt.setIsLeaf(true);
			if(isTreeFlag) { // 如果为树则加入模块list
				moduleMemuList.add(moduleMemuOpt);
			} else { // 不为树则维护操作按钮map
				Long modEnId = moduleMemu3.getModuleId(); // 模块英文id, 作为操作列表map的key值
				if(modOptMap.containsKey(modEnId)) {
					List<ModuleMemu> modOptList = modOptMap.get(modEnId);
					// 根据操作顺序进行排序
					int listIndex = 0; // 元素索引
					boolean orderBol = true; //是否插入指定索引元素
					for(ModuleMemu moduleMemuOptt : modOptList) {
						int listModOrder = moduleMemuOptt.getModOrder(); // 已经有的操作顺序
						int currModOrder = moduleMemuOpt.getModOrder(); // 当前操作顺序
						if(listModOrder > currModOrder) {
							modOptList.add(listIndex, moduleMemuOpt); // 顺序小的插在前
							orderBol = false;
							break;
						}
						listIndex++;
					}
					if(orderBol) {
						modOptList.add(moduleMemuOpt); // 加到操作list中
					}
				} else {
					List<ModuleMemu> modOptList = new ArrayList<ModuleMemu>(); // 操作list
					modOptList.add(moduleMemuOpt); // 加到操作list中
					modOptMap.put(modEnId, modOptList);
				}
			}
			
			// 如果包含相应菜单项则加入相应模块菜单
			if(!moduleMemuList.contains(moduleMemu3)) {
				moduleMemuList.add(moduleMemu3);
				ModuleMemu moduleMemu2 = moduleMemu3.getModuleMemu(); // 第二级菜单
				moduleMemu3.setUpModId(moduleMemu2.getModuleId()); // 设置上级菜单
				if(!moduleMemuList.contains(moduleMemu2)) {
					moduleMemuList.add(moduleMemu2);
					ModuleMemu moduleMemu1 = moduleMemu2.getModuleMemu();
					moduleMemu2.setUpModId(moduleMemu1.getModuleId());
					if(!moduleMemuList.contains(moduleMemu1)) {
						moduleMemuList.add(moduleMemu1);
						moduleMemu1.setUpModId(moduleMemu1.getModuleMemu().getModuleId());
					}
				}
			}
		}
		
		Collections.sort(moduleMemuList, new ModuleMemu());
		
		return new ModuleOperationVo(moduleMemuList, modOptMap);
	}
	

	public String findRowPrivilegeByRoleIdAPriType(long roleId) {
		// 查找已经分配的权限
		Long[] roleIds = new Long[] { roleId };
		String[] ownerTypes = new String[] { OWNER_TYPE_ROL };
		String[] privilegeTypes = new String[] { PRIVILEGE_TYPE_ROW };
		
		List<Privilege> privilegeOptList = privilegeMapper.findByOwnIdAndPrivilegeType(roleIds, ownerTypes, privilegeTypes);
		
		if(privilegeOptList.isEmpty()) return PrivilegeScopeEnum.ORGAN.value();
		
		Privilege uumPrivilege = privilegeOptList.get(0);
		String resourceId = uumPrivilege.getResourceId();
		
		return resourceId;
	}

	public Map findSelectAUnselectModOptByOwnIdAOwnType(long ownerId, String ownerType) {
		// 查找已经分配的权限
		Long[] roleIds = new Long[] { ownerId };
		String[] ownerTypes = new String[] { ownerType };
		String[] privilegeTypes = new String[] { PRIVILEGE_TYPE_OPT };
		List<Privilege> privilegeOptList = privilegeMapper.findByOwnIdAndPrivilegeType(roleIds, ownerTypes, privilegeTypes);
		// 操作id集
		List<Long> resourceIdList = new ArrayList<Long>();
		String privilegeScope = "";
		for(Privilege uumPrivilege : privilegeOptList) {
			String resouceId = uumPrivilege.getResourceId();
			try {
				resourceIdList.add(Long.parseLong(resouceId));
			} catch (NumberFormatException e) {
				logger.warn("{} 不能转换成模块id, 忽略此id.", resouceId);
			}
			if(StringUtils.isBlank(privilegeScope)) {
				privilegeScope = uumPrivilege.getPrivilegeScope();
			}
		}
		List<ModOptRef> assignedModOptList = platformServiceFacade.findModOptRefByModOptRefIds(resourceIdList);
		// 查找所有模块操作关系
		List<ModOptRef> allModOptList = platformServiceFacade.findAllModOptRefs();
		allModOptList.removeAll(assignedModOptList);
		// 未分配的权限
		List<ModOptRef> unassignedModOptList = allModOptList;
		//将操作转化成列表数据
		List<ModuleMemu> assignedModMenuList = this.filterModOpt(assignedModOptList, true).getModuleMenu();
		List<ModuleMemu>  unassignedModMenuList = this.filterModOpt(unassignedModOptList, true).getModuleMenu();
		//将已分配和未分配的模块操作放入map
		Map moduleMemuMap = Maps.newHashMap();
		moduleMemuMap.put("assignedModOpts", convertToTree(assignedModMenuList));
		moduleMemuMap.put("unassignedModOpts", convertToTree(unassignedModMenuList));
		moduleMemuMap.put(PRIVILEGE_SCOPE, privilegeScope);
		
		return moduleMemuMap;
	}

	public Map findSelectRowPrivilegeByUserId(long userId) {
		// 查找已经分配的权限
		Long[] userIds = new Long[] { userId };
		String[] ownerTypes = new String[] { OWNER_TYPE_USR };
		String[] privilegeTypes = new String[] { PRIVILEGE_TYPE_ROW };
		List<Privilege> privilegeOptList = privilegeMapper.findByOwnIdAndPrivilegeType(userIds, ownerTypes, privilegeTypes);
		List<Long> resourceIdList = new ArrayList<Long>();
		String privilegeScope = "";
		for(Privilege uumPrivilege : privilegeOptList) {
			String resourceId = uumPrivilege.getResourceId();
			resourceIdList.add(Long.parseLong(resourceId));
			if("".endsWith(privilegeScope)) {
				privilegeScope = uumPrivilege.getPrivilegeScope();
			}
		}
		
		Long[] resourceIdArray = resourceIdList.toArray(new Long[resourceIdList.size()]);
		List userInfoList = new ArrayList();
		if(PrivilegeScopeEnum.USER.value().equals(privilegeScope)) {
			List userList = userService.findByUserIds(resourceIdArray);
			userInfoList = userList;
		} else if(PrivilegeScopeEnum.ROLE.value().equals(privilegeScope)) {
			List<OrgRoleRef> orgRoleRefList = roleOrganService.findByOrgRoleIds(resourceIdArray);
			for(OrgRoleRef uumOrgRoleRef : orgRoleRefList) {
				long orgRoleId = uumOrgRoleRef.getOrgRoleId();
				String orgName = uumOrgRoleRef.getOrgan().getOrgSimpleName();
				String roleName = uumOrgRoleRef.getRole().getRoleName();
				Object[] orgObj = new Object[2];
				orgObj[0] = orgRoleId;
				orgObj[1] = orgName + "-" + roleName;
				userInfoList.add(orgObj);
			}
		} else {
			List organList = organService.findByOrgIds(resourceIdArray);
			userInfoList = organList;
		}
		
		Map map = new HashMap();
		map.put(USER, userInfoList);
		map.put(PRIVILEGE_SCOPE, privilegeScope);
		
		return map;
	}

	public boolean existsSpecialPrivilegeByUserId(long userId) {
		int count = privilegeMapper.countByUserIdAndOwnerType(userId, OWNER_TYPE_USR);
		
		return count > 0;
	}

	public HierarchyModuleVo findUserPrivilegesForAdminLogin() {
		List<ModuleMemu> allModuleMemus = platformServiceFacade.findAllModule();// 所有模块菜单
		List<ModOptRef> modOptRefList = platformServiceFacade.findAllModOptRefs(); // 所有操作按钮
		HierarchyModuleVo hierarchyModuleVo = platformServiceFacade.filterModule(allModuleMemus); // 过滤模块 
		
		Map<Long, List<ModuleMemu>> modOptMap = Maps.newHashMap(); // 四级模块操作
		for (ModOptRef modOptRef : modOptRefList) {
			ModuleMemu moduleMemu = modOptRef.getModuleMemu(); // 所属模块
			Operate operate = modOptRef.getOperate(); // 具体操作
			// 操作按钮
			ModuleMemu moduleMemuOpt = new ModuleMemu();
			moduleMemuOpt.setModuleId(modOptRef.getModOptId() + 1000); // 将模块操作关系的id加上1000,防id重复
			moduleMemuOpt.setModName(operate.getOperateName());
			moduleMemuOpt.setModImgCls(operate.getOptImgLink());
			moduleMemuOpt.setOptFunLink(operate.getOptFunLink()); // 为操作准备功能链接
			moduleMemuOpt.setModOrder(operate.getOptOrder()); // 操作按钮的顺序
			moduleMemuOpt.setUpModId(moduleMemu.getModuleId()); // 将第三级菜单设置为操作
			moduleMemuOpt.setIsLeaf(true);
			
			Long modEnId = moduleMemu.getModuleId(); // 模块英文id,作为操作列表map的key值
			if (modOptMap.containsKey(modEnId)) {
				List<ModuleMemu> modOptList = modOptMap.get(modEnId);
				// 根据操作顺序进行排序
				int listIndex = 0; // 元素索引
				boolean orderBol = true; // 是否插入指定索引元素
				for (ModuleMemu moduleMemuOptt : modOptList) {
					int listModOrder = moduleMemuOptt.getModOrder(); // 已经有的操作顺序
					int currModOrder = moduleMemuOpt.getModOrder(); // 当前操作顺序
					if (listModOrder > currModOrder) {
						modOptList.add(listIndex, moduleMemuOpt); // 顺序小的插在前
						orderBol = false;
						break;
					}
					listIndex++;
				}
				if (orderBol) {
					modOptList.add(moduleMemuOpt);// 加到操作list中
				}
			} else {
				List<ModuleMemu> modOptList = Lists.newArrayList();// 操作list
				modOptList.add(moduleMemuOpt);// 加到操作list中
				modOptMap.put(modEnId, modOptList);// 设置map
			}
		}
		// hierarchyModuleVo:{"1" : list, "2" : map, "3" : map, "4" : map}
		hierarchyModuleVo.setFourth(modOptMap);
		
		return hierarchyModuleVo;
	}
	
	public HierarchyModuleVo findUserPrivilegesForAgentLogin(long userId, long roleId, 
			long organId, long organRoleId, List privilegeIdList) {
		
		// 操作id
		List<Long> resourceIdList = new ArrayList<Long>();
		for (Object obj : privilegeIdList) {
			String resourceId = obj.toString();
			resourceIdList.add(Long.parseLong(resourceId));
		}
		List<ModOptRef> modOptRefList = platformServiceFacade.findModOptRefByModOptRefIds(resourceIdList);
		
		// 过滤所有的模块操作
		ModuleOperationVo moduleOperationVo = this.filterModOpt(modOptRefList, false);
		List<ModuleMemu> moduleMemuList = moduleOperationVo.getModuleMenu();
		Map<Long, List<ModuleMemu>> modOptMap = moduleOperationVo.getModuleOperate();
		// 1,2,3级菜单分类
		HierarchyModuleVo hierarchyModuleVo = platformServiceFacade.filterModule(moduleMemuList);
		hierarchyModuleVo.setFourth(modOptMap);
		
		// 行
		// 查找已经分配的权限
		Long[] ownerIds = {userId}; // 拥有者数组
		String[] ownerTypes = OWNER_TYPE_ARR; // 拥有者类型数组
		String[] privilegeTypes = {PRIVILEGE_TYPE_ROW}; // 权限类型数组
		List<Privilege> privilegeRowList = privilegeMapper.findByOwnIdAndPrivilegeType(ownerIds, ownerTypes, privilegeTypes);
		StringBuilder ownerTypeAUserPriBuilder = new StringBuilder();
		String privilegeScopeFlag = "";
		String ownerTypeFlag = "";
		if(privilegeRowList != null) {
			for(Privilege uumPrivilege : privilegeRowList) {
				String ownerType = uumPrivilege.getOwnerType();
				String privilegeScopeRow = uumPrivilege.getPrivilegeScope();
				String resourceId = uumPrivilege.getResourceId();
				if(OWNER_TYPE_ROL.equals(ownerType)) { // 角色只有其一权限
					ownerTypeFlag = OWNER_TYPE_ROL;
					privilegeScopeFlag = privilegeScopeRow;
				} else if(OWNER_TYPE_USR.equals(ownerType)) {
					if(isNullOrEmpty(ownerTypeFlag) || isNullOrEmpty(privilegeScopeFlag) 
							|| !OWNER_TYPE_USR.equals(ownerType)) { // 为空不等ownerTypeUser
						ownerTypeFlag = OWNER_TYPE_USR;
						privilegeScopeFlag = privilegeScopeRow;
					}
					ownerTypeAUserPriBuilder = ownerTypeAUserPriBuilder.length() == 0 
							? ownerTypeAUserPriBuilder.append(resourceId) : ownerTypeAUserPriBuilder.append("," + resourceId);
				}
			}
		}
		
		// 行权限条件sql
		StringBuilder resourceIdBuf = new StringBuilder();
		resourceIdBuf.append(" AND ");
		if (OWNER_TYPE_ROL.equals(ownerTypeFlag)) {
			if (PrivilegeScopeEnum.ROLE.value().equals(privilegeScopeFlag)) {
				resourceIdBuf.append(ROLE_ID + " = " + organRoleId + " "); // 自我真实角色
			} else if (PrivilegeScopeEnum.USER.value().equals(privilegeScopeFlag)) {
				resourceIdBuf.append(USER_ID + " = " + userId + " ");
			} else {
				String organChildren = getPrivilegeOrganChildren(organId);
				resourceIdBuf.append(ORGAN_ID + " in ( " + organChildren + ") ");
			}
		} else if (OWNER_TYPE_USR.equals(ownerTypeFlag)) {
			if (PrivilegeScopeEnum.ROLE.value().equals(privilegeScopeFlag)) {
				resourceIdBuf.append(ROLE_ID + " in (" + ownerTypeAUserPriBuilder + ") ");
			} else if (PrivilegeScopeEnum.USER.value().equals(privilegeScopeFlag)) {
				resourceIdBuf.append(USER_ID + " in (" + ownerTypeAUserPriBuilder + ") ");
			} else {
				resourceIdBuf.append(ORGAN_ID + " in (" + ownerTypeAUserPriBuilder + ") ");
			}
		} else {
			SystemConfigBean systemConfigBean = SystemConfigXmlParse.getInstance().getSystemConfigBean(); // 系统配置权限
			String rowPrivilegeLevel = systemConfigBean.getRowPrivilegeLevel();
			if (PrivilegeScopeEnum.ROLE.value().equals(rowPrivilegeLevel)) {
				resourceIdBuf.append(ROLE_ID + " = " + organRoleId + " "); // 自我真实角色
			} else if (PrivilegeScopeEnum.USER.value().equals(rowPrivilegeLevel)) {
				resourceIdBuf.append(USER_ID + " = " + userId + " ");
			} else {
				String organChildren = getPrivilegeOrganChildren(organId);
				resourceIdBuf.append(ORGAN_ID + " in ( " + organChildren + ") ");
			}
		}
		hierarchyModuleVo.setRow(resourceIdBuf.toString());
		
		return hierarchyModuleVo;
	}

	public HierarchyModuleVo findUserPrivilegesForUserLogin(long userId, long roleId, long organId, long organRoleId) {
		Long[] ownerIds = {userId, roleId, organRoleId}; // 拥有者数组
		String[] ownerTypes = OWNER_TYPE_ARR; // 拥有者类型数组
		String[] privilegeTypes = PRIVILEGE_TYPE_ARR; // 权限类型数组
		// 查找用户相关所有权限
		List<Privilege> privilegeList = privilegeMapper
				.findByOwnIdAndPrivilegeType(ownerIds, ownerTypes, privilegeTypes);
		
		// 1 ====== 用户, 角色权限划分 ======
		List<Privilege> privilegeOptList = Lists.newArrayList();
		List<Privilege> privilegeRowList = Lists.newArrayList();
		List<Privilege> privilegeOptSpecialList = Lists.newArrayList();
		List<Privilege> privilegeRowSpecialList = Lists.newArrayList();
		for(Privilege uumPrivilege : privilegeList) {
			String ownerType = uumPrivilege.getOwnerType();
			String privilegeType = uumPrivilege.getPrivilegeType();
			if(OWNER_TYPE_USR.equals(ownerType)) { // 用户
				if(PRIVILEGE_TYPE_OPT.equals(privilegeType)) {
					privilegeOptSpecialList.add(uumPrivilege);
				} else if(PRIVILEGE_TYPE_ROW.equals(privilegeType)) {
					privilegeRowSpecialList.add(uumPrivilege);
				}
			} else if(OWNER_TYPE_ROL.equals(ownerType)) {
				if(PRIVILEGE_TYPE_OPT.equals(privilegeType)) {
					privilegeOptList.add(uumPrivilege);
				} else if(PRIVILEGE_TYPE_ROW.equals(privilegeType)) {
					privilegeRowList.add(uumPrivilege);
				}
			}
		}
		
		// 权限校验过滤
		if (!privilegeOptSpecialList.isEmpty()) {
			privilegeOptList = privilegeOptSpecialList;
		}
		if (!privilegeRowSpecialList.isEmpty()) {
			privilegeRowList = privilegeRowSpecialList;
		}
		
		// 操作
		List<Long> resourceIdList = new ArrayList<Long>();
		String privilegeScope = "";
		for(Privilege uumPrivilege : privilegeOptList) {
			String resourceId = uumPrivilege.getResourceId();
			resourceIdList.add(Long.parseLong(resourceId));
			if("".equals(privilegeScope)) {
				privilegeScope = uumPrivilege.getPrivilegeScope();
			}
		}
		List<ModOptRef> modOptRefList = platformServiceFacade.findModOptRefByModOptRefIds(resourceIdList);
		if (PrivilegeScopeEnum.EXCLUDE.value().equals(privilegeScope)) {
			List<ModOptRef> allModOptRefList = platformServiceFacade.findAllModOptRefs(); // 所有操作按钮
			allModOptRefList.removeAll(modOptRefList);
			modOptRefList = allModOptRefList;
		} else if (PrivilegeScopeEnum.ALL.value().equals(privilegeScope)) {
			List<ModOptRef> allModOptRefList = platformServiceFacade.findAllModOptRefs(); // 所有操作按钮
			modOptRefList = allModOptRefList;
		}
		
		// 过滤所有的模块操作
		ModuleOperationVo moduleOperationVo = this.filterModOpt(modOptRefList, false);
		List<ModuleMemu> moduleMemuList = moduleOperationVo.getModuleMenu();
		Map<Long, List<ModuleMemu>> modOptMap = moduleOperationVo.getModuleOperate();
		
		// 1,2,3 级菜单分类
		HierarchyModuleVo hierarchyModuleVo = platformServiceFacade.filterModule(moduleMemuList);
		hierarchyModuleVo.setFourth(modOptMap);
		
		// 2 ====== 行权限 ======
		StringBuilder ownerTypeAUserPriBulider = new StringBuilder();
		String privilegeScopeFlag = "";
		String ownerTypeFlag = "";
		for(Privilege uumPrivilege : privilegeRowList) { 
			String ownerType = uumPrivilege.getOwnerType();
			String privilegeScopeRow = uumPrivilege.getPrivilegeScope();
			String resourceId = uumPrivilege.getResourceId();
			if(OWNER_TYPE_ROL.equals(ownerType)) { // 角色只有其一权限
				ownerTypeFlag = OWNER_TYPE_ROL;
				privilegeScopeFlag = privilegeScopeRow;
				break;
			} else if(OWNER_TYPE_USR.equals(ownerType)) {
				if(isNullOrEmpty(ownerTypeFlag) || isNullOrEmpty(privilegeScopeFlag) 
						|| !OWNER_TYPE_USR.equals(ownerType)) { // 为空不等ownerTypeUser
					ownerTypeFlag = OWNER_TYPE_USR;
					privilegeScopeFlag = privilegeScopeRow;
				}
				ownerTypeAUserPriBulider = ownerTypeAUserPriBulider.length() == 0 
						? ownerTypeAUserPriBulider.append(resourceId) : ownerTypeAUserPriBulider.append("," + resourceId);
			}
		}
		// 行权限条件sql
		StringBuilder resourceIdBuf = new StringBuilder();
		resourceIdBuf.append(" AND ");
		if(OWNER_TYPE_ROL.equals(ownerTypeFlag)) {
			if(PrivilegeScopeEnum.ROLE.value().equals(privilegeScopeFlag)) {
				resourceIdBuf.append(ROLE_ID + " = " + organRoleId + " "); // 自我真实角色
			} else if(PrivilegeScopeEnum.USER.value().equals(privilegeScopeFlag)) {
				resourceIdBuf.append(USER_ID + " = " + userId + " ");
			} else {
				String organChildren = getPrivilegeOrganChildren(organId);
				resourceIdBuf.append(ORGAN_ID + " in ( " + organChildren + ") ");
			}
		} else if(OWNER_TYPE_USR.equals(ownerTypeFlag)) {
			if (PrivilegeScopeEnum.ROLE.value().equals(privilegeScopeFlag)) {
				resourceIdBuf.append(ROLE_ID + " in (" + ownerTypeAUserPriBulider + ") "); 
			} else if (PrivilegeScopeEnum.USER.value().equals(privilegeScopeFlag)) {
				resourceIdBuf.append(USER_ID + " in (" + ownerTypeAUserPriBulider + ") "); 
			} else {
				resourceIdBuf.append(ORGAN_ID + " in (" + ownerTypeAUserPriBulider + ") "); 
			}
		} else {
			SystemConfigBean systemConfigBean = SystemConfigXmlParse.getInstance().getSystemConfigBean(); // 系统配置权限
			String rowPrivilegeLevel = systemConfigBean.getRowPrivilegeLevel();
			if (PrivilegeScopeEnum.ROLE.value().equals(rowPrivilegeLevel)) {
				resourceIdBuf.append(ROLE_ID + " = " + organRoleId + " "); // 自我真实角色
			} else if (PrivilegeScopeEnum.USER.value().equals(rowPrivilegeLevel)) {
				resourceIdBuf.append(USER_ID + " = " + userId + " ");
			} else {
				String organChildren = getPrivilegeOrganChildren(organId);
				resourceIdBuf.append(ORGAN_ID + " in ( " + organChildren + ") ");
			}
		}
		
		hierarchyModuleVo.setRow(resourceIdBuf.toString());
		
		return hierarchyModuleVo;
	}

	@Transactional(readOnly=false)
	public void save(List<String> modOptIds, long ownerId, String privilegeType, 
			String ownerType, String privilegeScope) throws ApplicationException {

		// 删除角色或用戶拥有操作权限
		delete(ownerId, ownerType, privilegeType); 
		// 删除角色或用戶代理权限
		userAgentService.deleteUserAgentsByOwnerId(ownerId, ownerType);
		
		StringBuilder optContent = new StringBuilder();
		optContent.append("模块操作关系ID: ");
		if(PrivilegeScopeEnum.ALL.value().equals(privilegeScope)) {
			Privilege privilege = new Privilege();
			privilege.setResourceId(PrivilegeScopeEnum.ALL.value());
			privilege.setOwnerId(ownerId);
			privilege.setOwnerType(ownerType);
			privilege.setPrivilegeType(privilegeType);
			privilege.setPrivilegeScope(privilegeScope);
			privilegeMapper.save(privilege);
		} else {
			if(modOptIds != null) {
				List<Privilege> uumPrivilegeList = new ArrayList<Privilege>();
				for (String modOptId : modOptIds) {
					if (isNullOrEmpty(modOptId)) continue;
					Privilege privilege = new Privilege();
					privilege.setResourceId(modOptId);
					privilege.setOwnerId(ownerId);
					privilege.setOwnerType(ownerType);
					privilege.setPrivilegeType(privilegeType);
					privilege.setPrivilegeScope(privilegeScope);
					privilegeMapper.save(privilege);
					
					optContent.append(modOptId + SPLIT);
				}
				
				sysOptLogService.saveLog(LogLevelEnum.FIRST, OptNameEnum.SAVE, MOD_ASSIGN_OPT, 
						optContent.toString(), ownerId+SPLIT+privilegeType);
			}
		}
	}
	
	public List<TreeVoExtend> convertToTree(List<ModuleMemu> moduleMemuList) {
		List<TreeVoExtend> treeList = new ArrayList<TreeVoExtend>();
		for(ModuleMemu moduleMemu : moduleMemuList) {
			TreeVoExtend tree = new TreeVoExtend();
			tree.setId(moduleMemu.getModuleId().toString());
			tree.setParentId(moduleMemu.getUpModId().toString());
			tree.setText(moduleMemu.getModName());
			tree.setIconSkin(moduleMemu.getModImgCls());
			if (!moduleMemu.getIsLeaf()) {
				// 模块菜单级别为3是叶子
				tree.setIsParent(true);
//				tree.setNocheck(true);
			}
			treeList.add(tree);
		}
		
		return treeList;
	}

	
	
	// --------------------------- 私有方法 -------------------------------
	/**
	 * 查询用户的机构上级
	 *
	 * @param organId
	 * @return
	 * @author rutine
	 * @time Oct 21, 2012 4:37:53 PM
	 */
	private String getPrivilegeOrganChildren(long organId) {
		List<Organ> organList = organService.findChildNodes(organId, 0);
		StringBuffer orgSb = new StringBuffer();
		for (Organ uumOrgan : organList) {
			if (orgSb.length() > 0) {
			orgSb.append(",").append(uumOrgan.getOrgId());
			} else {
			orgSb.append(uumOrgan.getOrgId());
			}
		}
		
		return orgSb.toString();
	}
}
