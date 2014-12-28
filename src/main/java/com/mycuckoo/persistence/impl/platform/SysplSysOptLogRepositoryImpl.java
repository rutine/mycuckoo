package com.mycuckoo.persistence.impl.platform;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplSysOptLog;
import com.mycuckoo.persistence.iface.platform.SysplSysOptLogRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 系统操作日志持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 8:57:46 PM
 * @version 2.0.0
 */
@Repository
public class SysplSysOptLogRepositoryImpl extends AbstractRepository<SysplSysOptLog, Long> 
		implements SysplSysOptLogRepository {

	@Override
	public void deleteLogger(int keepdays) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -keepdays);
		
		int count = getSqlSession().delete(obtainMapperNamespace() + ".deleteLogger", calendar.getTime());
		
		logger.debug("success deleting logger: " + count);
	}

	@Override
	public Page<SysplSysOptLog> findOptLogsByCon(
			SysplSysOptLog sysplSysOptLog, Pageable page) {
		
		String optModName = sysplSysOptLog.getOptModName();
		String optName = sysplSysOptLog.getOptName();
		String optUserName = sysplSysOptLog.getOptUserName();
		String optUserRole = sysplSysOptLog.getOptUserRole();
		String optPcIp = sysplSysOptLog.getOptPcIp();
		String optBusinessId = sysplSysOptLog.getOptBusinessId();
		Date startTime = sysplSysOptLog.getStartTime();
		Date endTime = sysplSysOptLog.getEndTime();

		Map<String, Object> map = new HashMap<String, Object>(10);
		map.put("optModName", isNotBlank(optModName) ? "%" + optModName + "%" : null);
		map.put("optName", isNotBlank(optName) ? "%" + optName + "%" : null);
		map.put("optUserName", isNotBlank(optUserName) ? "%" + optUserName + "%" : null);
		map.put("optUserRole", isNotBlank(optUserRole) ? "%" + optUserRole + "%" : null);
		map.put("optPcIp", isNotBlank(optPcIp) ? "%" + optPcIp + "%" : null);
		map.put("optBusinessId", isNotBlank(optBusinessId) ? optBusinessId : null);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("start", page.getOffset());
		map.put("limit", page.getPageSize());

		List<SysplSysOptLog> list = getSqlSession().selectList(obtainMapperNamespace() + ".findOptLogsByCon", map);
		int count = getSqlSession().selectOne(obtainMapperNamespace() + ".countOptLogsByCon", map);

		return new PageImpl<SysplSysOptLog>(list, page, count);
	}

	@Override
	public String getOptLogContentById(long optId) {
		return getSqlSession().selectOne(obtainMapperNamespace() + ".getOptLogContentById", optId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(SysplSysOptLog entity) {
		return entity.getOptId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.platform.SysplSysOptLogMapper";
	}

}
