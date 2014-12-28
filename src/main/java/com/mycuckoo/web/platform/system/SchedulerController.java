package com.mycuckoo.web.platform.system;

import static com.mycuckoo.common.constant.ActionVariable.LIMIT;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mycuckoo.common.utils.JsonUtils;
import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.platform.SysplSchedulerJob;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.platform.SchedulerService;
import com.mycuckoo.service.impl.platform.SchedulerHandle;

/**
 * 功能说明: 系统调度Controller
 * 
 * @author rutine
 * @time Oct 17, 2014 11:10:02 PM
 * @version 2.0.0
 */
@Controller
@RequestMapping("/platform/schedulerMgr")
public class SchedulerController {
	private static Logger logger = LoggerFactory.getLogger(SchedulerController.class);

	@Autowired
	private SchedulerService schedulerService;

	@RequestMapping(value = "/index")
	public String schedulerMgr(
			@RequestParam(value = "jobName", defaultValue = "") String jobName,
			@RequestParam(value = "triggerType", defaultValue = "") String triggerType,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			Model model) {
		
		logger.info("---------------- 请求系统调度管理界面 -----------------");

		Page<SysplSchedulerJob> page = schedulerService.findSchedulerJobsByCon(
				jobName, triggerType, new PageRequest(pageNo - 1, pageSize));
		model.addAttribute("page", page);
		model.addAttribute("searchParams", "jobName=" + jobName + "&triggerType=" + triggerType);

		return "business/platform/system/schedulerMgr";
	}

	/**
	 * 功能说明 : 创建任务
	 * 
	 * @param opt 操作(保存, 保存添加)
	 * @param sysplSchedulerJob
	 * @param request
	 * @param session
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Jun 2, 2013 8:30:06 PM
	 */
	@RequestMapping(value = "/createForm", method = RequestMethod.POST)
	public String postCreateForm(
			@RequestParam(required = false) String opt,
			SysplSchedulerJob sysplSchedulerJob,
			HttpServletRequest request, 
			HttpSession session,
			RedirectAttributes redirectAttributes) {

		logger.debug(JsonUtils.toJson(sysplSchedulerJob));

		boolean success = true;
		try {
			sysplSchedulerJob.setCreateDate(new Date());
			sysplSchedulerJob.setCreator(SessionUtil.getUserCode(session));
			schedulerService.saveSchedulerJob(sysplSchedulerJob, request);
		} catch (ApplicationException e) {
			logger.error("1. 任务保存成功失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 任务保存成功失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", "任务保存成功失败!");
		}

		return success && !"saveadd".equals(opt) ? "redirect:index" : "redirect:createForm";
	}

	@RequestMapping(value = "/createForm", method = RequestMethod.GET)
	public String getCreateForm(Model model) {
		model.addAttribute("scheduler", new SysplSchedulerJob());
		model.addAttribute("action", "create");

		return "business/platform/system/schedulerMgrForm";
	}

	/**
	 * 功能说明 : 根据id删除任务
	 * 
	 * @param id 任务id
	 * @param request
	 * @return
	 * @author rutine
	 * @time Jun 25, 2013 8:59:46 PM
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult delete(
			@RequestParam(value = "id") Long jobId,
			HttpServletRequest request) {
		
		AjaxResult ajaxResult = new AjaxResult(true, "成功删除任务");
		try {
			schedulerService.deleteSchedulerJobByJobId(jobId, request);
		} catch (ApplicationException e) {
			logger.error("1. 删除任务失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 删除任务失败!", e);
			
			ajaxResult.setStatus(true);
			ajaxResult.setMsg("删除任务失败");
		}

		return ajaxResult;
	}

	/**
	 * 功能说明 : 修改任务
	 * 
	 * @param scheduler
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Jun 29, 2013 8:55:38 AM
	 */
	@RequestMapping(value = "/updateForm", method = RequestMethod.POST)
	public String postUpdateForm(
			@ModelAttribute("preload") SysplSchedulerJob scheduler,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		
		boolean success = true;
		try {
			schedulerService.updateSchedulerJob(scheduler, request);
		} catch (ApplicationException e) {
			logger.error("1. 修改任务失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 修改任务失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", "修改任务失败!");
		}

		return success ? "redirect:index" : "redirect:updateForm";
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public String getUpdateForm(@RequestParam long id, Model model) {
		SysplSchedulerJob scheduler = schedulerService.getSchedulerJobByJobId(id);

		logger.debug(JsonUtils.toJson(scheduler));

		model.addAttribute("scheduler", scheduler);
		model.addAttribute("action", "update");

		return "business/platform/system/schedulerMgrForm";
	}

	@RequestMapping(value = "/viewForm", method = RequestMethod.GET)
	public String getViewForm(@RequestParam long id, Model model) {
		SysplSchedulerJob scheduler = schedulerService.getSchedulerJobByJobId(id);

		logger.debug(JsonUtils.toJson(scheduler));

		model.addAttribute("scheduler", scheduler);
		model.addAttribute("action", "view");

		return "business/platform/system/schedulerMgrForm";
	}

	/**
	 * 功能说明 : 启动调度器
	 * 
	 * @param request
	 * @return
	 * @author rutine
	 * @time Nov 24, 2013 4:48:25 PM
	 */
	@RequestMapping("startScheduler")
	@ResponseBody
	public AjaxResult startScheduler(HttpServletRequest request) {
		AjaxResult ajaxResult = new AjaxResult(true, "启动调度器成功!");
		try {
			schedulerService.startScheduler(request);
		} catch (ApplicationException e) {
			logger.error("1. 调度器启动失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 调度器启动失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("调度器启动失败!");
		}

		return ajaxResult;
	}

	/**
	 * 功能说明 : 停止调度器
	 * 
	 * @param request
	 * @return
	 * @author rutine
	 * @time Nov 24, 2013 4:48:25 PM
	 */
	@RequestMapping("stopScheduler")
	@ResponseBody
	public AjaxResult stopScheduler(HttpServletRequest request) {
		AjaxResult ajaxResult = new AjaxResult(true, "停止调度器成功!");
		try {
			schedulerService.stopScheduler(request);
		} catch (ApplicationException e) {
			logger.error("1. 停止调度器失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 停止调度器失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("停止调度器失败!");
		}

		return ajaxResult;
	}

	/**
	 * 功能说明 : 启动job
	 * 
	 * @param jobId
	 * @param request
	 * @return
	 * @author rutine
	 * @time Nov 24, 2013 4:48:25 PM
	 */
	@RequestMapping("startJob")
	@ResponseBody
	public AjaxResult startJob(@RequestParam Long jobId, HttpServletRequest request) {
		AjaxResult ajaxResult = new AjaxResult(true, "任务调度启动成功!");
		try {
			schedulerService.startJob(jobId, request);
		} catch (ApplicationException e) {
			logger.error("1. 任务调度启动失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 任务调度启动失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("任务调度启动失败!");
		}

		return ajaxResult;
	}

	/**
	 * 功能说明 :停止job
	 * 
	 * @param jobId
	 * @param jobName
	 * @param request
	 * @return
	 * @author rutine
	 * @time Nov 24, 2013 4:48:25 PM
	 */
	@RequestMapping("stopJob")
	@ResponseBody
	public AjaxResult stopJob(
			@RequestParam Long jobId,
			@RequestParam String jobName, 
			HttpServletRequest request) {
		
		AjaxResult ajaxDone = new AjaxResult(true, "任务调度停止成功!");
		try {
			schedulerService.stopJob(jobId, jobName, request);
		} catch (ApplicationException e) {
			logger.error("1. 任务调度停止失败!", e);
			
			ajaxDone.setStatus(false);
			ajaxDone.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 任务调度停止失败!", e);
			
			ajaxDone.setStatus(false);
			ajaxDone.setMsg("任务调度停止失败!");
		}

		return ajaxDone;
	}

	/**
	 * 功能说明 : 调度器状态
	 * 
	 * @return
	 * @author rutine
	 * @time Nov 24, 2013 5:17:38 PM
	 */
	@RequestMapping("schedulerStatus")
	@ResponseBody
	public boolean schedulerStatus() {
		if (SchedulerHandle.getInstance().getScheduler() == null) {
			return false;
		} else {
			return true;
		}
	}

	@ModelAttribute("preload")
	public SysplSchedulerJob get(@RequestParam(value = "jobId", required = false) Long id) {
		if (id != null) {
			return schedulerService.getSchedulerJobByJobId(id);
		}
		return null;
	}

	/**
	 * 设置自定义属性转换器
	 * 
	 * @param request the current request
	 * @param binder the data binder
	 */
	@InitBinder
	public void InitBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		// 不要删除下行注释! 将来"yyyy-MM-dd"将配置到properties文件中
		// SimpleDateFormat dateFormat = new
		// SimpleDateFormat(getText("date.format", request.getLocale()));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);

		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
	}
}
