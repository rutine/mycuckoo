package com.mycuckoo.service.uum;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.common.constant.OwnerType;
import com.mycuckoo.common.constant.PrivilegeScope;
import com.mycuckoo.common.constant.PrivilegeType;
import com.mycuckoo.common.utils.CommonUtils;
import com.mycuckoo.common.utils.SystemConfigXmlParse;
import com.mycuckoo.domain.platform.ModOptRef;
import com.mycuckoo.domain.platform.ModuleMenu;
import com.mycuckoo.domain.platform.Operate;
import com.mycuckoo.domain.uum.OrgRoleRef;
import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.domain.uum.Privilege;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.repository.uum.PrivilegeMapper;
import com.mycuckoo.service.facade.PlatformServiceFacade;
import com.mycuckoo.service.platform.SystemOptLogService;
import com.mycuckoo.vo.AssignVo;
import com.mycuckoo.vo.HierarchyModuleVo;
import com.mycuckoo.vo.ModuleOperationVo;
import com.mycuckoo.vo.SystemConfigBean;
import com.mycuckoo.vo.TreeVoExtend;
import com.mycuckoo.vo.platform.ModuleMenuVo;
import com.mycuckoo.vo.uum.UserRowPrivilegeVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mycuckoo.common.constant.Common.*;
import static com.mycuckoo.common.constant.ServiceVariable.MOD_ASSIGN_OPT;

/**
 * 功能说明: 权限业务类
 *
 * @author rutine
 * @version 4.0.0
 * @time Sep 25, 2014 11:21:42 AM
 */
@Service
@Transactional(readOnly = true)
public class PrivilegeService {
    static Logger logger = LoggerFactory.getLogger(PrivilegeService.class);

    @Autowired
    private PrivilegeMapper privilegeMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private OrganService organService;
    @Autowired
    private OrganRoleService organRoleService;
    @Autowired
    private SystemOptLogService sysOptLogService;
    @Autowired
    private PlatformServiceFacade platformServiceFacade;


    public void delete(long ownerId, String ownerType, String privilegeType) {
        privilegeMapper.deleteByOwnerIdAndPrivilegeType(ownerId, ownerType, privilegeType);
    }

    public void deletePrivilegeByModOptId(String[] modOptRefIds) {
        if (modOptRefIds == null || modOptRefIds.length == 0) return;

        privilegeMapper.deleteByModOptId(modOptRefIds, PrivilegeType.OPT.value());
    }

    public void deleteByOwnerIdAndOwnerType(long ownerId, String ownerType) {
        privilegeMapper.deleteByOwnerIdAndOwnerType(ownerId, ownerType);
    }

    public void deleteRowPrivilegeByOrgId(String orgId) {
        privilegeMapper.deleteRowPrivilegeByOrgId(orgId, PrivilegeType.ROW.value(), PrivilegeScope.ORGAN.value());
    }

    public String findRowPrivilegeByRoleIdAPriType(long roleId) {
        // 查找已经分配的权限
        Long[] roleIds = { roleId };
        String[] ownerTypes = { OwnerType.ROLE.value() };
        String[] privilegeTypes = { PrivilegeType.ROW.value() };
        List<Privilege> privilegeList = privilegeMapper.findByOwnIdAndPrivilegeType(roleIds, ownerTypes, privilegeTypes);

        if (privilegeList.isEmpty()) return PrivilegeScope.ORGAN.value();

        Privilege privilege = privilegeList.get(0);
        String resourceId = privilege.getResourceId();

        return resourceId;
    }

    public AssignVo<TreeVoExtend> findSelectAndUnselectModOptByOwnIdAOwnType(long ownerId, String ownerType) {
        // 查找已经分配的权限
        Long[] roleIds = { ownerId };
        String[] ownerTypes = { ownerType };
        String[] privilegeTypes = { PrivilegeType.OPT.value() };
        List<Privilege> privilegeList = privilegeMapper.findByOwnIdAndPrivilegeType(roleIds, ownerTypes, privilegeTypes);

        // 操作id集
        List<Long> resourceIdList = new ArrayList<>();
        String privilegeScope = "";
        for (Privilege privilege : privilegeList) {
            String resourceId = privilege.getResourceId();
            try {
                resourceIdList.add(Long.parseLong(resourceId));
            } catch (NumberFormatException e) {
                logger.warn("{} 不能转换成模块id, 忽略此id.", resourceId);
            }
            if (StringUtils.isBlank(privilegeScope)) {
                privilegeScope = privilege.getPrivilegeScope();
            }
        }

        List<ModOptRef> assignedModOptList = platformServiceFacade.findModOptRefByModOptRefIds(resourceIdList);
        // 查找所有模块操作关系
        List<ModOptRef> allModOptList = platformServiceFacade.findAllModOptRefs();
        List<ModOptRef> allModOptListTemp = Lists.newArrayList(allModOptList);
        allModOptListTemp.removeAll(assignedModOptList);
        // 未分配的权限
        List<ModOptRef> unassignedModOptList = allModOptListTemp;
        //将操作转化成列表数据
        List<ModuleMenuVo> assignedModMenuList = this.filterModOpt(assignedModOptList, true).getModuleMenu();
        List<ModuleMenuVo> unassignedModMenuList = this.filterModOpt(unassignedModOptList, true).getModuleMenu();

        //将已分配和未分配的模块操作放入
        return new AssignVo<>(
                this.convertToTree(assignedModMenuList),
                this.convertToTree(unassignedModMenuList),
                privilegeScope);
    }

    public UserRowPrivilegeVo findSelectRowPrivilegeByUserId(long userId) {
        // 查找已经分配的权限
        Long[] userIds = { userId };
        String[] ownerTypes = { OwnerType.USR.value() };
        String[] privilegeTypes = { PrivilegeType.ROW.value() };
        List<Privilege> privilegeList = privilegeMapper.findByOwnIdAndPrivilegeType(userIds, ownerTypes, privilegeTypes);

        List<Long> resourceIdList = new ArrayList<>();
        PrivilegeScope privilegeScope = null;
        for (Privilege privilege : privilegeList) {
            String resourceId = privilege.getResourceId();
            resourceIdList.add(Long.parseLong(resourceId));
            if (null == privilegeScope) {
                privilegeScope = PrivilegeScope.of(privilege.getPrivilegeScope());
            }
        }

        Long[] resourceIdArray = resourceIdList.toArray(new Long[resourceIdList.size()]);
        final List<UserRowPrivilegeVo.RowVo> rowVos = new ArrayList();
        if (PrivilegeScope.USER == privilegeScope) {
            List<User> userList = userService.findByUserIds(resourceIdArray);
            userList.forEach(item -> {
                UserRowPrivilegeVo.RowVo vo = new UserRowPrivilegeVo.RowVo();
                vo.setId(item.getUserId());
                vo.setName(item.getUserName());
                rowVos.add(vo);
            });
        }
        else if (PrivilegeScope.ROLE == privilegeScope) {
            List<OrgRoleRef> orgRoleRefList = organRoleService.findByOrgRoleIds(resourceIdArray);
            orgRoleRefList.forEach(item -> {
                String orgName = item.getOrgan().getOrgSimpleName();
                String roleName = item.getRole().getRoleName();
                UserRowPrivilegeVo.RowVo vo = new UserRowPrivilegeVo.RowVo();
                vo.setId(item.getOrgRoleId());
                vo.setName(orgName + "-" + roleName);
                rowVos.add(vo);
            });
        }
        else {
            List<Organ> organList = organService.findByOrgIds(resourceIdArray);
            organList.forEach(item -> {
                UserRowPrivilegeVo.RowVo vo = new UserRowPrivilegeVo.RowVo();
                vo.setId(item.getOrgId());
                vo.setName(item.getOrgSimpleName());
                rowVos.add(vo);
            });
        }

        UserRowPrivilegeVo vo = new UserRowPrivilegeVo();
        vo.setRowPrivilege(privilegeScope == null ? "" : privilegeScope.value());
        vo.setRow(rowVos);

        return vo;
    }

    public boolean existsSpecialPrivilegeByUserId(long userId) {
        int count = privilegeMapper.countByUserIdAndOwnerType(userId, OwnerType.USR.value());

        return count > 0;
    }

    public HierarchyModuleVo findPrivilegesForAdminLogin() {
        List<ModOptRef> modOptRefList = platformServiceFacade.findAllModOptRefs(); // 所有操作按钮
        Map<Long, List<ModuleMenuVo>> modOptMap = Maps.newHashMap(); // 四级模块操作
        for (ModOptRef modOptRef : modOptRefList) {
            ModuleMenu moduleMenu = modOptRef.getModuleMemu(); // 所属模块
            Operate operate = modOptRef.getOperate(); // 具体操作
            ModuleMenuVo vo = new ModuleMenuVo();
            BeanUtils.copyProperties(moduleMenu, vo);

            // 操作按钮
            ModuleMenuVo modOptVo = new ModuleMenuVo();
            modOptVo.setModuleId(modOptRef.getModOptId() + 1000); // 将模块操作关系的id加上1000,防id重复
            modOptVo.setModName(operate.getOperateName());
            modOptVo.setModImgCls(operate.getOptImgLink());
            modOptVo.setOptFunLink(operate.getOptFunLink()); // 为操作准备功能链接
            modOptVo.setModOrder(operate.getOptOrder()); // 操作按钮的顺序
            modOptVo.setParentId(vo.getModuleId()); // 将第三级菜单设置为操作
            modOptVo.setIsLeaf(true);

            Long modEnId = vo.getModuleId(); // 模块英文id,作为操作列表map的key值
            if (modOptMap.containsKey(modEnId)) {
                List<ModuleMenuVo> modOptVoList = modOptMap.get(modEnId);
                // 根据操作顺序进行排序
                int listIndex = 0; // 元素索引
                boolean orderBol = true; // 是否插入指定索引元素
                for (ModuleMenu modOptVoo : modOptVoList) {
                    int listModOrder = modOptVoo.getModOrder(); // 已经有的操作顺序
                    int currModOrder = modOptVo.getModOrder(); // 当前操作顺序
                    if (listModOrder > currModOrder) {
                        modOptVoList.add(listIndex, modOptVo); // 顺序小的插在前
                        orderBol = false;
                        break;
                    }
                    listIndex++;
                }
                if (orderBol) {
                    modOptVoList.add(modOptVo);// 加到操作list中
                }
            } else {
                List<ModuleMenuVo> modOptVoList = Lists.newArrayList();// 操作list
                modOptVoList.add(modOptVo);// 加到操作list中
                modOptMap.put(modEnId, modOptVoList);// 设置map
            }
        }

        List<ModuleMenuVo> allModuleMenus = platformServiceFacade.findAllModule();// 所有模块菜单
        HierarchyModuleVo hierarchyModuleVo = platformServiceFacade.filterModule(allModuleMenus); // 过滤模块
        hierarchyModuleVo.setFourth(modOptMap);

        return hierarchyModuleVo;
    }

    public HierarchyModuleVo findPrivilegesForUserLogin(long userId, long roleId, long organId, long organRoleId) {
        // 查找用户相关所有权限
        Long[] ownerIds = { userId, roleId, organRoleId }; // 拥有者数组
        String[] ownerTypes =  { OwnerType.USR.value(), OwnerType.ROLE.value() }; // 拥有者类型数组
        String[] privilegeTypes = { PrivilegeType.ROW.value(), PrivilegeType.OPT.value() }; // 权限类型数组
        List<Privilege> privilegeList = privilegeMapper
                .findByOwnIdAndPrivilegeType(ownerIds, ownerTypes, privilegeTypes);

        // 1 ====== 用户, 角色权限划分 ======
        List<Privilege> privilegeOptList = Lists.newArrayList();
        List<Privilege> privilegeRowList = Lists.newArrayList();
        List<Privilege> privilegeOptSpecialList = Lists.newArrayList();
        List<Privilege> privilegeRowSpecialList = Lists.newArrayList();
        for (Privilege privilege : privilegeList) {
            OwnerType ownerType = OwnerType.of(privilege.getOwnerType());
            PrivilegeType privilegeType = PrivilegeType.of(privilege.getPrivilegeType());
            if (OwnerType.USR == ownerType) { // 用户
                if (PrivilegeType.OPT == privilegeType) {
                    privilegeOptSpecialList.add(privilege);
                } else if (PrivilegeType.ROW == privilegeType) {
                    privilegeRowSpecialList.add(privilege);
                }
            }
            else if (OwnerType.ROLE == ownerType) {
                if (PrivilegeType.OPT == privilegeType) {
                    privilegeOptList.add(privilege);
                } else if (PrivilegeType.ROW == privilegeType) {
                    privilegeRowList.add(privilege);
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

        // 2 ====== 操作权限 ======
        List<Long> resourceIdList = new ArrayList<>();
        PrivilegeScope privilegeScope = null;
        for (Privilege privilege : privilegeOptList) {
            String resourceId = privilege.getResourceId();
            resourceIdList.add(Long.parseLong(resourceId));
            if (null == privilegeScope) {
                PrivilegeScope.of(privilege.getPrivilegeScope());
            }
        }
        List<ModOptRef> modOptRefList = platformServiceFacade.findModOptRefByModOptRefIds(resourceIdList);
        if (PrivilegeScope.EXCLUDE == privilegeScope) {
            List<ModOptRef> allModOptRefList = platformServiceFacade.findAllModOptRefs(); // 所有操作按钮
            allModOptRefList.removeAll(modOptRefList);
            modOptRefList = allModOptRefList;
        } else if (PrivilegeScope.ALL == privilegeScope) {
            List<ModOptRef> allModOptRefList = platformServiceFacade.findAllModOptRefs(); // 所有操作按钮
            modOptRefList = allModOptRefList;
        }

        // 过滤所有的模块操作
        ModuleOperationVo moduleOperationVo = this.filterModOpt(modOptRefList, false);
        List<ModuleMenuVo> moduleMenuList = moduleOperationVo.getModuleMenu();
        Map<Long, List<ModuleMenuVo>> modOptMap = moduleOperationVo.getModuleOperate();

        // 1,2,3 级菜单分类
        HierarchyModuleVo hierarchyModuleVo = platformServiceFacade.filterModule(moduleMenuList);
        hierarchyModuleVo.setFourth(modOptMap);

        // 3 ====== 行权限 ======
        StringBuilder ownerTypeAndUserPriBuilder = new StringBuilder();
        OwnerType ownerTypeFlag = null;
        PrivilegeScope privilegeScopeFlag = null;
        for (Privilege privilege : privilegeRowList) {
            String resourceId = privilege.getResourceId();
            OwnerType ownerType = OwnerType.of(privilege.getOwnerType());
            PrivilegeScope privilegeScopeRow = PrivilegeScope.of(privilege.getPrivilegeScope());
            if (OwnerType.ROLE == ownerType) { // 角色只有其一权限
                ownerTypeFlag = OwnerType.ROLE;
                privilegeScopeFlag = privilegeScopeRow;
                break;
            }
            else if (OwnerType.USR == ownerType) {
                if (ownerTypeFlag == null || privilegeScopeFlag == null || OwnerType.USR != ownerTypeFlag) {
                    ownerTypeFlag = OwnerType.USR;
                    privilegeScopeFlag = privilegeScopeRow;
                }
                ownerTypeAndUserPriBuilder = ownerTypeAndUserPriBuilder.length() == 0
                        ? ownerTypeAndUserPriBuilder.append(resourceId) : ownerTypeAndUserPriBuilder.append("," + resourceId);
            }
        }
        // 行权限条件sql
        StringBuilder resourceIdBuf = new StringBuilder();
        resourceIdBuf.append(" AND ");
        if (OwnerType.ROLE == ownerTypeFlag) {
            if (PrivilegeScope.ROLE == privilegeScopeFlag) {
                resourceIdBuf.append(ROLE_ID + " = " + organRoleId + " "); // 自我真实角色
            } else if (PrivilegeScope.USER == privilegeScopeFlag) {
                resourceIdBuf.append(USER_ID + " = " + userId + " ");
            } else {
                String organChildren = getPrivilegeOrganChildren(organId);
                resourceIdBuf.append(ORGAN_ID + " in ( " + organChildren + ") ");
            }
        }
        else if (OwnerType.USR == ownerTypeFlag) {
            if (PrivilegeScope.ROLE == privilegeScopeFlag) {
                resourceIdBuf.append(ROLE_ID + " in (" + ownerTypeAndUserPriBuilder + ") ");
            } else if (PrivilegeScope.USER == privilegeScopeFlag) {
                resourceIdBuf.append(USER_ID + " in (" + ownerTypeAndUserPriBuilder + ") ");
            } else {
                resourceIdBuf.append(ORGAN_ID + " in (" + ownerTypeAndUserPriBuilder + ") ");
            }
        }
        else {
            SystemConfigBean systemConfigBean = SystemConfigXmlParse.getInstance().getSystemConfigBean(); // 系统配置权限
            String rowPrivilegeLevel = systemConfigBean.getRowPrivilegeLevel();
            if (PrivilegeScope.ROLE.value().equals(rowPrivilegeLevel)) {
                resourceIdBuf.append(ROLE_ID + " = " + organRoleId + " "); // 自我真实角色
            } else if (PrivilegeScope.USER.value().equals(rowPrivilegeLevel)) {
                resourceIdBuf.append(USER_ID + " = " + userId + " ");
            } else {
                String organChildren = getPrivilegeOrganChildren(organId);
                resourceIdBuf.append(ORGAN_ID + " in ( " + organChildren + ") ");
            }
        }

        hierarchyModuleVo.setRow(resourceIdBuf.toString());

        return hierarchyModuleVo;
    }

    @Transactional
    public void save(List<String> modOptIds, long ownerId, String privilegeType,
                     String ownerType, String privilegeScope) {

        // 删除角色或用戶拥有操作权限
        delete(ownerId, ownerType, privilegeType);

        StringBuilder optContent = new StringBuilder();
        optContent.append("模块操作关系ID: ");
        if (PrivilegeScope.ALL.value().equals(privilegeScope)) {
            Privilege privilege = new Privilege();
            privilege.setResourceId(PrivilegeScope.ALL.value());
            privilege.setOwnerId(ownerId);
            privilege.setOwnerType(ownerType);
            privilege.setPrivilegeType(privilegeType);
            privilege.setPrivilegeScope(privilegeScope);
            privilegeMapper.save(privilege);
        } else {
            if (modOptIds != null) {
                for (String modOptId : modOptIds) {
                    if (CommonUtils.isNullOrEmpty(modOptId)) continue;
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
                        optContent.toString(), ownerId + SPLIT + privilegeType);
            }
        }
    }

    public List<TreeVoExtend> convertToTree(List<ModuleMenuVo> vos) {
        List<TreeVoExtend> treeList = new ArrayList<>();
        for (ModuleMenuVo vo : vos) {
            TreeVoExtend tree = new TreeVoExtend();
            tree.setId(vo.getModuleId().toString());
            tree.setParentId(vo.getParentId().toString());
            tree.setText(vo.getModName());
            tree.setIconSkin(vo.getModImgCls());
            if (!vo.getIsLeaf()) {
                // 模块菜单级别为3是叶子
                tree.setIsParent(true);
//                tree.setNocheck(true);
            }
            treeList.add(tree);
        }

        return treeList;
    }


    // --------------------------- 私有方法 -------------------------------



    private ModuleOperationVo filterModOpt(List<ModOptRef> modOptRefList, boolean isTreeFlag) {
        Map<Long, List<ModuleMenuVo>> modOptMap = Maps.newHashMap(); // 四级模块操作
        List<ModuleMenuVo> moduleMenuVoList = Lists.newArrayList(); // 模块菜单list
        for (ModOptRef modOptRef : modOptRefList) {
            ModuleMenu moduleMenu3 = modOptRef.getModuleMemu(); // 第三级菜单
            ModuleMenuVo vo3 = new ModuleMenuVo();
            BeanUtils.copyProperties(moduleMenu3, vo3);

            // 操作按钮
            Operate operate = modOptRef.getOperate();
            ModuleMenuVo modOptVo = new ModuleMenuVo();
            modOptVo.setModuleId(modOptRef.getModOptId() + 1000); // 将模块操作关系的id加上1000,防id重复
            modOptVo.setModName(operate.getOperateName());
            modOptVo.setModImgCls(operate.getOptImgLink());
            modOptVo.setOptFunLink(operate.getOptFunLink()); // 为操作准备功能链接
            modOptVo.setModOrder(operate.getOptOrder()); // 操作按钮的顺序
            modOptVo.setParentId(vo3.getModuleId()); // 将第三级菜单设置为操作
            modOptVo.setIsLeaf(true);

            if (isTreeFlag) { // 如果为树则加入模块list
                moduleMenuVoList.add(modOptVo);
            }
            else { // 不为树则维护操作按钮map
                Long modEnId = vo3.getModuleId(); // 模块英文id, 作为操作列表map的key值
                if (modOptMap.containsKey(modEnId)) {
                    List<ModuleMenuVo> modOptVoList = modOptMap.get(modEnId);
                    // 根据操作顺序进行排序
                    int listIndex = 0; // 元素索引
                    boolean orderBol = true; //是否插入指定索引元素
                    for (ModuleMenuVo modOptVoo : modOptVoList) {
                        int listModOrder = modOptVoo.getModOrder(); // 已经有的操作顺序
                        int currModOrder = modOptVo.getModOrder(); // 当前操作顺序
                        if (listModOrder > currModOrder) {
                            modOptVoList.add(listIndex, modOptVo); // 顺序小的插在前
                            orderBol = false;
                            break;
                        }
                        listIndex++;
                    }
                    if (orderBol) {
                        modOptVoList.add(modOptVo); // 加到操作list中
                    }
                } else {
                    List<ModuleMenuVo> modOptVoList = Lists.newArrayList(); // 操作list
                    modOptVoList.add(modOptVo); // 加到操作list中
                    modOptMap.put(modEnId, modOptVoList);
                }
            }

            // 如果包含相应菜单项则加入相应模块菜单
            if (!moduleMenuVoList.contains(vo3)) {
                moduleMenuVoList.add(vo3);
                ModuleMenuVo vo2 = new ModuleMenuVo(vo3.getParentId()); // 第二级菜单
                if (!moduleMenuVoList.contains(vo2)) {
                    vo2 = platformServiceFacade.getModule(vo2.getModuleId());
                    moduleMenuVoList.add(vo2);
                    ModuleMenuVo vo1 = new ModuleMenuVo(vo2.getParentId()); // 第一级菜单
                    if (!moduleMenuVoList.contains(vo1)) {
                        moduleMenuVoList.add(platformServiceFacade.getModule(vo1.getModuleId()));
                    }
                }
            }
        }
        Collections.sort(moduleMenuVoList, new ModuleMenu());

        return new ModuleOperationVo(moduleMenuVoList, modOptMap);
    }

    /**
     * 查询用户的机构上级
     *
     * @param organId
     * @return
     * @author rutine
     * @time Oct 21, 2012 4:37:53 PM
     */
    private String getPrivilegeOrganChildren(long organId) {
        List<Long> orgIds = organService.findChildNodes(organId, 0);
        String orgIdStr = orgIds.stream()
                .map(String::valueOf).collect(Collectors.joining("," ));

        return orgIdStr;
    }
}
