package com.mycuckoo.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import com.mycuckoo.gateway.config.ReactiveFeignConfiguration;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableReactiveFeignClients(value = "com.mycuckoo.gateway.client", defaultConfiguration = ReactiveFeignConfiguration.class)
public class Mycuckoo {
    private static Logger logger = LoggerFactory.getLogger(Mycuckoo.class);

    /**
     * Main Start
     */
    public static void main(String[] args) {
        SpringApplication.run(Mycuckoo.class, args);
        logger.info("============= Mycuckoo Start Success =============");
    }
}