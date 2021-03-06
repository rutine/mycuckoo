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

import static com.mycuckoo.web.constant.ActionVariable.LIMIT;

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
    public AjaxResponse<Page<DicBigType>> list(
            @RequestParam(value = "bigTypeName", defaultValue = "") String dictName,
            @RequestParam(value = "bigTypeCode", defaultValue = "") String dictCode,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {

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
    @PostMapping
    public AjaxResponse<String> create(@RequestBody DicBigType dicBigType) {
        logger.debug(JsonUtils.toJson(dicBigType.getSmallTypes(), DicSmallType.class));

        dictionaryService.saveDicBigType(dicBigType);

        return AjaxResponse.create("保存成功");
    }

    /**
     * 功能说明 : 修改字典, 直接删除字典大类关联的字典小类，保存字典小类
     *
     * @param dicBigType 字典大类对象
     * @return
     * @author rutine
     * @time Jun 11, 2013 5:43:50 PM
     */
    @PutMapping
    public AjaxResponse<String> update(@RequestBody DicBigType dicBigType) {
        logger.debug(JsonUtils.toJson(dicBigType.getSmallTypes(), DicSmallType.class));

        dictionaryService.updateDicBigType(dicBigType);

        return AjaxResponse.create("修改字典成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<DicBigType> get(@PathVariable long id) {
        DicBigType dicBigType = dictionaryService.getDicBigTypeByBigTypeId(id);
        List<DicSmallType> smallTypes = dictionaryService
                .findDicSmallTypesByBigTypeCode(dicBigType.getBigTypeCode());
        dicBigType.setSmallTypes(smallTypes);

        return AjaxResponse.create(dicBigType);
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
    @GetMapping("/{bigTypeCode}/small-type")
    public AjaxResponse<List<DicSmallType>> getSmallTypeByBigTypeCode(@PathVariable String bigTypeCode) {

        List<DicSmallType> dicSmallTypeList = dictionaryService.findDicSmallTypesByBigTypeCode(bigTypeCode);

        return AjaxResponse.create(dicSmallTypeList);
    }
}
