package com.idea101.backendengine.auth.service;

import java.util.UUID;

public interface UserService {
    void updateCredentials(UUID id, String email, String phoneNumber);
    void activateUser(UUID id);
    void deactivateUser(UUID id);
    void deleteUser(UUID id);
}
