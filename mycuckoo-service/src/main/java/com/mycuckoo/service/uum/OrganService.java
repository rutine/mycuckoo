package com.mycuckoo.service.uum;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.domain.platform.District;
import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.operator.LogOperator;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageImpl;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.uum.OrganMapper;
import com.mycuckoo.service.facade.PlatformServiceFacade;
import com.mycuckoo.utils.TreeHelper;
import com.mycuckoo.vo.CheckBoxTree;
import com.mycuckoo.vo.SimpleTree;
import com.mycuckoo.vo.uum.OrganVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mycuckoo.constant.ServiceConst.*;

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
    private OrganRoleService organRoleService;
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

            int roleCount = organRoleService.countByOrgId(organId);
            if (roleCount > 0) throw new ApplicationException("机构下有角色");

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

    public List<Long> findChildIds(long organId, int flag) {
        List<Organ> all = organMapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE)).getContent();

        List<? extends SimpleTree> vos = toTree(all, N);
        List<? extends SimpleTree> trees = TreeHelper.buildTree(vos, String.valueOf(organId));

        List<String> nodeIds = Lists.newArrayList();
        TreeHelper.collectNodeIds(nodeIds, trees);

        //过滤出所有下级节点ID
        List<Long> orgIds = nodeIds.stream().map(Long::valueOf).collect(Collectors.toList());
        if (organId != 0) {
            orgIds.add(organId);
        }

        if (flag == 1) {
            List<Long> allIds = all.stream().map(Organ::getOrgId).collect(Collectors.toList());
            allIds.remove(0L);  //删除根元素
            allIds.removeAll(orgIds);

            orgIds = allIds;
        }

        return orgIds;
    }

    public List<? extends SimpleTree> findChildNodes(long organId, String isCheckbox) {
        List<Organ> all = organMapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE)).getContent();

        List<? extends SimpleTree> vos = toTree(all, isCheckbox);

        return TreeHelper.buildTree(vos, String.valueOf(organId));
    }

    @Deprecated
    public List<CheckBoxTree> findNextLevelChildNodesWithCheckbox(long organId, long filterOutOrgId) {
        List<Organ> list = organMapper.findByParentIdAndFilterOutOrgId(organId, filterOutOrgId);
        List<CheckBoxTree> treeVoList = new ArrayList<>();
        for (Organ organ : list) {
            CheckBoxTree treeVo = new CheckBoxTree();
            treeVo.setId(organ.getOrgId().toString());
            treeVo.setText(organ.getSimpleName());
            if (ModuleLevel.TWO.value().toString().equals(organ.getType())) {
                treeVo.setIsLeaf(true);
            }
            treeVoList.add(treeVo);
        }

        return treeVoList;
    }

    public Page<OrganVo> findByPage(String code, String name, Pageable page) {
        Integer organId = 0; //最顶级机构
        List<Long> idList = this.findChildIds(organId, 0);
        if (idList.isEmpty()) return new PageImpl<>(new ArrayList<>(), page, 0);

        Map<String, Object> params = Maps.newHashMap();
        params.put("filterOrgIds", idList);
        params.put("code", code);
        params.put("simpleName", name);
        Page<Organ> entityPage = organMapper.findByPage(params, page);

        List<OrganVo> vos = Lists.newArrayList();
        for (Organ entity : entityPage.getContent()) {
            OrganVo vo = new OrganVo();
            BeanUtils.copyProperties(entity, vo);
            vos.add(vo);
        }

        return new PageImpl<>(vos, page, entityPage.getTotalElements());
    }

    public OrganVo get(Long organId) {
        if (organId == null) {
            return null;
        }

        Organ organ = organMapper.get(organId);
        Organ parentOrgan = organMapper.get(organ.getParentId());
        OrganVo vo = new OrganVo();
        BeanUtils.copyProperties(organ, vo);
        vo.setParentName(parentOrgan.getSimpleName());

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
        Organ old = get(organ.getOrgId());
        Assert.notNull(old, "机构不存在!");
        Assert.state(old.getSimpleName().equals(organ.getSimpleName())
                || !existByOrganName(organ.getSimpleName()), "机构[" + organ.getSimpleName() + "]已存在!");

        organMapper.update(organ);

        writeLog(organ, LogLevel.SECOND, OptName.MODIFY);
    }

    @Transactional
    public void save(Organ organ) {
        Assert.state(!existByOrganName(organ.getSimpleName()), "机构[" + organ.getSimpleName() + "]已存在!");

        organ.setStatus(ENABLE);
        organMapper.save(organ);

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
     * @param isCheckbox  Y:带复选框 N:无复选框
     * @return
     * @author rutine
     * @time Oct 29, 2020 17:39:35 PM
     */
    private List<? extends SimpleTree> toTree(List<Organ> list, String isCheckbox) {
        return list.stream().map(mapper -> {
            SimpleTree tree = null;
            if (Y.equals(isCheckbox)) {
                tree = new CheckBoxTree();
                CheckBoxTree boxTree = (CheckBoxTree) tree;
                boxTree.setNocheck(false);
                boxTree.setChecked(false);
                boxTree.setCheckBox(new CheckBoxTree.CheckBox(0));
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
