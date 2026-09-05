package com.mycuckoo.flow;

import com.mycuckoo.core.Querier;
import com.mycuckoo.core.UserInfo;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.util.web.SessionContextHolder;
import com.mycuckoo.flow.base.SimpleWorkflowConfig;
import com.mycuckoo.flow.base.WorkflowService;
import com.mycuckoo.flow.constant.enums.CommentType;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author rutine
 * @date 2024/11/13 16:40
 */
@SpringBootTest(classes = ContextConfig.class)
@Transactional
class WorkflowTest {

    private static final String processDefId = "test1001";

    @Autowired
    private WorkflowService workflowService;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected TaskService taskService;

    @BeforeEach
    void setUpSession() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        UserInfo user = new UserInfo();
        user.setId(1L);
        user.setOrgId(1L);
        user.setUserName("测试用户");
        user.setPinyin("test");
        SessionContextHolder.setRequest(request);
        SessionContextHolder.setUserInfo(user);
    }

    @AfterEach
    void tearDownSession() {
        SessionContextHolder.setRequest(null);
    }

    private String deployAndStart() {
        List<SimpleWorkflowConfig.SimpleStage> stage = List.of(
                new SimpleWorkflowConfig.SimpleStage(true, List.of(new SimpleWorkflowConfig.SimpleUser("1"))),
                new SimpleWorkflowConfig.SimpleStage(true, List.of(new SimpleWorkflowConfig.SimpleUser("2"))),
                new SimpleWorkflowConfig.SimpleStage(true, List.of(new SimpleWorkflowConfig.SimpleUser("3"))));
        SimpleWorkflowConfig config = new SimpleWorkflowConfig(true, processDefId, "test", "submitPage", stage);
        workflowService.deployModel(processDefId, "测试审批", config);
        return workflowService.startProcess(config, "1", "test", "1");
    }

    @Test
    void testRejectReturnsToPreviousApprover() {
        String processInstanceId = deployAndStart();

        // 第1级审批人1 同意
        Task task0 = taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey("task_0").singleResult();
        workflowService.completeTask(task0.getId(), "1", CommentType.NORMAL, "同意");

        // 第2级审批人2 驳回
        Task task1 = taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey("task_1").singleResult();
        workflowService.completeTask(task1.getId(), "2", CommentType.REJECT, "不同意");

        // 驳回后应回到第1级，且办理人仍为1
        Task back = taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey("task_0").singleResult();
        assertNotNull(back, "驳回后应在task_0生成新任务");
        assertEquals("1", back.getAssignee(), "驳回后任务应回到办理人1");

        // 模拟“我的待办”查询（按当前登录人过滤）
        long count1 = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskCandidateOrAssigned("1")
                .count();
        assertTrue(count1 >= 1, "办理人1的待办中应能看到驳回后的任务");
    }

    @Test
    void testRejectAtFirstStageGoesToResubmit() {
        String processInstanceId = deployAndStart();

        // 第1级审批人1 直接驳回（无上级审批节点，应回到“重新申请”节点）
        Task task0 = taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey("task_0").singleResult();
        workflowService.completeTask(task0.getId(), "1", CommentType.REJECT, "不同意");

        Task resubmit = taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey("resubmit").singleResult();
        assertNotNull(resubmit, "首级驳回后应生成重新申请任务");
        assertEquals("1", resubmit.getAssignee(), "重新申请任务应回到发起人");
    }

    @Test
    void testRejectParallelStageReturnsToPreviousApprover() {
        List<SimpleWorkflowConfig.SimpleStage> stage = List.of(
                new SimpleWorkflowConfig.SimpleStage(false, List.of(new SimpleWorkflowConfig.SimpleUser("1"))),
                new SimpleWorkflowConfig.SimpleStage(false, List.of(new SimpleWorkflowConfig.SimpleUser("2"), new SimpleWorkflowConfig.SimpleUser("3"))));
        SimpleWorkflowConfig config = new SimpleWorkflowConfig(true, processDefId, "test", "submitPage", stage);
        workflowService.deployModel(processDefId, "测试审批", config);
        String processInstanceId = workflowService.startProcess(config, "1", "test", "1");

        Task task0 = taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey("task_0").singleResult();
        workflowService.completeTask(task0.getId(), "1", CommentType.NORMAL, "同意");

        List<Task> task1s = taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey("task_1").list();
        System.out.println("[parallel] stage2 tasks=" + task1s.size());
        workflowService.completeTask(task1s.get(0).getId(), task1s.get(0).getAssignee(), CommentType.REJECT, "不同意");

        long backCount = taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey("task_0").count();
        System.out.println("[parallel-afterReject] task_0 count=" + backCount);
        assertEquals(1, backCount, "并行会签驳回后 task_0 应只有 1 个任务");
    }

    @Test
    void testMyDoneTaskPage() {
        String processInstanceId = deployAndStart();

        // 第1级审批人1 同意
        Task task0 = taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey("task_0").singleResult();
        workflowService.completeTask(task0.getId(), "1", CommentType.NORMAL, "同意");

        // 查询办理人1的“我已办理”
        Page<Map<String, Object>> page = workflowService.findMyDoneTaskPage(new Querier(1, 10));
        assertTrue(page.getTotalElements() >= 1, "办理人1的已办理中应能看到已完成的任务");

        boolean found = page.getContent().stream()
                .anyMatch(data -> task0.getId().equals(data.get("taskId")));
        assertTrue(found, "已办理列表中应包含task_0的任务记录");
        assertTrue(page.getContent().stream().allMatch(data -> "1".equals(data.get("assignee"))),
                "已办理列表中的任务办理人应为当前登录用户");
    }
}
