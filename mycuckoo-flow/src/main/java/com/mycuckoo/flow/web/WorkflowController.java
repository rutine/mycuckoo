package com.mycuckoo.flow.web;

import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.flow.base.WorkflowService;
import com.mycuckoo.flow.web.vo.req.WorkflowVos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 5.0.0
 * @time 2026/4/17 20:57
 */
@RestController
@RequestMapping("/flow/mgr")
public class WorkflowController {

    @Autowired
    private WorkflowService workflowService;

    @GetMapping("/definitions")
    public AjaxResponse<Page<Map<String, Object>>> list(Querier querier) {
        return AjaxResponse.create(workflowService.findDefinitionPage(querier));
    }

    @GetMapping("/instances")
    public AjaxResponse<Page<Map<String, Object>>> list2(Querier querier) {
        return AjaxResponse.create(workflowService.findInstancePage(querier));
    }

    @PostMapping("/definitions")
    public AjaxResponse<Map<String, Object>> deployDefinition(@RequestBody WorkflowVos.CreateDefinitionVo vo) {
        return AjaxResponse.create(workflowService.deployBpmnXml(vo.getXml()));
    }

    @GetMapping("/definitions/bpmn-model")
    public AjaxResponse<String> getBpmnModel(@RequestParam String processDefinitionKey) {
        return AjaxResponse.create(workflowService.getLatestBpmnXml(processDefinitionKey));
    }
}
