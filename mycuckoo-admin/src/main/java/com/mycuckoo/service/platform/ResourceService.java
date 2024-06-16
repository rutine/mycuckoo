package com.mycuckoo.service.platform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.exception.ApplicationException;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.Pageable;
import com.mycuckoo.domain.platform.ModuleMenu;
import com.mycuckoo.domain.platform.Operate;
import com.mycuckoo.domain.platform.Resource;
import com.mycuckoo.repository.platform.ResourceMapper;
import com.mycuckoo.util.CommonUtils;
import com.mycuckoo.util.web.SessionUtil;
import com.mycuckoo.web.vo.res.platform.ModuleMenuVo;
import com.mycuckoo.web.vo.res.platform.ResourceVos;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mycuckoo.constant.AdminConst.*;
import static com.mycuckoo.constant.enums.ModuleLevel.FOUR;
import static com.mycuckoo.constant.enums.ModuleLevel.THREE;

/**
 * 功能说明: 资源业务类
 *
 * @author rutine
 * @version 4.1.0
 * @time May 5, 2024 12:03:15 AM
 */
@Service
@Transactional(readOnly = true)
public class ResourceService {
    static Logger logger = LoggerFactory.getLogger(ResourceService.class);

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private ModuleService moduleService;
    @Autowired
    private OperateService operateService;


    @Transactional
    public boolean disEnable(long id, String disEnableFlag) {
        boolean enable = ENABLE.equals(disEnableFlag);
        Resource entity = get(id);
        if (!enable) {
            entity.setStatus(DISABLE);
        } else {
            entity.setStatus(ENABLE);
        }
        resourceMapper.update(entity);

        writeLog(entity, LogLevel.SECOND, enable ?  OptName.ENABLE : OptName.DISABLE);

        return true;
    }

    public List<ResourceVos.Tree> findAll() {
        List<ModuleMenuVo> menuVos = moduleService.findAll();
        List<Operate> operates = operateService.findAll();
        Map<Long, String> operateMap = operates.stream().collect(Collectors.toMap(Operate::getOperateId, Operate::getCode));

        List<ResourceVos.Tree> all = Lists.newArrayList();
        for (ModuleMenuVo menu : menuVos) {
            ResourceVos.Tree tree = new ResourceVos.Tree();
            tree.setId(menu.getId());
            tree.setParentId(menu.getParentId() + "");
            tree.setIsParent(true);
            tree.setText(menu.getName());
            tree.setLevel(menu.getLevel());
            tree.setOrder(menu.getOrder());
            tree.setIsLeaf(false);
            all.add(tree);
        }

        List<Resource> resources = resourceMapper.findByPage(null, Querier.EMPTY).getContent();
        resources.forEach(o -> {
            ResourceVos.Tree tree = new ResourceVos.Tree();
            tree.setId(LEAF_ID + o.getResourceId());
            tree.setParentId(o.getModuleId() + "");
            tree.setIsParent(false);
            tree.setText(o.getName());
            tree.setCode(operateMap.get(o.getOperateId()));
            tree.setMethod(o.getMethod());
            tree.setPath(o.getPath());
            tree.setOrder(o.getOrder());
            tree.setMemo(o.getMemo());
            tree.setStatus(o.getStatus());
            tree.setCreator(o.getCreator());
            tree.setCreateTime(o.getCreateTime());
            tree.setLevel(FOUR.value());
            tree.setIsLeaf(true);
            all.add(tree);
        });

        return all;
    }

    public Page<Resource> findByPage(String name, Pageable page) {
        Map<String, Object> params = Maps.newHashMap();
        if (!CommonUtils.isNullOrEmpty(name)) {
            params.put("name", "%" + name + "%");
        }

        return resourceMapper.findByPage(params, page);
    }

    public Resource get(Long id) {
        return resourceMapper.get(id);
    }

    @Transactional
    public void update(Resource entity) {
        ModuleMenu menu = moduleService.get(entity.getModuleId());
        Assert.notNull(menu, "菜单不存在!");
        Assert.state(THREE.value().equals(menu.getLevel()), "请选择三个菜单!");
        Assert.state(StringUtils.isNotBlank(entity.getIdentifier())
                || operateService.get(entity.getOperateId()) != null, "操作或资源标识符不能同时为空!");
        Assert.state(resourceMapper.exists(entity.getResourceId()), "资源不存在!");

        entity.setUpdateTime(LocalDateTime.now());
        entity.setUpdator(SessionUtil.getUserCode());
        resourceMapper.update(entity);

        writeLog(entity, LogLevel.SECOND, OptName.MODIFY);
    }

    @Transactional
    public void save(Resource entity) {
        ModuleMenu menu = moduleService.get(entity.getModuleId());
        Assert.notNull(menu, "菜单不存在!");
        Assert.state(THREE.value().equals(menu.getLevel()), "请选择三个菜单!");
        Assert.state(StringUtils.isNotBlank(entity.getIdentifier())
                || operateService.get(entity.getOperateId()) != null, "操作或资源标识符不能同时为空!");

        entity.setStatus(ENABLE);
        entity.setUpdateTime(LocalDateTime.now());
        entity.setUpdator(SessionUtil.getUserCode());
        entity.setCreateTime(LocalDateTime.now());
        entity.setCreator(SessionUtil.getUserCode());
        resourceMapper.save(entity);

        writeLog(entity, LogLevel.FIRST, OptName.SAVE);
    }


    // --------------------------- 私有方法-------------------------------

    /**
     * 公用写日志
     *
     * @param entity  对象
     * @param logLevel 日志级别
     * @param opt      操作名称
     * @throws ApplicationException
     * @author rutine
     * @time May 5, 2024 12:07:21 AM
     */
    private void writeLog(Resource entity, LogLevel logLevel, OptName opt) {
        LogOperator.begin()
                .module(ModuleName.SYS_RESOURCE_MRG)
                .operate(opt)
                .id(entity.getOperateId())
                .title(null)
                .content("名称：%s, method：%s, path: %s",
                        entity.getName(), entity.getMethod(), entity.getPath())
                .level(logLevel)
                .emit();
    }

}
