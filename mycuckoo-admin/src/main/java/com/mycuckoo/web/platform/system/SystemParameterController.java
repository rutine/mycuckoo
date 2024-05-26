package com.mycuckoo.web.platform.system;


import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.SysParameter;
import com.mycuckoo.service.platform.SystemParameterService;
import com.mycuckoo.util.JsonUtils;
import com.mycuckoo.util.web.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 功能说明: 系统参数Controller
 *
 * @author rutine
 * @version 3.0.0
 * @time Oct 14, 2014 8:49:02 PM
 */
@RestController
@RequestMapping("/platform/system/parameter/mgr")
public class SystemParameterController {
    private static Logger logger = LoggerFactory.getLogger(SystemParameterController.class);

    @Autowired
    private SystemParameterService systemParameterService;


    @GetMapping
    public AjaxResponse<Page<SysParameter>> list(Querier querier) {
        Page<SysParameter> page = systemParameterService.findByPage(querier);

        return AjaxResponse.create(page);
    }

    /**
     * 功能说明 : 创建系统参数
     *
     * @param sysParameter
     * @return
     * @author rutine
     * @time Jul 2, 2013 10:10:06 AM
     */
    @PostMapping
    public AjaxResponse<String> create(@RequestBody SysParameter sysParameter) {

        logger.debug(JsonUtils.toJson(sysParameter));

        sysParameter.setUpdateDate(new Date());
        sysParameter.setUpdater(SessionUtil.getUserCode());
        sysParameter.setCreateDate(new Date());
        sysParameter.setCreator(SessionUtil.getUserCode());
        systemParameterService.save(sysParameter);

        return AjaxResponse.success("保存系统参数成功");
    }

    /**
     * 功能说明 : 修改系统参数
     *
     * @param sysParameter
     * @return
     * @author rutine
     * @time Jul 2, 2013 10:21:10 AM
     */
    @PutMapping
    public AjaxResponse<String> update(@RequestBody SysParameter sysParameter) {
        sysParameter.setUpdateDate(new Date());
        sysParameter.setUpdater(SessionUtil.getUserCode());
        systemParameterService.update(sysParameter);

        return AjaxResponse.success("修改系统参数成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<SysParameter> get(@PathVariable long id) {
        SysParameter sysParameter = systemParameterService.get(id);

        return AjaxResponse.create(sysParameter);
    }

    /**
     * 功能说明 : 停用/启用系统参数
     *
     * @param id
     * @param disEnableFlag 停用/启用标志
     * @return
     * @author rutine
     * @time Jul 2, 2013 10:14:48 AM
     */
    @PutMapping("/{id}/disEnable/{disEnableFlag}")
    public AjaxResponse<String> disEnable(
            @PathVariable long id,
            @PathVariable String disEnableFlag) {

        systemParameterService.disEnable(id, disEnableFlag);

        return AjaxResponse.success("操作成功");
    }
}
