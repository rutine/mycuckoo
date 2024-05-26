package com.mycuckoo.web.platform.system;


import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.exception.SystemException;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.SchedulerJob;
import com.mycuckoo.service.platform.SchedulerHandle;
import com.mycuckoo.service.platform.SchedulerService;
import com.mycuckoo.util.JsonUtils;
import com.mycuckoo.util.web.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 功能说明: 系统调度Controller
 *
 * @author rutine
 * @version 3.0.0
 * @time Oct 17, 2014 11:10:02 PM
 */
@RestController
@RequestMapping("/platform/system/scheduler/mgr")
public class SchedulerController {
    private static Logger logger = LoggerFactory.getLogger(SchedulerController.class);

    @Autowired
    private SchedulerService schedulerService;


    @GetMapping
    public AjaxResponse<Page<SchedulerJob>> list(Querier querier) {
        Page<SchedulerJob> page = schedulerService.findByPage(querier);

        return AjaxResponse.create(page);
    }

    /**
     * 功能说明 : 创建任务
     *
     * @param schedulerJob
     * @return
     * @author rutine
     * @time Jun 2, 2013 8:30:06 PM
     */
    @PostMapping
    public AjaxResponse<String> create(@RequestBody SchedulerJob schedulerJob) throws SystemException {

        logger.debug(JsonUtils.toJson(schedulerJob));

        schedulerJob.setUpdateDate(new Date());
        schedulerJob.setUpdater(SessionUtil.getUserCode());
        schedulerJob.setCreateDate(new Date());
        schedulerJob.setCreator(SessionUtil.getUserCode());
        schedulerService.save(schedulerJob);

        return AjaxResponse.success("任务保存成功");
    }

    /**
     * 功能说明 : 根据id删除任务
     *
     * @param id 任务id
     * @return
     * @author rutine
     * @time Jun 25, 2013 8:59:46 PM
     */
    @DeleteMapping("/{id}")
    public AjaxResponse<String> delete(@PathVariable Long id) throws SystemException {
        schedulerService.delete(id);

        return AjaxResponse.success("成功删除任务");
    }

    /**
     * 功能说明 : 修改任务
     *
     * @param scheduler
     * @return
     * @author rutine
     * @time Jun 29, 2013 8:55:38 AM
     */
    @PutMapping
    public AjaxResponse<String> update(@RequestBody SchedulerJob scheduler) {
        scheduler.setUpdateDate(new Date());
        scheduler.setUpdater(SessionUtil.getUserCode());
        schedulerService.update(scheduler);

        return AjaxResponse.success("修改任务成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<SchedulerJob> get(@PathVariable long id) {
        SchedulerJob scheduler = schedulerService.get(id);

        logger.debug(JsonUtils.toJson(scheduler));

        return AjaxResponse.create(scheduler);
    }

    /**
     * 功能说明 : 启动job
     *
     * @param id
     * @return
     * @author rutine
     * @time Nov 24, 2013 4:48:25 PM
     */
    @PutMapping("/{id}/start-job")
    public AjaxResponse<String> startJob(@PathVariable Long id) throws SystemException {
        schedulerService.startJob(id);

        return AjaxResponse.success("任务调度启动成功");
    }

    /**
     * 功能说明 :停止job
     *
     * @param id
     * @return
     * @author rutine
     * @time Nov 24, 2013 4:48:25 PM
     */
    @PutMapping("/{id}/stop-job")
    public AjaxResponse<String> stopJob(@PathVariable Long id) throws SystemException {

        schedulerService.stopJob(id);

        return AjaxResponse.success("任务调度停止成功");
    }

    /**
     * 功能说明 : 启动调度器
     *
     * @return
     * @author rutine
     * @time Nov 24, 2013 4:48:25 PM
     */
    @PostMapping("/start-scheduler")
    public AjaxResponse<String> startScheduler() throws SystemException {
        schedulerService.startScheduler();

        return AjaxResponse.success("启动调度器成功");
    }

    /**
     * 功能说明 : 停止调度器
     *
     * @return
     * @author rutine
     * @time Nov 24, 2013 4:48:25 PM
     */
    @PostMapping("/stop-scheduler")
    public AjaxResponse<String> stopScheduler() throws SystemException {
        schedulerService.stopScheduler();

        return AjaxResponse.success("停止调度器成功");
    }

    /**
     * 功能说明 : 调度器状态
     *
     * @return
     * @author rutine
     * @time Nov 24, 2013 5:17:38 PM
     */
    @GetMapping("/scheduler-status")
    public boolean schedulerStatus() {
        if (SchedulerHandle.getInstance() == null) {
            return false;
        } else {
            return true;
        }
    }
}
