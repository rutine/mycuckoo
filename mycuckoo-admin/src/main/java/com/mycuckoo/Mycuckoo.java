package com.mycuckoo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
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