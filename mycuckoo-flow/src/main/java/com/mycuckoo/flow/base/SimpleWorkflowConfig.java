package com.mycuckoo.flow.base;

import java.util.List;

/**
 * @author rutine
 * @date 2024/11/13 15:07
 */
public class SimpleWorkflowConfig implements WorkflowConfig {

    private boolean unique;
    private String processDefId;
    private String module;
    private String submitPage;
    private List<SimpleWorkflowConfig.SimpleStage> stage;

    public SimpleWorkflowConfig(boolean unique, String processDefId, String module, String submitPage, List<SimpleWorkflowConfig.SimpleStage> stage) {
        this.unique = unique;
        this.processDefId = processDefId;
        this.module = module;
        this.submitPage = submitPage;
        this.stage = stage;
    }

    @Override
    public boolean isUnique() {
        return this.unique;
    }

    @Override
    public String getProcessDefId() {
        return processDefId;
    }

    @Override
    public String getModule() {
        return module;
    }

    @Override
    public String getSubmitPage() {
        return submitPage;
    }


    public List<SimpleWorkflowConfig.SimpleStage> getStage() {
        return this.stage;
    }



    public static class SimpleStage implements Stage<SimpleUser> {
        private boolean sequential;
        private List<SimpleUser> user;

        public SimpleStage(boolean sequential, List<SimpleUser> user) {
            this.sequential = sequential;
            this.user = user;
        }

        @Override
        public boolean isSequential() {
            return sequential;
        }

        @Override
        public List<SimpleUser> getUser() {
            return user;
        }
    }


    public static class SimpleUser implements User {
        private String userId;

        public SimpleUser(String userId) {
            this.userId = userId;
        }

        @Override
        public String getUserId() {
            return this.userId;
        }
    }
}
