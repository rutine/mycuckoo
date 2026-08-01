package com.mycuckoo.flow;

import com.mycuckoo.flow.base.SimpleWorkflowConfig;
import com.mycuckoo.flow.base.WorkflowService;
import com.mycuckoo.flow.constant.enums.CommentType;
import org.assertj.core.util.Lists;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.test.FlowableRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

/**
 * @author rutine
 * @date 2024/11/13 16:40
 */
@SpringBootTest(classes = ContextConfig.class)
public class WorkflowTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static String processDefId = "test1001";

    @Autowired
    private WorkflowService workflowService;
    @Autowired
    protected RepositoryService repositoryService;

    @Rule
    @Autowired
    public FlowableRule flowableRule;



    @Test
    public void test() {
        List<SimpleWorkflowConfig.SimpleStage> stage = Lists.newArrayList();
        stage.add(new SimpleWorkflowConfig.SimpleStage(true, Lists.newArrayList(new SimpleWorkflowConfig.SimpleUser("1"))));
        stage.add(new SimpleWorkflowConfig.SimpleStage(true, Lists.newArrayList(new SimpleWorkflowConfig.SimpleUser("2"))));
        stage.add(new SimpleWorkflowConfig.SimpleStage(true, Lists.newArrayList(new SimpleWorkflowConfig.SimpleUser("3"))));

        SimpleWorkflowConfig config = new SimpleWorkflowConfig(true, processDefId, "test", "", stage);

        workflowService.deployModel(processDefId, "测试审批", config);
        List list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefId).list();

        String processInstanceId = workflowService.startProcess(config, "1", "test", "1");

        workflowService.completeTask(processInstanceId, "1", CommentType.NORMAL, "同意");

        workflowService.completeTask(processInstanceId, "2", CommentType.REJECT, "不同意");

        System.out.println(list);
    }
}
