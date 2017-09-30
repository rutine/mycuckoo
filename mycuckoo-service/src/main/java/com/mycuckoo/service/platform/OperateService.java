package com.mycuckoo.service.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.common.utils.CommonUtils;
import com.mycuckoo.domain.platform.Operate;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.OperateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.*;

/**
 * 功能说明: 模块操作业务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 10:35:54 AM
 */
@Service
@Transactional(readOnly = true)
public class OperateService {
	static Logger logger = LoggerFactory.getLogger(OperateService.class);

	@Autowired
	private OperateMapper operateMapper;
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private SystemOptLogService sysOptLogService;


	@Transactional
	public boolean disEnable(long operateId, String disEnableFlag) {
		if (DISABLE.equals(disEnableFlag)) {
			Operate operate = get(operateId);
			operate.setStatus(DISABLE);
			update(operate); //修改模块操作
			moduleService.deleteModOptRefByOperateId(operateId); //根据操作ID删除模块操作关系,级联删除权限

			writeLog(operate, LogLevelEnum.SECOND, OptNameEnum.DISABLE);

			return true;
		} else {
			Operate operate = get(operateId);
			operate.setStatus(ENABLE);
			update(operate); //修改模块操作

			writeLog(operate, LogLevelEnum.SECOND, OptNameEnum.ENABLE);

			return true;
		}
	}

	public List<Operate> findAll() {
		return operateMapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE)).getContent();
	}

	public Page<Operate> findByPage(String modOptName, Pageable page) {
		logger.debug("start={} limit={} modOptName={}", page.getOffset(), page.getPageSize(), modOptName);

		Map<String, Object> params = Maps.newHashMap();
		if (!CommonUtils.isNullOrEmpty(modOptName)) {
			params.put("modOptName", "%" + modOptName + "%");
		}

		return operateMapper.findByPage(params, page);
	}

	public boolean existsByName(String moduleOptName) {
		int count = operateMapper.countByName(moduleOptName);

		logger.debug("find module total is {}", count);

		if (count > 0) {
			return true;
		}
		return false;
	}

	public Operate get(Long operateId) {
		logger.debug("will find moduleopt id is {}", operateId);

		return operateMapper.get(operateId);
	}

	@Transactional
	public void update(Operate operate) {
		operateMapper.update(operate);

		writeLog(operate, LogLevelEnum.SECOND, OptNameEnum.MODIFY);
	}

	@Transactional
	public void save(Operate operate) {
		operateMapper.save(operate);

		writeLog(operate, LogLevelEnum.FIRST, OptNameEnum.SAVE);
	}


	// --------------------------- 私有方法-------------------------------

	/**
	 * 公用模块写日志
	 *
	 * @param operate  模块对象
	 * @param logLevel 日志级别
	 * @param opt      操作名称
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 14, 2012 1:17:12 PM
	 */
	private void writeLog(Operate operate, LogLevelEnum logLevel, OptNameEnum opt) {
		StringBuilder optContent = new StringBuilder();
		optContent.append(operate.getOperateName()).append(SPLIT)
				.append(operate.getOptImgLink()).append(SPLIT)
				.append(operate.getOptFunLink()).append(SPLIT);

		sysOptLogService.saveLog(logLevel, opt, SYS_MODOPT_MGR,
				optContent.toString(), operate.getOperateId() + "");
	}

}
