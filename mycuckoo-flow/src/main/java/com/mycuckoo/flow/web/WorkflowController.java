package com.mycuckoo.flow.web;

import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.util.web.SessionContextHolder;
import com.mycuckoo.flow.base.WorkflowService;
import com.mycuckoo.flow.base.WorkflowState;
import com.mycuckoo.flow.web.vo.req.WorkflowVos;
import jakarta.validation.Valid;
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

    @GetMapping("/definition")
    public AjaxResponse<Page<Map<String, Object>>> listModels(Querier querier) {
        return AjaxResponse.create(workflowService.findDefinitionPage(querier));
    }

    @PutMapping("/definition")
    public AjaxResponse<Map<String, Object>> deployModel(@RequestBody @Valid WorkflowVos.CreateDefinitionVo vo) {
        return AjaxResponse.create(workflowService.deployModel(vo.getXml()));
    }

    @GetMapping("/definition/{key}")
    public AjaxResponse<String> getModel(@PathVariable String key) {
        return AjaxResponse.create(workflowService.getLatestModel(key));
    }

    @GetMapping("/definition/{definitionId}/variables")
    public AjaxResponse<Map<String, Object>> getModelVariables(@PathVariable String definitionId) {
        return AjaxResponse.create(workflowService.getModelVariables(definitionId));
    }

    @GetMapping("/instance")
    public AjaxResponse<Page<Map<String, Object>>> listInstances(Querier querier) {
        return AjaxResponse.create(workflowService.findInstancePage(querier));
    }

    @PostMapping("/instance")
    public AjaxResponse<String> create(@RequestBody @Valid WorkflowVos.CreateInstanceVo vo) {
        return AjaxResponse.create(workflowService.startProcess(
                vo.getDefinitionId(),
                vo.getFormId(),
                vo.getFormType(),
                vo.getFormVariables()));
    }

    @GetMapping("/instance/process-records")
    public AjaxResponse<Map<String, Object>> listProcessRecord(@RequestParam String instanceId,
                                                               @RequestParam String deployId) {
        return AjaxResponse.create(workflowService.getProcessRecords(instanceId, deployId));
    }

    @GetMapping("/task/todo")
    public AjaxResponse<Page<Map<String, Object>>> listMyTodoTasks(Querier querier) {
        return AjaxResponse.create(workflowService.findMyTodoTaskPage(querier));
    }

    @GetMapping("/task/done")
    public AjaxResponse<Page<Map<String, Object>>> listMyDoneTasks(Querier querier) {
        return AjaxResponse.create(workflowService.findMyDoneTaskPage(querier));
    }


    @PutMapping("/task/complete")
    public AjaxResponse<WorkflowState> completeTask(@RequestBody @Valid WorkflowVos.CompleteTaskVo vo) {
        Long userId = SessionContextHolder.getUserId();
        return AjaxResponse.create(workflowService.completeTask(vo.getTaskId(),
                String.valueOf(userId),
                vo.getType(), vo.getComment()));
    }
}
