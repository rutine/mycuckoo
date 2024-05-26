package com.mycuckoo.web.platform.system;


import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.SysOptLog;
import com.mycuckoo.service.platform.SystemOptLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public AjaxResponse<Page<SysOptLog>> list(Querier querier) {
        Page<SysOptLog> page = systemOptLogService.findByPage(querier);

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
