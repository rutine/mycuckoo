package com.mycuckoo.service.platform;

import com.google.common.collect.Lists;
import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.SimpleTree;
import com.mycuckoo.core.exception.MyCuckooException;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageImpl;
import com.mycuckoo.domain.platform.DictSmallType;
import com.mycuckoo.domain.platform.District;
import com.mycuckoo.repository.platform.DistrictMapper;
import com.mycuckoo.core.util.TreeHelper;
import com.mycuckoo.core.util.web.SessionContextHolder;
import com.mycuckoo.web.vo.res.platform.DistrictVo;
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
import java.util.Map;
import java.util.stream.Collectors;

import static com.mycuckoo.constant.AdminConst.*;

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


    @Transactional
    public boolean disEnable(long districtId, String disEnableFlag) {
        boolean enable = ENABLE.equals(disEnableFlag);
        if (!enable) {
            int count = districtMapper.countByParentId(districtId);
            if (count > 0) { //有下级地区
                throw new MyCuckooException("存在下级地区, 停用失败!");
            }

            districtMapper.update(new District(districtId, DISABLE));
        } else {
            districtMapper.update(new District(districtId, ENABLE));

        }

        District district = get(districtId);
        writeLog(district, LogLevel.SECOND, enable ? OptName.ENABLE : OptName.DISABLE);

        return true;
    }

    public boolean existByName(String districtName) {
        int count = districtMapper.countByName(districtName);
        if (count > 0) return true;

        return false;
    }

    public Page<DistrictVo> findByPage(long treeId, Querier querier) {
        List<Long> idList = new ArrayList<Long>();
        if (treeId >= 0) {
            idList = findChildIds(treeId, 0); // 过滤出所有下级
            if (idList.isEmpty()) {
                idList.add(-1l);
            }
        }

        querier.putQ("array", idList.isEmpty() ? null : idList.toArray(new Long[idList.size()]));
        Page<District> pageResult = districtMapper.findByPage(querier.getQ(), querier);
        List<DictSmallType> dictSmallTypeList = dictionaryService.findSmallTypesByBigTypeCode(DICT_DISTRICT);
        Map<String, String> dicSmallTypeMap = dictSmallTypeList.stream()
                .collect(Collectors.toMap(k -> k.getCode().toLowerCase(), DictSmallType::getName));

        List<DistrictVo> vos = Lists.newArrayList();
        for (District entity : pageResult.getContent()) {
            String distLevel = entity.getLevel().toLowerCase();
            if (dicSmallTypeMap.containsKey(distLevel)) {
                entity.setLevel(dicSmallTypeMap.get(distLevel));
            }

            DistrictVo vo = new DistrictVo();
            BeanUtils.copyProperties(entity, vo);
            vo.setParentName(get(entity.getParentId()).getName());//上级地区名称
            vos.add(vo);
        }

        return new PageImpl<>(vos, querier, pageResult.getTotalElements());
    }

    public DistrictVo get(Long districtId) {
        if (districtId == null) {
            return null;
        }

        District district = districtMapper.get(districtId);
        DistrictVo vo = new DistrictVo();
        BeanUtils.copyProperties(district, vo);

        return vo;
    }

    public List<? extends SimpleTree> findChildNodes(long districtId) {
        List<District> all = districtMapper.findByPage(null, Querier.EMPTY).getContent();

        List<? extends SimpleTree> vos = toTree(all);

        return TreeHelper.buildTree(vos, String.valueOf(districtId));
    }

    @Transactional
    public void update(District district) {
        District old = get(district.getDistrictId());
        Assert.notNull(old, "地区不存在!");
        Assert.state(old.getName().equals(district.getName())
                || !existByName(district.getName()), "地区名称[" + district.getName() + "]已存在!");

        district.setUpdateTime(LocalDateTime.now());
        district.setUpdator(SessionContextHolder.getUserId().toString());
        districtMapper.update(district);

        writeLog(district, LogLevel.SECOND, OptName.MODIFY);
    }

    @Transactional
    public void save(District district) {
        Assert.state(!existByName(district.getName()), "地区名称[" + district.getName() + "]已存在!");
        district.setParentId(district.getParentId());
        district.setStatus(ENABLE);
        district.setUpdateTime(LocalDateTime.now());
        district.setUpdator(SessionContextHolder.getUserId().toString());
        district.setCreateTime(LocalDateTime.now());
        district.setCreator(SessionContextHolder.getUserId().toString());
        districtMapper.save(district);

        writeLog(district, LogLevel.FIRST, OptName.SAVE);
    }


    // --------------------------- 私有方法 -------------------------------

    /**
     * 公用地区写日志
     *
     * @param entity 地区对象
     * @param logLevel
     * @param opt
     * @throws MyCuckooException
     * @author rutine
     * @time Oct 16, 2012 7:38:53 PM
     */
    private void writeLog(District entity, LogLevel logLevel, OptName opt) {
        LogOperator.begin()
                .module(ModuleName.SYS_DISTRICT)
                .operate(opt)
                .id(entity.getDistrictId())
                .title(null)
                .content("地区名称：%s, 地区级别：%s", entity.getName(), entity.getLevel())
                .level(logLevel)
                .emit();
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
        List<District> all = districtMapper.findByPage(null, Querier.EMPTY).getContent();

        List<? extends SimpleTree> vos = toTree(all);
        List<? extends SimpleTree> trees = TreeHelper.buildTree(vos, String.valueOf(districtId));

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
            tree.setText(mapper.getName());
            if (CITY.equalsIgnoreCase(mapper.getLevel())) {
                tree.setIsLeaf(true); // 城市节点
            }

            return tree;
        }).collect(Collectors.toList());
    }
}
