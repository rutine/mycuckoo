package com.mycuckoo.persistence.iface.platform;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplSchedulerJob;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 工作计划持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:40:22 AM
 * @version 2.0.0
 */
public interface SysplSchedulerJobRepository extends
		Repository<SysplSchedulerJob, Long> {

	/**
	 * 根据任务名称统计任务数量
	 * 
	 * @param jobName 任务名称
	 * @return
	 */
	int countSchedulerJobsByJobName(String jobName);

	/**
	 * 按条件查询调度任务
	 * 
	 * @param jobName 任务名称
	 * @param triggerType 触发器类型
	 * @param page
	 * @return
	 */
	Page<SysplSchedulerJob> findSchedulerJobsByCon(String jobName,
			String triggerType, Pageable page);

	/**
	 * 批量修改任务状态
	 * 
	 * @param jobIds 任务IDs
	 * @param status 任务status
	 */
	void updateSchedulerJobsStatus(Long[] jobIds, String status);

	/**
	 * 根据任务id修改任务状态
	 * 
	 * @param jobId 任务ID
	 * @param status 任务status
	 */
	void updateScheduleJobStatus(long jobId, String status);

}
