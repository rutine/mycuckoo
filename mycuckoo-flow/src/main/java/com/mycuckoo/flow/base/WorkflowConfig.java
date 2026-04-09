package com.mycuckoo.flow.base;

import java.util.List;

/**
 * @author rutine
 * @date 2024/11/12 15:01
 */
public interface WorkflowConfig<S extends WorkflowConfig.Stage> {

    /**
     * 节点审批时, 是否去掉同人重复审批
     */
    boolean isUnique();

    /**
     * 审批模板
     */
    String getProcessDefId();

    /**
     * 审批所属模块
     */
    String getModule();

    /**
     * 审批提交页
     */
    String getSubmitPage();

    /**
     * 多节点审批人数组
     */
    List<S> getStage();


    // ===========================
    interface Stage<U extends User> {
        boolean isSequential();

        List<U> getUser();
    }

    interface User {
        String getUserId();
    }
}
