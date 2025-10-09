package com.idea101.backendengine.common.event.model.user;

import com.idea101.backendengine.auth.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper=true)
public class UserCredentialsUpdatedEvent extends UserEvent {

    private final String phoneNumber;
    private final String email;

    @Builder
    public UserCredentialsUpdatedEvent(User user, String performedBy, String phoneNumber, String email) {
        super(user, performedBy);
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    @Override
    public String getEventType() {
        return "UPDATE_USER_CREDENTIALS";
    }

}
