package com.mycuckoo.flow;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.test.FlowableRule;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rutine
 * @date 2024/11/13 17:05
 */
@SpringBootApplication
public class ContextConfig {

    @Bean
    public FlowableRule flowableRule(ProcessEngine processEngine) {
        FlowableRule flowableRule = new FlowableRule();
        flowableRule.setProcessEngine(processEngine);

        return flowableRule;
    }
}
