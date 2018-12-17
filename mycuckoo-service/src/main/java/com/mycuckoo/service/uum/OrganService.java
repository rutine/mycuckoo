package com.mycuckoo.service.uum;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.ModuleLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.domain.platform.District;
import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageImpl;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.uum.OrganMapper;
import com.mycuckoo.service.facade.PlatformServiceFacade;
import com.mycuckoo.service.platform.SystemOptLogService;
import com.mycuckoo.vo.SimpleTree;
import com.mycuckoo.vo.CheckBoxTree;
import com.mycuckoo.vo.uum.OrganVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.*;

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
    @Autowired
    private SystemOptLogService sysOptLogService;


    @Transactional
    public void disEnable(long organId, String disEnableFlag) {
        if (DISABLE.equals(disEnableFlag)) {
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

            Organ organ = new Organ(organId, DISABLE);
            organMapper.update(organ);
            privilegeService.deleteRowPrivilegeByOrgId(organId + ""); // 删除机构行权限

            organ = get(organId);
            this.writeLog(organ, LogLevelEnum.SECOND, OptNameEnum.DISABLE);
        } else {
            Organ organ = new Organ(organId, ENABLE);
            organMapper.update(organ);

            organ = get(organId);
            writeLog(organ, LogLevelEnum.SECOND, OptNameEnum.ENABLE);
        }
    }

    public boolean existByOrganName(String organName) {
        int count = organMapper.countByOrgSimpleName(organName);

        return count > 0;
    }

    public List<Long> findChildIds(long organId, int flag) {
        List<Organ> all = organMapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE)).getContent();
        List<Organ> filterList = Lists.newArrayList();
        List<Organ> tempList = Lists.newArrayList();
        tempList.addAll(all);
        tempList.remove(new Organ(0L, null)); //删除根元素

        //过滤出所有下级元素
        filterList = this.filterChildren(filterList, tempList, organId);
        if (organId != 0) {
            // 加入本元素
            for (Organ organ : all) {
                if (organ.getOrgId() == organId) {
                    filterList.add(organ);
                }
            }
        }
        if (flag == 1) {
            all.removeAll(filterList);
            filterList = all;
        }

        List<Long> orgIds = filterList.parallelStream()
                .map(Organ::getOrgId).collect(Collectors.toList());

        return orgIds;
    }

    public List<? super SimpleTree> findChildNodes(long organId) {
        List<Organ> all = organMapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE)).getContent();
        Organ parent = new Organ(organId, null);
        parent.setParentId(organId);

        List<Organ> tempList = Lists.newArrayList();
        tempList.addAll(all);
        tempList.remove(parent); //删除根元素
        SimpleTree vo = this.buildTree(parent, tempList);

        return vo.getChildren();
    }

    public List<CheckBoxTree> findNextLevelChildNodesWithCheckbox(long organId, long filterOutOrgId) {
        List<Organ> list = organMapper.findByParentIdAndFilterOutOrgId(organId, filterOutOrgId);
        List<CheckBoxTree> treeVoList = new ArrayList<>();
        for (Organ organ : list) {
            CheckBoxTree treeVo = new CheckBoxTree();
            treeVo.setId(organ.getOrgId().toString());
            treeVo.setText(organ.getOrgSimpleName());
            if (ModuleLevelEnum.TWO.value().equals(organ.getOrgType())) {
                treeVo.setIsLeaf(true);
            }
            treeVoList.add(treeVo);
        }

        return treeVoList;
    }

    public Page<OrganVo> findByPage(String orgCode, String orgName, Pageable page) {
        logger.debug("start={} limit={} orgName={} orgCode={}",
                page.getOffset(), page.getPageSize(), orgName, orgCode);

        Integer organId = 0; //最顶级机构
        List<Long> idList = this.findChildIds(organId, 0);
        if (idList.isEmpty()) return new PageImpl<>(new ArrayList<>(), page, 0);

        Map<String, Object> params = Maps.newHashMap();
        params.put("orgIds", idList);
        params.put("orgCode", orgCode);
        params.put("orgName", orgName);
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
        logger.debug("will find organ id is {}", organId);

        Organ organ = organMapper.get(organId);
        Organ parentOrgan = organMapper.get(organ.getParentId());
        OrganVo vo = new OrganVo();
        BeanUtils.copyProperties(organ, vo);
        vo.setParentName(parentOrgan.getOrgSimpleName());

        District district = platformServiceFacade.getDistrict(organ.getOrgBelongDist());
        if (district != null) {
            vo.setOrgBelongDistName(district.getDistrictName());
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
        Assert.state(old.getOrgSimpleName().equals(organ.getOrgSimpleName())
                || !existByOrganName(organ.getOrgSimpleName()), "机构[" + organ.getOrgSimpleName() + "]已存在!");

        organMapper.update(organ);

        writeLog(organ, LogLevelEnum.SECOND, OptNameEnum.MODIFY);
    }

    @Transactional
    public void save(Organ organ) {
        Assert.state(!existByOrganName(organ.getOrgSimpleName()), "机构[" + organ.getOrgSimpleName() + "]已存在!");

        organMapper.save(organ);

        writeLog(organ, LogLevelEnum.FIRST, OptNameEnum.SAVE);
    }


    // --------------------------- 私有方法 -------------------------------

    /**
     * 公用模块写日志
     *
     * @param organ    机构对象
     * @param logLevel
     * @param opt
     * @throws ApplicationException
     * @author rutine
     * @time Oct 17, 2012 7:39:34 PM
     */
    private void writeLog(Organ organ, LogLevelEnum logLevel, OptNameEnum opt) {
        StringBuilder optContent = new StringBuilder();
        optContent.append("机构名称 : ").append(organ.getOrgSimpleName()).append(SPLIT);
        optContent.append("机构代码 : ").append(organ.getOrgCode()).append(SPLIT);
        optContent.append("上级机构 : ").append(organ.getParentId()).append(SPLIT);

        sysOptLogService.saveLog(logLevel, opt, ORGAN_MGR, optContent.toString(), organ.getOrgId() + "");
    }

    /**
     * 根据机构ID过滤出下级，下下级机构
     *
     * @param filterList 存放下级，下下级机构的集合
     * @param allList    所有机构
     * @param organId    上级机构ID
     * @return 下级，下下级机构
     * @author rutine
     * @time Oct 17, 2012 7:49:15 PM
     */
    private List<Organ> filterChildren(List<Organ> filterList, List<Organ> allList, long organId) {
        List<Organ> subList = this.filterChildren(allList, organId);
        if (!subList.isEmpty()) {
            filterList.addAll(subList);
        }
        for (Organ organ : subList) {
            filterChildren(filterList, allList, organ.getOrgId());
        }

        return filterList;
    }

    /**
     * 根据机构ID过滤出下级机构
     *
     * @param allList 所有机构
     * @param organId 上级机构ID
     * @return 所有子结点
     * @author rutine
     * @time Oct 17, 2012 7:46:36 PM
     */
    private List<Organ> filterChildren(List<Organ> allList, long organId) {
        List<Organ> subList = Lists.newArrayList();
        Iterator<Organ> it = allList.iterator();
        while (it.hasNext()) {
            Organ organ = it.next();
            if (organ.getParentId() != null && organ.getParentId() == organId) {
                subList.add(organ);
                it.remove();
            }
        }

        return subList;
    }

    /**
     * 根据父级机构构建机构树
     *
     * @param parent    父级机构
     * @param children    所有子机构
     * @return 机构树
     * @author rutine
     * @time Dec 7, 2018 11:29:15 AM
     */
    private SimpleTree buildTree(Organ parent, List<Organ> children) {
        long id = parent.getOrgId();
        List<? super SimpleTree> childNodes = Lists.newArrayList();
        Iterator<Organ> it = children.iterator();
        while (it.hasNext()) {
            Organ item = it.next();
            if (item.getParentId().equals(id)) {
                it.remove();
                List<Organ> others = Lists.newArrayList();
                others.addAll(children);
                childNodes.add(this.buildTree(item, others));
            }
        }

        SimpleTree vo = new SimpleTree();
        vo.setId(parent.getOrgId().toString());
        vo.setText(parent.getOrgSimpleName());
        vo.setChildren(childNodes);
        if (ModuleLevelEnum.TWO.value().equals(parent.getOrgType())) {
            vo.setIsLeaf(true);
        }

        return vo;
    }
}
