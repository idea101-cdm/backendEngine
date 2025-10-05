//package com.idea101.backendengine.notificationEngine;
//
//import com.google.cloud.spring.pubsub.core.PubSubTemplate;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class PubSubSubscriberConfig {
//
//    @Bean
//    public Object testSubscriber(PubSubTemplate pubSubTemplate) {
//        pubSubTemplate.subscribe("test-topic-sub", (message) -> {
//            String data = message.getPubsubMessage().getData().toStringUtf8();
//            System.out.println("📩 Received message: " + data);
//            message.ack();
//        });
//        return new Object();
//    }
//}
