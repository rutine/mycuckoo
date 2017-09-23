package com.mycuckoo.web.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.domain.platform.Affiche;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.platform.AfficheService;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.mycuckoo.web.constant.ActionVariable.LIMIT;

/**
 * 功能说明: 系统公告Controller
 * 
 * @author rutine
 * @time Oct 14, 2014 9:25:37 PM
 * @version 3.0.0
 */
@RestController
@RequestMapping("/platform/affiche/mgr")
public class AfficheController {
	private static Logger logger = LoggerFactory.getLogger(AfficheController.class);

	@Autowired
	private AfficheService afficheService;

	@RequestMapping(value = "/list")
	public AjaxResponse<Page<Affiche>> afficheMgr(
			@RequestParam(value = "afficheTitle", defaultValue = "") String afficheTitle,
			@RequestParam(value = "affichePulish", defaultValue = "0") Short affichePulish,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {

		Map<String, Object> params = Maps.newHashMap();
		params.put("afficheTitle", StringUtils.isBlank(afficheTitle) ? null : "%" + afficheTitle + "%");
		params.put("affichePulish", affichePulish);
		Page<Affiche> page = afficheService.findByPage(params, new PageRequest(pageNo - 1, pageSize));

		return AjaxResponse.create(page);
	}

	/**
	 * 功能说明 : 创建新公告
	 * 
	 * @param affiche
	 * @return
	 * @author rutine
	 * @time Jun 29, 2013 8:39:57 AM
	 */
	@PutMapping(value = "/create")
	public AjaxResponse<String> putCreate(@RequestBody Affiche affiche) {
		
		logger.debug(JsonUtils.toJson(affiche));

		afficheService.save(affiche);

		return AjaxResponse.create("保存公告成功");
	}


	/**
	 * 功能说明 : 删除公告
	 * 
	 * @param afficheIdList 公告IDs
	 * @return
	 * @author rutine
	 * @time Jun 25, 2013 8:59:46 PM
	 */
	@DeleteMapping(value = "/delete")
	public AjaxResponse<String> delete(@RequestParam(value = "ids") List<Long> afficheIdList) {

		afficheService.deleteByIds(afficheIdList);

		return AjaxResponse.create("删除公告成功");
	}

	/**
	 * 功能说明 : 修改公告
	 * 
	 * @param affiche
	 * @return
	 * @author rutine
	 * @time Jun 29, 2013 8:55:38 AM
	 */
	@PutMapping(value = "/update")
	public AjaxResponse<String> putUpdate(@RequestBody Affiche affiche) {

		afficheService.update(affiche);

		return AjaxResponse.create("修改公告成功");
	}

	@GetMapping(value = "/view")
	public AjaxResponse<Affiche> getView(@RequestParam long id) {
		Affiche affiche = afficheService.get(id);

		logger.debug(JsonUtils.toJson(affiche));

		return AjaxResponse.create(affiche);
	}

}
