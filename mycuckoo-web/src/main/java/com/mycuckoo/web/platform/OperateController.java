package com.mycuckoo.web.platform;


import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.platform.Operate;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.platform.OperateService;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;
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

import static com.mycuckoo.web.constant.ActionVariable.LIMIT;

/**
 * 功能说明: 操作按钮Controller
 *
 * @author rutine
 * @version 3.0.0
 * @time Oct 12, 2014 3:41:26 PM
 */
@RestController
@RequestMapping(value = "/platform/operate/mgr")
public class OperateController {
    private static Logger logger = LoggerFactory.getLogger(OperateController.class);

    @Autowired
    private OperateService optService;


    /**
     * 功能说明 : 操作按钮管理界面入口
     *
     * @param optName     操作名称
     * @param pageNo      页码
     * @param pageSize    每页大小
     * @return
     * @author rutine
     * @time Jun 2, 2013 5:52:09 PM
     */
    @GetMapping
    public AjaxResponse<Page<Operate>> list(
            @RequestParam(value = "optName", defaultValue = "") String optName,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {

        logger.info("---------------- 请求操作按钮管理界面 -----------------");

        Page<Operate> page = optService.findByPage(optName, new PageRequest(pageNo - 1, pageSize));

        return AjaxResponse.create(page);
    }

    /**
     * 功能说明 : 创建模块操作
     *
     * @param operate
     * @return
     * @author rutine
     * @time Jun 2, 2013 8:30:06 PM
     */
    @PostMapping
    public AjaxResponse<String> create(@RequestBody Operate operate) {
        logger.debug(JsonUtils.toJson(operate));

        operate.setCreateDate(new Date());
        operate.setCreator(SessionUtil.getUserCode());
        optService.save(operate);

        return AjaxResponse.create("保存成功");
    }

    /**
     * 功能说明 : 修改模块操作
     *
     * @param operate
     * @return
     * @author rutine
     * @time Jun 2, 2013 8:52:32 PM
     */
    @PutMapping
    public AjaxResponse<String> update(@RequestBody Operate operate) {
        optService.update(operate);

        return AjaxResponse.create("保存成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<Operate> get(@PathVariable long id) {
        Operate operate = optService.get(id);

        return AjaxResponse.create(operate);
    }

    /**
     * 功能说明 : 停用/启用模块操作
     *
     * @param id
     * @param disEnableFlag 停用/启用标志
     * @return
     * @author rutine
     * @time Jun 2, 2013 9:00:07 PM
     */
    @PutMapping("/{id}/disEnable/{disEnableFlag}")
    public AjaxResponse<String> disEnable(
            @PathVariable long id,
            @PathVariable String disEnableFlag) {

        optService.disEnable(id, disEnableFlag);

        return AjaxResponse.create("操作成功");
    }
}
