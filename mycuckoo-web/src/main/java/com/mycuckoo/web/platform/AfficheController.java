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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.mycuckoo.web.constant.ActionConst.LIMIT;

/**
 * 功能说明: 系统公告Controller
 *
 * @author rutine
 * @version 3.0.0
 * @time Oct 14, 2014 9:25:37 PM
 */
@RestController
@RequestMapping("/platform/affiche/mgr")
public class AfficheController {
    private static Logger logger = LoggerFactory.getLogger(AfficheController.class);

    @Autowired
    private AfficheService afficheService;

    @RequestMapping
    public AjaxResponse<Page<Affiche>> list(
            @RequestParam(value = "title", defaultValue = "") String title,
            @RequestParam(value = "publish", defaultValue = "0") Short publish,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("title", StringUtils.isBlank(title) ? null : "%" + title + "%");
        params.put("publish", publish);
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
    @PostMapping
    public AjaxResponse<String> create(@RequestBody Affiche affiche) {

        logger.debug(JsonUtils.toJson(affiche));

        afficheService.save(affiche);

        return AjaxResponse.create("保存公告成功");
    }

    /**
     * 功能说明 : 修改公告
     *
     * @param affiche
     * @return
     * @author rutine
     * @time Jun 29, 2013 8:55:38 AM
     */
    @PutMapping
    public AjaxResponse<String> update(@RequestBody Affiche affiche) {

        afficheService.update(affiche);

        return AjaxResponse.create("修改公告成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<Affiche> get(@PathVariable long id) {
        Affiche affiche = afficheService.get(id);

        logger.debug(JsonUtils.toJson(affiche));

        return AjaxResponse.create(affiche);
    }


    /**
     * 功能说明 : 删除公告
     *
     * @param afficheIdList 公告IDs
     * @return
     * @author rutine
     * @time Jun 25, 2013 8:59:46 PM
     */
    @DeleteMapping("/{ids}")
    public AjaxResponse<String> delete(@PathVariable(value = "ids") List<Long> afficheIdList) {

        afficheService.deleteByIds(afficheIdList);

        return AjaxResponse.create("删除公告成功");
    }
}
