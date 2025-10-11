package com.idea101.backendengine.auth.service.impl;

import com.idea101.backendengine.auth.dto.VerifyOtpRequestDto;
import com.idea101.backendengine.auth.entity.OtpCode;
import com.idea101.backendengine.auth.entity.User;
import com.idea101.backendengine.auth.repository.UserRepository;
import com.idea101.backendengine.auth.service.OtpService;
import com.idea101.backendengine.auth.service.UserService;
import com.idea101.backendengine.common.jwt.JwtUtil;
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
    private final OtpService otpService;
    private final JwtUtil jwtUtil;

    @Transactional
    @Override
    public String loginUserWithOtp(VerifyOtpRequestDto dto) {
        log.info("Initiating OTP verification for OTP ID: {}", dto.getId());

        OtpCode otpCode = otpService.verifyOtp(dto);
        User user = otpCode.getUser();

        if (user.getIsGhostAccount()) {
            log.info("Ghost account detected for user ID: {}. Marking as regular user.", user.getId());
            user.setIsGhostAccount(false);
            userRepository.save(user);
        } else {
            log.info("Verified OTP linked to existing user ID: {}", user.getId());
        }

        String token = jwtUtil.generateToken(user.getId(), user.getRole(), user.getIsActive());
        log.info("JWT token successfully generated for user ID: {}", user.getId());

        return token;
    }

    @Transactional
    @Override
    public void updateCredentials(UUID id, String email, String phoneNumber) {
        if (!userRepository.existsById(id)) {
            log.warn("Attempted to update credentials for non-existent user ID: {}", id);
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        log.info("Updating credentials for user ID: {}", id);
        userRepository.updateUserCredentials(id, email, phoneNumber);
    }

    @Transactional
    @Override
    public void activateUser(UUID id) {
        if (!userRepository.existsById(id)) {
            log.warn("Attempted to activate non-existent user ID: {}", id);
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        log.info("Activating user ID: {}", id);
        userRepository.updateUserActiveStatus(id, true);
    }

    @Transactional
    @Override
    public void deactivateUser(UUID id) {
        if (!userRepository.existsById(id)) {
            log.warn("Attempted to deactivate non-existent user ID: {}", id);
            throw new IllegalArgumentException("User not found with ID: " + id);
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