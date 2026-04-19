package com.mycuckoo.flow.base;

import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import org.flowable.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.flowable.engine.impl.bpmn.behavior.TaskActivityBehavior;
import org.flowable.engine.impl.delegate.ActivityBehavior;
import org.flowable.task.service.delegate.BaseTaskListener;

import java.util.*;

public class WorkflowHelper {
    /**
     * 默认的service名称
     */
    public static final String SERVICE_NAME = "auditFlowService";
    // 默认租户id
    public static final String TENANT_ID = "1";

    /**
     * 任务完成使用的方法，对应
     */
    private static final String EXPRESSION_BEFORE_TASK = SERVICE_NAME + ".beforeTask(execution)";
    private static final String EXPRESSION_HAS_PASS = SERVICE_NAME + ".hasPass(execution)";
    private static final String EXPRESSION_HAS_COMPLETE = SERVICE_NAME + ".hasComplete(execution)";
    private static final String EXPRESSION_LISTEN_ON = SERVICE_NAME + ".on('%s', task)";

    private static final String RESUBMIT_NODE_ID = "resubmit";
    private static final String STAGE_REJECTED = "_stageRejected";
    private static final String INITIATOR = "initiator"; //发起人变量
    private static final String FORM_ID = "formId";
    private static final String FORM_TYPE = "formType";


    // ============== 变量名相关 ============
    /**
     * 审批人变量key
     */
    public static String getAssgineeListKey(int i) {
        return "assigneeList_" + i;
    }

    /**
     * 发起人变量key
     */
    public static String getInitiatorKey() {
        return INITIATOR;
    }

    /**
     * 审批驳回变量key
     */
    public static String getStageRejectedKey(String taskNodeId) {
        return taskNodeId + STAGE_REJECTED;
    }

    /**
     * 表单id(即业务id)变量key
     */
    public static String getFormIdKey() {
        return FORM_ID;
    }

    /**
     * 表单类型(即业务类型)变量key
     */
    public static String getFormTypeKey() {
        return FORM_TYPE;
    }


    // ============== 节点id相关 ============
    /**
     * 待重新提交任务id
     */
    public static String getResubmitId() {
        return RESUBMIT_NODE_ID;
    }


    private static String getTaskId(int i) {
        return "task_" + i;
    }

    private static String getShortOutGatewayId(int i) {
        return "shortOutGateway_" + i;
    }

    private static String getAuditGatewayId(int i) {
        return "auditGateway_" + i;
    }



    /**
     * 获取上一步节点的id, 由于是配置在线上, 获取的activity是网关, 那么网关的入口就是任务节点
     *
     * @param execution 执行器
     * @return 上一步流程节点nodeId
     */
    public static String getSourceTaskId(DelegateExecution execution) {
        FlowNode gatewayNode = (FlowNode) execution.getCurrentFlowElement();
        ActivityBehavior behavior = (ActivityBehavior) gatewayNode.getBehavior();
        if (!(behavior instanceof ExclusiveGatewayActivityBehavior)) {
            throw new RuntimeException("表达式校验必须配置在排他网关上！");
        }

        String sourceNodeId = "";
        List<SequenceFlow> incomingFlows = gatewayNode.getIncomingFlows();
        if (incomingFlows.size() == 1) {
            SequenceFlow sequenceFlow = incomingFlows.get(0);
            Activity source = (Activity) sequenceFlow.getSourceFlowElement();
            if (!(source.getBehavior() instanceof TaskActivityBehavior)
                    && !(source.getBehavior() instanceof MultiInstanceActivityBehavior)) {
                throw new RuntimeException("网关入口必须是task或multi-instance类型");
            }
            sourceNodeId = source.getId();
        } else {
            throw new RuntimeException("排他网关的入口只能有1个");
        }

        return sourceNodeId;
    }

    public static BpmnModel createBpmnModel(String processDefId, String processName, WorkflowConfig config) {
        validateConfig(processDefId, processName, config);

        String startNodeId = "start";
        String endNodeId = "end";
        String resubmitNodeId = RESUBMIT_NODE_ID;
        List<WorkflowConfig.Stage> stages = config.getStage();

        Process process = new Process();
        process.setId(processDefId);
        process.setName(processName);

        process.addFlowElement(createStartEvent(startNodeId));
        process.addFlowElement(createEndEvent(endNodeId));
        process.addFlowElement(createResubmitTask(resubmitNodeId, config.getSubmitPage(), config.getModule()));

        if (stages == null || stages.isEmpty()) {
            process.addFlowElement(createSequence(startNodeId, endNodeId, null, "直接结束"));
        } else {
            String shortcutGatewayId = createStage(process, config.getModule(), 0, stages, resubmitNodeId, endNodeId);

            //连接开始和第一个节点
            process.addFlowElement(createSequence(startNodeId, shortcutGatewayId, null, "进入审批"));
            process.addFlowElement(createSequence(resubmitNodeId, shortcutGatewayId, null, "重新提交"));
        }

        BpmnModel model = new BpmnModel();
        model.addProcess(process);

        applySimpleLayout(model, stages == null ? 0 : stages.size());

        return model;
    }

    public static Map<String, Object> resolveBpmnModelVariables(BpmnModel model) {
//        BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
        Map<String, Object> variables = new LinkedHashMap<>();
        if (model == null || model.getProcesses() == null) {
            return variables;
        }

        for (Process process : model.getProcesses()) {
            for (FlowElement element : process.getFlowElements()) {
                if (!(element instanceof UserTask userTask)) {
                    continue;
                }
                MultiInstanceLoopCharacteristics mi = userTask.getLoopCharacteristics();
                if (mi == null) {
                    continue;
                }
                String assigneeMode = WorkflowHelper.getParameter(userTask, "assigneeMode");
                if (!"user".equalsIgnoreCase(assigneeMode)) {
                    continue;
                }
                String ids = WorkflowHelper.getParameter(userTask, "ids");
                if (ids == null || ids.isBlank()) {
                    throw new IllegalStateException("Multi-instance user task [" + userTask.getId() + "] has empty ids");
                }

                List<String> userIds = Arrays.stream(ids.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .distinct()
                        .toList();

                if (userIds.isEmpty()) {
                    throw new IllegalStateException("Multi-instance user task [" + userTask.getId() + "] resolved empty user list");
                }

                String collectionVar = resolveCollectionVariable(userTask);
                variables.put(collectionVar, userIds);
            }
        }

        return variables;
    }

    private static void validateConfig(String processDefId, String processName, WorkflowConfig config) {
        if (isBlank(processDefId)) {
            throw new IllegalArgumentException("processDefId不能为空");
        }
        if (isBlank(processName)) {
            throw new IllegalArgumentException("processName不能为空");
        }
        if (config == null) {
            throw new IllegalArgumentException("workflowConfig不能为空");
        }
        if (isBlank(config.getModule())) {
            throw new IllegalArgumentException("workflowConfig.module不能为空");
        }
        if (isBlank(config.getSubmitPage())) {
            throw new IllegalArgumentException("workflowConfig.submitPage不能为空");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static StartEvent createStartEvent(String id) {
        StartEvent event = new StartEvent();
        event.setId(id);
        return event;
    }
    private static EndEvent createEndEvent(String id) {
        EndEvent event = new EndEvent();
        event.setId(id);
        return event;
    }
    private static UserTask createResubmitTask(String id, String formKey, String module) {
        FlowableListener listener = new FlowableListener();
        listener.setEvent(BaseTaskListener.EVENTNAME_ASSIGNMENT);
        listener.setImplementationType("expression"); //delegateExpression(委托表达式)
        listener.setImplementation(String.format("${%s}", EXPRESSION_BEFORE_TASK));

        UserTask task = new UserTask();
        task.setId(id);
        task.setName("重新申请");
        task.setAssignee(String.format("${%s}", INITIATOR));
        task.setFormKey(formKey);
        task.setTaskListeners(Arrays.asList(listener));

        return task;
    }
    /**
     * 递归创建多实例节点
     *
     * @param process        process对象
     * @param i              当前节点位置
     * @param stages         审批流配置
     * @param resubmitNodeId 重复提交节点id
     * @param endNodeId      结束节点id
     * @return 当前节点的短路网关
     */
    private static String createStage(Process process, String module, int i, List<WorkflowConfig.Stage> stages, String resubmitNodeId, String endNodeId) {
        WorkflowConfig.Stage stage = stages.get(i);
        String shortOutGatewayId = getShortOutGatewayId(i);
        process.addFlowElement(createGateway(shortOutGatewayId, getStageSkipGatewayName(i)));

        String taskId = getTaskId(i);
        String assigneesKey = getAssgineeListKey(i);
        String taskName = getStageTaskName(i, stage);

        process.addFlowElement(createMultiInstance(taskId, taskName, assigneesKey, stage.isSequential(), module));

        //已配置处理人, 到任务节点
        process.addFlowElement(createSequence(shortOutGatewayId, taskId,
                String.format("${%s != null && %s.size() > 0}", assigneesKey, assigneesKey), "存在审批人"));

        String auditGatewayId = getAuditGatewayId(i);
        process.addFlowElement(createGateway(auditGatewayId, getStageResultGatewayName(i)));
        process.addFlowElement(createSequence(taskId, auditGatewayId, null, "提交审批结果"));

        //不通过，直接到重新发起节点
        process.addFlowElement(createSequence(auditGatewayId, resubmitNodeId,
                String.format("${!%s}", EXPRESSION_HAS_PASS), "驳回重提"));

        if (i == stages.size() - 1) {
            //未配置处理人, 直接结束
            process.addFlowElement(createSequence(shortOutGatewayId, endNodeId,
                    String.format("${%s == null || %s.size() == 0}", assigneesKey, assigneesKey), "无人审批直接结束"));
            //审批通过, 直接结束
            process.addFlowElement(createSequence(auditGatewayId, endNodeId,
                    String.format("${%s}", EXPRESSION_HAS_PASS), "审批通过结束"));
        } else {
            String nextShortOutGatewayId = createStage(process, module, i + 1, stages, resubmitNodeId, endNodeId);

            //未配置处理人, 直接到下个节点的短路网关
            process.addFlowElement(createSequence(shortOutGatewayId, nextShortOutGatewayId,
                    String.format("${%s == null || %s.size() == 0}", assigneesKey, assigneesKey), "无人审批跳过"));
            //审批通过, 下个短路网关
            process.addFlowElement(createSequence(auditGatewayId, nextShortOutGatewayId,
                    String.format("${%s}", EXPRESSION_HAS_PASS), "审批通过进入下一阶段"));
        }

        return shortOutGatewayId;
    }

    private static String getStageTaskName(int i, WorkflowConfig.Stage stage) {
        String mode = stage.isSequential() ? "串行会签" : "并行会签";
        return "第" + (i + 1) + "级审批(" + mode + ")";
    }

    private static String getStageSkipGatewayName(int i) {
        return "第" + (i + 1) + "级审批人判断";
    }

    private static String getStageResultGatewayName(int i) {
        return "第" + (i + 1) + "级审批结果判断";
    }

    private static void applySimpleLayout(BpmnModel model, int stageCount) {
        addGraphic(model, "start", 60, 120, 30, 30);
        addGraphic(model, RESUBMIT_NODE_ID, 180, 220, 120, 80);

        if (stageCount <= 0) {
            addGraphic(model, "end", 220, 120, 30, 30);
            addStraightFlow(model, getSequenceId("start", "end"), 90, 135, 220, 135);
            return;
        }

        for (int i = 0; i < stageCount; i++) {
            double baseX = 180 + i * 360;
            addGraphic(model, getShortOutGatewayId(i), baseX, 110, 40, 40);
            addGraphic(model, getTaskId(i), baseX + 90, 90, 120, 80);
            addGraphic(model, getAuditGatewayId(i), baseX + 270, 110, 40, 40);

            addStraightFlow(model, getSequenceId(getShortOutGatewayId(i), getTaskId(i)), baseX + 40, 130, baseX + 90, 130);
            addStraightFlow(model, getSequenceId(getTaskId(i), getAuditGatewayId(i)), baseX + 210, 130, baseX + 270, 130);
        }

        double endX = 180 + stageCount * 360;
        addGraphic(model, "end", endX + 90, 115, 30, 30);

        addStraightFlow(model, getSequenceId("start", getShortOutGatewayId(0)), 90, 135, 180, 130);
        addBentFlow(model, getSequenceId(RESUBMIT_NODE_ID, getShortOutGatewayId(0)),
                300, 260, 140, 260, 140, 180, 180, 130);

        for (int i = 0; i < stageCount; i++) {
            if (i < stageCount - 1) {
                double baseX = 180 + i * 360;
                double nextX = 180 + (i + 1) * 360;
                addStraightFlow(model, getSequenceId(getShortOutGatewayId(i), getShortOutGatewayId(i + 1)),
                        baseX + 20, 110, nextX + 20, 110);
                addStraightFlow(model, getSequenceId(getAuditGatewayId(i), getShortOutGatewayId(i + 1)),
                        baseX + 290, 110, nextX + 20, 110);
            } else {
                double baseX = 180 + i * 360;
                addStraightFlow(model, getSequenceId(getShortOutGatewayId(i), "end"),
                        baseX + 20, 110, endX + 90, 130);
                addStraightFlow(model, getSequenceId(getAuditGatewayId(i), "end"),
                        baseX + 290, 130, endX + 90, 130);
            }

            double baseX = 180 + i * 360;
            addBentFlow(model, getSequenceId(getAuditGatewayId(i), RESUBMIT_NODE_ID),
                    baseX + 290, 150, baseX + 290, 330, 240, 330, 240, 300);
        }
    }

    private static void addGraphic(BpmnModel model, String elementId, double x, double y, double width, double height) {
        model.addGraphicInfo(elementId, new GraphicInfo(x, y, width, height));
    }

    private static void addStraightFlow(BpmnModel model, String flowId, double x1, double y1, double x2, double y2) {
        List<GraphicInfo> points = new ArrayList<>();
        points.add(new GraphicInfo(x1, y1));
        points.add(new GraphicInfo(x2, y2));
        model.addFlowGraphicInfoList(flowId, points);
    }

    private static void addBentFlow(BpmnModel model, String flowId, double... coordinates) {
        List<GraphicInfo> points = new ArrayList<>();
        for (int i = 0; i < coordinates.length; i += 2) {
            points.add(new GraphicInfo(coordinates[i], coordinates[i + 1]));
        }
        model.addFlowGraphicInfoList(flowId, points);
    }

    private static UserTask createMultiInstance(String id, String name, String assigneesKey, boolean isSequential, String module) {
        FlowableListener listener = new FlowableListener();
        listener.setEvent(BaseTaskListener.EVENTNAME_ASSIGNMENT);
        listener.setImplementationType("expression");
        listener.setImplementation(String.format("${%s}", EXPRESSION_BEFORE_TASK));

        FlowableListener allListener = new FlowableListener();
        allListener.setEvent(BaseTaskListener.EVENTNAME_ALL_EVENTS);
        allListener.setImplementationType("expression");
        allListener.setImplementation(String.format("${" + EXPRESSION_LISTEN_ON + "}", module));

        UserTask task = new UserTask();
        task.setId(id);
        task.setName(name);
        task.setAssignee("${assignee}");
        task.setTaskListeners(Arrays.asList(listener, allListener));

        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = new MultiInstanceLoopCharacteristics();
        multiInstanceLoopCharacteristics.setInputDataItem(assigneesKey);
        multiInstanceLoopCharacteristics.setElementVariable("assignee");
        multiInstanceLoopCharacteristics.setCompletionCondition(String.format("${%s}", EXPRESSION_HAS_COMPLETE));
        multiInstanceLoopCharacteristics.setSequential(isSequential);
        task.setLoopCharacteristics(multiInstanceLoopCharacteristics);

        return task;
    }
    private static Gateway createGateway(String id, String name) {
        ExclusiveGateway gateway = new ExclusiveGateway();
        gateway.setId(id);
        gateway.setName(name);
        return gateway;
    }
    private static SequenceFlow createSequence(String source, String target, String condition, String name) {
        SequenceFlow flow = new SequenceFlow(source, target);
        flow.setId(getSequenceId(source, target));
        flow.setName(name);
        if (condition != null && !condition.trim().isEmpty()) {
            flow.setConditionExpression(condition);
        }

        return flow;
    }

    private static String getSequenceId(String source, String target) {
        return source + "_to_" + target;
    }

    private static String resolveCollectionVariable(UserTask userTask) {
        MultiInstanceLoopCharacteristics mi = userTask.getLoopCharacteristics();

        // 如果能直接读到 collection，优先用 BPMN 里的配置
        if (mi != null && mi.getInputDataItem() != null && !mi.getInputDataItem().isBlank()) {
            return mi.getInputDataItem();
        }

        // 回退到你当前项目的 task_n -> assigneeList_n 约定
        String taskId = userTask.getId();
        if (taskId != null && taskId.startsWith("task_")) {
            String suffix = taskId.substring("task_".length());
            if (suffix.matches("\\d+")) {
                return "assigneeList_" + suffix;
            }
        }

        return "assigneeList_0";
    }

    private static String getParameter(UserTask userTask, String targetName) {
        if (userTask == null || targetName == null || targetName.isBlank()) {
            return null;
        }

        Map<String, List<ExtensionElement>> extensionElements = userTask.getExtensionElements();
        if (extensionElements == null || extensionElements.isEmpty()) {
            return null;
        }

        List<ExtensionElement> wrappers =
                extensionElements.getOrDefault("parameters", Collections.emptyList());

        for (ExtensionElement wrapper : wrappers) {
            Map<String, List<ExtensionElement>> children = wrapper.getChildElements();
            if (children == null || children.isEmpty()) {
                continue;
            }

            List<ExtensionElement> parameters =
                    children.getOrDefault("parameter", Collections.emptyList());

            for (ExtensionElement parameter : parameters) {
                String name = parameter.getAttributeValue(null, "name");
                if (targetName.equals(name)) {
                    return parameter.getAttributeValue(null, "value");
                }
            }
        }

        return null;
    }

}
