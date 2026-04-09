package com.mycuckoo.flow.base;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.service.delegate.BaseTaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author rutine
 * @date 2024/11/12 15:37
 */
public class WorkflowInterceptorAdapter implements WorkflowInterceptor {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired(required = false)
    private List<WorkflowTaskListener> listeners;



    @Override
    public boolean hasPass(DelegateExecution execution) {
        String sourceTaskId = WorkflowHelper.getSourceTaskId(execution);
        Object result = execution.getVariable(WorkflowHelper.getStageRejectedKey(sourceTaskId));

        return !Boolean.TRUE.equals(result);
    }

    @Override
    public boolean hasComplete(DelegateExecution execution) {
        String nodeId = execution.getCurrentActivityId();
        Object refuse = execution.getVariable(WorkflowHelper.getStageRejectedKey(nodeId));

        return Boolean.TRUE.equals(refuse);
    }

    @Override
    public void beforeTask(DelegateExecution execution) {
        logger.debug("进入task监听器");

        String nodeId = execution.getCurrentActivityId();
        String variableName = WorkflowHelper.getStageRejectedKey(nodeId);
        // 仅在当前审批节点首次进入时重置该阶段结果，避免历史驳回状态影响重提后的下一轮审批。
        Integer nrOfCompletedInstances = (Integer) execution.getVariable("nrOfCompletedInstances");
        if (nrOfCompletedInstances != null) {
            if (nrOfCompletedInstances.equals(0)) {
                execution.setVariable(variableName, false);
            }
        } else {
            execution.setVariable(variableName, false);
        }

        logger.debug("成功初始化当前审批阶段结果");
    }

    @Override
    public void on(String type, DelegateTask task) {
        if (listeners == null) {
            return;
        }

        for (WorkflowTaskListener listener : listeners) {
            try {
                this.on(listener, type, task);
            } catch (Exception e) {
                logger.error("执行{}任务监听器异常", type, e);
            }
        }
    }


    private void on(WorkflowTaskListener listener, String type, DelegateTask task) {
        if (!listener.supportType(type)) {
            return;
        }

        switch (task.getEventName()) {
            case BaseTaskListener.EVENTNAME_ASSIGNMENT:
                listener.onAssignment(task);
                break;
            case BaseTaskListener.EVENTNAME_CREATE:
                listener.onCreate(task);
                break;
            case BaseTaskListener.EVENTNAME_COMPLETE:
                listener.onComplete(task);
                break;
            case BaseTaskListener.EVENTNAME_DELETE:
                listener.onDelete(task);
                break;
            default:
                break;
        }
    }
}
