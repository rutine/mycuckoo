package com.mycuckoo.web.uum;

import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.uum.OrganService;
import com.mycuckoo.vo.TreeVo;
import com.mycuckoo.vo.uum.OrganVo;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.mycuckoo.web.constant.ActionVariable.LIMIT;

/**
 * 功能说明: 机构管理Controller
 * 
 * @author rutine
 * @time Oct 18, 2014 1:26:18 PM
 * @version 3.0.0
 */
@RestController
@RequestMapping("/uum/organ/mgr")
public class OrganController {
	private static Logger logger = LoggerFactory.getLogger(OrganController.class);

	@Autowired
	private OrganService organService;




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
	public AjaxResponse<Page<OrganVo>> list(
			@RequestParam(value = "treeId", defaultValue = "-1") long treeId,
			@RequestParam(value = "orgCode", defaultValue = "") String orgCode,
			@RequestParam(value = "orgName", defaultValue = "") String orgName,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			Model model) {

		Page<OrganVo> page = organService.findByPage(treeId, orgCode, orgName, new PageRequest(pageNo - 1, pageSize));
		model.addAttribute("page", page);
		model.addAttribute("searchParams", "orgCode=" + orgCode + "&orgName=" + orgName);

		return AjaxResponse.create(page);
	}

	/**
	 * 功能说明 : 创建新机构
	 * 
	 * @param organ
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 3:35:51 PM
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public AjaxResponse<String> postCreate(Organ organ) {
		
		logger.debug(JsonUtils.toJson(organ));

		organ.setCreator(SessionUtil.getUserCode());
		organService.save(organ);

		return AjaxResponse.create("保存机构成功");
	}

	/**
	 * 功能说明 : 停用/启用机构
	 * 
	 * @param id 机构ID
	 * @param disEnableFlag 停用/启用标志
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 3:38:46 PM
	 */
	@RequestMapping(value = "/disEnable", method = RequestMethod.GET)
	public AjaxResponse<String> disEnable(
			@RequestParam long id,
			@RequestParam String disEnableFlag) {

		organService.disEnable(id, disEnableFlag);

		return AjaxResponse.create("停用启用成功");
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
	@RequestMapping(value = "/get/child/nodes", method = RequestMethod.GET)
	public AjaxResponse<List<TreeVo>> getChildNodes(
			@RequestParam(value = "treeId", defaultValue = "0") long id,
			@RequestParam(value = "filterOrgId", defaultValue = "0") long filterId) {
		
		List<TreeVo> asyncTreeList = organService.findNextLevelChildNodes(id, filterId);

		logger.debug("json --> " + JsonUtils.toJson(asyncTreeList));

		return AjaxResponse.create(asyncTreeList);
	}

	/**
	 * 功能说明 : 修改机构
	 * 
	 * @param organ
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 3:42:51 PM
	 */
	@RequestMapping(value = "/updateForm", method = RequestMethod.POST)
	public AjaxResponse<String> postUpdateForm(Organ organ) {

		organService.update(organ);

		return AjaxResponse.create("修改机构成功");
	}


	@RequestMapping(value = "/viewForm", method = RequestMethod.GET)
	public AjaxResponse<Organ> getViewForm(@RequestParam long id, Model model) {
		Organ organ = organService.get(id);

		return AjaxResponse.create(organ);
	}

}
