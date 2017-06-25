package com.mycuckoo.repository.platform;

import org.apache.ibatis.annotations.Param;

import com.mycuckoo.domain.platform.SchedulerJob;
import com.mycuckoo.repository.Repository;

/**
 * 功能说明: 工作计划持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:40:22 AM
 * @version 3.0.0
 */
public interface SchedulerJobMapper extends Repository<SchedulerJob, Long> {

	/**
	 * 根据任务名称统计任务数量
	 * 
	 * @param jobName 任务名称
	 * @return
	 */
	int countByJobName(String jobName);

	/**
	 * 批量修改任务状态
	 * 
	 * @param jobIds 任务IDs
	 * @param status 任务status
	 */
	void updateStatuses(@Param("jobIds") Long[] jobIds, @Param("status") String status);

	/**
	 * 根据任务id修改任务状态
	 * 
	 * @param jobId 任务ID
	 * @param status 任务status
	 */
	void updateStatus(@Param("jobId") long jobId, @Param("status") String status);

}
