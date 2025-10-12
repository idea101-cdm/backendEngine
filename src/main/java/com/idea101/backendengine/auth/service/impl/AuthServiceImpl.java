package com.idea101.backendengine.auth.service.impl;

import com.idea101.backendengine.auth.entity.OtpCode;
import com.idea101.backendengine.auth.entity.User;
import com.idea101.backendengine.auth.repository.UserRepository;
import com.idea101.backendengine.auth.service.AuthService;
import com.idea101.backendengine.auth.service.OtpService;
import com.idea101.backendengine.auth.service.UserService;
import com.idea101.backendengine.common.enums.OtpReason;
import com.idea101.backendengine.common.enums.UserRole;
import com.idea101.backendengine.common.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Transactional
    @Override
    public UUID initiateLoginViaOtp(String identifier, UserRole role) {
        User user = userRepository.findByPhoneNumberOrEmailAndRole(identifier, role)
                .orElseGet(() -> {
                    User ghost = new User();
                    ghost.setRole(role);
                    if (identifier.contains("@")) ghost.setEmail(identifier);
                    else ghost.setPhoneNumber(identifier);
                    return userRepository.save(ghost);
                });

        return otpService.requestOtp(user, user.getPhoneNumber(), user.getEmail(), OtpReason.LOGIN);
    }

    @Transactional
    @Override
    public String verifyLoginOtp(UUID otpId, String otpCode) {
        OtpCode verified = otpService.verifyOtp(otpId, otpCode);
        User user = verified.getUser();
        if (user.getIsGhostAccount()) {
            user.setIsGhostAccount(false);
            userRepository.save(user);
        }
        return jwtUtil.generateToken(user.getId(), user.getRole(), user.getIsActive());
    }

    @Transactional
    @Override
    public UUID initiatePhoneUpdate(UUID userId, String newPhoneNumber) {
        User user = userService.getUserById(userId);
        userService.validatePhoneAvailability(newPhoneNumber, user.getRole(), user.getId());
        return otpService.requestOtp(user, newPhoneNumber, null, OtpReason.PHONE_NUMBER_CHANGE);
    }

    @Transactional
    @Override
    public void verifyPhoneUpdateOtp(UUID otpId, String otpCode, String newPhoneNumber) {
        OtpCode verified = otpService.verifyOtp(otpId, otpCode);
        userService.updatePhoneNumber(verified.getUser().getId(), newPhoneNumber);
    }

    @Transactional
    @Override
    public UUID initiateEmailUpdate(UUID userId, String newEmail) {
        User user = userService.getUserById(userId);
        userService.validateEmailAvailability(newEmail, user.getRole(), user.getId());
        return otpService.requestOtp(user, null, newEmail, OtpReason.EMAIL_CHANGE);
    }

    @Transactional
    @Override
    public void verifyEmailUpdateOtp(UUID otpId, String otpCode, String newEmail) {
        OtpCode verified = otpService.verifyOtp(otpId, otpCode);
        userService.updateEmail(verified.getUser().getId(), newEmail);
    }
}
