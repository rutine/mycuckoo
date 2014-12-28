package com.mycuckoo.web.platform;

import static com.mycuckoo.common.constant.ActionVariable.LIMIT;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.mycuckoo.common.utils.JsonUtils;
import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.platform.SysplDistrict;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.domain.vo.TreeVo;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.platform.DistrictService;

/**
 * 功能说明: 地区Controller
 * 
 * @author rutine
 * @time Oct 18, 2014 10:42:49 AM
 * @version 2.0.0
 */
@Controller
@RequestMapping("/platform/districtMgr")
public class DistrictController {
	private static Logger logger = LoggerFactory.getLogger(ModuleController.class);

	@Autowired
	private DistrictService districtService;

	@RequestMapping(value = "/index")
	public String moduleMgr() {
		logger.info("---------------- 请求地区管理界面 -----------------");

		return "business/platform/module/districtMgr";
	}

	/**
	 * 功能说明 : 列表展示页面
	 * 
	 * @param treeId 查找指定节点下的地区
	 * @param districtName 地区名称
	 * @param districtLevel 地区级别
	 * @param pageNo 第几页
	 * @param pageSize 页面大小, 暂时没有使用
	 * @param model 业务数据
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 11:12:40 AM
	 */
	@RequestMapping(value = "/list")
	public String list(
			@RequestParam(value = "treeId", defaultValue = "-1") long treeId,
			@RequestParam(value = "districtName", defaultValue = "") String districtName,
			@RequestParam(value = "districtLevel", defaultValue = "") String districtLevel,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			Model model) {

		Page<SysplDistrict> page = districtService.findDistrictsByCon(treeId,
				districtName, districtLevel, new PageRequest(pageNo - 1, pageSize));
		model.addAttribute("page", page);
		model.addAttribute("searchParams", "districtName=" + districtName + "&districtLevel=" + districtLevel);

		return "business/platform/module/districtMgrList";
	}

	/**
	 * 功能说明 : 创建新地区
	 * 
	 * @param district
	 * @param request
	 * @param session
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 11:18:03 AM
	 */
	@RequestMapping(value = "/createForm", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult postCreateForm(
			SysplDistrict district,
			HttpServletRequest request, 
			HttpSession session) {
		
		logger.debug(JsonUtils.toJson(district));

		AjaxResult ajaxResult = new AjaxResult(true, "保存地区成功.");
		try {
			SysplDistrict parentDistrict = new SysplDistrict();
			parentDistrict.setDistrictId(district.getUpDistrictId());
			district.setCreateDate(new Date());
			district.setCreator(SessionUtil.getUserCode(session));
			district.setSysplDistrict(parentDistrict);
			districtService.saveDistrict(district, request);
		} catch (ApplicationException e) {
			logger.error("1. 保存地区失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 保存地区失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("保存地区失败!");
		}

		return ajaxResult;
	}

	@RequestMapping(value = "/createForm", method = RequestMethod.GET)
	public String getCreateForm(Model model) {
		model.addAttribute("district", new SysplDistrict());
		model.addAttribute("action", "create");

		return "business/platform/module/districtMgrForm";
	}

	/**
	 * 功能说明 : 停用/启用模块
	 * 
	 * @param id
	 * @param disEnableFlag 停用/启用标志
	 * @param request
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 11:33:24 AM
	 */
	@RequestMapping(value = "/disEnable", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult disEnable(
			@RequestParam long id,
			@RequestParam String disEnableFlag, 
			HttpServletRequest request) {

		AjaxResult ajaxResult = new AjaxResult(true, "操作失败!");
		try {
			boolean disEnableBol = districtService.disEnableDistrict(id, disEnableFlag, request);
			ajaxResult.setStatus(disEnableBol);
		} catch (ApplicationException e) {
			logger.error("1. 停用启用失败!", e);
			
			ajaxResult.setStatus(false);
		} catch (SystemException e) {
			logger.error("2. 停用启用失败!", e);
			
			ajaxResult.setStatus(false);
		}

		return ajaxResult;
	}

	/**
	 * 功能说明 : 查找节点的下级节点
	 * 
	 * @param id 地区id
	 * @param filterId
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 11:27:54 AM
	 */
	@RequestMapping(value = "/getChildNodes", method = RequestMethod.GET, produces = "text/html;charset=utf-8")
	@ResponseBody
	public List<TreeVo> getChildNodes(
			@RequestParam(value = "districtId", defaultValue = "-1") long id,
			@RequestParam(value = "filterModuleId", defaultValue = "0") long filterId) {
		
		List<TreeVo> asyncTreeList = Lists.newArrayList();
		if(id == -1L) {
			TreeVo treeVo = new TreeVo();
			treeVo.setId("0");
			treeVo.setText("中国");
			treeVo.setIsParent(true);
			asyncTreeList.add(treeVo);
		} else {
			asyncTreeList = districtService.findNextLevelChildNodes(id, filterId);
		}

		logger.debug("json --> " + JsonUtils.toJson(asyncTreeList));

		return asyncTreeList;
	}

	/**
	 * 功能说明 : 修改地区
	 * 
	 * @param district 地区对象
	 * @param request
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 11:37:39 AM
	 */
	@RequestMapping(value = "/updateForm", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult postUpdateForm(
			@ModelAttribute("preload") SysplDistrict district,
			HttpServletRequest request) {
		
		AjaxResult ajaxResult = new AjaxResult(true, "修改地区成功");
		try {
			SysplDistrict parentDistrict = new SysplDistrict();
			// upDistrictId为0参数传不过来
			parentDistrict.setDistrictId(district.getUpDistrictId() == null ? 0 : district.getUpDistrictId());
			district.setSysplDistrict(parentDistrict);
			districtService.updateDistrict(district, request);
		} catch (ApplicationException e) {
			logger.error("1. 修改地区失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 修改地区失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("修改地区失败!");
		}

		return ajaxResult;
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public String getUpdateForm(@RequestParam long id, Model model) {
		SysplDistrict district = districtService.getDistrictByDistrictId(id);
		SysplDistrict parentDistrict = districtService
				.getDistrictByDistrictId(district.getSysplDistrict().getDistrictId());
		district.setUpDistrictId(parentDistrict.getDistrictId());
		district.setUpDistrictName(parentDistrict.getDistrictName());

		model.addAttribute("district", district);
		model.addAttribute("action", "update");

		return "business/platform/module/districtMgrForm";
	}

	@RequestMapping(value = "/viewForm", method = RequestMethod.GET)
	public String getViewForm(@RequestParam long id, Model model) {
		SysplDistrict district = districtService.getDistrictByDistrictId(id);
		SysplDistrict parentDistrict = districtService
				.getDistrictByDistrictId(district.getSysplDistrict().getDistrictId());
		district.setUpDistrictId(parentDistrict.getDistrictId());
		district.setUpDistrictName(parentDistrict.getDistrictName());

		model.addAttribute("district", district);
		model.addAttribute("action", "view");

		return "business/platform/module/districtMgrForm";
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出Task对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preload")
	public SysplDistrict get(@RequestParam(value = "districtId", required = false) Long id) {
		if (id != null) {
			return districtService.getDistrictByDistrictId(id);
		}
		return null;
	}

}
