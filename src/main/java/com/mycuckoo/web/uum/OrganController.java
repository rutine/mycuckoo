package com.mycuckoo.web.uum;

import static com.mycuckoo.common.constant.ActionVariable.LIMIT;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

import com.mycuckoo.common.utils.JsonUtils;
import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.domain.uum.UumOrgan;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.domain.vo.TreeVo;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.uum.OrganService;

/**
 * 功能说明: 机构管理Controller
 * 
 * @author rutine
 * @time Oct 18, 2014 1:26:18 PM
 * @version 2.0.0
 */
@Controller
@RequestMapping("/uum/organMgr")
public class OrganController {
	private static Logger logger = LoggerFactory.getLogger(OrganController.class);

	@Autowired
	private OrganService organService;

	@RequestMapping(value = "/index")
	public String organMgr() {
		logger.info("---------------- 请求机构菜单管理界面 -----------------");

		return "business/uum/user/organMgr";
	}

	/**
	 * 功能说明 : 列表展示页面
	 * 
	 * @param treeId 机构ID
	 * @param orgCode 机构代码
	 * @param orgName 机构名称
	 * @param pageNo 第几页
	 * @param pageSize 每页显示数量, 暂时没有使用
	 * @param model 业务数据
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 3:31:22 PM
	 */
	@RequestMapping(value = "/list")
	public String list(
			@RequestParam(value = "treeId", defaultValue = "-1") long treeId,
			@RequestParam(value = "orgCode", defaultValue = "") String orgCode,
			@RequestParam(value = "orgName", defaultValue = "") String orgName,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			Model model) {

		Page<UumOrgan> page = organService.findOrgansByCon(treeId, orgCode, orgName, new PageRequest(pageNo - 1, pageSize));
		model.addAttribute("page", page);
		model.addAttribute("searchParams", "orgCode=" + orgCode + "&orgName=" + orgName);

		return "business/uum/user/organMgrList";
	}

	/**
	 * 功能说明 : 创建新机构
	 * 
	 * @param uumOrgan
	 * @param request
	 * @param session
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 3:35:51 PM
	 */
	@RequestMapping(value = "/createForm", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult postCreateForm(
			UumOrgan uumOrgan, 
			HttpServletRequest request,
			HttpSession session) {
		
		logger.debug(JsonUtils.toJson(uumOrgan));

		AjaxResult ajaxResult = new AjaxResult(true, "保存机构成功");
		try {
			UumOrgan parentOrgan = new UumOrgan();
			parentOrgan.setOrgId(uumOrgan.getUpOrgId());
			uumOrgan.setUumOrgan(parentOrgan);
			uumOrgan.setCreator(SessionUtil.getUserCode(session));
			uumOrgan.setUumOrgan(parentOrgan);
			organService.saveOrgan(uumOrgan, request);
		} catch (ApplicationException e) {
			logger.error("1. 保存机构失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 保存机构失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("保存机构失败!");
		}

		return ajaxResult;
	}

	@RequestMapping(value = "/createForm", method = RequestMethod.GET)
	public String getCreateForm(Model model) {
		model.addAttribute("district", new SysplModuleMemu());
		model.addAttribute("action", "create");

		return "business/uum/user/organMgrForm";
	}

	/**
	 * 功能说明 : 停用/启用机构
	 * 
	 * @param id 机构ID
	 * @param disEnableFlag 停用/启用标志
	 * @param request
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 3:38:46 PM
	 */
	@RequestMapping(value = "/disEnable", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult disEnable(@RequestParam long id,
			@RequestParam String disEnableFlag, HttpServletRequest request) {

		AjaxResult ajaxResult = new AjaxResult(true, "");
		try {
			boolean disEnableBol = organService.disEnableOrgan(id, disEnableFlag, request) == 0;
			ajaxResult.setStatus(disEnableBol);
		} catch (ApplicationException e) {
			logger.error("1. 停用启用失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 停用启用失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("停用启用失败!");
		}

		return ajaxResult;
	}

	/**
	 * 功能说明 : 获取下级机构json数据
	 * 
	 * @param id 机构id
	 * @param filterId
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 3:40:18 PM
	 */
	@RequestMapping(value = "/getChildNodes", method = RequestMethod.GET, produces = "text/html;charset=utf-8")
	@ResponseBody
	public List<TreeVo> getChildNodes(
			@RequestParam(value = "treeId", defaultValue = "0") long id,
			@RequestParam(value = "filterOrgId", defaultValue = "0") long filterId) {
		
		List<TreeVo> asyncTreeList = organService.findNextLevelChildNodes(id, filterId);

		logger.debug("json --> " + JsonUtils.toJson(asyncTreeList));

		return asyncTreeList;
	}

	/**
	 * 功能说明 : 修改机构
	 * 
	 * @param uumOrgan
	 * @param request
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 3:42:51 PM
	 */
	@RequestMapping(value = "/updateForm", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult postUpdateForm(@ModelAttribute("preload") UumOrgan uumOrgan,
			HttpServletRequest request) {
		
		AjaxResult ajaxResult = new AjaxResult(true, "修改机构成功.");
		try {
			UumOrgan parentOrgan = new UumOrgan();
			// 为0参数传不过来
			parentOrgan.setOrgId(uumOrgan.getUpOrgId() == null ? 0 : uumOrgan.getUpOrgId());
			uumOrgan.setUumOrgan(parentOrgan);
			organService.updateOrgan(uumOrgan, request);
		} catch (ApplicationException e) {
			logger.error("1. 修改机构失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 修改机构失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("修改机构失败!");
		}

		return ajaxResult;
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public String getUpdateForm(@RequestParam long id, Model model) {
		UumOrgan uumOrgan = organService.getOrganByOrgId(id);

		model.addAttribute("organ", uumOrgan);
		model.addAttribute("action", "update");

		return "business/uum/user/organMgrForm";
	}

	@RequestMapping(value = "/viewForm", method = RequestMethod.GET)
	public String getViewForm(@RequestParam long id, Model model) {
		UumOrgan uumOrgan = organService.getOrganByOrgId(id);

		model.addAttribute("organ", uumOrgan);
		model.addAttribute("action", "view");

		return "business/uum/user/organMgrForm";
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出Task对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preload")
	public UumOrgan get(@RequestParam(value = "orgId", required = false) Long id) {
		if (id != null) {
			return organService.getOrganByOrgId(id);
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
