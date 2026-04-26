package com.mycuckoo.flow.web;

import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.flow.base.WorkflowService;
import com.mycuckoo.flow.web.vo.req.WorkflowVos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public AjaxResponse<Page<Map<String, Object>>> listModels(Querier querier) {
        return AjaxResponse.create(workflowService.findDefinitionPage(querier));
    }

    @PostMapping("/definitions")
    public AjaxResponse<Map<String, Object>> deployModel(@RequestBody WorkflowVos.CreateDefinitionVo vo) {
        return AjaxResponse.create(workflowService.deployModel(vo.getXml()));
    }

    @GetMapping("/definitions/{key}")
    public AjaxResponse<String> getModel(@PathVariable String key) {
        return AjaxResponse.create(workflowService.getLatestModel(key));
    }

    @GetMapping("/instances")
    public AjaxResponse<Page<Map<String, Object>>> listInstances(Querier querier) {
        return AjaxResponse.create(workflowService.findInstancePage(querier));
    }

    @GetMapping("/tasks/todo")
    public AjaxResponse<Page<Map<String, Object>>> listMyTodoTasks(Querier querier) {
        return AjaxResponse.create(workflowService.findMyTodoTaskPage(querier));
    }
}
