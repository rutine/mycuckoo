package com.mycuckoo.service.uum;

import com.google.common.collect.Lists;
import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.core.CheckboxTree;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.SimpleTree;
import com.mycuckoo.core.exception.ApplicationException;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageImpl;
import com.mycuckoo.domain.platform.District;
import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.repository.uum.OrganMapper;
import com.mycuckoo.service.facade.PlatformServiceFacade;
import com.mycuckoo.util.TreeHelper;
import com.mycuckoo.util.web.SessionUtil;
import com.mycuckoo.web.vo.res.uum.OrganVo;
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

import static com.mycuckoo.constant.AdminConst.DISABLE;
import static com.mycuckoo.constant.AdminConst.ENABLE;

/**
 * 功能说明: 机构业务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 11:18:21 AM
 */
@Service
@Transactional(readOnly = true)
public class OrganService {

    static Logger logger = LoggerFactory.getLogger(OrganService.class);

    @Autowired
    private OrganMapper organMapper;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private PlatformServiceFacade platformServiceFacade;


    @Transactional
    public void disEnable(long organId, String disEnableFlag) {
        boolean enable = ENABLE.equals(disEnableFlag);
        if (!enable) {
            /**
             * 1、机构有下级
             * 2、机构下有角色
             * 3、机构下有用户 暂不用实现 删除机构行权限
             * 4、被其它系统引用
             * 0、停用启用成功
             */
            int childCount = organMapper.countByParentId(organId);
            if (childCount > 0) throw new ApplicationException("机构有下级");

            organMapper.update(new Organ(organId, DISABLE));
            privilegeService.deleteRowPrivilegeByOrgId(organId + ""); // 删除机构行权限
        } else {
            organMapper.update(new Organ(organId, ENABLE));
        }

        Organ organ = get(organId);
        writeLog(organ, LogLevel.SECOND, enable ? OptName.ENABLE : OptName.DISABLE);
    }

    public boolean existByOrganName(String organName) {
        int count = organMapper.countByOrgSimpleName(organName);

        return count > 0;
    }

    public List<? extends SimpleTree> findChildNodes(long organId, boolean isCheckbox) {
        List<Organ> all = organMapper.findByPage(null, Querier.EMPTY).getContent();

        List<? extends SimpleTree> vos = toTree(all, isCheckbox);

        return TreeHelper.buildTree(vos, String.valueOf(organId));
    }

    @Deprecated
    public List<CheckboxTree> findNextLevelChildNodesWithCheckbox(long organId, long filterOutOrgId) {
        List<Organ> list = organMapper.findByParentIdAndFilterOutOrgId(organId, filterOutOrgId);
        List<CheckboxTree> treeVoList = new ArrayList<>();
        for (Organ organ : list) {
            CheckboxTree treeVo = new CheckboxTree();
            treeVo.setId(organ.getOrgId().toString());
            treeVo.setText(organ.getSimpleName());
            if (ModuleLevel.TWO.value().toString().equals(organ.getType())) {
                treeVo.setIsLeaf(true);
            }
            treeVoList.add(treeVo);
        }

        return treeVoList;
    }

    public Page<OrganVo> findByPage(Querier querier) {
        String treeId = "1"; //最顶级机构
        querier.putQ("treeId", treeId);
        Page<Organ> entityPage = organMapper.findByPage(querier.getQ(), querier);

        List<OrganVo> vos = Lists.newArrayList();
        for (Organ entity : entityPage.getContent()) {
            OrganVo vo = new OrganVo();
            BeanUtils.copyProperties(entity, vo);
            vos.add(vo);
        }

        return new PageImpl<>(vos, querier, entityPage.getTotalElements());
    }

    public OrganVo get(Long organId) {
        if (organId == null) {
            return null;
        }

        Organ organ = organMapper.get(organId);
        Organ parentOrgan = organMapper.get(organ.getParentId());
        OrganVo vo = new OrganVo();
        BeanUtils.copyProperties(organ, vo);
        vo.setParentName(parentOrgan == null ? null: parentOrgan.getSimpleName());

        District district = platformServiceFacade.getDistrict(organ.getBelongDist());
        if (district != null) {
            vo.setBelongDistName(district.getName());
        }

        return vo;
    }

    public List<Organ> findByOrgIds(Long[] orgIds) {
        if (orgIds == null || orgIds.length == 0) return Lists.newArrayList();

        return organMapper.findByOrgIds(orgIds);
    }

    @Transactional
    public void update(Organ organ) {
        Organ parent = get(organ.getParentId());
        Assert.notNull(parent, "上级组织不存在!");
        Organ old = get(organ.getOrgId());
        Assert.notNull(old, "机构不存在!");
        Assert.state(old.getSimpleName().equals(organ.getSimpleName())
                || !existByOrganName(organ.getSimpleName()), "机构[" + organ.getSimpleName() + "]已存在!");

        //不允许更新字段置空
        organ.setParentId(null);
        organ.setTreeId(null);
        organ.setRoleId(null);
        organ.setLevel(null);
        organ.setStatus(null);
        organ.setCreateTime(null);
        organ.setCreator(null);
        organ.setUpdator(SessionUtil.getUserCode());
        organ.setUpdateTime(LocalDateTime.now());
        organMapper.update(organ);

        writeLog(organ, LogLevel.SECOND, OptName.MODIFY);
    }

    @Transactional
    public void save(Organ organ) {
        Organ parent = get(organ.getParentId());
        Assert.notNull(parent, "上级组织不存在!");
        Assert.state(!existByOrganName(organ.getSimpleName()), "机构[" + organ.getSimpleName() + "]已存在!");

        organ.setLevel(parent.getLevel() + 1);
        organ.setStatus(ENABLE);
        organ.setUpdator(SessionUtil.getUserCode());
        organ.setUpdateTime(LocalDateTime.now());
        organ.setCreator(SessionUtil.getUserCode());
        organ.setCreateTime(LocalDateTime.now());
        organMapper.save(organ);

        Organ updateEntity = new Organ();
        updateEntity.setTreeId(String.format("%s.%s", parent.getTreeId(), organ.getOrgId()));
        organMapper.update(updateEntity);

        writeLog(organ, LogLevel.FIRST, OptName.SAVE);
    }


    // --------------------------- 私有方法 -------------------------------

    /**
     * 公用模块写日志
     *
     * @param entity    机构对象
     * @param logLevel
     * @param opt
     * @throws ApplicationException
     * @author rutine
     * @time Oct 17, 2012 7:39:34 PM
     */
    private void writeLog(Organ entity, LogLevel logLevel, OptName opt) {
        LogOperator.begin()
                .module(ModuleName.ORGAN_MGR)
                .operate(opt)
                .id(entity.getOrgId())
                .title(null)
                .content("机构名称：%s, 机构代码：%s, 上级机构: %s",
                        entity.getSimpleName(), entity.getCode(), entity.getParentId())
                .level(logLevel)
                .emit();
    }

    /**
     * 转换树vo
     *
     * @param list 机构
     * @param isCheckbox  true:带复选框 false:无复选框
     * @return
     * @author rutine
     * @time Oct 29, 2020 17:39:35 PM
     */
    private List<? extends SimpleTree> toTree(List<Organ> list, boolean isCheckbox) {
        return list.stream().map(mapper -> {
            SimpleTree tree = null;
            if (isCheckbox) {
                tree = new CheckboxTree();
                CheckboxTree boxTree = (CheckboxTree) tree;
                boxTree.setNocheck(false);
                boxTree.setChecked(false);
                boxTree.setCheckbox(new CheckboxTree.Checkbox(0));
            } else {
                tree = new SimpleTree();
            }
            tree.setId(mapper.getOrgId().toString());
            tree.setParentId(mapper.getOrgId() == 0 ? "-1" : mapper.getParentId().toString());
            tree.setText(mapper.getSimpleName());
            if (ModuleLevel.TWO.value().toString().equals(mapper.getType())) {
                tree.setIsLeaf(true);
            }

            return tree;
        }).collect(Collectors.toList());
    }
}
