package com.idea101.backendengine.auth.service;

import com.idea101.backendengine.auth.entity.User;
import com.idea101.backendengine.common.enums.UserRole;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID userId);
    void updatePhoneNumber(UUID userId, String newPhoneNumber);
    void updateEmail(UUID userId, String newEmail);
    void validatePhoneAvailability(String phoneNumber, UserRole role, UUID currentUserId);
    void validateEmailAvailability(String email, UserRole role, UUID currentUserId);
    void activateUser(UUID id);
    void deactivateUser(UUID id);
    void deleteUser(UUID id);
}
