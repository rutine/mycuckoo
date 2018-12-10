package com.mycuckoo.service.platform;

import com.google.common.collect.Lists;
import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.domain.platform.DicSmallType;
import com.mycuckoo.domain.platform.District;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageImpl;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.DistrictMapper;
import com.mycuckoo.vo.TreeVo;
import com.mycuckoo.vo.platform.DistrictVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.*;

/**
 * 功能说明: 地区业务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 10:31:29 AM
 */
@Service
@Transactional(readOnly = true)
public class DistrictService {
    static Logger logger = LoggerFactory.getLogger(DistrictService.class);

    @Autowired
    private DistrictMapper districtMapper;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private SystemOptLogService sysOptLogService;


    @Transactional
    public boolean disEnable(long districtId, String disEnableFlag) {
        if (DISABLE.equals(disEnableFlag)) {
            int count = districtMapper.countByParentId(districtId);
            if (count > 0) { //有下级地区
                throw new ApplicationException("存在下级地区, 停用失败!");
            }

            District district = new District(districtId, DISABLE);
            districtMapper.update(district);

            district = get(districtId);
            writeLog(district, LogLevelEnum.SECOND, OptNameEnum.DISABLE);
        } else {
            District district = new District(districtId, ENABLE);
            districtMapper.update(district);

            district = get(districtId);
            writeLog(district, LogLevelEnum.SECOND, OptNameEnum.ENABLE);
        }

        return true;
    }

    public boolean existByName(String districtName) {
        int count = districtMapper.countByDistrictName(districtName);
        if (count > 0) return true;

        return false;
    }

    public Page<DistrictVo> findByPage(Map<String, Object> params, Pageable page) {
        long treeId = (Long) params.get("treeId");
        String districtName = (String) params.get("districtName");
        String districtLevel = (String) params.get("districtLevel");

        logger.debug("start={} limit={} treeId={} districtName={} districtLevel={}",
                page.getOffset(), page.getPageSize(), treeId, districtName, districtLevel);

        List<Long> idList = new ArrayList<Long>();
        if (treeId >= 0) {
            idList = findChildIds(treeId, 0); // 过滤出所有下级
            if (idList.isEmpty()) {
                idList.add(-1l);
            }
        }

        params.put("array", idList.isEmpty() ? null : idList.toArray(new Long[idList.size()]));
        Page<District> entityPage = districtMapper.findByPage(params, page);
        List<DicSmallType> dicSmallTypeList = dictionaryService.findDicSmallTypesByBigTypeCode(DISTRICT);

        List<DistrictVo> vos = Lists.newArrayList();
        for (District entity : entityPage.getContent()) {
            String districtLevell = entity.getDistrictLevel();
            for (DicSmallType dicSmallType : dicSmallTypeList) {
                if (districtLevell.equalsIgnoreCase(dicSmallType.getSmallTypeCode())) {
                    entity.setDistrictLevel(dicSmallType.getSmallTypeName());
                    break;
                }
            }

            DistrictVo vo = new DistrictVo();
            BeanUtils.copyProperties(entity, vo);
            vo.setParentName(get(entity.getParentId()).getDistrictName());//上级地区名称
            vos.add(vo);
        }

        return new PageImpl<>(vos, page, entityPage.getTotalElements());
    }

    public DistrictVo get(Long districtId) {
        if (districtId == null) {
            return null;
        }

        logger.debug("will find district id is {}", districtId);

        District district = districtMapper.get(districtId);
        DistrictVo vo = new DistrictVo();
        BeanUtils.copyProperties(district, vo);

        return vo;
    }

    public List<? super TreeVo> findChildNodes(long districtId) {
        List<District> all = districtMapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE)).getContent();

        District parent = new District(districtId, null);
        parent.setParentId(districtId);

        List<District> tempList = Lists.newArrayList();
        tempList.addAll(all);
        tempList.remove(parent); //删除根元素
        TreeVo vo = this.buildTree(parent, tempList);

        return vo.getChildren();
    }

    @Transactional
    public void update(District district) {
        districtMapper.update(district);

        writeLog(district, LogLevelEnum.SECOND, OptNameEnum.MODIFY);
    }

    @Transactional
    public void save(District district) {
        districtMapper.save(district);

        writeLog(district, LogLevelEnum.FIRST, OptNameEnum.SAVE);
    }


    // --------------------------- 私有方法 -------------------------------

    /**
     * 公用地区写日志
     *
     * @param district 地区对象
     * @param logLevel
     * @param opt
     * @throws ApplicationException
     * @author rutine
     * @time Oct 16, 2012 7:38:53 PM
     */
    private void writeLog(District district, LogLevelEnum logLevel, OptNameEnum opt) {
        String optContent = district.getDistrictName() + SPLIT + district.getDistrictLevel() + SPLIT;
        sysOptLogService.saveLog(logLevel, opt, SYS_DISTRICT, optContent, district.getDistrictId() + "");
    }

    /**
     * 根据地区id查询所有地区节点
     *
     * @param districtId 上级地区id
     * @param flag       0为下级，1 为上级
     * @return
     * @author rutine
     * @time Oct 16, 2012 8:31:35 PM
     */
    private List<Long> findChildIds(long districtId, int flag) {
        List<District> all = districtMapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE)).getContent();
        List<District> tempList = new ArrayList<District>();
        tempList.addAll(all);

        //删除根元素
        District district = new District(0L, null);
        tempList.remove(district);

        //过滤出所有下级元素
        List<District> filterList = new ArrayList<District>();
        filterList = filterChildren(filterList, tempList, districtId);

        if (flag == 1) {
            // 本元素
            District districtOld = new District();
            districtOld.setDistrictId(districtId);
            filterList.add(districtOld);
            all.removeAll(filterList);
            filterList = all;
        }


        List<Long> ids = filterList.parallelStream()
                .map(District::getDistrictId).collect(Collectors.toList());

        return ids;
    }

    /**
     * 根据上级地区id递归过滤结点
     *
     * @param filterList 过滤的子节点
     * @param allList    所有地区结果集
     * @param districtId 上级地区id
     * @return 所有子结点
     * @author rutine
     * @time Oct 16, 2012 7:44:35 PM
     */
    private List<District> filterChildren(List<District> filterList, List<District> allList, long districtId) {
        List<District> subList = filterChildren(allList, districtId);
        if (!subList.isEmpty()) {
            filterList.addAll(subList);
        }
        for (District district : subList) {
            filterChildren(filterList, allList, district.getDistrictId());
        }

        return filterList;
    }

    /**
     * 根据地区id获得所有子结点
     *
     * @param allList    所有地区结果集
     * @param districtId 上级地区id
     * @return 所有子结点
     * @author rutine
     * @time Oct 16, 2012 7:40:46 PM
     */
    private List<District> filterChildren(List<District> allList, long districtId) {
        List<District> subList = new ArrayList<District>();
        Iterator<District> it = allList.iterator();
        while (it.hasNext()) {
            District district = it.next();
            if (district.getParentId() != null && district.getParentId() == districtId) {
                subList.add(district);
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
    private TreeVo buildTree(District parent, List<District> children) {
        long id = parent.getDistrictId();
        List<? super TreeVo> childNodes = Lists.newArrayList();
        Iterator<District> it = children.iterator();
        while (it.hasNext()) {
            District item = it.next();
            if (item.getParentId().equals(id)) {
                it.remove();
                List<District> others = Lists.newArrayList();
                others.addAll(children);
                childNodes.add(this.buildTree(item, others));
            }
        }

        TreeVo vo = new TreeVo();
        vo.setId(parent.getDistrictId().toString());
        vo.setText(parent.getDistrictName());
        vo.setChildren(childNodes);
        if (CITY.equalsIgnoreCase(parent.getDistrictLevel())) {
            vo.setIsLeaf(true); // 城市节点
        }

        return vo;
    }
}
