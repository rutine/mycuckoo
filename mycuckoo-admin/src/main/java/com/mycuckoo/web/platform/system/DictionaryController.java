package com.mycuckoo.web.platform.system;


import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.DictBigType;
import com.mycuckoo.domain.platform.DictSmallType;
import com.mycuckoo.service.platform.DictionaryService;
import com.mycuckoo.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能说明: 字典Controller
 *
 * @author rutine
 * @version 3.0.0
 * @time Oct 18, 2014 7:50:17 AM
 */
@RestController
@RequestMapping(value = "/platform/system/dictionary/mgr")
public class DictionaryController {
    private static Logger logger = LoggerFactory.getLogger(DictionaryController.class);

    @Autowired
    private DictionaryService dictionaryService;


    @GetMapping
    public AjaxResponse<Page<DictBigType>> list(Querier querier) {
        Page<DictBigType> page = dictionaryService.findBigTypesByPage(querier);

        return AjaxResponse.create(page);
    }

    /**
     * 功能说明 : 创建新字典大类
     *
     * @param dictBigType
     * @return
     * @author rutine
     * @time Jun 11, 2013 4:29:28 PM
     */
    @PostMapping
    public AjaxResponse<String> create(@RequestBody DictBigType dictBigType) {
        logger.debug(JsonUtils.toJson(dictBigType.getSmallTypes(), DictSmallType.class));

        dictionaryService.saveBigType(dictBigType);

        return AjaxResponse.success("保存成功");
    }

    /**
     * 功能说明 : 修改字典, 直接删除字典大类关联的字典小类，保存字典小类
     *
     * @param dictBigType 字典大类对象
     * @return
     * @author rutine
     * @time Jun 11, 2013 5:43:50 PM
     */
    @PutMapping
    public AjaxResponse<String> update(@RequestBody DictBigType dictBigType) {
        logger.debug(JsonUtils.toJson(dictBigType.getSmallTypes(), DictSmallType.class));

        dictionaryService.updateBigType(dictBigType);

        return AjaxResponse.success("修改字典成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<DictBigType> get(@PathVariable long id) {
        DictBigType dictBigType = dictionaryService.getBigTypeByBigTypeId(id);
        List<DictSmallType> smallTypes = dictionaryService
                .findSmallTypesByBigTypeCode(dictBigType.getCode());
        dictBigType.setSmallTypes(smallTypes);

        return AjaxResponse.create(dictBigType);
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
    @PutMapping("/{id}/disEnable/{disEnableFlag}")
    public AjaxResponse<String> disEnable(
            @PathVariable long id,
            @PathVariable String disEnableFlag) {

        dictionaryService.disEnableBigType(id, disEnableFlag);

        return AjaxResponse.success("操作成功");
    }

    /**
     * 功能说明 : 根据大类代码查询所有小类
     *
     * @return
     * @author rutine
     * @time Dec 15, 2012 4:21:10 PM
     */
    @GetMapping("/{bigTypeCode}/small-type")
    public AjaxResponse<List<DictSmallType>> getSmallTypeByBigTypeCode(@PathVariable String bigTypeCode) {

        List<DictSmallType> dictSmallTypeList = dictionaryService.findSmallTypesByBigTypeCode(bigTypeCode);

        return AjaxResponse.create(dictSmallTypeList);
    }
}
