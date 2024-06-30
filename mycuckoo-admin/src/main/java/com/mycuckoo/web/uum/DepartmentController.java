package com.mycuckoo.web.uum;

import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.SimpleTree;
import com.mycuckoo.core.util.CommonUtils;
import com.mycuckoo.core.util.TreeHelper;
import com.mycuckoo.domain.uum.Department;
import com.mycuckoo.service.uum.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能说明: 部门管理Controller
 *
 * @author rutine
 * @version 4.1.0
 * @time May 19, 2024 12:36:22 AM
 */
@RestController
@RequestMapping("/uum/dept/mgr")
public class DepartmentController {
    private static Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentService deptService;


    /**
     * 功能说明 : 列表展示页面
     */
    @GetMapping
    public AjaxResponse<List<? extends SimpleTree>> list(Querier querier) {
        List<? extends SimpleTree> all = deptService.findAll(Querier.EMPTY);
        String name = (String) querier.getRequired("name");
        if (CommonUtils.isNotBlank(name)) {
            all = TreeHelper.treeFilter(all, name, (node, keyword) -> node.getText().contains(keyword));
        }

        return AjaxResponse.create(all);
    }

    @PostMapping
    public AjaxResponse<String> create(@RequestBody Department entity) {
        deptService.save(entity);

        return AjaxResponse.success("保存成功");
    }

    @PutMapping
    public AjaxResponse<String> update(@RequestBody Department entity) {
        deptService.update(entity);

        return AjaxResponse.success("修改成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<Department> get(@PathVariable long id) {
        Department entity = deptService.get(id);

        return AjaxResponse.create(entity);
    }

    @PutMapping("/{id}/disEnable/{disEnableFlag}")
    public AjaxResponse<String> disEnable(
            @PathVariable long id,
            @PathVariable String disEnableFlag) {

        deptService.disEnable(id, disEnableFlag);

        return AjaxResponse.success("停用启用成功");
    }

    @GetMapping("/{id}/child/nodes")
    public AjaxResponse<List<? extends SimpleTree>> getChildNodes(
            @PathVariable long id,
            @RequestParam(value = "isCheckbox", defaultValue = "false") Boolean isCheckbox,
            @RequestParam(value = "withRole", defaultValue = "false") Boolean withRole) {
        List<? extends SimpleTree> asyncTreeList = deptService.findChildNodes(id, isCheckbox, withRole);

        return AjaxResponse.create(asyncTreeList);
    }

    @PutMapping("/{id}/assign")
    public AjaxResponse<String> assign(
            @PathVariable long id,
            @RequestBody Department dept) {

        Assert.notNull(dept.getRoleId(), "角色id不能为空!");

        deptService.assignRole(id, dept.getRoleId());

        return AjaxResponse.success("分配角色成功");
    }

}
