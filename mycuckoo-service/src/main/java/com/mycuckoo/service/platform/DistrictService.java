package com.mycuckoo.service.platform;

import com.google.common.collect.Lists;
import com.mycuckoo.common.constant.LogLevel;
import com.mycuckoo.common.constant.OptName;
import com.mycuckoo.common.utils.TreeHelper;
import com.mycuckoo.domain.platform.DicSmallType;
import com.mycuckoo.domain.platform.District;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageImpl;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.DistrictMapper;
import com.mycuckoo.vo.SimpleTree;
import com.mycuckoo.vo.platform.DistrictVo;
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
            writeLog(district, LogLevel.SECOND, OptName.DISABLE);
        } else {
            District district = new District(districtId, ENABLE);
            districtMapper.update(district);

            district = get(districtId);
            writeLog(district, LogLevel.SECOND, OptName.ENABLE);
        }

        return true;
    }

    public boolean existByName(String districtName) {
        int count = districtMapper.countByName(districtName);
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
        Page<District> pageResult = districtMapper.findByPage(params, page);
        List<DicSmallType> dicSmallTypeList = dictionaryService.findDicSmallTypesByBigTypeCode(DISTRICT);
        Map<String, String> dicSmallTypeMap = dicSmallTypeList.stream()
                .collect(Collectors.toMap(k -> k.getSmallTypeCode().toLowerCase(), DicSmallType::getSmallTypeName));

        List<DistrictVo> vos = Lists.newArrayList();
        for (District entity : pageResult.getContent()) {
            String distLevel = entity.getDistrictLevel().toLowerCase();
            if (dicSmallTypeMap.containsKey(distLevel)) {
                entity.setDistrictLevel(dicSmallTypeMap.get(distLevel));
            }

            DistrictVo vo = new DistrictVo();
            BeanUtils.copyProperties(entity, vo);
            vo.setParentName(get(entity.getParentId()).getDistrictName());//上级地区名称
            vos.add(vo);
        }

        return new PageImpl<>(vos, page, pageResult.getTotalElements());
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

    public List<? super SimpleTree> findChildNodes(long districtId) {
        List<District> all = districtMapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE)).getContent();

        List<? extends SimpleTree> vos = toTree(all);

        return TreeHelper.buildTree(vos, String.valueOf(districtId));
    }

    @Transactional
    public void update(District district) {
        District old = get(district.getDistrictId());
        Assert.notNull(old, "地区不存在!");
        Assert.state(old.getDistrictName().equals(district.getDistrictName())
                || !existByName(district.getDistrictName()), "地区名称[" + district.getDistrictName() + "]已存在!");

        districtMapper.update(district);

        writeLog(district, LogLevel.SECOND, OptName.MODIFY);
    }

    @Transactional
    public void save(District district) {
        Assert.state(!existByName(district.getDistrictName()), "地区名称[" + district.getDistrictName() + "]已存在!");
        district.setStatus(ENABLE);
        districtMapper.save(district);

        writeLog(district, LogLevel.FIRST, OptName.SAVE);
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
    private void writeLog(District district, LogLevel logLevel, OptName opt) {
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

        List<? extends SimpleTree> vos = toTree(all);
        List<SimpleTree> trees = TreeHelper.buildTree(vos, String.valueOf(districtId));

        List<String> nodeIds = Lists.newArrayList();
        TreeHelper.collectNodeIds(nodeIds, trees);

        //过滤出所有下级节点ID
        List<Long> ids = nodeIds.stream().map(Long::valueOf).collect(Collectors.toList());

        if (flag == 1) {
            List<Long> allIds = all.stream().map(District::getDistrictId).collect(Collectors.toList());
            allIds.remove(0L);  //删除根元素
            allIds.remove(districtId);
            allIds.removeAll(ids);

            ids = allIds;
        }

        return ids;
    }

    /**
     * 转换树vo
     *
     * @param list 地区
     * @return
     * @author rutine
     * @time Oct 29, 2020 17:39:35 PM
     */
    private List<? extends SimpleTree> toTree(List<District> list) {
        return list.stream().map(mapper -> {
            SimpleTree tree = new SimpleTree();
            tree.setId(mapper.getDistrictId().toString());
            tree.setParentId(mapper.getParentId().toString());
            tree.setText(mapper.getDistrictName());
            if (CITY.equalsIgnoreCase(mapper.getDistrictLevel())) {
                tree.setIsLeaf(true); // 城市节点
            }

            return tree;
        }).collect(Collectors.toList());
    }
}
