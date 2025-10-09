package com.idea101.backendengine.auth.event.subscriber;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.idea101.backendengine.auth.service.UserService;
import com.idea101.backendengine.common.event.model.user.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class UserEventSubscriber {

    private final UserService userService;

    @Value("${pubsub.subscription.user}")
    private String SUBSCRIPTION_NAME;

    public UserEventSubscriber(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public Object subscribeUserEvents(PubSubTemplate pubSubTemplate) {
        pubSubTemplate.subscribeAndConvert(
                SUBSCRIPTION_NAME,
                message -> {
                    try {
                        UserEvent event = message.getPayload();
                        log.info("Received user event: {}", event);
                        handleEvent(event);
                        message.ack();
                    } catch (Exception e) {
                        log.error("Error handling user event: {}", e.getMessage(), e);
                        message.nack();
                    }
                },
                UserEvent.class
        );
        return null;
    }

    private void handleEvent(UserEvent event) {

        try {
            switch (event.getEventType()) {
                case "USER_CREDENTIALS_UPDATED" -> {
                    UserCredentialsUpdatedEvent user = (UserCredentialsUpdatedEvent) event;
                    userService.updateCredentials(user.getUser().getId(), user.getEmail(), user.getPhoneNumber());
                }
                case "USER_ACTIVATED" -> {
                    UserActivatedEvent user = (UserActivatedEvent) event;
                    userService.activateUser(user.getUser().getId());
                }
                case "USER_DEACTIVATED" -> {
                    UserDeactivatedEvent user = (UserDeactivatedEvent) event;
                    userService.deactivateUser(user.getUser().getId());
                }
                case "USER_DELETED" -> {
                    UserDeletedEvent user = (UserDeletedEvent) event;
                    userService.deleteUser(user.getUser().getId());
                }
                default -> log.warn("Unknown event type received: {}", event.getEventType());
            }

            log.info("Successfully handled event: {}", event.getEventType());

        } catch (Exception ex) {
            log.error("Failed to handle event {}: {}", event.getEventType(), ex.getMessage(), ex);
            throw ex; // Re-throw to trigger message.nack() in outer try/catch
        }
    }
}
