//package com.idea101.backendengine.notificationEngine;
//
//import com.google.cloud.spring.pubsub.core.PubSubTemplate;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class PubSubTestRunner implements CommandLineRunner {
//
//    private final PubSubTemplate pubSubTemplate;
//
//    public PubSubTestRunner(PubSubTemplate pubSubTemplate) {
//        this.pubSubTemplate = pubSubTemplate;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        String topicName = "test-topic";
//        String message = "Hello from Spring Boot!";
//
//        pubSubTemplate.publish(topicName, message);
//        System.out.println("✅ Published test message: " + message);
//    }
//}
