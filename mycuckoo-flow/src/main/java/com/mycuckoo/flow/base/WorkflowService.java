package com.mycuckoo.flow.base;

import com.mycuckoo.core.Querier;
import com.mycuckoo.core.exception.MyCuckooException;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageImpl;
import com.mycuckoo.core.util.web.SessionContextHolder;
import com.mycuckoo.flow.util.FlowUtils;
import com.mycuckoo.flow.web.vo.req.WorkflowVos;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.api.FlowableOptimisticLockingException;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author rutine
 * @date 2024/11/13 13:52
 */
@Service(WorkflowHelper.SERVICE_NAME)
public class WorkflowService extends WorkflowInterceptorAdapter {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TransactionTemplate transactionTemplate;



    //查询流程最新版本流程定义
    public Page<Map<String, Object>> findDefinitionPage(Querier querier) {
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionKey().desc()
                .latestVersion();
        long count = query.count();
        List<ProcessDefinition> list = query.listPage((querier.getPageNo() - 1) * querier.getPageSize(), querier.getPageSize());

        List<Map<String, Object>> datas = list.stream()
                .map(o -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", o.getId());
                    map.put("name", o.getName());
                    map.put("description", o.getDescription());
                    map.put("key", o.getKey());
                    map.put("resourceName", o.getResourceName());
                    map.put("tenantId", o.getTenantId());
                    map.put("diagramResourceName", o.getDiagramResourceName());
                    map.put("version", o.getVersion());
                    map.put("hasStartFormKey", o.hasStartFormKey());
                    return map;
                }).collect(Collectors.toList());


        return new PageImpl<>(datas, querier, count);
    }

    public Page<Map<String, Object>>  findInstancePage(Querier querier) {
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery()
                .involvedUser(null) //参与审批的
                .startedBy(null) //发起人
                .orderByProcessInstanceId().desc();

        long count = query.count();
        List<HistoricProcessInstance> list = query.listPage((querier.getPageNo() - 1) * querier.getPageSize(), querier.getPageSize());
        List<Map<String, Object>> datas = list.stream()
                .map(o -> {
                    String processInstanceId = o.getId();
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", o.getProcessDefinitionId());
                    map.put("name", o.getProcessDefinitionName());
                    map.put("processInstanceId", processInstanceId);
                    map.put("key", o.getProcessDefinitionKey());
                    map.put("businessKey", o.getBusinessKey());
                    map.put("startUser", o.getStartUserId());
                    map.put("deleteReason", o.getDeleteReason());
                    map.put("startTime", o.getStartTime());
                    map.put("endTime", o.getEndTime());
                    if (o.getEndTime() == null) {
                        map.put("status", "审批中");
                    } else {
                        map.put("status", "审批完成");
                    }
                    // 获取与任务相关的所有变量
                    // 获取该流程实例的变量
                    List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery()
                            .processInstanceId(processInstanceId)
                            .list();
                    if(variables != null) {
                        for (HistoricVariableInstance variable : variables) {
                            map.put(variable.getVariableName(), variable.getValue());
                            if ("reject".equals(variable.getVariableName())
                                    && "true".equals(Objects.toString(variable.getValue()).trim())) {
                                map.put("status", "审批驳回");
                            }
                        }
                    }
                    return map;
                }).collect(Collectors.toList());

        return new PageImpl<>(datas, querier, count);
    }

    public Page<Map<String, Object>> findMyTodoTaskPage(Querier querier) {
        Long tenantId = SessionContextHolder.getOrganId();
        Long userId = SessionContextHolder.getUserId();
        if (userId == null) {
            throw new MyCuckooException("用户未登录");
        }

        return this.findTodoTaskPage(querier, String.valueOf(tenantId), String.valueOf(userId));
    }

    public Page<Map<String, Object>> findTodoTaskPage(Querier querier, String tenantId, String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new MyCuckooException("用户id不能为空");
        }

        TaskQuery query = taskService.createTaskQuery()
                .taskTenantId(tenantId)
                .taskCandidateOrAssigned(userId)
                .orderByTaskCreateTime().desc();

        long count = query.count();
        List<Task> list = query.listPage((querier.getPageNo() - 1) * querier.getPageSize(), querier.getPageSize());
        Map<String, ProcessDefinition> definitionCache = new HashMap<>();
        List<Map<String, Object>> datas = list.stream()
                .map(task -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("taskId", task.getId());
                    map.put("taskName", task.getName());
                    map.put("taskDefinitionKey", task.getTaskDefinitionKey());
                    map.put("assignee", task.getAssignee());
                    map.put("owner", task.getOwner());
                    map.put("createTime", task.getCreateTime());
                    map.put("claimTime", task.getClaimTime());
                    map.put("dueDate", task.getDueDate());
                    map.put("priority", task.getPriority());
                    map.put("processInstanceId", task.getProcessInstanceId());
                    map.put("processDefinitionId", task.getProcessDefinitionId());
                    map.put("executionId", task.getExecutionId());

                    ProcessDefinition definition = definitionCache.computeIfAbsent(task.getProcessDefinitionId(), id ->
                            repositoryService.createProcessDefinitionQuery()
                                    .processDefinitionId(id)
                                    .singleResult());
                    if (definition != null) {
                        map.put("processDefinitionName", definition.getName());
                        map.put("processDefinitionKey", definition.getKey());
                        map.put("processDefinitionVersion", definition.getVersion());
                    }

                    Map<String, Object> variables = taskService.getVariables(task.getId());
                    map.put("variables", variables);
                    map.put(WorkflowHelper.getInitiatorKey(), variables.get(WorkflowHelper.getInitiatorKey()));
                    map.put(WorkflowHelper.getFormIdKey(), variables.get(WorkflowHelper.getFormIdKey()));
                    map.put(WorkflowHelper.getFormTypeKey(), variables.get(WorkflowHelper.getFormTypeKey()));
                    return map;
                }).collect(Collectors.toList());

        return new PageImpl<>(datas, querier, count);
    }

    public String getLatestModel(String processDefinitionKey) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .latestVersion()
                .singleResult();
        if (processDefinition == null) {
            throw new MyCuckooException("流程定义不存在");
        }

        BpmnModel model = repositoryService.getBpmnModel(processDefinition.getId());
        return new String(new BpmnXMLConverter().convertToXML(model), StandardCharsets.UTF_8);
    }

    @Transactional
    public Map<String, Object> deployModel(String bpmnXml) {
        if (!StringUtils.hasText(bpmnXml)) {
            throw new MyCuckooException("流程XML不能为空");
        }

        WorkflowVos.CreateDefinitionVo parseVo = FlowUtils.parseBpmnXml(bpmnXml);
        Deployment deployment = repositoryService.createDeployment()
                .tenantId(String.valueOf(SessionContextHolder.getOrganId()))
                .key(parseVo.getKey())
                .name(parseVo.getName())
                .addString(parseVo.getKey() + ".bpmn20.xml", bpmnXml)
                .deploy();

        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        if (definition == null) {
            throw new MyCuckooException("流程定义创建失败");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("deploymentId", deployment.getId());
        result.put("definitionId", definition.getId());
        result.put("definitionKey", definition.getKey());
        result.put("definitionName", definition.getName());
        result.put("version", definition.getVersion());
        return result;
    }

    @Transactional
    public void deployModel(String processDefId, String processName, WorkflowConfig config) {
        BpmnModel model = WorkflowHelper.createBpmnModel(processDefId, processName, config);

//        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
//        String bpmn20Xml = new String(bpmnXMLConverter.convertToXML(model), StandardCharsets.UTF_8);
//        try {
//            FileUtils.write(new File("C:\\Users\\Admin\\Desktop\\cuck.xml"), bpmn20Xml, StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        repositoryService.createDeployment()
                .name(processName)
                .tenantId(String.valueOf(SessionContextHolder.getOrganId()))
                .addBpmnModel(processDefId + ".bpmn20.xml", model)
                .deploy();
    }

    @Transactional
    public String startProcess(WorkflowConfig<WorkflowConfig.Stage> config, String formId, String formType, String initiator) {
//        WorkflowDefVo def = workflowDefInfoMapper.getDetail(config.getAuditTemplateId());
//        if (def == null) {
//            throw new InvalidArgumentException("审批流无效");
//        }
//        if (config.getAuditor().size() != def.getStage().size()) {
//            throw new InvalidArgumentException("审批流程已变更，请重新选择");
//        }

        Set<String> set = new HashSet<>();
        Map<String, Object> variables = new HashMap<>();
        for (int i = config.getStage().size() - 1; i >= 0; i--) {
            List<WorkflowConfig.User> auditors = config.getStage().get(i).getUser();
            List<String> users = new ArrayList<>();
            for (int j = auditors.size() - 1; j >= 0; j--) {
                if (auditors.get(j).getUserId() != null && (!config.isUnique() || set.add(auditors.get(j).getUserId()))) {
                    users.add(0, auditors.get(j).getUserId());
                }
            }
            variables.put(WorkflowHelper.getAssgineeListKey(i), users);
        }
//        variables.put("auditTemplateId", config.getAuditTemplateId());
        //保存发起人到流程变量
        variables.put(WorkflowHelper.getInitiatorKey(), initiator);
        variables.put(WorkflowHelper.getFormIdKey(), formId);
        variables.put(WorkflowHelper.getFormTypeKey(), formType);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(config.getProcessDefId(), formType, variables, String.valueOf(SessionContextHolder.getOrganId()));

        String processInstanceId = processInstance.getId();

//        this.saveWorkflowConfig(processInstanceId, formId, formType, config, userInfo);

        return processInstanceId;
    }

    public String startProcess(String processDefinitionId, String formType, Map<String, Object> formVariables) {
        Map<String, Object> variables = new HashMap<>();
        if (formVariables != null) {
            variables.putAll(formVariables);
        }

        BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
        variables.putAll(WorkflowHelper.resolveBpmnModelVariables(model));

        return runtimeService.startProcessInstanceById(processDefinitionId, formType, variables).getId();
    }

    public WorkflowState completeTask(String workflowId, String userId, boolean pass) {
        FlowableOptimisticLockingException lastException = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                return transactionTemplate.execute(status -> doCompleteTask(workflowId, userId, pass));
            } catch (FlowableOptimisticLockingException e) {
                lastException = e;
                logger.warn("完成任务发生乐观锁冲突，第{}次重试, workflowId={}, userId={}", attempt, workflowId, userId);
            }
        }

        throw lastException;
    }

    private WorkflowState doCompleteTask(String workflowId, String userId, boolean pass) {
        Task task = taskService.createTaskQuery()
                .taskTenantId(String.valueOf(SessionContextHolder.getOrganId()))
                .processInstanceId(workflowId)
                .taskCandidateOrAssigned(userId)
                .singleResult();
        if (task == null) {
            throw new MyCuckooException("任务不存在或已被处理");
        }

        String refuseId = WorkflowHelper.getResubmitId();
        if (task.getTaskDefinitionKey().equals(refuseId)) {
            //重新提交, 直接完成任务
            taskService.complete(task.getId());
        }
        else {
            Map<String, Object> variables = new HashMap<>();
            if (!pass) {
                variables.put(WorkflowHelper.getStageRejectedKey(task.getTaskDefinitionKey()), true);
            }

            taskService.complete(task.getId(), variables);
        }

        //查询任务当前状态
        List<Task> allTask = taskService.createTaskQuery().processInstanceId(workflowId).list();
        if (allTask == null || allTask.isEmpty()) {
            //工作流已经完成
            return WorkflowState.FINISH;
        } else if (allTask.stream().map(Task::getTaskDefinitionKey).filter(key -> key.equals(refuseId)).findFirst().isPresent()) {
            return WorkflowState.REJECT;
        } else {
            return WorkflowState.AUDITING;
        }
    }

    public void transferTask(String workflowId, String fromAssignee, String toAssignee) {
        Task task = taskService.createTaskQuery()
                .processInstanceId(workflowId)
                .taskAssignee(fromAssignee)
                .singleResult();
        if (task == null) {
            throw new MyCuckooException("任务不存在或已被处理");
        }

        taskService.setAssignee(task.getId(), toAssignee);
    }

    @Transactional
    public void closeWorkflow(String workflowId) {
        long count = runtimeService.createProcessInstanceQuery()
                .processInstanceTenantId(String.valueOf(SessionContextHolder.getOrganId()))
                .processInstanceId(workflowId)
                .count();
        if (count > 0) {
            runtimeService.deleteProcessInstance(workflowId, "手动删除");
//            this.deleteConfigByWorkflowId(workflowId);
        }
    }
}
