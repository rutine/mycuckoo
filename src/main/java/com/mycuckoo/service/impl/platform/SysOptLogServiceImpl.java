package com.mycuckoo.service.impl.platform;

import static com.mycuckoo.common.utils.CommonUtils.isNullOrEmpty;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.common.utils.SystemConfigXmlParse;
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplSysOptLog;
import com.mycuckoo.domain.vo.SystemConfigBean;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.persistence.iface.platform.SysplSysOptLogRepository;
import com.mycuckoo.service.iface.platform.SystemOptLogService;

/**
 * 功能说明: TODO(这里用一句话描述这个类的作用)
 *
 * @author rutine
 * @time Sep 25, 2014 10:50:20 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class SysOptLogServiceImpl implements SystemOptLogService {
	
	private static Logger logger = LoggerFactory.getLogger(SysOptLogServiceImpl.class);
	
	private SysplSysOptLogRepository sysplSysOptLogRepository;

	@Transactional(readOnly=false)
	@Override
	public void deleteLog(int keepdays) {
		sysplSysOptLogRepository.deleteLogger(keepdays);
	}

	@Override
	public Page<SysplSysOptLog> findOptLogsByCon(SysplSysOptLog sysplSysOptLog, Pageable page) {
		return sysplSysOptLogRepository.findOptLogsByCon(sysplSysOptLog, page);
	}

	@Override
	public String getOptLogContentById(long optId) {
		return sysplSysOptLogRepository.getOptLogContentById(optId);
	}

	@Transactional(readOnly = false)
	@Override
	public void saveLog(String level, String optModName, String optName,
			String optContent, String optBusinessId, HttpServletRequest request)
					throws ApplicationException {
		
		SystemConfigXmlParse.getInstance();
		SystemConfigBean systemConfigBean = SystemConfigXmlParse.getSystemConfigBean();
		String sysConfigLevel = systemConfigBean.getLoggerLevel();
		String[] levelArray = {level, sysConfigLevel};
		for(String alevel : levelArray){//检查日志级别是否合法
			if(isNullOrEmpty(alevel) || "0123".indexOf(alevel) < 0 || alevel.length() > 1) {
				throw new ApplicationException("日志级别错误,值为: " + alevel);
			}
		}
		int iLevel = Integer.parseInt(level);
		int iSysConfigLevel = Integer.parseInt(sysConfigLevel);
		if(iSysConfigLevel == 0) {
			return;
		} else {
			if(iLevel < iSysConfigLevel) return;
		}
		if(request == null) {
			logger.error("request is null.");
			throw new SystemException("request is null.");
		}
		String ipAddr = request.getRemoteAddr();
		InetAddress IP = null;
		try {
			IP = InetAddress.getByName(ipAddr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			logger.error("unknown IP" + IP + ". " + e.toString());
		}
		HttpSession session = request.getSession();
		//得到计算机名称
		String computName = IP.getHostName();
		
		SysplSysOptLog sysplSysOptLog = new SysplSysOptLog();
		sysplSysOptLog.setOptModName(optModName);
		sysplSysOptLog.setOptName(optName);
		sysplSysOptLog.setOptContent(optContent);
		sysplSysOptLog.setOptBusinessId(optBusinessId);
		sysplSysOptLog.setOptTime(new Date());
		sysplSysOptLog.setOptPcName(computName);
		sysplSysOptLog.setOptPcIp(ipAddr);
		sysplSysOptLog.setOptUserName(SessionUtil.getUserName(session));
		sysplSysOptLog.setOptUserRole(SessionUtil.getRoleName(session));
		sysplSysOptLog.setOptUserOgan(SessionUtil.getOrganName(session));
		
		sysplSysOptLogRepository.save(sysplSysOptLog);
	}

	
	
	
	
	// ---------------------- 依赖注入-----------------------
	@Autowired
	public void setSysplSysOptLogRepository(SysplSysOptLogRepository sysplSysOptLogRepository) {
		this.sysplSysOptLogRepository = sysplSysOptLogRepository;
	}

}
