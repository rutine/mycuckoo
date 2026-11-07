package com.mycuckoo.admin.web.cms;

import com.mycuckoo.admin.entity.dto.cms.FormDTOs;
import com.mycuckoo.admin.entity.vo.cms.FormBusinessVO;
import com.mycuckoo.admin.entity.vo.cms.FormDataVO;
import com.mycuckoo.admin.entity.vo.cms.FormVO;
import com.mycuckoo.admin.service.cms.FormService;
import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cms/form/mgr")
public class FormController {
    @Autowired
    private FormService cmsFormService;

    /**
     * 按模块编码查表单列表
     */
    @GetMapping
    public AjaxResponse<Page<FormVO>> list(Querier querier) {
        return AjaxResponse.create(cmsFormService.findByPage(querier));
    }

    /**
     * 保存表单定义（新建或更新）
     */
    @PostMapping
    public AjaxResponse<Void> save(@RequestBody FormDTOs.SaveDTO dto) {
        cmsFormService.save(dto);
        return AjaxResponse.success("保存成功");
    }

    /**
     * 按ID查表单定义
     */
    @GetMapping("/{id}")
    public AjaxResponse<FormVO> get(@PathVariable Long id) {
        return AjaxResponse.create(cmsFormService.get(id));
    }

    /**
     * 按编码查表单定义
     */
    @GetMapping("/biz-type/{bizType}")
    public AjaxResponse<FormVO> get(@PathVariable String bizType) {
        return AjaxResponse.create(cmsFormService.get(bizType));
    }

    /**
     * 分页查表单数据
     */
    @GetMapping("/data")
    public AjaxResponse<List<FormDataVO>> listData(@RequestParam String formCode,
                                                   @RequestParam(required = false) String bizId) {
        return AjaxResponse.create(cmsFormService.findDataByPage(formCode, bizId));
    }

    /**
     * 保存表单数据
     */
    @PostMapping("/data")
    public AjaxResponse<Long> saveData(@RequestBody FormDTOs.DataSaveDTO dto) {
        return AjaxResponse.create(cmsFormService.saveData(dto));
    }

    /**
     * 按业务编码和业务ID查表单配置和数据
     */
    @GetMapping("/data/{bizType}")
    public AjaxResponse<FormBusinessVO> getData(@PathVariable String bizType,
                                                @RequestParam(required = false) String bizId) {
        return AjaxResponse.create(cmsFormService.getData(bizType, bizId));
    }

    /**
     * 修改单条数据的字段值
     */
    @PostMapping("/data/updateField")
    public AjaxResponse<Void> updateFieldValue(@RequestBody FormDTOs.UpdateFieldDTO dto) {
        cmsFormService.updateFieldValue(dto.getDataId(), dto.getField(), dto.getValue(), dto.getOperator());
        return AjaxResponse.success("");
    }

    /**
     * 批量重跑某字段的处理器
     */
    @PostMapping("/data/reprocess")
    public AjaxResponse<Integer> reprocessField(@RequestBody FormDTOs.ReprocessDTO dto) {
        return AjaxResponse.create(cmsFormService.reprocessField(dto.getBizType(), dto.getField(), dto.getOperator()));
    }
}