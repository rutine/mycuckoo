package com.mycuckoo.flow.base;

import com.mycuckoo.core.exception.MyCuckooException;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.api.FlowableOptimisticLockingException;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author rutine
 * @date 2024/11/13 13:52
 */
@Service(WorkflowHelper.SERVICE_NAME)
public class WorkflowService extends WorkflowInterceptorAdapter {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TransactionTemplate transactionTemplate;


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
                .tenantId(WorkflowHelper.TENANT_ID)
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
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(config.getProcessDefId(), variables, WorkflowHelper.TENANT_ID);

        String processInstanceId = processInstance.getId();

//        this.saveWorkflowConfig(processInstanceId, formId, formType, config, userInfo);

        return processInstanceId;
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
                .taskTenantId(WorkflowHelper.TENANT_ID)
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
                .processInstanceTenantId(WorkflowHelper.TENANT_ID)
                .processInstanceId(workflowId)
                .count();
        if (count > 0) {
            runtimeService.deleteProcessInstance(workflowId, "手动删除");
//            this.deleteConfigByWorkflowId(workflowId);
        }
    }
}
