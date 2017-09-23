package com.mycuckoo.web.platform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.platform.District;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.platform.DistrictService;
import com.mycuckoo.vo.TreeVo;
import com.mycuckoo.vo.platform.DistrictVo;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.mycuckoo.web.constant.ActionVariable.LIMIT;

/**
 * 功能说明: 地区Controller
 * 
 * @author rutine
 * @time Oct 18, 2014 10:42:49 AM
 * @version 3.0.0
 */
@RestController
@RequestMapping("/platform/district/mgr")
public class DistrictController {
	private static Logger logger = LoggerFactory.getLogger(DistrictController.class);

	@Autowired
	private DistrictService districtService;



	/**
	 * 功能说明 : 列表展示页面
	 * 
	 * @param treeId 查找指定节点下的地区
	 * @param districtName 地区名称
	 * @param districtLevel 地区级别
	 * @param pageNo 第几页
	 * @param pageSize 页面大小, 暂时没有使用
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 11:12:40 AM
	 */
	@GetMapping(value = "/list")
	public AjaxResponse<Page<DistrictVo>> list(
			@RequestParam(value = "treeId", defaultValue = "-1") long treeId,
			@RequestParam(value = "districtName", defaultValue = "") String districtName,
			@RequestParam(value = "districtLevel", defaultValue = "") String districtLevel,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {

		Map<String, Object> params = Maps.newHashMap();
		params.put("treeId", treeId);
		params.put("districtName", StringUtils.isBlank(districtName) ? null : "%" + districtName + "%");
		params.put("districtLevel", districtLevel);

		Page<DistrictVo> page = districtService.findByPage(params, new PageRequest(pageNo - 1, pageSize));

		return AjaxResponse.create(page);
	}

	/**
	 * 功能说明 : 创建新地区
	 * 
	 * @param district
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 11:18:03 AM
	 */
	@PutMapping(value = "/create")
	public AjaxResponse<String> putCreate(District district) {
		
		logger.debug(JsonUtils.toJson(district));

		district.setCreateDate(new Date());
		district.setCreator(SessionUtil.getUserCode());
		district.setParentId(district.getParentId());
		districtService.save(district);

		return AjaxResponse.create("保存地区成功");
	}


	/**
	 * 功能说明 : 停用/启用模块
	 * 
	 * @param id
	 * @param disEnableFlag 停用/启用标志
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 11:33:24 AM
	 */
	@GetMapping(value = "/disEnable")
	public AjaxResponse<String> disEnable(
			@RequestParam long id,
			@RequestParam String disEnableFlag) {

		districtService.disEnable(id, disEnableFlag);

		return AjaxResponse.create("操作成功");
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
	@GetMapping(value = "/get/child/nodes")
	public AjaxResponse<List<TreeVo>> getChildNodes(
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

		return AjaxResponse.create(asyncTreeList);
	}

	/**
	 * 功能说明 : 修改地区
	 * 
	 * @param district 地区对象
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 11:37:39 AM
	 */
	@PutMapping(value = "/update")
	public AjaxResponse<String> putUpdate(District district) {
		districtService.update(district);

		return AjaxResponse.create("修改地区成功");
	}


	@GetMapping(value = "/viewForm")
	public AjaxResponse<DistrictVo> getView(@RequestParam long id) {
		DistrictVo district = districtService.get(id);
		DistrictVo parentDistrict = districtService.get(district.getParentId());
		district.setParentId(parentDistrict.getDistrictId());
		district.setParentName(parentDistrict.getDistrictName());

		return AjaxResponse.create(district);
	}

}
