package com.mycuckoo.web.platform.system;

import static com.mycuckoo.common.constant.ActionVariable.LIMIT;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.platform.SysplSysOptLog;
import com.mycuckoo.service.iface.platform.SystemOptLogService;

/**
 * 功能说明: 系统操作日志Controller
 * 
 * @author rutine
 * @time Oct 18, 2014 11:56:55 AM
 * @version 2.0.0
 */
@Controller
@RequestMapping("/platform/systemLogMgr")
public class SystemOptLogController {
	private static Logger logger = LoggerFactory.getLogger(SystemOptLogController.class);

	@Autowired
	private SystemOptLogService systemOptLogService;

	@RequestMapping(value = "/index")
	public String systemLogMgr(
			SysplSysOptLog sysOptLog,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			Model model) {
		
		logger.info("---------------- 请求系统操作日志管理界面 -----------------");

		Page<SysplSysOptLog> page = systemOptLogService.findOptLogsByCon(sysOptLog,
				new PageRequest(pageNo - 1, pageSize));

		StringBuilder searchParams = new StringBuilder();
		searchParams.append("");

		model.addAttribute("page", page);
		model.addAttribute("searchParams", searchParams.toString());

		return "business/platform/system/systemLogMgr";
	}

	/**
	 * 功能说明 : 查看日志内容视图
	 * 
	 * @param id
	 * @return
	 * @author rutine
	 * @time Jun 22, 2013 2:27:06 PM
	 */
	@RequestMapping(value = "/viewForm", method = RequestMethod.GET)
	@ResponseBody
	public String getViewForm(@RequestParam long id) {
		String logContent = systemOptLogService.getOptLogContentById(id);

		return logContent;
	}

	/**
	 * Set up a custom property editor for converting form inputs to real
	 * objects
	 * 
	 * @param request the current request
	 * @param binder the data binder
	 */
	@InitBinder
	public void InitBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		// 不要删除下行注释! 将来"yyyy-MM-dd"将配置到properties文件中
		// SimpleDateFormat dateFormat = new
		// SimpleDateFormat(getText("date.format", request.getLocale()));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);

		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
	}
}
