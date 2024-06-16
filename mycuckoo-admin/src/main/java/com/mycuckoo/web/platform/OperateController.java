package com.mycuckoo.web.platform;


import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.Operate;
import com.mycuckoo.service.platform.OperateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @param querier   查询参数
     * @return
     * @author rutine
     * @time Jun 2, 2013 5:52:09 PM
     */
    @GetMapping
    public AjaxResponse<Page<Operate>> list(Querier querier) {
        Page<Operate> page = optService.findByPage(querier);

        return AjaxResponse.create(page);
    }

    /**
     * 功能说明 : 创建操作
     *
     * @param operate
     * @return
     * @author rutine
     * @time Jun 2, 2013 8:30:06 PM
     */
    @PostMapping
    public AjaxResponse<String> create(@RequestBody Operate operate) {
        optService.save(operate);

        return AjaxResponse.success("保存成功");
    }

    /**
     * 功能说明 : 修改操作
     *
     * @param operate
     * @return
     * @author rutine
     * @time Jun 2, 2013 8:52:32 PM
     */
    @PutMapping
    public AjaxResponse<String> update(@RequestBody Operate operate) {
        optService.update(operate);

        return AjaxResponse.success("保存成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<Operate> get(@PathVariable long id) {
        Operate operate = optService.get(id);

        return AjaxResponse.create(operate);
    }

    /**
     * 功能说明 : 停用/启用操作
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

        return AjaxResponse.success("操作成功");
    }
}
