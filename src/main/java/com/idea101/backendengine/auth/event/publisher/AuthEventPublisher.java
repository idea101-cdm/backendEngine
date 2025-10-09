package com.idea101.backendengine.auth.event.publisher;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.idea101.backendengine.common.event.model.otp.OtpEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthEventPublisher {

    private final PubSubTemplate pubSubTemplate;
    private final String otpTopic;

    public AuthEventPublisher(PubSubTemplate pubSubTemplate, @Value("${pubsub.topic.otp}") String otpTopic) {
        this.pubSubTemplate = pubSubTemplate;
        this.otpTopic = otpTopic;
    }

    public void publishOtpEvent(UUID id, String channel, String otp, String purpose, String email, String phoneNumber){
        OtpEvent event = OtpEvent.builder()
                .userId(id)
                .channel(channel)
                .otp(otp)
                .purpose(purpose)
                .email(email)
                .phoneNumber(phoneNumber).build();

        pubSubTemplate.publish(otpTopic, event);
        System.out.println("✅ Published test message");

    }

}
