package com.mycuckoo.web.platform.system;


import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.SysLog;
import com.mycuckoo.service.platform.SystemLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能说明: 系统日志Controller
 *
 * @author rutine
 * @version 5.0.0
 * @time 2026/9/12 11:00
 */
@RestController
@RequestMapping("/platform/system/log/mgr")
public class SystemLogController {
    private static Logger logger = LoggerFactory.getLogger(SystemLogController.class);

    @Autowired
    private SystemLogService systemLogService;


    @GetMapping
    public AjaxResponse<Page<SysLog>> list(Querier querier) {
        Page<SysLog> page = systemLogService.findByPage(querier);

        return AjaxResponse.create(page);
    }

    /**
     * 功能说明 : 查看日志内容视图
     *
     * @param id 日志ID
     * @return
     */
    @GetMapping("/{id}")
    public AjaxResponse<String> get(@PathVariable long id) {
        String logContent = systemLogService.getContentById(id);

        return AjaxResponse.create(logContent);
    }
}