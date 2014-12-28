package com.mycuckoo.service.impl.uum;

import static com.mycuckoo.common.constant.Common.FOURTH;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.ORGAN_ID;
import static com.mycuckoo.common.constant.Common.OWNER_TYPE_ARR;
import static com.mycuckoo.common.constant.Common.OWNER_TYPE_ROL;
import static com.mycuckoo.common.constant.Common.OWNER_TYPE_USR;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_SCOPE;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_SCOPE_ALL;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_SCOPE_EXC;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_SCOPE_ORG;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_SCOPE_ROL;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_SCOPE_USR;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_TYPE_ARR;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_TYPE_OPT;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_TYPE_ROW;
import static com.mycuckoo.common.constant.Common.ROLE_ID;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.common.utils.SystemConfigXmlParse;
import com.mycuckoo.domain.platform.SysplModOptRef;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.domain.platform.SysplOperate;
import com.mycuckoo.domain.uum.UumOrgRoleRef;
import com.mycuckoo.domain.uum.UumOrgan;
import com.mycuckoo.domain.uum.UumPrivilege;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.domain.vo.SystemConfigBean;
import com.mycuckoo.domain.vo.TreeVoExtend;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.uum.UumPrivilegeRepository;
import com.mycuckoo.service.iface.commonused.PlatformCommonService;
import com.mycuckoo.service.iface.platform.SystemOptLogService;
import com.mycuckoo.service.iface.uum.OrganService;
import com.mycuckoo.service.iface.uum.PrivilegeService;
import com.mycuckoo.service.iface.uum.RoleOrganService;
import com.mycuckoo.service.iface.uum.UserAgentService;
import com.mycuckoo.service.iface.uum.UserService;

/**
 * 功能说明: 权限业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:21:42 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class PrivilegeServiceImpl implements PrivilegeService {
	
	static Logger logger = LoggerFactory.getLogger(PrivilegeServiceImpl.class);
	private UumPrivilegeRepository privilegeRepository;
	private UserService userService;
	private UserAgentService userAgentService;
	private OrganService organService;
	private RoleOrganService roleOrganService;
	private SystemOptLogService sysOptLogService;
	private PlatformCommonService platformCommonService;

	@Override
	public void deletePrivilege(long ownerId, String ownerType, String privilegeType, 
			HttpServletRequest request) throws ApplicationException {
		
		privilegeRepository.deletePrivilegeByOwnerIdAPriType(ownerId, ownerType, privilegeType);
	}

	@Override
	public void deletePrivilegeByModOptId(String[] modOptRefIds, HttpServletRequest request) {
		if(modOptRefIds == null || modOptRefIds.length == 0) return;
		privilegeRepository.deletePrivilegeByModOptId(modOptRefIds);
	}

	@Override
	public void deletePrivilegeByOwnerIdAType(long ownerId, String ownerType, 
			HttpServletRequest request) throws ApplicationException {
		
		privilegeRepository.deletePrivilegeByOwnerIdAType(ownerId, ownerType);
	}

	@Override
	public void deleteRowPriByResourceId(String resourceId, HttpServletRequest request) 
			throws ApplicationException {
		
		privilegeRepository.deleteRowPrivilegeByOrgId(resourceId);
	}

	@Override
	public List<UumUser> findUsersByUserName(String userName) {
		return userService.findUsersByUserName(userName);
	}

	@Override
	public Map filterModOpt(List<SysplModOptRef> sysplModOptRefList, boolean isTreeFlag) {
		Map<Long, List<SysplModuleMemu>> sysplModOptMap = Maps.newHashMap(); // 四级模块操作
		List<SysplModuleMemu> sysplModuleMemuList = Lists.newArrayList(); // 模块菜单list
		
		for(SysplModOptRef sysplModOptRef : sysplModOptRefList) {
			SysplModuleMemu sysplModuleMemu3 = sysplModOptRef.getSysplModuleMemu(); // 第三级菜单
			SysplOperate sysplOperate = sysplModOptRef.getSysplOperate();
			// 操作按钮
			SysplModuleMemu sysplModuleMemuOpt = new SysplModuleMemu();
			sysplModuleMemuOpt.setModuleId(sysplModOptRef.getModOptId() + 1000); // 将模块操作关系的id加上1000,防id重复
			sysplModuleMemuOpt.setModName(sysplOperate.getOperateName());
			sysplModuleMemuOpt.setModImgCls(sysplOperate.getOptImgLink());
			sysplModuleMemuOpt.setOptFunLink(sysplOperate.getOptFunLink()); // 为操作准备功能链接
			sysplModuleMemuOpt.setModOrder(sysplOperate.getOptOrder()); // 操作按钮的顺序
			sysplModuleMemuOpt.setUpModId(sysplModuleMemu3.getModuleId()); // 将第三级菜单设置为操作
			sysplModuleMemuOpt.setIsLeaf(true);
			if(isTreeFlag) { // 如果为树则加入模块list
				sysplModuleMemuList.add(sysplModuleMemuOpt);
			} else { // 不为树则维护操作按钮map
				Long modEnId = sysplModuleMemu3.getModuleId(); // 模块英文id, 作为操作列表map的key值
				if(sysplModOptMap.containsKey(modEnId)) {
					List<SysplModuleMemu> modOptList = sysplModOptMap.get(modEnId);
					// 根据操作顺序进行排序
					int listIndex = 0; // 元素索引
					boolean orderBol = true; //是否插入指定索引元素
					for(SysplModuleMemu sysplModuleMemuOptt : modOptList) {
						int listModOrder = sysplModuleMemuOptt.getModOrder(); // 已经有的操作顺序
						int currModOrder = sysplModuleMemuOpt.getModOrder(); // 当前操作顺序
						if(listModOrder > currModOrder) {
							modOptList.add(listIndex, sysplModuleMemuOpt); // 顺序小的插在前
							orderBol = false;
							break;
						}
						listIndex++;
					}
					if(orderBol) {
						modOptList.add(sysplModuleMemuOpt); // 加到操作list中
					}
				} else {
					List<SysplModuleMemu> modOptList = new ArrayList<SysplModuleMemu>(); // 操作list
					modOptList.add(sysplModuleMemuOpt); // 加到操作list中
					sysplModOptMap.put(modEnId, modOptList);
				}
			}
			
			// 如果包含相应菜单项则加入相应模块菜单
			if(!sysplModuleMemuList.contains(sysplModuleMemu3)) {
				sysplModuleMemuList.add(sysplModuleMemu3);
				SysplModuleMemu sysplModuleMemu2 = sysplModuleMemu3.getSysplModuleMemu(); // 第二级菜单
				sysplModuleMemu3.setUpModId(sysplModuleMemu2.getModuleId()); // 设置上级菜单
				if(!sysplModuleMemuList.contains(sysplModuleMemu2)) {
					sysplModuleMemuList.add(sysplModuleMemu2);
					SysplModuleMemu sysplModuleMemu1 = sysplModuleMemu2.getSysplModuleMemu();
					sysplModuleMemu2.setUpModId(sysplModuleMemu1.getModuleId());
					if(!sysplModuleMemuList.contains(sysplModuleMemu1)) {
						sysplModuleMemuList.add(sysplModuleMemu1);
						sysplModuleMemu1.setUpModId(sysplModuleMemu1.getSysplModuleMemu().getModuleId());
					}
				}
			}
		}
		
		Collections.sort(sysplModuleMemuList, new SysplModuleMemu());
		Map moduleOptMap = new HashMap();
		moduleOptMap.put("moduleMenu", sysplModuleMemuList);
		moduleOptMap.put("moduleOperate", sysplModOptMap);
		
		return moduleOptMap;
	}
	

	@Override
	public String findRowPrivilegeByRoleIdAPriType(long roleId) {
		// 查找已经分配的权限
		Long[] roleIds = new Long[] { roleId };
		String[] ownerTypes = new String[] { OWNER_TYPE_ROL };
		String[] privilegeTypes = new String[] { PRIVILEGE_TYPE_ROW };
		List<UumPrivilege> privilegeOptList = privilegeRepository.findPrivilegesByOwnIdAPriType(roleIds, ownerTypes, privilegeTypes);
		
		if(privilegeOptList.isEmpty()) return PRIVILEGE_SCOPE_ORG;
		UumPrivilege uumPrivilege = privilegeOptList.get(0);
		String resourceId = uumPrivilege.getResourceId();
		
		return resourceId;
	}

	@Override
	public Map findSelectAUnselectModOptByOwnIdAOwnType(long ownerId, String ownerType) {
		// 查找已经分配的权限
		Long[] roleIds = new Long[] { ownerId };
		String[] ownerTypes = new String[] { ownerType };
		String[] privilegeTypes = new String[] { PRIVILEGE_TYPE_OPT };
		List<UumPrivilege> privilegeOptList = privilegeRepository.findPrivilegesByOwnIdAPriType(roleIds, ownerTypes, privilegeTypes);
		// 操作id集
		List<Long> resourceIdList = new ArrayList<Long>();
		String privilegeScope = "";
		for(UumPrivilege uumPrivilege : privilegeOptList) {
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
		List<SysplModOptRef> assignedModOptList = platformCommonService.findModOptRefByModOptRefIds(resourceIdList);
		// 查找所有模块操作关系
		List<SysplModOptRef> allModOptList = platformCommonService.findAllModOptRefs();
		allModOptList.removeAll(assignedModOptList);
		// 未分配的权限
		List<SysplModOptRef> unassignedModOptList = allModOptList;
		//将操作转化成列表数据
		List<SysplModuleMemu> assignedModMenuList = (List<SysplModuleMemu>) this.filterModOpt(assignedModOptList, true).get("moduleMenu");
		List<SysplModuleMemu>  unassignedModMenuList = (List<SysplModuleMemu>) this.filterModOpt(unassignedModOptList, true).get("moduleMenu");
		//将已分配和未分配的模块操作放入map
		Map moduleMemuMap = new HashMap();
		moduleMemuMap.put("assignedModOpts", convertToTree(assignedModMenuList));
		moduleMemuMap.put("unassignedModOpts", convertToTree(unassignedModMenuList));
		moduleMemuMap.put(PRIVILEGE_SCOPE, privilegeScope);
		
		return moduleMemuMap;
	}

	@Override
	public Map findSelectRowPrivilegeByUserId(long userId) {
		// 查找已经分配的权限
		Long[] userIds = new Long[] { userId };
		String[] ownerTypes = new String[] { OWNER_TYPE_USR };
		String[] privilegeTypes = new String[] { PRIVILEGE_TYPE_ROW };
		List<UumPrivilege> privilegeOptList = privilegeRepository.findPrivilegesByOwnIdAPriType(userIds, ownerTypes, privilegeTypes);
		List<Long> resourceIdList = new ArrayList<Long>();
		String privilegeScope = "";
		for(UumPrivilege uumPrivilege : privilegeOptList) {
			String resourceId = uumPrivilege.getResourceId();
			resourceIdList.add(Long.parseLong(resourceId));
			if("".endsWith(privilegeScope)) {
				privilegeScope = uumPrivilege.getPrivilegeScope();
			}
		}
		
		Long[] resourceIdArray = resourceIdList.toArray(new Long[resourceIdList.size()]);
		
		List userInfoList = new ArrayList();
		if(PRIVILEGE_SCOPE_USR.equals(privilegeScope)) {
			List userList = userService.findUsersByUserIds(resourceIdArray);
			if(userList != null) {
				userInfoList = userList;
			}
		} else if(PRIVILEGE_SCOPE_ROL.equals(privilegeScope)) {
			List<UumOrgRoleRef> orgRoleRefList = roleOrganService.findOrgRoleRefByOrgRoleIds(resourceIdArray);
			if(orgRoleRefList != null) {
				for(UumOrgRoleRef uumOrgRoleRef : orgRoleRefList) {
					long orgRoleId = uumOrgRoleRef.getOrgRoleId();
					String orgName = uumOrgRoleRef.getUumOrgan().getOrgSimpleName();
					String roleName = uumOrgRoleRef.getUumRole().getRoleName();
					Object[] orgObj = new Object[2];
					orgObj[0] = orgRoleId;
					orgObj[1] = orgName + "-" + roleName;
					userInfoList.add(orgObj);
				}
			}
		} else {
			List organList = organService.findOrgansByOrgIds(resourceIdArray);
			if(organList != null) {
				userInfoList = organList;
			}
		}
		
		Map map = new HashMap();
		map.put(USER, userInfoList);
		map.put(PRIVILEGE_SCOPE, privilegeScope);
		
		return map;
	}

	@Override
	public boolean existsSpecialPrivilegeByUserId(long userId) {
		int count = privilegeRepository.countSpecialPrivilegesByUserId(userId);
		if(count > 0) return true;
		
		return false;
	}

	@Override
	public Map findUserPrivilegesForAdminLogin() {
		List<SysplModuleMemu> allSysplModuleMemus = platformCommonService.findAllModules();// 所有模块菜单
		List<SysplModOptRef> sysplModOptRefList = platformCommonService.findAllModOptRefs(); // 所有操作按钮
		Map moduleMenuMap = platformCommonService.filterModuleMenu(allSysplModuleMemus); // 过滤模块 
		
		Map<Long, List<SysplModuleMemu>> sysplModOptMap = Maps.newHashMap(); // 四级模块操作
		for (SysplModOptRef sysplModOptRef : sysplModOptRefList) {
			SysplModuleMemu sysplModuleMemu = sysplModOptRef.getSysplModuleMemu(); // 所属模块
			SysplOperate sysplOperate = sysplModOptRef.getSysplOperate(); // 具体操作
			// 操作按钮
			SysplModuleMemu sysplModuleMemuOpt = new SysplModuleMemu();
			sysplModuleMemuOpt.setModuleId(sysplModOptRef.getModOptId() + 1000); // 将模块操作关系的id加上1000,防id重复
			sysplModuleMemuOpt.setModName(sysplOperate.getOperateName());
			sysplModuleMemuOpt.setModImgCls(sysplOperate.getOptImgLink());
			sysplModuleMemuOpt.setOptFunLink(sysplOperate.getOptFunLink()); // 为操作准备功能链接
			sysplModuleMemuOpt.setModOrder(sysplOperate.getOptOrder()); // 操作按钮的顺序
			sysplModuleMemuOpt.setUpModId(sysplModuleMemu.getModuleId()); // 将第三级菜单设置为操作
			sysplModuleMemuOpt.setIsLeaf(true);
			
			Long modEnId = sysplModuleMemu.getModuleId(); // 模块英文id,作为操作列表map的key值
			if (sysplModOptMap.containsKey(modEnId)) {
				List<SysplModuleMemu> modOptList = sysplModOptMap.get(modEnId);
				// 根据操作顺序进行排序
				int listIndex = 0; // 元素索引
				boolean orderBol = true; // 是否插入指定索引元素
				for (SysplModuleMemu sysplModuleMemuOptt : modOptList) {
					int listModOrder = sysplModuleMemuOptt.getModOrder(); // 已经有的操作顺序
					int currModOrder = sysplModuleMemuOpt.getModOrder(); // 当前操作顺序
					if (listModOrder > currModOrder) {
						modOptList.add(listIndex, sysplModuleMemuOpt); // 顺序小的插在前
						orderBol = false;
						break;
					}
					listIndex++;
				}
				if (orderBol) {
					modOptList.add(sysplModuleMemuOpt);// 加到操作list中
				}
			} else {
				List<SysplModuleMemu> modOptList = Lists.newArrayList();// 操作list
				modOptList.add(sysplModuleMemuOpt);// 加到操作list中
				sysplModOptMap.put(modEnId, modOptList);// 设置map
			}
		}
		// modMenuMap:{"1" : list, "2" : map, "3" : map, "4" : map}
		moduleMenuMap.put(FOURTH, sysplModOptMap);
		
		return moduleMenuMap;
	}
	
	@Override
	public Map findUserPrivilegesForAgentLogin(long userId, long roleId, long organId, long organRoleId, List privilegeIdList) {
		// 操作id
		List<Long> resourceIdList = new ArrayList<Long>();
		for (Object obj : privilegeIdList) {
			String resourceId = obj.toString();
			resourceIdList.add(Long.parseLong(resourceId));
		}
		List<SysplModOptRef> modOptRefList = platformCommonService.findModOptRefByModOptRefIds(resourceIdList);
		
		// 过滤所有的模块操作
		Map map = this.filterModOpt(modOptRefList, false);
		List<SysplModuleMemu> moduleMemuList = (List<SysplModuleMemu>) map.get("moduleMenu");
		Map<String, List<SysplModuleMemu>> modOptMap = (Map<String, List<SysplModuleMemu>>) map.get("moduleOperate");
		// 1,2,3级菜单分类
		Map moduleMenuMap = platformCommonService.filterModuleMenu(moduleMemuList);
		moduleMenuMap.put(FOURTH, modOptMap);
		
		// 行
		// 查找已经分配的权限
		Long[] ownerIds = {userId}; // 拥有者数组
		String[] ownerTypes = OWNER_TYPE_ARR; // 拥有者类型数组
		String[] privilegeTypes = {PRIVILEGE_TYPE_ROW}; // 权限类型数组
		List<UumPrivilege> privilegeRowList = privilegeRepository.findPrivilegesByOwnIdAPriType(ownerIds, ownerTypes, privilegeTypes);
		StringBuilder ownerTypeAUserPriBuilder = new StringBuilder();
		String privilegeScopeFlag = "";
		String ownerTypeFlag = "";
		if(privilegeRowList != null) {
			for(UumPrivilege uumPrivilege : privilegeRowList) {
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
			if (PRIVILEGE_SCOPE_ROL.equals(privilegeScopeFlag)) {
				resourceIdBuf.append(ROLE_ID + " = " + organRoleId + " "); // 自我真实角色
			} else if (PRIVILEGE_SCOPE_USR.equals(privilegeScopeFlag)) {
				resourceIdBuf.append(USER_ID + " = " + userId + " ");
			} else {
				String organChildren = getPrivilegeOrganChildren(organId);
				resourceIdBuf.append(ORGAN_ID + " in ( " + organChildren + ") ");
			}
		} else if (OWNER_TYPE_USR.equals(ownerTypeFlag)) {
			if (PRIVILEGE_SCOPE_ROL.equals(privilegeScopeFlag)) {
				resourceIdBuf.append(ROLE_ID + " in (" + ownerTypeAUserPriBuilder + ") ");
			} else if (PRIVILEGE_SCOPE_USR.equals(privilegeScopeFlag)) {
				resourceIdBuf.append(USER_ID + " in (" + ownerTypeAUserPriBuilder + ") ");
			} else {
				resourceIdBuf.append(ORGAN_ID + " in (" + ownerTypeAUserPriBuilder + ") ");
			}
		} else {
			SystemConfigBean systemConfigBean = SystemConfigXmlParse.getSystemConfigBean(); // 系统配置权限
			String rowPrivilegeLevel = systemConfigBean.getRowPrivilegeLevel();
			if (PRIVILEGE_SCOPE_ROL.equals(rowPrivilegeLevel)) {
				resourceIdBuf.append(ROLE_ID + " = " + organRoleId + " "); // 自我真实角色
			} else if (PRIVILEGE_SCOPE_USR.equals(rowPrivilegeLevel)) {
				resourceIdBuf.append(USER_ID + " = " + userId + " ");
			} else {
				String organChildren = getPrivilegeOrganChildren(organId);
				resourceIdBuf.append(ORGAN_ID + " in ( " + organChildren + ") ");
			}
		}
	
		moduleMenuMap.put(PRIVILEGE_TYPE_ROW, resourceIdBuf.toString());
		
		return moduleMenuMap;
	}

	@Override
	public Map findUserPrivilegesForUserLogin(long userId, long roleId, long organId, long organRoleId) {
		Long[] ownerIds = {userId, roleId, organRoleId}; // 拥有者数组
		String[] ownerTypes = OWNER_TYPE_ARR; // 拥有者类型数组
		String[] privilegeTypes = PRIVILEGE_TYPE_ARR; // 权限类型数组
		// 查找用户相关所有权限
		List<UumPrivilege> privilegeList = privilegeRepository
				.findPrivilegesByOwnIdAPriType(ownerIds, ownerTypes, privilegeTypes);
		
		// 1 ====== 用户, 角色权限划分 ======
		List<UumPrivilege> privilegeOptList = Lists.newArrayList();
		List<UumPrivilege> privilegeRowList = Lists.newArrayList();
		List<UumPrivilege> privilegeOptSpecialList = Lists.newArrayList();
		List<UumPrivilege> privilegeRowSpecialList = Lists.newArrayList();
		for(UumPrivilege uumPrivilege : privilegeList) {
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
		for(UumPrivilege uumPrivilege : privilegeOptList) {
			String resourceId = uumPrivilege.getResourceId();
			resourceIdList.add(Long.parseLong(resourceId));
			if("".equals(privilegeScope)) {
				privilegeScope = uumPrivilege.getPrivilegeScope();
			}
		}
		List<SysplModOptRef> modOptRefList = platformCommonService.findModOptRefByModOptRefIds(resourceIdList);
		if (PRIVILEGE_SCOPE_EXC.equals(privilegeScope)) {
			List<SysplModOptRef> allModOptRefList = platformCommonService.findAllModOptRefs(); // 所有操作按钮
			allModOptRefList.removeAll(modOptRefList);
			modOptRefList = allModOptRefList;
		} else if (PRIVILEGE_SCOPE_ALL.equals(privilegeScope)) {
			List<SysplModOptRef> allModOptRefList = platformCommonService.findAllModOptRefs(); // 所有操作按钮
			modOptRefList = allModOptRefList;
		}
		
		// 过滤所有的模块操作
		Map map = this.filterModOpt(modOptRefList, false);
		List<SysplModuleMemu> moduleMemuList = (List<SysplModuleMemu>) map.get("moduleMenu");
		Map<String, List<SysplModuleMemu>> modOptMap = (Map<String, List<SysplModuleMemu>>) map.get("moduleOperate");
		
		// 1,2,3 级菜单分类
		Map moduleMenuMap = platformCommonService.filterModuleMenu(moduleMemuList);
		moduleMenuMap.put(FOURTH, modOptMap);
		
		// 2 ====== 行权限 ======
		StringBuilder ownerTypeAUserPriBulider = new StringBuilder();
		String privilegeScopeFlag = "";
		String ownerTypeFlag = "";
		for(UumPrivilege uumPrivilege : privilegeRowList) { 
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
			if(PRIVILEGE_SCOPE_ROL.equals(privilegeScopeFlag)) {
				resourceIdBuf.append(ROLE_ID + " = " + organRoleId + " "); // 自我真实角色
			} else if(PRIVILEGE_SCOPE_USR.equals(privilegeScopeFlag)) {
				resourceIdBuf.append(USER_ID + " = " + userId + " ");
			} else {
				String organChildren = getPrivilegeOrganChildren(organId);
				resourceIdBuf.append(ORGAN_ID + " in ( " + organChildren + ") ");
			}
		} else if(OWNER_TYPE_USR.equals(ownerTypeFlag)) {
			if (PRIVILEGE_SCOPE_ROL.equals(privilegeScopeFlag)) {
				resourceIdBuf.append(ROLE_ID + " in (" + ownerTypeAUserPriBulider + ") "); 
			} else if (PRIVILEGE_SCOPE_USR.equals(privilegeScopeFlag)) {
				resourceIdBuf.append(USER_ID + " in (" + ownerTypeAUserPriBulider + ") "); 
			} else {
				resourceIdBuf.append(ORGAN_ID + " in (" + ownerTypeAUserPriBulider + ") "); 
			}
		} else {
			SystemConfigBean systemConfigBean = SystemConfigXmlParse.getSystemConfigBean(); // 系统配置权限
			String rowPrivilegeLevel = systemConfigBean.getRowPrivilegeLevel();
			if (PRIVILEGE_SCOPE_ROL.equals(rowPrivilegeLevel)) {
				resourceIdBuf.append(ROLE_ID + " = " + organRoleId + " "); // 自我真实角色
			} else if (PRIVILEGE_SCOPE_USR.equals(rowPrivilegeLevel)) {
				resourceIdBuf.append(USER_ID + " = " + userId + " ");
			} else {
				String organChildren = getPrivilegeOrganChildren(organId);
				resourceIdBuf.append(ORGAN_ID + " in ( " + organChildren + ") ");
			}
		}
		
		moduleMenuMap.put(PRIVILEGE_TYPE_ROW, resourceIdBuf.toString());
		
		return moduleMenuMap;
	}

	@Transactional(readOnly=false)
	@Override
	public void savePrivilege(List<String> modOptIds, long ownerId,
			String privilegeType, String ownerType, String privilegeScope,
				HttpServletRequest request) throws ApplicationException {

		// 删除角色或用戶拥有操作权限
		deletePrivilege(ownerId, ownerType, privilegeType, request); 
		// 删除角色或用戶代理权限
		userAgentService.deleteUserAgentsByOwnerId(ownerId, ownerType, request);
		
		StringBuilder optContent = new StringBuilder();
		optContent.append("模块操作关系ID: ");
		if(PRIVILEGE_SCOPE_ALL.equals(privilegeScope)) {
			UumPrivilege uumPrivilege = new UumPrivilege();
			uumPrivilege.setResourceId(PRIVILEGE_SCOPE_ALL);
			uumPrivilege.setOwnerId(ownerId);
			uumPrivilege.setOwnerType(ownerType);
			uumPrivilege.setPrivilegeType(privilegeType);
			uumPrivilege.setPrivilegeScope(privilegeScope);
			
			privilegeRepository.save(uumPrivilege);
		} else {
			if(modOptIds != null) {
				List<UumPrivilege> uumPrivilegeList = new ArrayList<UumPrivilege>();
				for (String modOptId : modOptIds) {
					if (isNullOrEmpty(modOptId)) continue;
					UumPrivilege uumPrivilege = new UumPrivilege();
					uumPrivilege.setResourceId(modOptId);
					uumPrivilege.setOwnerId(ownerId);
					uumPrivilege.setOwnerType(ownerType);
					uumPrivilege.setPrivilegeType(privilegeType);
					uumPrivilege.setPrivilegeScope(privilegeScope);
					
					uumPrivilegeList.add(uumPrivilege);
					optContent.append(modOptId + SPLIT);
				}
				
				privilegeRepository.save(uumPrivilegeList);
				sysOptLogService.saveLog(LOG_LEVEL_FIRST, MOD_ASSIGN_OPT, SAVE_OPT, optContent.toString(), 
						ownerId+SPLIT+privilegeType, request);
			}
		}
	}
	
	@Override
	public List<TreeVoExtend> convertToTree(List<SysplModuleMemu> moduleMemuList) {
		List<TreeVoExtend> treeList = new ArrayList<TreeVoExtend>();
		for(SysplModuleMemu moduleMemu : moduleMemuList) {
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
		List<UumOrgan> organList = organService.findChildNodes(organId, 0);
		StringBuffer orgSb = new StringBuffer();
		for (UumOrgan uumOrgan : organList) {
			if (orgSb.length() > 0) {
			orgSb.append(",").append(uumOrgan.getOrgId());
			} else {
			orgSb.append(uumOrgan.getOrgId());
			}
		}
		
		return orgSb.toString();
	}
	
	
	
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setPrivilegeRepository(UumPrivilegeRepository privilegeRepository) {
		this.privilegeRepository = privilegeRepository;
	}
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setUserAgentService(UserAgentService userAgentService) {
		this.userAgentService = userAgentService;
	}
	@Autowired
	public void setOrganService(OrganService organService) {
		this.organService = organService;
	}
	@Autowired
	public void setRoleOrganService(RoleOrganService roleOrganService) {
		this.roleOrganService = roleOrganService;
	}
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}
	@Autowired
	public void setPlatformCommonService(PlatformCommonService platformCommonService) {
		this.platformCommonService = platformCommonService;
	}
	
}
