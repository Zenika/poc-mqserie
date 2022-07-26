package com.bnpp.pocmqserie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class PocMqserieApplication {

    public static void main(String[] args) {
//        Trace.setOn(true);
        // To enable re-use same clientId for multiple durable subscription
        System.setProperty("com.ibm.mq.jms.SupportMQExtensions", "true");
        SpringApplication.run(PocMqserieApplication.class, args);
    }
}
