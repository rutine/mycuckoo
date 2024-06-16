package com.mycuckoo.web.platform;

import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.exception.ApplicationException;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.Code;
import com.mycuckoo.service.platform.CodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public AjaxResponse<Page<Code>> list(Querier querier) {
        Page<Code> page = codeService.findByPage(querier);

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
        codeService.save(code);

        return AjaxResponse.success("保存系统编码成功");
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
        codeService.update(code);

        return AjaxResponse.success("修改系统编码成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<Code> get(@PathVariable long id) {
        Code code = codeService.get(id);

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

        return AjaxResponse.success("操作成功");
    }

}
