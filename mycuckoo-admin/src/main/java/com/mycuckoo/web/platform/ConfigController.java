package com.mycuckoo.web.platform;

import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.service.platform.TableConfigService;
import com.mycuckoo.web.vo.res.TableConfigVos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/6/2 11:52
 */
@RestController
@RequestMapping("/platform/config")
public class ConfigController {

    @Autowired
    private TableConfigService tableConfigService;


    @PostMapping("/list-table-config")
    public AjaxResponse<List<TableConfigVos.Config>> listByTableCode(String tableCode) {
        List<TableConfigVos.Config> list = tableConfigService.findByTableCode(tableCode);

        return AjaxResponse.create(list);
    }
}
