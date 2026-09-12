package com.mycuckoo.web.platform.system;


import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.Dictionary;
import com.mycuckoo.domain.platform.DictionaryItem;
import com.mycuckoo.service.platform.DictionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 功能说明: 字典Controller
 *
 * @author rutine
 * @version 5.0.0
 * @time 2026/9/12 10:00
 */
@RestController
@RequestMapping(value = "/platform/system/dictionary/mgr")
public class DictionaryController {
    private static Logger logger = LoggerFactory.getLogger(DictionaryController.class);

    @Autowired
    private DictionaryService dictionaryService;


    @GetMapping
    public AjaxResponse<Page<Dictionary>> list(Querier querier) {
        return AjaxResponse.create(dictionaryService.findByPage(querier));
    }

    /**
     * 功能说明 : 创建新字典
     *
     * @param dictionary 字典对象
     * @return
     */
    @PostMapping
    public AjaxResponse<String> create(@RequestBody Dictionary dictionary) {
        dictionaryService.save(dictionary);

        return AjaxResponse.success("保存成功");
    }

    /**
     * 功能说明 : 修改字典, 直接删除字典关联的字典项，保存字典项
     *
     * @param dictionary 字典对象
     * @return
     */
    @PutMapping
    public AjaxResponse<String> update(@RequestBody Dictionary dictionary) {
        dictionaryService.update(dictionary);

        return AjaxResponse.success("修改字典成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<Dictionary> get(@PathVariable long id) {
        Dictionary dictionary = dictionaryService.get(id);
        List<DictionaryItem> items = dictionaryService.findItemsByDictCode(dictionary.getCode());
        dictionary.setItems(items);

        return AjaxResponse.create(dictionary);
    }

    /**
     * 功能说明 : 停用启用
     *
     * @param id             字典ID
     * @param disEnableFlag  停用/启用标志
     * @return
     */
    @PutMapping("/{id}/disEnable/{disEnableFlag}")
    public AjaxResponse<String> disEnable(
            @PathVariable long id,
            @PathVariable String disEnableFlag) {

        dictionaryService.disEnable(id, disEnableFlag);

        return AjaxResponse.success("操作成功");
    }

    /**
     * 功能说明 : 根据字典编码查询所有字典项
     *
     * @return
     */
    @GetMapping("/{dictCode}/items")
    public AjaxResponse<List<DictionaryItem>> getItems(@PathVariable String dictCode) {
        List<DictionaryItem> items = dictionaryService.findItemsByDictCode(dictCode);

        return AjaxResponse.create(items);
    }

    @GetMapping("/items")
    public AjaxResponse<Map<String, List<DictionaryItem>>> getItemMap(@RequestParam("typeCodes") List<String> dictCodes) {
        return AjaxResponse.create(dictionaryService.findItemMapByDictCodes(dictCodes));
    }
}