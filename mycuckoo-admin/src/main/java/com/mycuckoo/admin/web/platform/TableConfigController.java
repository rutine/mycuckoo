package com.mycuckoo.admin.web.platform;

import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.admin.service.platform.TableConfigService;
import com.mycuckoo.admin.entity.vo.TableConfigVos;
import com.mycuckoo.admin.entity.dto.TableConfigDtos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/6/2 7:33
 */
@RestController
@RequestMapping("/platform/table-config/mgr")
public class TableConfigController {

    @Autowired
    private TableConfigService tableConfigService;


    /**
     * 功能说明 : 保存
     *
     */
    @PostMapping
    public AjaxResponse<String> create(@RequestBody TableConfigVos.Create vo) {
        tableConfigService.save(vo);

        return AjaxResponse.success("保存成功");
    }

    @GetMapping("/{moduleId}/list")
    public AjaxResponse<List<TableConfigDtos.Detail>> listByModuleId(@PathVariable long moduleId) {
        return AjaxResponse.create(tableConfigService.findByModuleId(moduleId));
    }
}
