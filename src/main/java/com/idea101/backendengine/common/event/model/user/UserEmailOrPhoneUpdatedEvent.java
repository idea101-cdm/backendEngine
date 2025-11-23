package com.idea101.backendengine.common.event.model.user;

import com.idea101.backendengine.auth.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper=true)
public class UserEmailOrPhoneUpdatedEvent extends UserEvent {

    private final String phoneNumber;
    private final String identifier;

    @Builder
    public UserEmailOrPhoneUpdatedEvent(User user, String performedBy, String phoneNumber, String identifier) {
        super(user, performedBy);
        this.phoneNumber = phoneNumber;
        this.identifier = identifier;
    }

    @Override
    public String getEventType() {
        return "UPDATE_USER_EMAIL_OR_PHONE_NUMBER";
    }

}