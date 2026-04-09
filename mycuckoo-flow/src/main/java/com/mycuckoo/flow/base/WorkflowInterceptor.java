package com.mycuckoo.flow.base;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.service.delegate.DelegateTask;

public interface WorkflowInterceptor {
    /**
     * 当前任务是否完成
     * 1、配置在紧接任务的网关的分支上
     * 2、必须是排他网关
     *
     * @param execution
     * @return 任务是否完成
     */
    boolean hasPass(DelegateExecution execution);

    /**
     *  判断当前任务是否已完成, 多实例拒绝或者全部审批
     *
     * @param execution
     * @return 任务是否结束
     */
    boolean hasComplete(DelegateExecution execution);

    /**
     * 进入任务前, 如移除之前存放的流程变量
     */
    void beforeTask(DelegateExecution execution);

    /**
     * 监听器
     */
    void on(String type, DelegateTask task);
}
