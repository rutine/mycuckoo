package com.mycuckoo.web.platform.system;


import com.google.common.collect.Maps;
import com.mycuckoo.domain.platform.SysOptLog;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.platform.SystemOptLogService;
import com.mycuckoo.web.vo.AjaxResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.mycuckoo.web.constant.ActionVariable.LIMIT;

/**
 * 功能说明: 系统操作日志Controller
 *
 * @author rutine
 * @version 3.0.0
 * @time Oct 18, 2014 11:56:55 AM
 */
@RestController
@RequestMapping("/platform/system/log/mgr")
public class SystemOptLogController {
    private static Logger logger = LoggerFactory.getLogger(SystemOptLogController.class);

    @Autowired
    private SystemOptLogService systemOptLogService;


    @GetMapping
    public AjaxResponse<Page<SysOptLog>> list(
            @RequestParam(required = false) String optModName,
            @RequestParam(required = false) String optName,
            @RequestParam(required = false) String optUserName,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = LIMIT + "") int pageSize) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("optModName", StringUtils.isBlank(optModName) ? null : "%" + optModName + "%");
        params.put("optName", StringUtils.isBlank(optName) ? null : "%" + optName + "%");
        params.put("optUserName", StringUtils.isBlank(optUserName) ? null : "%" + optUserName + "%");

        Page<SysOptLog> page = systemOptLogService.findByPage(params, new PageRequest(pageNo - 1, pageSize));

        return AjaxResponse.create(page);
    }

    /**
     * 功能说明 : 查看日志内容视图
     *
     * @param id
     * @return
     * @author rutine
     * @time Jun 22, 2013 2:27:06 PM
     */
    @GetMapping("/{id}")
    public AjaxResponse<String> get(@PathVariable long id) {
        String logContent = systemOptLogService.getOptContentById(id);

        return AjaxResponse.create(logContent);
    }
}
