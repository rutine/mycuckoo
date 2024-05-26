package com.mycuckoo.web.uum;

import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.SimpleTree;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.service.uum.OrganService;
import com.mycuckoo.util.web.SessionUtil;
import com.mycuckoo.web.vo.res.uum.OrganVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 功能说明: 机构管理Controller
 *
 * @author rutine
 * @version 3.0.0
 * @time Oct 18, 2014 1:26:18 PM
 */
@RestController
@RequestMapping("/uum/organ/mgr")
public class OrganController {
    private static Logger logger = LoggerFactory.getLogger(OrganController.class);

    @Autowired
    private OrganService organService;


    /**
     * 功能说明 : 列表展示页面
     *
     * @param querier
     * @return
     * @author rutine
     * @time Jul 2, 2013 3:31:22 PM
     */
    @GetMapping
    public AjaxResponse<Page<OrganVo>> list(Querier querier) {
        Page<OrganVo> page = organService.findByPage(querier);

        return AjaxResponse.create(page);
    }

    /**
     * 功能说明 : 创建新机构
     *
     * @param organ
     * @return
     * @author rutine
     * @time Jul 2, 2013 3:35:51 PM
     */
    @PostMapping
    public AjaxResponse<String> create(@RequestBody Organ organ) {
        organ.setUpdater(SessionUtil.getUserCode());
        organ.setUpdateDate(new Date());
        organ.setCreator(SessionUtil.getUserCode());
        organ.setCreateDate(new Date());
        organService.save(organ);

        return AjaxResponse.success("保存机构成功");
    }

    /**
     * 功能说明 : 修改机构
     *
     * @param organ
     * @return
     * @author rutine
     * @time Jul 2, 2013 3:42:51 PM
     */
    @PutMapping
    public AjaxResponse<String> update(@RequestBody Organ organ) {
        organ.setUpdater(SessionUtil.getUserCode());
        organ.setUpdateDate(new Date());
        organService.update(organ);

        return AjaxResponse.success("修改机构成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<Organ> get(@PathVariable long id) {
        Organ organ = organService.get(id);

        return AjaxResponse.create(organ);
    }

    /**
     * 功能说明 : 停用/启用机构
     *
     * @param id            机构ID
     * @param disEnableFlag 停用/启用标志
     * @return
     * @author rutine
     * @time Jul 2, 2013 3:38:46 PM
     */
    @PutMapping("/{id}/disEnable/{disEnableFlag}")
    public AjaxResponse<String> disEnable(
            @PathVariable long id,
            @PathVariable String disEnableFlag) {

        organService.disEnable(id, disEnableFlag);

        return AjaxResponse.success("停用启用成功");
    }

    /**
     * 功能说明 : 获取下级机构json数据
     *
     * @param id             机构id
     * @return
     * @author rutine
     * @time Jul 2, 2013 3:40:18 PM
     */
    @GetMapping("/{id}/child/nodes")
    public AjaxResponse<List<? extends SimpleTree>> getChildNodes(
            @PathVariable long id,
            @RequestParam(value = "isCheckbox", defaultValue = "false") boolean isCheckbox) {
        List<? extends SimpleTree> asyncTreeList = organService.findChildNodes(id, isCheckbox);

        return AjaxResponse.create(asyncTreeList);
    }

}
