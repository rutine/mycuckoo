package com.mycuckoo.web.platform;

import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.Affiche;
import com.mycuckoo.service.platform.AfficheService;
import com.mycuckoo.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public AjaxResponse<Page<Affiche>> list(Querier querier) {
        Page<Affiche> page = afficheService.findByPage(querier);

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

        return AjaxResponse.success("保存公告成功");
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

        return AjaxResponse.success("修改公告成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<Affiche> get(@PathVariable long id) {
        Affiche affiche = afficheService.get(id);

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

        return AjaxResponse.success("删除公告成功");
    }
}
