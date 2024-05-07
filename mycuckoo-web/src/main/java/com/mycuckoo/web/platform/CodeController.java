package com.mycuckoo.web.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.utils.SessionUtil;
import com.mycuckoo.domain.platform.Code;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.platform.CodeService;
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

import java.util.Date;
import java.util.Map;

import static com.mycuckoo.web.constant.ActionConst.LIMIT;

/**
 * 功能说明: 系统编码Controller
 *
 * @author rutine
 * @version 3.0.0
 * @time Oct 14, 2014 3:16:02 PM
 */
@RestController
@RequestMapping("/platform/code/mgr")
public class CodeController {
    private static Logger logger = LoggerFactory.getLogger(CodeController.class);

    @Autowired
    private CodeService codeService;


    @GetMapping
    public AjaxResponse<Page<Code>> list(
            @RequestParam(value = "code", defaultValue = "") String code,
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "moduleName", defaultValue = "") String moduleName,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {


        Map<String, Object> params = Maps.newHashMap();
        params.put("code", StringUtils.isBlank(code) ? null : "%" + code + "%");
        params.put("name", StringUtils.isBlank(name) ? null : "%" + name + "%");
        params.put("moduleName", StringUtils.isBlank(moduleName) ? null : "%" + moduleName + "%");
        Page<Code> page = codeService.findByPage(params, new PageRequest(pageNo - 1, pageSize));

        return AjaxResponse.create(page);
    }

    /**
     * 功能说明 : 创建新编码
     *
     * @param code
     * @return
     * @author rutine
     * @time Jun 25, 2013 8:48:33 PM
     */
    @PostMapping
    public AjaxResponse<String> create(@RequestBody Code code) {

        logger.debug(JsonUtils.toJson(code));

        code.setUpdater(SessionUtil.getUserCode());
        code.setUpdateDate(new Date());
        code.setCreator(SessionUtil.getUserCode());
        code.setCreateDate(new Date());
        codeService.saveCode(code);

        return AjaxResponse.create("保存系统编码成功");
    }

    /**
     * 功能说明 : 修改编码
     *
     * @param code
     * @return
     * @author rutine
     * @time Jun 25, 2013 8:53:05 PM
     */
    @PutMapping
    public AjaxResponse<String> update(@RequestBody Code code) {
        code.setUpdater(SessionUtil.getUserCode());
        code.setUpdateDate(new Date());
        codeService.update(code);

        return AjaxResponse.create("修改系统编码成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<Code> get(@PathVariable long id) {
        Code code = codeService.get(id);

        logger.debug(JsonUtils.toJson(code));

        return AjaxResponse.create(code);
    }

    /**
     * 功能说明 : 删除系统编码
     *
     * @param id
     * @return
     * @author rutine
     * @time Jun 25, 2013 8:59:46 PM
     */
    @DeleteMapping("/{id}")
    public AjaxResponse<String> delete(@PathVariable long id) {

        throw new ApplicationException("找不到删除记录的方法!");

//        return AjaxResponse.create("删除系统编码成功");
    }

    /**
     * 功能说明 : 停用/启用编码
     *
     * @param id
     * @param disEnableFlag 停用/启用标志
     * @return
     * @author rutine
     * @time Jun 25, 2013 8:57:53 PM
     */
    @PutMapping("/{id}/disEnable/{disEnableFlag}")
    public AjaxResponse<String> disEnable(
            @PathVariable long id,
            @PathVariable String disEnableFlag) {

        boolean disEnableBol = codeService.disEnable(id, disEnableFlag);

        return AjaxResponse.create("操作成功");
    }

}
