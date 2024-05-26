package com.mycuckoo.web.platform.system;

import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.SystemConfigBean;
import com.mycuckoo.core.exception.SystemException;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.service.platform.SystemConfigService;
import com.mycuckoo.service.uum.UserService;
import com.mycuckoo.web.vo.res.uum.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 功能说明: 系统配置Controller
 *
 * @author rutine
 * @version 3.0.0
 * @time Oct 13, 2014 9:28:46 PM
 */
@RestController
@RequestMapping("/platform/system/config/mgr")
public class SystemConfigController {
    private static Logger logger = LoggerFactory.getLogger(SystemConfigController.class);

    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private UserService userService;


    /**
     * 功能说明 : 查询用户信息为管理员分配
     *
     * @param querier
     * @return
     * @author rutine
     * @time Nov 23, 2013 11:03:33 PM
     */
    @GetMapping("/users")
    public AjaxResponse<Page<UserVo>> list(Querier querier) {
        String userAddDelFlag = (String) querier.getRequired("userAddDelFlag");
        Page<UserVo> page = null;
        if ("add".equalsIgnoreCase(userAddDelFlag)) {
            page = userService.findUsersForSetAdmin(querier);
        } else {
            page = userService.findAdminUsers();
        }

        return AjaxResponse.create(page);
    }

    /**
     * 功能说明 : 设置系统配置信息
     *
     * @param systemConfig 设置系统配置信息
     * @return
     * @author rutine
     * @time Nov 23, 2013 9:36:00 PM
     */
    @PutMapping
    public AjaxResponse<String> update(@RequestBody SystemConfigBean systemConfig) {

        try {
            systemConfigService.setSystemConfigInfo(systemConfig, systemConfig.getUserAddDelFlag());
        } catch (SystemException e) {
            AjaxResponse.create(500, "设置失败");
        }

        return AjaxResponse.success("设置成功");
    }

    @GetMapping
    public AjaxResponse<SystemConfigBean> get() {
        SystemConfigBean systemConfigBean = systemConfigService.getSystemConfigInfo();

        return AjaxResponse.create(systemConfigBean);
    }

    /**
     * 功能说明 : 启动tomcat监控
     *
     * @author rutine
     * @time Nov 24, 2013 1:20:00 PM
     */
    @PostMapping("/start-jconsole")
    public AjaxResponse<String> startJConsole() {
        try {
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("cmd.exe /c JConsole ");
        } catch (IOException e) {
            logger.error("启动tomcat监控失败!", e);
            AjaxResponse.create(500, "启动失败");
        }


        return AjaxResponse.success("启动成功");
    }
}
