package com.idea101.backendengine.common.event.model.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.idea101.backendengine.auth.entity.User;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@ToString
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "eventType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserCredentialsUpdatedEvent.class, name = "USER_CREDENTIALS_UPDATED"),
        @JsonSubTypes.Type(value = UserActivatedEvent.class, name = "USER_ACTIVATED"),
        @JsonSubTypes.Type(value = UserDeactivatedEvent.class, name = "USER_DEACTIVATED"),
        @JsonSubTypes.Type(value = UserDeletedEvent.class, name = "USER_DELETED")
})
public abstract class UserEvent {

    private final User user;
    private final String performedBy;
    private final Instant timestamp;

    protected UserEvent(User user, String performedBy) {
        this.user = user;
        this.performedBy = performedBy;
        this.timestamp = Instant.now();
    }

    public abstract String getEventType();
}
