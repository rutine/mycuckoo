package com.mycuckoo.web.platform;


import com.mycuckoo.domain.platform.Resource;
import com.mycuckoo.service.platform.ResourceService;
import com.mycuckoo.utils.SessionUtil;
import com.mycuckoo.utils.TreeHelper;
import com.mycuckoo.vo.SimpleTree;
import com.mycuckoo.vo.platform.ResourceTreeVo;
import com.mycuckoo.web.vo.AjaxResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 功能说明: 资源Controller
 *
 * @author rutine
 * @version 4.1.0
 * @time May 5, 2024 4:14:15 PM
 */
@RestController
@RequestMapping(value = "/platform/resource/mgr")
public class ResourceController {
    private static Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private ResourceService resourceService;


    /**
     * 功能说明 : 资源管理界面入口
     *
     * @return
     * @author rutine
     * @time May 5, 2024 4:21:31 PM
     */
    @GetMapping
    public AjaxResponse<List<? extends SimpleTree>> list() {
        List<ResourceTreeVo> all = resourceService.findAll();
        List<? extends SimpleTree> list = TreeHelper.buildTree(all, "0");

        return AjaxResponse.create(list);
    }

    /**
     * 功能说明 : 创建资源
     *
     * @param resource
     * @return
     * @author rutine
     * @time May 5, 2024 4:21:31 PM
     */
    @PostMapping
    public AjaxResponse<String> create(@RequestBody Resource resource) {
        resource.setCreateDate(new Date());
        resource.setCreator(SessionUtil.getUserCode());
        resourceService.save(resource);

        return AjaxResponse.create("保存成功");
    }

    /**
     * 功能说明 : 修改资源
     *
     * @param resource
     * @return
     * @author rutine
     * @time May 5, 2024 4:21:31 PM
     */
    @PutMapping
    public AjaxResponse<String> update(@RequestBody Resource resource) {
        resourceService.update(resource);

        return AjaxResponse.create("保存成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<Resource> get(@PathVariable long id) {
        Resource resource = resourceService.get(id);

        return AjaxResponse.create(resource);
    }

    /**
     * 功能说明 : 停用/启用资源
     *
     * @param id
     * @param disEnableFlag 停用/启用标志
     * @return
     * @author rutine
     * @time May 5, 2024 4:25:31 PM
     */
    @PutMapping("/{id}/disEnable/{disEnableFlag}")
    public AjaxResponse<String> disEnable(
            @PathVariable long id,
            @PathVariable String disEnableFlag) {

        resourceService.disEnable(id, disEnableFlag);

        return AjaxResponse.create("操作成功");
    }
}
