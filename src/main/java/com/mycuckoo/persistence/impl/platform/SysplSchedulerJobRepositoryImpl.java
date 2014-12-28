package com.mycuckoo.persistence.impl.platform;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplSchedulerJob;
import com.mycuckoo.persistence.iface.platform.SysplSchedulerJobRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 工作计划持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 8:55:50 PM
 * @version 2.0.0
 */
@Repository
public class SysplSchedulerJobRepositoryImpl extends AbstractRepository<SysplSchedulerJob, Long> 
		implements SysplSchedulerJobRepository {

	@Override
	public int countSchedulerJobsByJobName(String jobName) {
		return getSqlSession().selectOne( obtainMapperNamespace() 
				+ ".countSchedulerJobsByJobName", jobName);
	}

	@Override
	public Page<SysplSchedulerJob> findSchedulerJobsByCon(String jobName,
			String triggerType, Pageable page) {
		
		Map<String, Object> map = new HashMap<String, Object>(4);
		map.put("jobName", isNotBlank(jobName) ? "%" + jobName + "%" : null);
		map.put("triggerType", isNotBlank(triggerType) ? "%" + triggerType + "%" : null);
		map.put("start", page.getOffset());
		map.put("limit", page.getPageSize());

		List<SysplSchedulerJob> list = getSqlSession().selectList(obtainMapperNamespace() + ".findSchedulerJobsByCon", map);
		int count = getSqlSession().selectOne(obtainMapperNamespace() + ".countSchedulerJobsByCon", map);

		return new PageImpl<SysplSchedulerJob>(list, page, count);
	}

	@Override
	public void updateSchedulerJobsStatus(Long[] jobIds, String status) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("array", jobIds);
		map.put("status", status);

		getSqlSession().update(obtainMapperNamespace() + ".updateSchedulerJobsStatus", map);
	}

	@Override
	public void updateScheduleJobStatus(long jobId, String status) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("jobId", jobId);
		map.put("status", status);

		getSqlSession().update(obtainMapperNamespace() + ".updateScheduleJobStatus", map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(SysplSchedulerJob entity) {
		return entity.getJobId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.platform.SysplSchedulerJobMapper";
	}

}
