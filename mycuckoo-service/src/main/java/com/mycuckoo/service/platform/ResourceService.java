package com.mycuckoo.service.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.domain.platform.ModuleMenu;
import com.mycuckoo.domain.platform.Resource;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.operator.LogOperator;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.ResourceMapper;
import com.mycuckoo.utils.CommonUtils;
import com.mycuckoo.utils.TreeHelper;
import com.mycuckoo.vo.SimpleTree;
import com.mycuckoo.vo.platform.ModuleMenuVo;
import com.mycuckoo.vo.platform.ResourceTreeVo;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

import static com.mycuckoo.constant.ServiceConst.*;
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

    public List<? extends SimpleTree> findAll() {
        List<ModuleMenuVo> menuVos = moduleService.findAll();

        List<ResourceTreeVo> all = Lists.newArrayList();
        for (ModuleMenuVo menu : menuVos) {
            ResourceTreeVo tree = new ResourceTreeVo();
            tree.setId(menu.getModuleId() + "");
            tree.setParentId(menu.getParentId() + "");
            tree.setText(menu.getName());
            tree.setLevel(menu.getLevel());
            tree.setOrder(menu.getOrder());
            //取反
            tree.setIsLeaf(true);
            all.add(tree);
        }

        List<Resource> resources = resourceMapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE)).getContent();
        resources.forEach(o -> {
            ResourceTreeVo tree = new ResourceTreeVo();
            tree.setId(LEAF_ID + o.getResourceId());
            tree.setParentId(o.getModuleId() + "");
            tree.setText(o.getName());
            tree.setMethod(o.getMethod());
            tree.setPath(o.getPath());
            tree.setOrder(o.getOrder());
            tree.setMemo(o.getMemo());
            tree.setStatus(o.getStatus());
            tree.setCreator(o.getCreator());
            tree.setCreateDate(o.getCreateDate());
            tree.setLevel(FOUR.value());
            //取反
            tree.setIsLeaf(false);
            all.add(tree);
        });

        return TreeHelper.buildTree(all, "0");
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
        Assert.notNull(operateService.get(entity.getOperateId()), "操作不存在!");
        Assert.state(resourceMapper.exists(entity.getResourceId()), "资源不存在!");

        resourceMapper.update(entity);

        writeLog(entity, LogLevel.SECOND, OptName.MODIFY);
    }

    @Transactional
    public void save(Resource entity) {
        ModuleMenu menu = moduleService.get(entity.getModuleId());
        Assert.notNull(menu, "菜单不存在!");
        Assert.state(THREE.value().equals(menu.getLevel()), "请选择三个菜单!");
        Assert.notNull(operateService.get(entity.getOperateId()), "操作不存在!");
        entity.setStatus(ENABLE);
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
