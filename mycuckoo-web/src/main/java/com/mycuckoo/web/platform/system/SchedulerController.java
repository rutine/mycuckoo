package com.mycuckoo.web.platform.system;


import com.google.common.collect.Maps;
import com.mycuckoo.utils.SessionUtil;
import com.mycuckoo.domain.platform.SchedulerJob;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.platform.SchedulerHandle;
import com.mycuckoo.service.platform.SchedulerService;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import static com.mycuckoo.web.constant.ActionConst.LIMIT;

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
    public AjaxResponse<Page<SchedulerJob>> list(
            @RequestParam(value = "jobName", defaultValue = "") String jobName,
            @RequestParam(value = "triggerType", defaultValue = "") String triggerType,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("jobName", StringUtils.isBlank(jobName) ? null : "%" + jobName + "%");
        params.put("triggerType", StringUtils.isBlank(triggerType) ? null : "%" + triggerType + "%");
        Page<SchedulerJob> page = schedulerService.findByPage(params, new PageRequest(pageNo - 1, pageSize));

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

        schedulerJob.setCreateDate(new Date());
        schedulerJob.setCreator(SessionUtil.getUserCode());
        schedulerService.save(schedulerJob);

        return AjaxResponse.create("任务保存成功");
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

        return AjaxResponse.create("成功删除任务");
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
        schedulerService.update(scheduler);

        return AjaxResponse.create("修改任务成功");
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

        return AjaxResponse.create("任务调度启动成功");
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

        return AjaxResponse.create("任务调度停止成功");
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

        return AjaxResponse.create("启动调度器成功");
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

        return AjaxResponse.create("停止调度器成功");
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
