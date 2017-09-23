package com.mycuckoo.web.platform.system;


import com.google.common.collect.Maps;
import com.mycuckoo.domain.platform.DicBigType;
import com.mycuckoo.domain.platform.DicSmallType;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.platform.DictionaryService;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.mycuckoo.web.constant.ActionVariable.LIMIT;

/**
 * 功能说明: 字典Controller
 * 
 * @author rutine
 * @time Oct 18, 2014 7:50:17 AM
 * @version 3.0.0
 */
@RestController
@RequestMapping(value = "/platform/type/dictionary/mgr")
public class DictionaryController {
	private static Logger logger = LoggerFactory.getLogger(DictionaryController.class);

	@Autowired
	private DictionaryService dictionaryService;



	@GetMapping(value = "/list")
	public AjaxResponse<Page<DicBigType>> dictionaryMgr(
			@RequestParam(value = "bigTypeName", defaultValue = "") String dictName,
			@RequestParam(value = "bigTypeCode", defaultValue = "") String dictCode,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			Model model) {

		logger.info("---------------- 请求字典菜单管理界面 -----------------");

		Map<String, Object> params = Maps.newHashMap();
		params.put("dictCode", StringUtils.isBlank(dictCode) ? null : "%" + dictCode + "%");
		params.put("dictName", StringUtils.isBlank(dictName) ? null : "%" + dictName + "%");

		Page<DicBigType> page = dictionaryService.findDicBigTypesByPage(params, new PageRequest(pageNo - 1, pageSize));

		return AjaxResponse.create(page);
	}

	/**
	 * 功能说明 : 创建新字典大类
	 * 
	 * @param dicBigType
	 * @return
	 * @author rutine
	 * @time Jun 11, 2013 4:29:28 PM
	 */
	@PutMapping(value = "/create")
	public AjaxResponse<String> postCreateForm(DicBigType dicBigType) {
		logger.debug(JsonUtils.toJson(dicBigType.getDicSmallTypes(), DicSmallType.class));
		
		boolean success = true;
		dictionaryService.saveDicBigType(dicBigType);

		for (DicSmallType dicSmallType : dicBigType.getDicSmallTypes()) {
			dicSmallType.setBigTypeId(dicBigType.getBigTypeId());
		}
		dictionaryService.saveDicSmallTypes(dicBigType.getDicSmallTypes());

		return AjaxResponse.create("保存成功");
	}

	/**
	 * 功能说明 : 停用启用
	 * 
	 * @param id
	 * @param disEnableFlag true为停用启用成功，false不能停用
	 * @return
	 * @author rutine
	 * @time Jun 11, 2013 6:08:04 PM
	 */
	@GetMapping(value = "/disEnable")
	public AjaxResponse<String> disEnable(
			@RequestParam long id,
			@RequestParam String disEnableFlag) {

		dictionaryService.disEnableDicBigType(id, disEnableFlag);

		return AjaxResponse.create("操作成功");
	}

	/**
	 * 功能说明 : 根据大类代码查询所有小类
	 * 
	 * @return
	 * @author rutine
	 * @time Dec 15, 2012 4:21:10 PM
	 */
	@GetMapping(value = "/get/small/type")
	public AjaxResponse<List<DicSmallType>> getSmallTypeByBigTypeCode(@RequestParam String bigTypeCode) {

		List<DicSmallType> dicSmallTypeList = dictionaryService.findDicSmallTypesByBigTypeCode(bigTypeCode);

		return AjaxResponse.create(dicSmallTypeList);
	}

	/**
	 * 功能说明 : 修改字典, 直接删除字典大类关联的字典小类，保存字典小类
	 * 
	 * @param dicBigType 字典大类对象
	 * @return
	 * @author rutine
	 * @time Jun 11, 2013 5:43:50 PM
	 */
	@PutMapping(value = "/update")
	public AjaxResponse<String> putUpdate(DicBigType dicBigType) {
		logger.debug(JsonUtils.toJson(dicBigType.getDicSmallTypes(), DicSmallType.class));
		
		dictionaryService.updateDicBigType(dicBigType);

		for (DicSmallType sysplDicSmallType : dicBigType.getDicSmallTypes()) {
			sysplDicSmallType.setBigTypeId(dicBigType.getBigTypeId());
		}
		dictionaryService.saveDicSmallTypes(dicBigType.getDicSmallTypes());

		return AjaxResponse.create("修改字典成功");
	}

	@GetMapping(value = "/view")
	public AjaxResponse<DictionaryViewVo> getView(@RequestParam long id) {
		DicBigType bigDictionary = dictionaryService.getDicBigTypeByBigTypeId(id);
		List<DicSmallType> smallDictionarys = dictionaryService
				.findDicSmallTypesByBigTypeCode(bigDictionary.getBigTypeCode());

		DictionaryViewVo vo = new DictionaryViewVo();
		vo.setBigType(bigDictionary);
		vo.setSmallTypes(smallDictionarys);

		return AjaxResponse.create(vo);
	}

	static class DictionaryViewVo {
		private DicBigType bigType;
		private List<DicSmallType> smallTypes;

		public DicBigType getBigType() {
			return bigType;
		}

		public void setBigType(DicBigType bigType) {
			this.bigType = bigType;
		}

		public List<DicSmallType> getSmallTypes() {
			return smallTypes;
		}

		public void setSmallTypes(List<DicSmallType> smallTypes) {
			this.smallTypes = smallTypes;
		}
	}
}
