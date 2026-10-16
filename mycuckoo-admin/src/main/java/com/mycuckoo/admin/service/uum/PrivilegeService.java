package com.mycuckoo.admin.service.uum;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.admin.constant.AdminConst;
import com.mycuckoo.admin.constant.enums.ModuleLevel;
import com.mycuckoo.admin.constant.enums.OwnerType;
import com.mycuckoo.admin.constant.enums.PrivilegeScope;
import com.mycuckoo.admin.constant.enums.PrivilegeType;
import com.mycuckoo.core.CheckboxTree;
import com.mycuckoo.core.SimpleTree;
import com.mycuckoo.core.constant.enums.ModuleName;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.util.StrUtils;
import com.mycuckoo.core.util.SystemConfigLoader;
import com.mycuckoo.core.util.web.SessionContextHolder;
import com.mycuckoo.core.xml.SystemConfigXml;
import com.mycuckoo.admin.entity.platform.ModuleMenu;
import com.mycuckoo.admin.entity.uum.DepartmentExtend;
import com.mycuckoo.admin.entity.uum.Privilege;
import com.mycuckoo.admin.entity.uum.User;
import com.mycuckoo.admin.repository.uum.PrivilegeMapper;
import com.mycuckoo.admin.service.facade.PlatformServiceFacade;
import com.mycuckoo.admin.service.facade.UumServiceFacade;
import com.mycuckoo.admin.entity.dto.platform.HierarchyModuleDto;
import com.mycuckoo.admin.entity.dto.platform.ModuleResourceDto;
import com.mycuckoo.admin.entity.dto.platform.ResourceDto;
import com.mycuckoo.admin.entity.dto.uum.AssignDto;
import com.mycuckoo.admin.entity.dto.uum.RowPrivilegeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.mycuckoo.core.constant.BaseConst.*;
import static com.mycuckoo.core.operator.LogOperator.DUNHAO;

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
    private PlatformServiceFacade platformServiceFacade;
    @Autowired
    private UumServiceFacade uumServiceFacade;


    public void delete(long ownerId, OwnerType ownerType, PrivilegeType privilegeType) {
        privilegeMapper.deleteByOwnerIdAndPrivilegeType(ownerId, ownerType.code, privilegeType.code);
    }

    public void deletePrivilegeByModResId(String[] modResRefIds) {
        if (modResRefIds == null || modResRefIds.length == 0) return;

        privilegeMapper.deleteByResourceIds(modResRefIds, PrivilegeType.RES.code);
    }

    public void deleteByOwnerIdAndOwnerType(long ownerId, String ownerType) {
        privilegeMapper.deleteByOwnerIdAndOwnerType(ownerId, ownerType);
    }

    public void deleteRowPrivilegeByDeptId(String deptId) {
        privilegeMapper.deleteRowPrivilegeByDeptId(deptId, PrivilegeType.ROW.code, PrivilegeScope.DEPT.scope);
    }

    public String findRowPrivilegeByRoleId(long roleId) {
        // 查找已经分配的权限
        Long[] roleIds = { roleId };
        String[] ownerTypes = { OwnerType.ROLE.code };
        String[] privilegeTypes = { PrivilegeType.ROW.code };
        List<Privilege> privilegeList = privilegeMapper.findByOwnIdAndPrivilegeType(roleIds, ownerTypes, privilegeTypes);
        if (privilegeList.isEmpty()) return PrivilegeScope.ORGAN.scope;

        Privilege privilege = privilegeList.get(0);
        String resourceId = privilege.getResourceId();

        return resourceId;
    }

    public AssignDto<CheckboxTree, String> findModResByOwnIdAOwnTypeWithCheck(long ownerId, OwnerType ownerType) {
        // 查找已经分配的权限
        Long[] roleIds = { ownerId };
        String[] ownerTypes = { ownerType.code };
        String[] privilegeTypes = { PrivilegeType.RES.code };
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
            if (StrUtils.isEmpty(privilegeScope)) {
                privilegeScope = privilege.getPrivilegeScope();
            }
        }

        // 查找所有模块资源关系
        List<String> myRes = SessionContextHolder.getResources();
        List<ResourceDto> resources = platformServiceFacade.findAllModResRefs();
        resources = resources.stream().filter(o -> myRes.contains(o.getId().toString())).collect(Collectors.toList());

        List<String> checkedOperations = resourceIdList.parallelStream()
                .map(id -> {return AdminConst.ID_LEAF + id; })
                .collect(Collectors.toList());

        //将操作转化成列表数据
        List<ModuleMenu> allModMenuList = this.filterModOpt(resources, true).getMenu();

        List<? extends SimpleTree> trees = platformServiceFacade.buildTree(allModMenuList, checkedOperations, true);

        //将已分配和未分配的模块操作放入
        return new AssignDto(trees, checkedOperations, privilegeScope);
    }

    public RowPrivilegeDto findRowPrivilegeByUserId(long userId) {
        // 查找已经分配的权限
        Long[] userIds = { userId };
        String[] ownerTypes = { OwnerType.USR.code };
        String[] privilegeTypes = { PrivilegeType.ROW.code };
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
        final List<RowPrivilegeDto.RowVo> rowVos = new ArrayList();
        if (PrivilegeScope.USER == privilegeScope) {
            List<User> userList = uumServiceFacade.findByUserIds(resourceIdArray);
            userList.forEach(item -> {
                RowPrivilegeDto.RowVo vo = new RowPrivilegeDto.RowVo();
                vo.setId(item.getUserId());
                vo.setName(item.getName());
                rowVos.add(vo);
            });
        }
        else if (PrivilegeScope.ROLE == privilegeScope) {
            List<DepartmentExtend> deptList = uumServiceFacade.findByDeptIds(resourceIdArray);
            deptList.forEach(item -> {
                String deptName = item.getName();
                String roleName = item.getRoleName();
                RowPrivilegeDto.RowVo vo = new RowPrivilegeDto.RowVo();
                vo.setId(item.getRoleId());
                vo.setName(deptName + "-" + roleName);
                rowVos.add(vo);
            });
        }
        else {
            List<DepartmentExtend> deptList = uumServiceFacade.findByDeptIds(resourceIdArray);
            deptList.forEach(item -> {
                RowPrivilegeDto.RowVo vo = new RowPrivilegeDto.RowVo();
                vo.setId(item.getDeptId());
                vo.setName(item.getName());
                rowVos.add(vo);
            });
        }

        return new RowPrivilegeDto(privilegeScope == null ? "" : privilegeScope.scope, rowVos);
    }

    public boolean existsSpecialPrivilegeByUserId(long userId) {
        int count = privilegeMapper.countByUserIdAndOwnerType(userId, OwnerType.USR.code);

        return count > 0;
    }

    public HierarchyModuleDto findPrivilegesForAdminLoginNew() {
        List<ResourceDto> resourceVos = platformServiceFacade.findAllModResRefs(); // 所有资源
        // 四级模块操作
        Map<Long, List<ResourceDto>> resMap = resourceVos.stream()
                .collect(Collectors.groupingBy(ResourceDto::getParentId,
                        Collectors.collectingAndThen(Collectors.toList(),
                                sub -> sub.stream().sorted(Comparator.comparing(ResourceDto::getOrder)).collect(Collectors.toList()))));
        List<ModuleMenu> allModuleMenus = platformServiceFacade.findAllModule();// 所有模块菜单
        HierarchyModuleDto hierarchyModuleVo = platformServiceFacade.filterModule(allModuleMenus); // 过滤模块
        hierarchyModuleVo.setFourth(resMap);

        return hierarchyModuleVo;
    }

    public HierarchyModuleDto findPrivilegesForUserLogin(long userId, long roleId, long organId) {
        // 查找用户相关所有权限
        Long[] ownerIds = { userId, roleId }; // 拥有者数组
        String[] ownerTypes =  { OwnerType.USR.code, OwnerType.ROLE.code }; // 拥有者类型数组
        String[] privilegeTypes = { PrivilegeType.ROW.code, PrivilegeType.RES.code }; // 权限类型数组
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
                if (PrivilegeType.RES == privilegeType) {
                    optSpecialPrivileges.add(privilege);
                } else if (PrivilegeType.ROW == privilegeType) {
                    rowSpecialPrivileges.add(privilege);
                }
            }
            else if (OwnerType.ROLE == ownerType) {
                if (PrivilegeType.RES == privilegeType) {
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

        List<ResourceDto> resources = Lists.newArrayList();
        if (PrivilegeScope.EXCLUDE == privilegeScope) {
            List<ResourceDto> allResources = platformServiceFacade.findAllModResRefs(); // 所有资源
            resources = allResources.stream()
                    .filter(r -> !resourceIds.contains(r.getId()))
                    .collect(Collectors.toList());
        } else if (PrivilegeScope.ALL == privilegeScope) {
            resources = platformServiceFacade.findAllModResRefs(); // 所有资源
        } else {
            List<ResourceDto> allResources = platformServiceFacade.findAllModResRefs(); // 所有资源
            resources = allResources.stream()
                    .filter(r -> resourceIds.contains(r.getId()))
                    .collect(Collectors.toList());
        }

        //过滤仅组织管理员拥有的资源权限
        List<Long> orgResIds = this.findOrgRes(roleId);
        resources = resources.stream().filter(res -> orgResIds.contains(res.getId())).collect(Collectors.toList());


        // 过滤所有的模块操作
        ModuleResourceDto moduleResourceVo = this.filterModOpt(resources, false);
        List<ModuleMenu> moduleMenuList = moduleResourceVo.getMenu();
        Map<Long, List<ResourceDto>> modOptMap = moduleResourceVo.getResource();

        // 1,2,3 级菜单分类
        HierarchyModuleDto hierarchyModuleVo = platformServiceFacade.filterModule(moduleMenuList);
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
                sql.append(ROLE_ID + " = " + roleId + " "); // 自我真实角色
            } else if (PrivilegeScope.USER == privilegeScopeFlag) {
                sql.append(USER_ID + " = " + userId + " ");
            } else {
                String organChildren = getPrivilegeDeptChildren(organId);
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
            SystemConfigXml systemConfig = SystemConfigLoader.getInstance().getConfig(); // 系统配置权限
            String defaultRowPrivilegeLevel = systemConfig.getDefaultRowPrivilegeLevel();
            if (PrivilegeScope.ROLE.scope.equals(defaultRowPrivilegeLevel)) {
                sql.append(ROLE_ID + " = " + roleId + " "); // 自我真实角色
            } else if (PrivilegeScope.USER.scope.equals(defaultRowPrivilegeLevel)) {
                sql.append(USER_ID + " = " + userId + " ");
            } else {
                String organChildren = getPrivilegeDeptChildren(organId);
                sql.append(ORGAN_ID + " in ( " + organChildren + ") ");
            }
        }

        hierarchyModuleVo.setRow(sql.toString());

        return hierarchyModuleVo;
    }

    @Transactional
    public void save(List<String> resourceIds, OwnerType ownerType, long ownerId,
                     PrivilegeType privilegeType, String privilegeScope) {

        resourceIds = resourceIds.parallelStream()
                .map(mapper -> {
                    int index = mapper.indexOf(AdminConst.ID_LEAF);
                    return index >= 0 ? mapper.substring(AdminConst.ID_LEAF.length()) : mapper;
                })
                .map(String::valueOf)
                .collect(Collectors.toList());

        // 删除角色或用戶拥有操作权限
        delete(ownerId, ownerType, privilegeType);

        if (PrivilegeScope.ALL.scope.equals(privilegeScope)) {
            Privilege privilege = new Privilege();
            privilege.setTenantId(SessionContextHolder.getTenantId());
            privilege.setResourceId(PrivilegeScope.ALL.scope);
            privilege.setOwnerId(ownerId);
            privilege.setOwnerType(ownerType.code);
            privilege.setPrivilegeType(privilegeType.code);
            privilege.setPrivilegeScope(privilegeScope);
            privilege.setCreator(SessionContextHolder.getUserId().toString());
            privilege.setCreateTime(LocalDateTime.now());
            privilegeMapper.save(privilege);
        } else {
            if (resourceIds != null) {
                for (String resourceId : resourceIds) {
                    if (StrUtils.isEmpty(resourceId)) continue;
                    Privilege privilege = new Privilege();
                    privilege.setTenantId(SessionContextHolder.getTenantId());
                    privilege.setResourceId(resourceId);
                    privilege.setOwnerId(ownerId);
                    privilege.setOwnerType(ownerType.code);
                    privilege.setPrivilegeType(privilegeType.code);
                    privilege.setPrivilegeScope(privilegeScope);
                    privilege.setCreator(SessionContextHolder.getUserId().toString());
                    privilege.setCreateTime(LocalDateTime.now());
                    privilegeMapper.save(privilege);
                }

                LogOperator.begin()
                        .module(ModuleName.SYS_PRIVILEGE)
                        .id(ownerId + ":" + privilegeType.code)
                        .title(SessionContextHolder.getUserName() + "新增" + "权限")
                        .content("资源IDs: %s",
                                resourceIds.stream().collect(Collectors.joining(DUNHAO)))
                        .emit();
            }
        }
    }





    // --------------------------- 私有方法 -------------------------------
    private ModuleResourceDto filterModOpt(List<ResourceDto> resources, boolean isTreeFlag) {
        List<ModuleMenu> modOptList = Lists.newArrayList(); // 四级模块操作
        List<ModuleMenu> moduleMenuList = Lists.newArrayList(); // 模块菜单list
        for (ResourceDto resource : resources) {
            // 第三级菜单
            ModuleMenu vo3 = platformServiceFacade.getModule(resource.getParentId());;
            if (isTreeFlag) {
                // 操作按钮
                ModuleMenu modOpt = new ModuleMenu();
                // 将id加上前缀,防id重复
                modOpt.setId(AdminConst.ID_LEAF + resource.getId());
                modOpt.setModuleId(resource.getId());
                modOpt.setParentId(resource.getParentId()); // 将第三级菜单设置为操作
                modOpt.setCode(resource.getCode()); // 为操作准备功能链接
                modOpt.setName(resource.getName());
                modOpt.setIconCls(resource.getIconCls());
                modOpt.setOrder(resource.getOrder()); // 操作按钮的顺序
                modOpt.setLevel(ModuleLevel.FOUR.code);
                modOptList.add(modOpt);
            }

            // 如果包含相应菜单项则加入相应模块菜单
            if (!moduleMenuList.contains(vo3)) {
                moduleMenuList.add(vo3);
                ModuleMenu vo2 = new ModuleMenu(vo3.getParentId()); // 第二级菜单
                if (!moduleMenuList.contains(vo2)) {
                    vo2 = platformServiceFacade.getModule(vo2.getModuleId());
                    moduleMenuList.add(vo2);
                    ModuleMenu vo1 = new ModuleMenu(vo2.getParentId()); // 第一级菜单
                    if (!moduleMenuList.contains(vo1)) {
                        moduleMenuList.add(platformServiceFacade.getModule(vo1.getModuleId()));
                    }
                }
            }
        }

        // 四级模块操作
        Map<Long, List<ResourceDto>> modResMap = Maps.newHashMap();
        if (isTreeFlag) {
            // 如果为树则加入模块list
            moduleMenuList.addAll(modOptList);
        }
        else {
            // 不为树则维护操作按钮map
            modResMap = resources.stream()
                    .collect(Collectors.groupingBy(ResourceDto::getParentId,
                            Collectors.collectingAndThen(Collectors.toList(),
                                    sub -> sub.stream().sorted(Comparator.comparing(ResourceDto::getOrder)).collect(Collectors.toList()))));
        }
        Collections.sort(moduleMenuList, new ModuleMenu());



        return new ModuleResourceDto(moduleMenuList, modResMap);
    }

    /**
     * 查询用户的部门上级
     *
     * @param deptId
     * @return
     * @author rutine
     * @time Oct 21, 2012 4:37:53 PM
     */
    private String getPrivilegeDeptChildren(long deptId) {
        List<Long> deptIds = uumServiceFacade.findChildIds(deptId, 0);
        return deptIds.stream()
                .map(String::valueOf).collect(Collectors.joining("," ));
    }

    private List<Long> findOrgRes(long roleId) {
        List<Privilege> privileges = privilegeMapper.findByOwnIdAndPrivilegeType(
                new Long[] { roleId }, new String[] { OwnerType.ROLE.code }, new String[] { PrivilegeType.RES.code });


        // 2 ====== 资源权限 ======
        List<Long> resourceIds = new ArrayList<>();
        PrivilegeScope privilegeScope = null;
        for (Privilege privilege : privileges) {
            String resourceId = privilege.getResourceId();
            resourceIds.add(Long.parseLong(resourceId));
            if (null == privilegeScope) {
                privilegeScope = PrivilegeScope.of(privilege.getPrivilegeScope());
            }
        }

        List<ResourceDto> resources = Lists.newArrayList();
        if (PrivilegeScope.EXCLUDE == privilegeScope) {
            List<ResourceDto> allResources = platformServiceFacade.findAllModResRefs(); // 所有资源
            resources = allResources.stream()
                    .filter(r -> !resourceIds.contains(r.getId()))
                    .collect(Collectors.toList());
        } else if (PrivilegeScope.ALL == privilegeScope) {
            resources = platformServiceFacade.findAllModResRefs(); // 所有资源
        } else {
            List<ResourceDto> allResources = platformServiceFacade.findAllModResRefs(); // 所有资源
            resources = allResources.stream()
                    .filter(r -> resourceIds.contains(r.getId()))
                    .collect(Collectors.toList());
        }

        return resources.stream().map(res -> res.getId()).collect(Collectors.toList());
    }
}
