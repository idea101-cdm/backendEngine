package com.idea101.backendengine.common.event.model.user;

import com.idea101.backendengine.auth.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class UserActivatedEvent extends UserEvent {

    @Builder
    public UserActivatedEvent(User user, String performedBy) {
        super(user, performedBy);
    }

    @Override
    public String getEventType() {
        return "ACTIVATE_USER";
    }
}