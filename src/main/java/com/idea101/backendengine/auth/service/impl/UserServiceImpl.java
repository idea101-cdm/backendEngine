package com.idea101.backendengine.auth.service.impl;

import com.idea101.backendengine.auth.entity.User;
import com.idea101.backendengine.auth.repository.UserRepository;
import com.idea101.backendengine.auth.service.UserService;
import com.idea101.backendengine.common.enums.UserRole;
import com.idea101.backendengine.common.exception.EmailAlreadyLinkedException;
import com.idea101.backendengine.common.exception.PhoneAlreadyLinkedException;
import com.idea101.backendengine.common.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found: userId={}", userId);
                    return new UserNotFoundException("User not found for ID: " + userId);
                });
    }

    @Transactional
    @Override
    public void updatePhoneNumber(UUID userId, String newPhoneNumber) {
        User user = getUserById(userId);
        validatePhoneAvailability(newPhoneNumber, user.getRole(), userId);
        user.setPhoneNumber(newPhoneNumber);
        userRepository.save(user);
        log.info("Phone number updated for userId={}, newPhone={}", userId, newPhoneNumber);
    }

    @Transactional
    @Override
    public void updateEmail(UUID userId, String newEmail) {
        User user = getUserById(userId);
        validateEmailAvailability(newEmail, user.getRole(), userId);
        user.setEmail(newEmail);
        userRepository.save(user);
        log.info("Email updated for userId={}, newEmail={}", userId, newEmail);
    }

    @Override
    public void validatePhoneAvailability(String phoneNumber, UserRole role, UUID currentUserId) {
        boolean exists = userRepository.existsByPhoneNumberAndRoleAndIdNot(phoneNumber, role, currentUserId);
        if (exists) {
            log.warn("Phone conflict: phone={}, role={}, userId={}", phoneNumber, role, currentUserId);
            throw new PhoneAlreadyLinkedException("Phone number already linked to another user with role " + role);
        }
    }

    @Override
    public void validateEmailAvailability(String email, UserRole role, UUID currentUserId) {
        boolean exists = userRepository.existsByEmailAndRoleAndIdNot(email, role, currentUserId);
        log.warn("Email conflict: email={}, role={}, userId={}", email, role, currentUserId);
        throw new EmailAlreadyLinkedException("Email already linked to another user with role " + role);
    }

    @Transactional
    @Override
    public void activateUser(UUID id) {
        if (!userRepository.existsById(id)) {
            log.warn("Attempted to activate non-existent user ID: {}", id);
            throw  new UserNotFoundException("User not found for ID: " + id);
        }
        log.info("Activating user ID: {}", id);
        userRepository.updateUserActiveStatus(id, true);
    }

    @Transactional
    @Override
    public void deactivateUser(UUID id) {
        if (!userRepository.existsById(id)) {
            log.warn("Attempted to deactivate non-existent user ID: {}", id);
            throw  new UserNotFoundException("User not found for ID: " + id);
        }
        log.info("Deactivating user ID: {}", id);
        userRepository.updateUserActiveStatus(id, false);
    }

    @Transactional
    @Override
    public void deleteUser(UUID id) {
        log.info("Deleting user ID: {}", id);
        userRepository.deleteUserById(id);
    }
}