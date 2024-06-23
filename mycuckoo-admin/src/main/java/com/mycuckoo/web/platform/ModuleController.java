package com.mycuckoo.web.platform;


import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.CheckboxTree;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.SimpleTree;
import com.mycuckoo.domain.platform.ModResRef;
import com.mycuckoo.domain.platform.ModuleMenu;
import com.mycuckoo.service.platform.ModuleService;
import com.mycuckoo.core.util.TreeHelper;
import com.mycuckoo.web.vo.res.platform.ModuleMenuVos;
import com.mycuckoo.web.vo.res.uum.AssignVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mycuckoo.constant.AdminConst.ID_ROOT_VALUE;

/**
 * 功能说明: 模块菜单Controller
 *
 * @author rutine
 * @version 3.0.0
 * @time Oct 2, 2014 7:37:26 AM
 */
@RestController
@RequestMapping("/platform/module/mgr")
public class ModuleController {
    private static Logger logger = LoggerFactory.getLogger(ModuleController.class);

    @Autowired
    private ModuleService moduleService;


    /**
     * 功能说明 : 列表展示页面
     *
     * @param querier  查询参数
     * @return
     * @author rutine
     * @time Dec 2, 2012 8:22:41 PM
     */
    @GetMapping
    public AjaxResponse<List<? extends SimpleTree>> list(Querier querier) {
        querier.setPageSize(0);
        List<? extends SimpleTree> all = moduleService.findByPage(querier);

        List<? extends SimpleTree> list = TreeHelper.buildTree(all, ID_ROOT_VALUE);

        return AjaxResponse.create(list);
    }

    /**
     * 功能说明 : 获得模块已经分配和未分配的操作列表
     *
     * @param id
     * @return
     * @author rutine
     * @time May 9, 2013 8:52:56 PM
     */
    @GetMapping(value = "/{id}/operation")
    public AjaxResponse<AssignVo<CheckboxTree, Long>> listOperation(@PathVariable long id) {

        AssignVo<CheckboxTree, Long> vo = moduleService.findOperationTreeByModId(id);

        return AjaxResponse.create(vo);
    }

    /**
     * 功能说明 : 获得模块已经分配和未分配的资源
     *
     * @param id
     * @return
     * @author rutine
     * @time May 12, 2024 7:35:46 PM
     */
    @GetMapping(value = "/{id}/resource")
    public AjaxResponse<AssignVo<SimpleTree, String>> listResource(@PathVariable long id) {

        AssignVo<SimpleTree, String> vo = moduleService.findResourceTreeByModId(id);

        return AjaxResponse.create(vo);
    }

    /**
     * 功能说明 : 创建模块
     *
     * @return
     * @author rutine
     * @time Jun 1, 2013 9:13:49 AM
     */
    @PostMapping
    public AjaxResponse<String> create(@RequestBody ModuleMenu modMenu) {
        moduleService.save(modMenu);

        return AjaxResponse.success("保存模块成功");
    }

    /**
     * 功能说明 : 修改模块
     *
     * @return
     * @author rutine
     * @time Jun 1, 2013 3:45:56 PM
     */
    @PutMapping
    public AjaxResponse<String> update(@RequestBody ModuleMenu modMenu) {
        moduleService.update(modMenu);

        return AjaxResponse.success("修改模块成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<ModuleMenuVos.Detail> get(@PathVariable long id) {
        ModuleMenu menu = moduleService.get(id);
        ModuleMenu parent = moduleService.get(menu.getParentId());

        ModuleMenuVos.Detail vo = new ModuleMenuVos.Detail();
        BeanUtils.copyProperties(menu, vo);
        vo.setParentName(parent.getName());

        return AjaxResponse.create(vo);
    }

    /**
     * 功能说明 : 保存模块操作关系
     *
     * @param id      模块ID
     * @param optIds  模块操作列表
     * @return
     * @author rutine
     * @time May 12, 2013 5:44:16 PM
     */
    @PostMapping(value = "/{id}/module-opt-ref")
    public AjaxResponse<String> createModuleOptRefs(
            @PathVariable long id,
            @RequestBody List<Long> optIds) {

        moduleService.saveModuleOptRefs(id, optIds);

        return AjaxResponse.success("分配模块权限成功");
    }

    /**
     * 功能说明 : 保存模块资源关系
     *
     * @param id            模块ID
     * @param modResRefs    模块资源列表
     * @return
     * @author rutine
     * @time May 12, 2024 12:04:13 AM
     */
    @PostMapping(value = "/{id}/module-res-ref")
    public AjaxResponse<String> createModuleResRefs(
            @PathVariable long id,
            @RequestBody List<ModResRef> modResRefs) {
        int order = 1;
        int mask = (1 << 10) - 1;
        boolean fund = false;
        for (ModResRef ref : modResRefs) {
            Assert.state((mask & ref.getGroup())  == ref.getGroup(), "应用配置有误!");
            if ((1 & ref.getGroup()) == 1) {
                Assert.state(!fund, "一个模块仅允许一个列表应用配置");
                fund = true;
            }
            ref.setOrder(order++);
        }

        moduleService.saveModuleResRefs(id, modResRefs);

        return AjaxResponse.success("分配模块权限成功");
    }

    /**
     * 功能说明 : 删除模块
     *
     * @param id
     * @return
     * @author rutine
     * @time Jun 2, 2013 11:09:30 AM
     */
    @DeleteMapping("/{id}")
    public AjaxResponse<String> delete(@PathVariable long id) {

        moduleService.delete(id);

        return AjaxResponse.success("模块菜单删除成功");
    }

    /**
     * 功能说明 : 停用/启用模块
     *
     * @param id
     * @param disEnableFlag 停用/启用标志
     * @return
     * @author rutine
     * @time Jun 2, 2013 1:53:04 PM
     */
    @PutMapping("/{id}/disEnable/{disEnableFlag}")
    public AjaxResponse<String> disEnable(
            @PathVariable long id,
            @PathVariable String disEnableFlag) {

        boolean disEnableBol = moduleService.disEnable(id, disEnableFlag);

        return AjaxResponse.success("操作成功");
    }

    /**
     * 功能说明 : 获取模块的下级模块
     *
     * @param id             模块id
     * @author rutine
     * @time Dec 1, 2012 1:45:37 PM
     */
    @GetMapping("/{id}/child/nodes")
    public AjaxResponse<List<? extends SimpleTree>> getChildNodes(@PathVariable long id) {

        List<? extends SimpleTree> asyncTreeList = moduleService.findChildNodes(id);

        return AjaxResponse.create(asyncTreeList);
    }
}