package com.mycuckoo.service.uum;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.constant.ServiceConst;
import com.mycuckoo.constant.enums.*;
import com.mycuckoo.domain.platform.ModuleMenu;
import com.mycuckoo.domain.uum.OrgRoleRef;
import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.domain.uum.Privilege;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.operator.LogOperator;
import com.mycuckoo.repository.uum.PrivilegeMapper;
import com.mycuckoo.service.facade.PlatformServiceFacade;
import com.mycuckoo.utils.CommonUtils;
import com.mycuckoo.utils.SystemConfigXmlParse;
import com.mycuckoo.vo.*;
import com.mycuckoo.vo.platform.ModuleMenuVo;
import com.mycuckoo.vo.platform.ResourceVo;
import com.mycuckoo.vo.uum.RowPrivilegeVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.mycuckoo.constant.BaseConst.*;
import static com.mycuckoo.operator.LogOperator.DUNHAO;

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
    private PlatformServiceFacade platformServiceFacade;


    public void delete(long ownerId, OwnerType ownerType, PrivilegeType privilegeType) {
        privilegeMapper.deleteByOwnerIdAndPrivilegeType(ownerId, ownerType.value(), privilegeType.value());
    }

    public void deletePrivilegeByModOptId(String[] modOptRefIds) {
        if (modOptRefIds == null || modOptRefIds.length == 0) return;

        privilegeMapper.deleteByModOptId(modOptRefIds, PrivilegeType.OPT.value());
    }

    public void deletePrivilegeByModResId(String[] modResRefIds) {
        if (modResRefIds == null || modResRefIds.length == 0) return;

        privilegeMapper.deleteByModOptId(modResRefIds, PrivilegeType.RES.value());
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

    public AssignVo<CheckBoxTree, Long> findModOptByOwnIdAOwnTypeWithCheck(long ownerId, OwnerType ownerType) {
        // 查找已经分配的权限
        Long[] roleIds = { ownerId };
        String[] ownerTypes = { ownerType.value() };
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

        // 查找所有模块操作关系
        List<ResourceVo> resources = platformServiceFacade.findAllModOptRefsNew();
        List<String> checkedOperations = resourceIdList.parallelStream()
                .map(id -> {return ServiceConst.LEAF_ID + id; })
                .collect(Collectors.toList());

        //将操作转化成列表数据
        List<ModuleMenuVo> allModMenuList = this.filterModOpt(resources, true).getMenu();

        List<? extends SimpleTree> trees = platformServiceFacade.buildTree(allModMenuList, checkedOperations, true);

        //将已分配和未分配的模块操作放入
        return new AssignVo(trees, checkedOperations, privilegeScope);
    }

    public RowPrivilegeVo findRowPrivilegeByUserId(long userId) {
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
        final List<RowPrivilegeVo.RowVo> rowVos = new ArrayList();
        if (PrivilegeScope.USER == privilegeScope) {
            List<User> userList = userService.findByUserIds(resourceIdArray);
            userList.forEach(item -> {
                RowPrivilegeVo.RowVo vo = new RowPrivilegeVo.RowVo();
                vo.setId(item.getUserId());
                vo.setName(item.getName());
                rowVos.add(vo);
            });
        }
        else if (PrivilegeScope.ROLE == privilegeScope) {
            List<OrgRoleRef> orgRoleRefList = organRoleService.findByOrgRoleIds(resourceIdArray);
            orgRoleRefList.forEach(item -> {
                String orgName = item.getOrgan().getSimpleName();
                String roleName = item.getRole().getName();
                RowPrivilegeVo.RowVo vo = new RowPrivilegeVo.RowVo();
                vo.setId(item.getOrgRoleId());
                vo.setName(orgName + "-" + roleName);
                rowVos.add(vo);
            });
        }
        else {
            List<Organ> organList = organService.findByOrgIds(resourceIdArray);
            organList.forEach(item -> {
                RowPrivilegeVo.RowVo vo = new RowPrivilegeVo.RowVo();
                vo.setId(item.getOrgId());
                vo.setName(item.getSimpleName());
                rowVos.add(vo);
            });
        }

        return new RowPrivilegeVo(privilegeScope == null ? "" : privilegeScope.value(), rowVos);
    }

    public boolean existsSpecialPrivilegeByUserId(long userId) {
        int count = privilegeMapper.countByUserIdAndOwnerType(userId, OwnerType.USR.value());

        return count > 0;
    }

    public HierarchyModuleVo findPrivilegesForAdminLogin() {
        List<ResourceVo> resourceVos = platformServiceFacade.findAllModOptRefsNew(); // 四级模块操作
        // 四级模块操作
        Map<Long, List<ResourceVo>> resMap = resourceVos.stream()
                .collect(Collectors.groupingBy(ResourceVo::getParentId,
                        Collectors.collectingAndThen(Collectors.toList(),
                                sub -> sub.stream().sorted(Comparator.comparing(ResourceVo::getOrder)).collect(Collectors.toList()))));
        List<ModuleMenuVo> allModuleMenus = platformServiceFacade.findAllModule();// 所有模块菜单
        HierarchyModuleVo hierarchyModuleVo = platformServiceFacade.filterModule(allModuleMenus); // 过滤模块
        hierarchyModuleVo.setFourth(resMap);

        return hierarchyModuleVo;
    }

    public HierarchyModuleVo findPrivilegesForAdminLoginNew() {
        List<ResourceVo> resourceVos = platformServiceFacade.findAllModResRefs(); // 所有资源
        // 四级模块操作
        Map<Long, List<ResourceVo>> resMap = resourceVos.stream()
                .collect(Collectors.groupingBy(ResourceVo::getParentId,
                        Collectors.collectingAndThen(Collectors.toList(),
                                sub -> sub.stream().sorted(Comparator.comparing(ResourceVo::getOrder)).collect(Collectors.toList()))));
        List<ModuleMenuVo> allModuleMenus = platformServiceFacade.findAllModule();// 所有模块菜单
        HierarchyModuleVo hierarchyModuleVo = platformServiceFacade.filterModule(allModuleMenus); // 过滤模块
        hierarchyModuleVo.setFourth(resMap);

        return hierarchyModuleVo;
    }

    public HierarchyModuleVo findPrivilegesForUserLogin(long userId, long roleId, long organId, long organRoleId) {
        // 查找用户相关所有权限
        Long[] ownerIds = { userId, roleId, organRoleId }; // 拥有者数组
        String[] ownerTypes =  { OwnerType.USR.value(), OwnerType.ROLE.value() }; // 拥有者类型数组
        String[] privilegeTypes = { PrivilegeType.ROW.value(), PrivilegeType.OPT.value() }; // 权限类型数组
        List<Privilege> privileges = privilegeMapper.findByOwnIdAndPrivilegeType(ownerIds, ownerTypes, privilegeTypes);

        // 1 ====== 用户, 角色权限划分 ======
        List<Privilege> optPrivileges = Lists.newArrayList();
        List<Privilege> optSpecialPrivileges = Lists.newArrayList();
        List<Privilege> rowPrivileges = Lists.newArrayList();
        List<Privilege> rowSpecialPrivileges = Lists.newArrayList();
        for (Privilege privilege : privileges) {
            OwnerType ownerType = OwnerType.of(privilege.getOwnerType());
            PrivilegeType privilegeType = PrivilegeType.of(privilege.getPrivilegeType());
            if (OwnerType.USR == ownerType) { // 用户
                if (PrivilegeType.OPT == privilegeType) {
                    optSpecialPrivileges.add(privilege);
                } else if (PrivilegeType.ROW == privilegeType) {
                    rowSpecialPrivileges.add(privilege);
                }
            }
            else if (OwnerType.ROLE == ownerType) {
                if (PrivilegeType.OPT == privilegeType) {
                    optPrivileges.add(privilege);
                } else if (PrivilegeType.ROW == privilegeType) {
                    rowPrivileges.add(privilege);
                }
            }
        }

        // 权限校验过滤
        if (!optSpecialPrivileges.isEmpty()) {
            optPrivileges = optSpecialPrivileges;
        }
        if (!rowSpecialPrivileges.isEmpty()) {
            rowPrivileges = rowSpecialPrivileges;
        }

        // 2 ====== 操作权限 ======
        List<Long> resourceIds = new ArrayList<>();
        PrivilegeScope privilegeScope = null;
        for (Privilege privilege : optPrivileges) {
            String resourceId = privilege.getResourceId();
            resourceIds.add(Long.parseLong(resourceId));
            if (null == privilegeScope) {
                privilegeScope = PrivilegeScope.of(privilege.getPrivilegeScope());
            }
        }

        List<ResourceVo> resources = Lists.newArrayList();
        if (PrivilegeScope.EXCLUDE == privilegeScope) {
            List<ResourceVo> allResources = platformServiceFacade.findAllModOptRefsNew(); // 所有操作按钮
            resources = allResources.stream()
                    .filter(r -> !resourceIds.contains(r.getId()))
                    .collect(Collectors.toList());
        } else if (PrivilegeScope.ALL == privilegeScope) {
            resources = platformServiceFacade.findAllModOptRefsNew(); // 所有操作按钮
        }

        // 过滤所有的模块操作
        ModuleResourceVo moduleResourceVo = this.filterModOpt(resources, false);
        List<ModuleMenuVo> moduleMenuList = moduleResourceVo.getMenu();
        Map<Long, List<ResourceVo>> modOptMap = moduleResourceVo.getResource();

        // 1,2,3 级菜单分类
        HierarchyModuleVo hierarchyModuleVo = platformServiceFacade.filterModule(moduleMenuList);
        hierarchyModuleVo.setFourth(modOptMap);

        // 3 ====== 行权限 ======
        List<String> rowResourceIds = Lists.newArrayList();
        OwnerType ownerTypeFlag = null;
        PrivilegeScope privilegeScopeFlag = null;
        for (Privilege privilege : rowPrivileges) {
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
                rowResourceIds.add(resourceId);
            }
        }
        String rowResourceIdStr = rowResourceIds.stream().collect(Collectors.joining(","));

                // 行权限条件sql
        StringBuilder sql = new StringBuilder();
        sql.append(" AND ");
        if (OwnerType.ROLE == ownerTypeFlag) {
            if (PrivilegeScope.ROLE == privilegeScopeFlag) {
                sql.append(ROLE_ID + " = " + organRoleId + " "); // 自我真实角色
            } else if (PrivilegeScope.USER == privilegeScopeFlag) {
                sql.append(USER_ID + " = " + userId + " ");
            } else {
                String organChildren = getPrivilegeOrganChildren(organId);
                sql.append(ORGAN_ID + " in ( " + organChildren + ") ");
            }
        }
        else if (OwnerType.USR == ownerTypeFlag) {
            if (PrivilegeScope.ROLE == privilegeScopeFlag) {
                sql.append(ROLE_ID + " in (" + rowResourceIdStr + ") ");
            } else if (PrivilegeScope.USER == privilegeScopeFlag) {
                sql.append(USER_ID + " in (" + rowResourceIdStr + ") ");
            } else {
                sql.append(ORGAN_ID + " in (" + rowResourceIdStr + ") ");
            }
        }
        else {
            SystemConfigBean systemConfigBean = SystemConfigXmlParse.getInstance().getSystemConfigBean(); // 系统配置权限
            String rowPrivilegeLevel = systemConfigBean.getRowPrivilegeLevel();
            if (PrivilegeScope.ROLE.value().equals(rowPrivilegeLevel)) {
                sql.append(ROLE_ID + " = " + organRoleId + " "); // 自我真实角色
            } else if (PrivilegeScope.USER.value().equals(rowPrivilegeLevel)) {
                sql.append(USER_ID + " = " + userId + " ");
            } else {
                String organChildren = getPrivilegeOrganChildren(organId);
                sql.append(ORGAN_ID + " in ( " + organChildren + ") ");
            }
        }

        hierarchyModuleVo.setRow(sql.toString());

        return hierarchyModuleVo;
    }

    @Transactional
    public void save(List<String> modOptIds, long ownerId, PrivilegeType privilegeType,
                     OwnerType ownerType, String privilegeScope) {

        modOptIds = modOptIds.parallelStream()
                .map(mapper -> {
                    int index = mapper.indexOf(ServiceConst.LEAF_ID);
                    return index >= 0 ? mapper.substring(ServiceConst.LEAF_ID.length()) : mapper;
                })
                .map(String::valueOf)
                .collect(Collectors.toList());

        // 删除角色或用戶拥有操作权限
        delete(ownerId, ownerType, privilegeType);

        if (PrivilegeScope.ALL.value().equals(privilegeScope)) {
            Privilege privilege = new Privilege();
            privilege.setResourceId(PrivilegeScope.ALL.value());
            privilege.setOwnerId(ownerId);
            privilege.setOwnerType(ownerType.value());
            privilege.setPrivilegeType(privilegeType.value());
            privilege.setPrivilegeScope(privilegeScope);
            privilegeMapper.save(privilege);
        } else {
            if (modOptIds != null) {
                for (String modOptId : modOptIds) {
                    if (CommonUtils.isNullOrEmpty(modOptId)) continue;
                    Privilege privilege = new Privilege();
                    privilege.setResourceId(modOptId);
                    privilege.setOwnerId(ownerId);
                    privilege.setOwnerType(ownerType.value());
                    privilege.setPrivilegeType(privilegeType.value());
                    privilege.setPrivilegeScope(privilegeScope);
                    privilegeMapper.save(privilege);
                }

                LogOperator.begin()
                        .module(ModuleName.SYS_PRIVILEGE)
                        .operate(OptName.SAVE)
                        .id(ownerId + ":" + privilegeType.value())
                        .title(null)
                        .content("模块操作关系IDs: %s",
                                modOptIds.stream().collect(Collectors.joining(DUNHAO)))
                        .level(LogLevel.FIRST)
                        .emit();
            }
        }
    }

    public List<CheckBoxTree> convertToTree(List<ModuleMenuVo> vos) {
        List<CheckBoxTree> treeList = new ArrayList<>();
        for (ModuleMenuVo vo : vos) {
            CheckBoxTree tree = new CheckBoxTree();
            tree.setId(vo.getModuleId().toString());
            tree.setParentId(vo.getParentId().toString());
            tree.setText(vo.getName());
            tree.setIconSkin(vo.getIconCls());
            if (!vo.getIsLeaf()) {
//                tree.setNocheck(true);
            }
            treeList.add(tree);
        }

        return treeList;
    }


    // --------------------------- 私有方法 -------------------------------



    private ModuleResourceVo filterModOpt(List<ResourceVo> resources, boolean isTreeFlag) {
        List<ModuleMenuVo> modOptVoList = Lists.newArrayList(); // 四级模块操作
        List<ModuleMenuVo> moduleMenuVoList = Lists.newArrayList(); // 模块菜单list
        for (ResourceVo resource : resources) {
            // 第三级菜单
            ModuleMenuVo vo3 = platformServiceFacade.getModule(resource.getParentId());;
            if (isTreeFlag) {
                // 操作按钮
                ModuleMenuVo modOptVo = new ModuleMenuVo();
                // 将id加上前缀,防id重复
                modOptVo.setId(ServiceConst.LEAF_ID + resource.getId());
                modOptVo.setModuleId(resource.getId());
                modOptVo.setParentId(resource.getParentId()); // 将第三级菜单设置为操作
                modOptVo.setCode(resource.getCode()); // 为操作准备功能链接
                modOptVo.setName(resource.getName());
                modOptVo.setIconCls(resource.getIconCls());
                modOptVo.setOrder(resource.getOrder()); // 操作按钮的顺序
                modOptVo.setLevel(ModuleLevel.FOUR.value());
                modOptVo.setIsLeaf(true);
                modOptVoList.add(modOptVo);
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

        // 四级模块操作
        Map<Long, List<ResourceVo>> modResMap = Maps.newHashMap();
        if (isTreeFlag) {
            // 如果为树则加入模块list
            moduleMenuVoList.addAll(modOptVoList);
        }
        else {
            // 不为树则维护操作按钮map
            modResMap = resources.stream()
                    .collect(Collectors.groupingBy(ResourceVo::getParentId,
                            Collectors.collectingAndThen(Collectors.toList(),
                                    sub -> sub.stream().sorted(Comparator.comparing(ResourceVo::getOrder)).collect(Collectors.toList()))));
        }
        Collections.sort(moduleMenuVoList, new ModuleMenu());



        return new ModuleResourceVo(moduleMenuVoList, modResMap);
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
        List<Long> orgIds = organService.findChildIds(organId, 0);
        String orgIdStr = orgIds.stream()
                .map(String::valueOf).collect(Collectors.joining("," ));

        return orgIdStr;
    }
}
