package com.mycuckoo.web.uum;

import com.mycuckoo.utils.SessionUtil;
import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.uum.OrganService;
import com.mycuckoo.vo.SimpleTree;
import com.mycuckoo.vo.uum.OrganVo;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static com.mycuckoo.web.constant.ActionConst.LIMIT;

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
     * @param code  机构代码
     * @param name  机构名称
     * @param pageNo   第几页
     * @param pageSize 每页显示数量, 暂时没有使用
     * @return
     * @author rutine
     * @time Jul 2, 2013 3:31:22 PM
     */
    @GetMapping
    public AjaxResponse<Page<OrganVo>> list(
            @RequestParam(value = "code", defaultValue = "") String code,
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {

        code = StringUtils.isNotBlank(code) ? "%" + code + "%" : null;
        name = StringUtils.isNotBlank(name) ? "%" + name + "%" : null;

        Page<OrganVo> page = organService.findByPage(code, name, new PageRequest(pageNo - 1, pageSize));

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

        return AjaxResponse.create("保存机构成功");
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

        return AjaxResponse.create("修改机构成功");
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

        return AjaxResponse.create("停用启用成功");
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
            @RequestParam(value = "isCheckbox", defaultValue = "N") String isCheckbox) {
        List<? extends SimpleTree> asyncTreeList = organService.findChildNodes(id, isCheckbox);

        logger.debug("json --> {}", JsonUtils.toJson(asyncTreeList));

        return AjaxResponse.create(asyncTreeList);
    }

}
