package com.mycuckoo.admin.web.platform;

import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.admin.service.platform.TableConfigService;
import com.mycuckoo.admin.entity.dto.TableConfigDTOS;
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
    public AjaxResponse<List<TableConfigDTOS.Config>> listByTableCode(String tableCode) {
        List<TableConfigDTOS.Config> list = tableConfigService.findByTableCode(tableCode);

        return AjaxResponse.create(list);
    }
}
