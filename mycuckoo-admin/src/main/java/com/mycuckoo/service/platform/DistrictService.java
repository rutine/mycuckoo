package com.mycuckoo.service.platform;

import com.google.common.collect.Lists;
import com.mycuckoo.core.constant.enums.ModuleName;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.SimpleTree;
import com.mycuckoo.core.exception.MyCuckooException;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageImpl;
import com.mycuckoo.domain.platform.DictionaryItem;
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
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mycuckoo.constant.AdminConst.*;

/**
 * 功能说明: 地区业务类
 *
 * @author rutine
 * @version 5.0.0
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


    public boolean existByName(String name) {
        int count = districtMapper.countByName(name);
        if (count > 0) return true;

        return false;
    }

    public Page<DistrictVo> findByPage(String treeId, Querier querier) {
        List<String> codeList = new ArrayList<>();
        if (Objects.nonNull(treeId)) {
            codeList = findChildCodes(treeId, 0); // 过滤出所有下级
        }

        querier.putQ("array", codeList.isEmpty() ? null : codeList.toArray(new Long[codeList.size()]));
        Page<District> pageResult = districtMapper.findByPage(querier.getQ(), querier);
        List<DictionaryItem> dicts = dictionaryService.findItemsByDictCode(DICT_DISTRICT);
        Map<String, String> dictMap = dicts.stream()
                .collect(Collectors.toMap(k -> k.getCode().toLowerCase(), DictionaryItem::getName));

        List<DistrictVo> vos = Lists.newArrayList();
        for (District entity : pageResult.getContent()) {
            String type = entity.getType().toLowerCase();
            if (dictMap.containsKey(type)) {
                entity.setType(dictMap.get(type));
            }

            DistrictVo vo = new DistrictVo();
            BeanUtils.copyProperties(entity, vo);
            vos.add(vo);
        }

        return new PageImpl<>(vos, querier, pageResult.getTotalElements());
    }

    public DistrictVo get(Long districtId) {
        if (districtId == null) {
            return null;
        }

        District district = districtMapper.get(districtId);
        if (district == null) {
            return null;
        }

        DistrictVo vo = new DistrictVo();
        BeanUtils.copyProperties(district, vo);

        return vo;
    }

    public DistrictVo getByCode(String code) {
        Assert.hasLength(code, "编码不能为空");

        District district = districtMapper.getByCode(code.trim());
        DistrictVo vo = new DistrictVo();
        BeanUtils.copyProperties(district, vo);

        return vo;
    }

    public List<? extends SimpleTree> findChildNodes(String code) {
        List<District> all = districtMapper.findByPage(null, Querier.EMPTY).getContent();

        List<? extends SimpleTree> vos = toTree(all);

        return TreeHelper.buildTree(vos, code);
    }

    @Transactional
    public void update(District district) {
        District old = get(district.getId());
        Assert.notNull(old, "地区不存在!");
        Assert.state(old.getName().equals(district.getName())
                || !existByName(district.getName()), "名称[" + district.getName() + "]已存在!");

        district.setUpdateTime(LocalDateTime.now());
        district.setUpdator(SessionContextHolder.getUserId().toString());
        districtMapper.update(district);

        writeLog(district, "修改");
    }

    @Transactional
    public void save(District district) {
        Assert.state(!existByName(district.getName()), "名称[" + district.getName() + "]已存在!");
        district.setUpdateTime(LocalDateTime.now());
        district.setUpdator(SessionContextHolder.getUserId().toString());
        district.setCreateTime(LocalDateTime.now());
        district.setCreator(SessionContextHolder.getUserId().toString());
        districtMapper.save(district);

        writeLog(district, "新增");
    }


    // --------------------------- 私有方法 -------------------------------

    /**
     * 公用地区写日志
     *
     * @param entity 地区对象
     * @param action
     * @throws MyCuckooException
     * @author rutine
     * @time Oct 16, 2012 7:38:53 PM
     */
    private void writeLog(District entity, String action) {
        LogOperator.begin()
                .module(ModuleName.SYS_DISTRICT)
                .id(entity.getId())
                .title(SessionContextHolder.getUserName() + action + "地区")
                .content("地区名称：%s, 地区级别：%s", entity.getName(), entity.getType())
                .emit();
    }

    /**
     * 根据地区id查询所有地区节点
     *
     * @param code  上级地区code
     * @param flag  0为下级，1 为上级
     * @return
     * @author rutine
     * @time Oct 16, 2012 8:31:35 PM
     */
    private List<String> findChildCodes(String code, int flag) {
        List<District> all = districtMapper.findByPage(null, Querier.EMPTY).getContent();

        List<? extends SimpleTree> vos = toTree(all);
        List<? extends SimpleTree> trees = TreeHelper.buildTree(vos, code);

        List<String> nodeIds = Lists.newArrayList();
        TreeHelper.collectNodeIds(nodeIds, trees);

        //过滤出所有下级节点code
        List<String> ids = nodeIds;

        if (flag == 1) {
            List<String> allIds = all.stream().map(District::getCode).collect(Collectors.toList());
            allIds.remove("0");  //删除根元素
            allIds.remove(code);
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
            tree.setId(mapper.getCode());
            tree.setParentId(mapper.getParentCode() == null ? ID_ROOT_VALUE : mapper.getParentCode());
            tree.setText(mapper.getName());
            if ("county".equalsIgnoreCase(mapper.getType())) {
                tree.setIsLeaf(true); // 城市节点
            }

            return tree;
        }).collect(Collectors.toList());
    }
}
