package com.mycuckoo.service.uum;

import com.google.common.collect.Lists;
import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.core.CheckboxTree;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.SimpleTree;
import com.mycuckoo.core.exception.ApplicationException;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.util.TreeHelper;
import com.mycuckoo.core.util.web.SessionUtil;
import com.mycuckoo.domain.uum.Department;
import com.mycuckoo.domain.uum.DepartmentExtend;
import com.mycuckoo.repository.uum.DepartmentMapper;
import com.mycuckoo.web.vo.res.platform.DeptVos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mycuckoo.constant.AdminConst.*;

/**
 * 功能说明: 部门业务类
 *
 * @author rutine
 * @version 4.1.0
 * @time May 19, 2024 11:56:45 AM
 */
@Service
@Transactional(readOnly = true)
public class DepartmentService {

    static Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private PrivilegeService privilegeService;


    @Transactional
    public void disEnable(long deptId, String disEnableFlag) {
        boolean enable = ENABLE.equals(disEnableFlag);
        if (!enable) {
            /**
             * 1、部门有下级
             * 4、被其它系统引用
             * 0、停用启用成功
             */
            int childCount = departmentMapper.countByParentId(deptId);
            if (childCount > 0) throw new ApplicationException("存在下级");

            departmentMapper.update(new Department(deptId, DISABLE));
            privilegeService.deleteRowPrivilegeByDeptId(deptId + ""); // 删除行权限
        } else {
            departmentMapper.update(new Department(deptId, ENABLE));
        }

        Department entity = get(deptId);
        writeLog(entity, LogLevel.SECOND, enable ? OptName.ENABLE : OptName.DISABLE);
    }

    public List<Long> findChildIds(long deptId, int flag) {
        List<Department> all = departmentMapper.findByPage(null, Querier.EMPTY).getContent();

        List<? extends SimpleTree> vos = toTree(all, false, false);
        List<? extends SimpleTree> trees = TreeHelper.buildTree(vos, String.valueOf(deptId));

        List<String> nodeIds = Lists.newArrayList();
        TreeHelper.collectNodeIds(nodeIds, trees);

        //过滤出所有下级节点ID
        List<Long> deptIds = nodeIds.stream().map(Long::valueOf).collect(Collectors.toList());
        if (deptId != 1) {
            deptIds.add(deptId);
        }

        if (flag == 1) {
            List<Long> allIds = all.stream().map(Department::getDeptId).collect(Collectors.toList());
            allIds.remove(1L);  //删除根元素
            allIds.removeAll(deptIds);

            deptIds = allIds;
        }

        return deptIds;
    }

    public List<? extends SimpleTree> findChildNodes(long deptId, boolean isCheckbox, boolean withRole) {
        List<Department> all = departmentMapper.findByPage(null, Querier.EMPTY).getContent();

        List<? extends SimpleTree> vos = toTree(all, isCheckbox, withRole);

        return TreeHelper.buildTree(vos, String.valueOf(deptId));
    }

    public List<? extends SimpleTree> findAll(Querier querier) {
        String treeId = "1"; //最顶级部门
        querier.putQ("treeId", treeId);

        querier.setPageSize(0);
        Page<Department> page = departmentMapper.findByPage(querier.getQ(), querier);

        List<DeptVos.Tree> all = Lists.newArrayList();
        for (Department entity : page) {
            DeptVos.Tree tree = new DeptVos.Tree();
            tree.setId(entity.getDeptId() + "");
            tree.setParentId(entity.getParentId() + "");
            tree.setText(entity.getName());
            tree.setRoleName(((DepartmentExtend) entity).getRoleName());
            tree.setStatus(entity.getStatus());
            all.add(tree);
        }

        return TreeHelper.buildTree(all, treeId);
    }

    public DepartmentExtend get(Long deptId) {
        if (deptId == null) {
            return null;
        }

        Department entity = departmentMapper.get(deptId);
        Department parentEntity = departmentMapper.get(entity.getParentId());
        DepartmentExtend extend = new DepartmentExtend();
        BeanUtils.copyProperties(entity, extend);
        extend.setParentName(parentEntity == null ? null: parentEntity.getName());

        return extend;
    }

    public List<DepartmentExtend> findByDeptIds(Long[] deptIds) {
        if (deptIds == null || deptIds.length == 0) return Lists.newArrayList();

        return departmentMapper.findByDeptIds(deptIds);
    }

    @Transactional
    public void update(Department entity) {
        Department parent = get(entity.getParentId());
        Assert.notNull(parent, "上级部门不存在!");
        Department old = get(entity.getDeptId());
        Assert.notNull(old, "部门不存在!");

        Long parentId = entity.getParentId();

        //不允许更新字段置空
        entity.setOrgId(null);
        entity.setTreeId(null);
        entity.setRoleId(null);
        entity.setLevel(null);
        entity.setStatus(null);
        entity.setCreateTime(null);
        entity.setCreator(null);

        if (parentId != null && !parentId.equals(old.getParentId())) {
            //调整了组织架构
            int level = parent.getLevel() - old.getLevel();
            String treeId = String.format("%s.%s", parent.getTreeId(), entity.getDeptId());
            departmentMapper.updateTreeId(old.getTreeId(), treeId, level);

            entity.setTreeId(treeId);
            entity.setLevel(parent.getLevel() + 1);
        }

        entity.setUpdator(SessionUtil.getUserId().toString());
        entity.setUpdateTime(LocalDateTime.now());
        departmentMapper.update(entity);

        writeLog(entity, LogLevel.SECOND, OptName.MODIFY);
    }

    @Transactional
    public void save(Department entity) {
        Department parent = get(entity.getParentId());
        Assert.notNull(parent, "上级不存在!");

        entity.setOrgId(SessionUtil.getOrganId());
        entity.setUpdator(SessionUtil.getUserId().toString());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setCreator(SessionUtil.getUserId().toString());
        entity.setCreateTime(LocalDateTime.now());
        entity.setLevel(parent.getLevel() + 1);
        entity.setStatus(ENABLE);
        departmentMapper.save(entity);

        Department updateEntity = new Department();
        updateEntity.setTreeId(String.format("%s.%s", parent.getTreeId(), entity.getDeptId()));
        departmentMapper.update(updateEntity);

        writeLog(entity, LogLevel.FIRST, OptName.SAVE);
    }

    @Transactional
    public void assignRole(Long deptId, Long roleId) {
        Department old = get(deptId);
        Assert.notNull(old, "部门不存在!");

        Department updateEntity = new Department();
        updateEntity.setDeptId(deptId);
        updateEntity.setRoleId(roleId);
        updateEntity.setUpdator(SessionUtil.getUserId().toString());
        updateEntity.setUpdateTime(LocalDateTime.now());
        departmentMapper.update(updateEntity);

        LogOperator.begin()
                .module(ModuleName.DEPT_MGR)
                .operate(OptName.ASSIGN)
                .id(deptId)
                .title(null)
                .content("名称：%s, 角色：%s", old.getName(), roleId)
                .level(LogLevel.SECOND)
                .emit();
    }


    // --------------------------- 私有方法 -------------------------------

    /**
     * 公用模块写日志
     */
    private void writeLog(Department entity, LogLevel logLevel, OptName opt) {
        LogOperator.begin()
                .module(ModuleName.DEPT_MGR)
                .operate(opt)
                .id(entity.getDeptId())
                .title(null)
                .content("名称：%s, 代码：%s, 上级: %s",
                        entity.getName(), entity.getCode(), entity.getParentId())
                .level(logLevel)
                .emit();
    }

    /**
     * 转换树vo
     *
     * @param list
     * @param isCheckbox  true:带复选框 false:无复选框
     */
    private List<? extends SimpleTree> toTree(List<Department> list, boolean isCheckbox, boolean withRole) {
        List<SimpleTree> all = new ArrayList<>();
        for (Department mapper : list)  {
            SimpleTree tree = null;
            if (isCheckbox) {
                tree = new CheckboxTree();
                CheckboxTree boxTree = (CheckboxTree) tree;
                boxTree.setNocheck(false);
//                boxTree.setCheckbox(withRole ? null : new CheckboxTree.Checkbox(0));
                boxTree.setCheckbox(new CheckboxTree.Checkbox(0));
            } else {
                tree = new SimpleTree();
            }
            tree.setId(mapper.getDeptId().toString());
            tree.setParentId(mapper.getDeptId() == 0 ? "-1" : mapper.getParentId().toString());
            tree.setText(mapper.getName());
            tree.setIsLeaf(false);
            all.add(tree);

            if (withRole && mapper.getRoleId() != null) {
                DepartmentExtend extend = (DepartmentExtend) mapper;
                SimpleTree node = null;
                if (isCheckbox) {
                    node = new CheckboxTree();
                    CheckboxTree boxTree = (CheckboxTree) node;
                    boxTree.setNocheck(false);
                    boxTree.setChecked(false);
                    boxTree.setCheckbox(new CheckboxTree.Checkbox(0));
                } else {
                    node = new SimpleTree();
                }
                node.setId(ID_LEAF + extend.getRoleId());
                node.setParentId(mapper.getDeptId() + "");
                node.setText(extend.getRoleName());
                node.setIsLeaf(true);
                all.add(node);
            }
        }

        return all;
    }
}
