package com.mycuckoo.service.iface.platform;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplSchedulerJob;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 任务业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:18:50 AM
 * @version 2.0.0
 */
public interface SchedulerService {

	/**
	 * <p>删除任务</p>
	 * 
	 * @param jobId 任务ID
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 27, 2012 2:09:47 PM
	 */
	void deleteSchedulerJobByJobId(Long jobId, HttpServletRequest request)
			throws ApplicationException;

	/**
	 * <p>分页查找调度任务</p>
	 * 
	 * @param jobName 任务名称
	 * @param triggerType 触发器类型
	 * @param page
	 * @return
	 * @author rutine
	 * @time Oct 27, 2012 2:10:38 PM
	 */
	Page<SysplSchedulerJob> findSchedulerJobsByCon(String jobName,
			String triggerType, Pageable page);

	/**
	 * <p>根据任务<code>ID</code>获取任务</p>
	 * 
	 * @param jobId 任务<code>ID</code>
	 * @return sysplSchedulerJob 任务对象
	 * @author rutine
	 * @time Oct 27, 2012 2:11:41 PM
	 */
	SysplSchedulerJob getSchedulerJobByJobId(Long jobId);

	/**
	 * <p>根据任务名称判断任务是否存在</p>
	 * 
	 * @param jobName 任务名称
	 * @return
	 * @author rutine
	 * @time Oct 27, 2012 2:12:16 PM
	 */
	boolean existsSchedulerJobByJobName(String jobName);

	/**
	 * <p>修改任务</p>
	 * 
	 * @param sysplSchedulerJob 任务对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 27, 2012 2:12:35 PM
	 */
	void updateSchedulerJob(SysplSchedulerJob sysplSchedulerJob,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>保存任务</p>
	 * 
	 * @param sysplSchedulerJob 任务对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 27, 2012 2:13:00 PM
	 */
	void saveSchedulerJob(SysplSchedulerJob sysplSchedulerJob,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>启动job</p>
	 * 
	 * @param jobId 任务ID
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 27, 2012 2:13:16 PM
	 */
	void startJob(long jobId, HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>启动调度器</p>
	 * 
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 27, 2012 2:13:45 PM
	 */
	void startScheduler(HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>停止job</p>
	 * 
	 * @param jobId 任务ID
	 * @param jobName 任务名称
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 27, 2012 2:13:56 PM
	 */
	void stopJob(long jobId, String jobName, HttpServletRequest request)
			throws ApplicationException;

	/**
	 * <p>停止调度器</p>
	 * 
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 27, 2012 2:14:13 PM
	 */
	void stopScheduler(HttpServletRequest request) throws ApplicationException;
}
