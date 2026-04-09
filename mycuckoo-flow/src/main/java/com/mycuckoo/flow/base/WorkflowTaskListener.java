package com.mycuckoo.flow.base;

import org.flowable.task.service.delegate.DelegateTask;

public interface WorkflowTaskListener {

    boolean supportType(String type);

    /**
     * 分配任务, 分配比创建先
     */
    default void onAssignment(DelegateTask task) {

    }

    /**
     * 创建任务
     */
    default void onCreate(DelegateTask task) {

    }

    /**
     * 完成任务
     */
    default void onComplete(DelegateTask task) {

    }

    /**
     * 删除任务
     */
    default void onDelete(DelegateTask task) {

    }
}
