package com.mycuckoo.flow.base;

import com.mycuckoo.core.Querier;
import com.mycuckoo.core.UserInfo;
import com.mycuckoo.core.exception.MyCuckooException;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageImpl;
import com.mycuckoo.core.util.web.SessionContextHolder;
import com.mycuckoo.flow.constant.enums.CommentType;
import com.mycuckoo.flow.util.FlowUtils;
import com.mycuckoo.flow.util.FlowableUtils;
import com.mycuckoo.flow.web.vo.req.WorkflowVos;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Gateway;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.api.FlowableOptimisticLockingException;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private IdentityService identityService;
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

        TaskQuery query = taskService.createTaskQuery()
                .taskTenantId(String.valueOf(tenantId))
//                .taskCandidateOrAssigned(String.valueOf(userId))
                .orderByTaskCreateTime().desc();

        long count = query.count();
        List<Task> list = query.listPage((querier.getPageNo() - 1) * querier.getPageSize(), querier.getPageSize());
        Map<String, ProcessDefinition> definitionCache = new HashMap<>();
        List<Map<String, Object>> datas = list.stream()
                .map(task -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("taskId", task.getId());
                    map.put("taskName", task.getName());
                    map.put("taskKey", task.getTaskDefinitionKey());
                    map.put("assignee", task.getAssignee());
                    map.put("owner", task.getOwner());
                    map.put("createTime", task.getCreateTime());
                    map.put("claimTime", task.getClaimTime());
                    map.put("dueDate", task.getDueDate());
                    map.put("priority", task.getPriority());
                    map.put("executionId", task.getExecutionId());
                    map.put("instanceId", task.getProcessInstanceId());
                    map.put("definitionId", task.getProcessDefinitionId());

                    ProcessDefinition definition = definitionCache.computeIfAbsent(task.getProcessDefinitionId(), id ->
                            repositoryService.createProcessDefinitionQuery()
                                    .processDefinitionId(id)
                                    .singleResult());
                    if (definition != null) {
                        map.put("deployId", definition.getDeploymentId());
                        map.put("definitionKey", definition.getKey());
                        map.put("definitionName", definition.getName());
                        map.put("definitionVersion", definition.getVersion());
                    }

                    Map<String, Object> variables = taskService.getVariables(task.getId());
                    map.put("variables", variables);
                    map.put(WorkflowHelper.getInitiatorKey(), variables.get(WorkflowHelper.getInitiatorKey()));
                    map.put(WorkflowHelper.getInitiatorNameKey(), variables.get(WorkflowHelper.getInitiatorNameKey()));
                    map.put(WorkflowHelper.getFormIdKey(), variables.get(WorkflowHelper.getFormIdKey()));
                    map.put(WorkflowHelper.getFormTypeKey(), variables.get(WorkflowHelper.getFormTypeKey()));
                    Object userMap = variables.get(WorkflowHelper.getUserMapKey());
                    if (userMap instanceof Map) {
                        map.put("assignee", task.getAssignee() + ":" + ((Map<?, ?>) userMap).get(task.getAssignee()));
                    }
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

    @Transactional
    public String startProcess(String processDefinitionId, String formId, String formType, Map<String, Object> formVariables) {
        Long userId = SessionContextHolder.getUserId();
        Map<String, Object> variables = new HashMap<>();
        BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
        variables.putAll(WorkflowHelper.resolveBpmnModelVariables(model));
        if (formVariables != null) {
            variables.putAll(formVariables);
        }
        if (StringUtils.hasText(formId)) {
            variables.put(WorkflowHelper.getFormIdKey(), formId);
        }
        if (StringUtils.hasText(formType)) {
            variables.put(WorkflowHelper.getFormTypeKey(), formType);
        }
        variables.put(WorkflowHelper.getInitiatorKey(), String.valueOf(userId));
        variables.put(WorkflowHelper.getInitiatorNameKey(), SessionContextHolder.getUserName());

        identityService.setAuthenticatedUserId(String.valueOf(userId));
        try {
            return runtimeService.startProcessInstanceById(processDefinitionId, formType, variables).getId();
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
    }

    @Transactional
    public WorkflowState completeTask(String taskId, String userId, CommentType type, String comment) {
        FlowableOptimisticLockingException lastException = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                return transactionTemplate.execute(status -> doCompleteTask(taskId, userId, type, comment));
            } catch (FlowableOptimisticLockingException e) {
                lastException = e;
                logger.warn("完成任务发生乐观锁冲突，第{}次重试, taskId={}, userId={}", attempt, taskId, userId);
            }
        }

        throw lastException;
    }

    private WorkflowState doCompleteTask(String taskId, String userId, CommentType type, String comment) {
        if (type != CommentType.NORMAL) {
            return this.doRejectTask(taskId, userId, comment);
        }

        Task task = taskService.createTaskQuery()
                .taskTenantId(String.valueOf(SessionContextHolder.getOrganId()))
                .taskId(taskId)
                .taskCandidateOrAssigned(userId)
                .singleResult();
        if (task == null) {
            throw new MyCuckooException("任务不存在或已被处理");
        }

        taskService.addComment(task.getId(), task.getProcessInstanceId(), type.code, comment);

        String refuseId = WorkflowHelper.getResubmitId();
        if (task.getTaskDefinitionKey().equals(refuseId)) {
            //重新提交, 直接完成任务
            taskService.complete(task.getId());
        }
        else {
            Map<String, Object> variables = new HashMap<>();
            if (type != CommentType.NORMAL) {
                variables.put(WorkflowHelper.getStageRejectedKey(task.getTaskDefinitionKey()), true);
            }

            taskService.complete(task.getId(), variables);
        }

        //查询任务当前状态
        List<Task> allTask = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        if (allTask == null || allTask.isEmpty()) {
            //工作流已经完成
            return WorkflowState.FINISH;
        } else if (allTask.stream().map(Task::getTaskDefinitionKey).filter(key -> key.equals(refuseId)).findFirst().isPresent()) {
            return WorkflowState.REJECT;
        } else {
            return WorkflowState.AUDITING;
        }
    }

    private WorkflowState doRejectTask(String taskId, String userId, String comment) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskTenantId(String.valueOf(SessionContextHolder.getOrganId()))
                .taskCandidateOrAssigned(userId)
                .singleResult();
        if (task == null) {
            throw new MyCuckooException("任务不存在或已被处理");
        }

        Collection<FlowElement> allElements = FlowableUtils.getAllElements(
                repositoryService.getBpmnModel(task.getProcessDefinitionId()).getMainProcess().getFlowElements(), null);
        FlowElement source = allElements.stream()
                .filter(o -> task.getTaskDefinitionKey().equals(o.getId()))
                .findFirst()
                .orElse(null);
        List<UserTask> parentTasks = source == null ? Collections.emptyList()
                : FlowableUtils.findParentUserTasks(source, null, null);
        if (parentTasks.isEmpty()) {
            taskService.addComment(task.getId(), task.getProcessInstanceId(), CommentType.REJECT.code, comment);
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(task.getProcessInstanceId())
                    .moveExecutionToActivityId(task.getExecutionId(), WorkflowHelper.getResubmitId())
                    .changeState();
            return WorkflowState.REJECT;
        }

        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .orderByHistoricTaskInstanceStartTime().asc()
                .list();
        List<String> taskKeys = FlowableUtils.cleanHistoricTaskInstanceKeys(allElements, historicTasks);
        Set<String> parentTaskKeys = parentTasks.stream()
                .map(UserTask::getId)
                .filter(key -> !WorkflowHelper.getResubmitId().equals(key))
                .collect(Collectors.toSet());
        List<String> targetTaskKeys = new ArrayList<>();
        String previousTaskKey = null;
        int currentTaskCount = 0;
        for (String historicTaskKey : taskKeys) {
            if (historicTaskKey.equals(previousTaskKey)) {
                continue;
            }
            previousTaskKey = historicTaskKey;
            if (historicTaskKey.equals(task.getTaskDefinitionKey()) && ++currentTaskCount == 2) {
                //清洗的审批记录出现节点循环
                break;
            }
            if (parentTaskKeys.contains(historicTaskKey)) {
                targetTaskKeys.add(historicTaskKey);
            }
        }
        if (targetTaskKeys.isEmpty()) {
            // 无历史审批节点可驳回，退回“重新申请”节点
            taskService.addComment(task.getId(), task.getProcessInstanceId(), CommentType.REJECT.code, comment);
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(task.getProcessInstanceId())
                    .moveExecutionToActivityId(task.getExecutionId(), WorkflowHelper.getResubmitId())
                    .changeState();
            return WorkflowState.REJECT;
        }

        List<Task> runningTasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runningTaskKeys = runningTasks.stream().map(Task::getTaskDefinitionKey).collect(Collectors.toList());
        List<String> currentTaskKeys = FlowableUtils.findChildUserTasks(parentTasks.get(0), runningTaskKeys, null, null)
                .stream().map(UserTask::getId).collect(Collectors.toList());
        if (targetTaskKeys.size() > 1 && currentTaskKeys.size() > 1) {
            throw new MyCuckooException("任务出现多对多情况，无法撤回");
        }

        runningTasks.stream()
                .filter(o -> currentTaskKeys.contains(o.getTaskDefinitionKey()))
                .forEach(o -> taskService.addComment(o.getId(), o.getProcessInstanceId(), CommentType.REJECT.code, comment));
        if (targetTaskKeys.size() > 1) {
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(task.getProcessInstanceId())
                    .moveSingleActivityIdToActivityIds(currentTaskKeys.get(0), targetTaskKeys)
                    .changeState();
        } else {
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(task.getProcessInstanceId())
//                    .moveActivityIdsToSingleActivityId(currentTaskKeys, targetTaskKeys.get(0))
                    .moveExecutionsToSingleActivityId(
                            runningTasks.stream().map(Task::getExecutionId).collect(Collectors.toList()),
                            targetTaskKeys.get(0))
                    .changeState();
        }
        return WorkflowState.REJECT;
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

    /**
     * 流程历史流转记录
     */
    public Map<String, Object> getProcessRecords(String instanceId, String deployId) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.hasText(instanceId)) {
            List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId)
                    .orderByHistoricActivityInstanceStartTime()
                    .desc()
                    .list();

            Map<String, List<Comment>> commentCache = new HashMap<>();
            List<Map<String, Object>> records = new ArrayList<>();
            for (HistoricActivityInstance instance : list) {
                // 展示开始节点
                if (!StringUtils.hasText(instance.getTaskId())) {
                    continue;
                }

                Map<String, Object> data = new HashMap<>();
                data.put("taskId", instance.getTaskId());
                data.put("taskName", instance.getActivityName());
                data.put("assigneeId", instance.getAssignee());
                data.put("assigneeName", instance.getAssignee()); //用户名名称
                /*
                // 展示审批人员
                List<HistoricIdentityLink> links = historyService.getHistoricIdentityLinksForTask(instance.getTaskId());
                StringBuilder userBuilder = new StringBuilder();
                for (HistoricIdentityLink identityLink : links) {
                    // 获选人,候选组/角色(多个)
                    if ("candidate".equals(identityLink.getType())) {
                        if (StringUtils.hasText(identityLink.getUserId())) {
                            UserInfo user = userService.selectUserById(Long.parseLong(identityLink.getUserId()));
                            userBuilder.append(user.getUserName()).append(",");
                        }
                        if (StringUtils.hasText(identityLink.getGroupId())) {
                            SysRole role = roleService.selectRoleById(Long.parseLong(identityLink.getGroupId()));
                            userBuilder.append(role.getRoleName()).append(",");
                        }
                    }
                }
                if (StringUtils.hasText(userBuilder)) {
                    data.put("candidate", userBuilder.substring(0, userBuilder.length() - 1));
                }
                */

                data.put("duration", FlowUtils.formatDate(instance.getDurationInMillis()));
                data.put("startTime", instance.getStartTime() == null ? null : LocalDateTime.from(instance.getStartTime().toInstant().atZone(ZoneId.systemDefault())));
                data.put("endTime", instance.getEndTime() == null ? null : LocalDateTime.from(instance.getEndTime().toInstant().atZone(ZoneId.systemDefault())));

                // 获取意见评论内容
                List<Comment> comments = commentCache.computeIfAbsent(instance.getProcessInstanceId(),
                        id -> taskService.getProcessInstanceComments(instance.getProcessInstanceId()));
                comments.forEach(comment -> {
                    if (instance.getTaskId().equals(comment.getTaskId())) {
                        data.put("comment", new FlowComment(comment.getType(), comment.getFullMessage()));
                    }
                });
                records.add(data);
            }
            result.put("records", records);
        }

        // 没有表单
        result.put("forms", null);
        result.putAll(this.getProcessXmlAndNode(instanceId, deployId));

        return result;
    }

    public Map<String, Object> getProcessXmlAndNode(String instanceId, String deployId) {
        List<Map<String, Object>> nodes = new ArrayList<>();
        // 获取已经完成的节点
        List<HistoricActivityInstance> finishes = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .finished()
                .list();

        // 保存已经完成的流程节点编号
        finishes.forEach(o -> {
            // 退回节点不进行展示
            if (!StringUtils.hasText(o.getDeleteReason())) {
                return;
            }

            Map<String, Object> node = new HashMap<>();
            node.put("key", o.getActivityId());
            node.put("completed", true);
            nodes.add(node);
        });

        // 获取代办节点
        List<HistoricActivityInstance> unFinishes = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .unfinished()
                .list();

        // 保存需要代办的节点编号
        unFinishes.forEach(o -> {
            // 删除已退回节点
            nodes.removeIf(node -> node.get("key").equals(o.getActivityId()));

            Map<String, Object> node = new HashMap<>();
            node.put("key", o.getActivityId());
            node.put("completed", false);
            nodes.add(node);
        });

        // xmlData 数据
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
        String xmlStr = null;
        try {
            InputStream in = repositoryService.getResourceAsStream(definition.getDeploymentId(), definition.getResourceName());
            xmlStr = IOUtils.toString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("获取流程图失败!", e);
        }

        Map<String, Object> result = new HashMap();
        result.put("nodes", nodes);
        result.put("xml", xmlStr);

        return result;
    }

}
