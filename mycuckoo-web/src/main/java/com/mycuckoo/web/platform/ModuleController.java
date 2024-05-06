package com.mycuckoo.web.platform;


import com.mycuckoo.domain.platform.ModuleMenu;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.platform.ModuleService;
import com.mycuckoo.utils.SessionUtil;
import com.mycuckoo.vo.AssignVo;
import com.mycuckoo.vo.CheckBoxTree;
import com.mycuckoo.vo.SimpleTree;
import com.mycuckoo.vo.platform.ModuleMenuVo;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.mycuckoo.web.constant.ActionConst.LIMIT;

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
     * @param treeId     查找指定节点下的模块`
     * @param code       模块编码
     * @param name       模块名称
     * @param pageNo     第几页
     * @param pageSize   页面大小, 暂时没有使用
     * @return
     * @author rutine
     * @time Dec 2, 2012 8:22:41 PM
     */
    @GetMapping
    public AjaxResponse<Page<ModuleMenuVo>> list(@RequestParam(defaultValue = "-1") long treeId,
                                                 @RequestParam(defaultValue = "") String code,
                                                 @RequestParam(defaultValue = "") String name,
                                                 @RequestParam(defaultValue = "1") int pageNo,
                                                 @RequestParam(defaultValue = LIMIT + "") int pageSize) {

        Page<ModuleMenuVo> page = moduleService.findByPage(treeId, code, name,
                new PageRequest(pageNo - 1, pageSize));

        return AjaxResponse.create(page);
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
    public AjaxResponse<AssignVo<CheckBoxTree, Long>> listOperation(@PathVariable long id) {

        AssignVo<CheckBoxTree, Long> vo = moduleService.findOperationTreeByModId(id);

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

        logger.debug(JsonUtils.toJson(modMenu));

        modMenu.setCreateDate(new Date());
        modMenu.setCreator(SessionUtil.getUserCode());
        moduleService.save(modMenu);

        return AjaxResponse.create("保存模块成功");
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

        return AjaxResponse.create("修改模块成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<ModuleMenuVo> get(@PathVariable long id) {
        ModuleMenuVo moduleMenu = moduleService.get(id);
        ModuleMenuVo parentMenu = moduleService.get(moduleMenu.getParentId());
        moduleMenu.setParentName(parentMenu.getName());

        return AjaxResponse.create(moduleMenu);
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

        return AjaxResponse.create("分配模块权限成功");
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

        return AjaxResponse.create("模块菜单删除成功");
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

        return AjaxResponse.create("操作成功");
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

        List<? extends SimpleTree> asyncTreeList = moduleService.findChildNodes(id, false);

        return AjaxResponse.create(asyncTreeList);
    }
}