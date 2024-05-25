package com.mycuckoo.web.platform.system;


import com.google.common.collect.Maps;
import com.mycuckoo.util.web.SessionUtil;
import com.mycuckoo.domain.platform.SysParameter;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageRequest;
import com.mycuckoo.service.platform.SystemParameterService;
import com.mycuckoo.util.JsonUtils;
import com.mycuckoo.core.AjaxResponse;
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
import java.util.Map;

import static com.mycuckoo.constant.ActionConst.LIMIT;

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
    public AjaxResponse<Page<SysParameter>> list(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String key,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = LIMIT + "") int pageSize) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("name", StringUtils.isBlank(name) ? null : "%" + name + "%");
        params.put("key", StringUtils.isBlank(key) ? null : "%" + key + "%");
        Page<SysParameter> page = systemParameterService.findByPage(params, new PageRequest(pageNo - 1, pageSize));

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

        return AjaxResponse.create("保存系统参数成功");
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

        return AjaxResponse.create("修改系统参数成功");
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

        return AjaxResponse.create("操作成功");
    }
}
