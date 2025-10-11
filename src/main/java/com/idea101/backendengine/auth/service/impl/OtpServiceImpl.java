package com.idea101.backendengine.auth.service.impl;

import com.idea101.backendengine.auth.dto.GenerateOtpRequestDto;
import com.idea101.backendengine.auth.dto.VerifyOtpRequestDto;
import com.idea101.backendengine.auth.entity.OtpCode;
import com.idea101.backendengine.auth.entity.User;
import com.idea101.backendengine.auth.event.publisher.AuthEventPublisher;
import com.idea101.backendengine.auth.repository.OtpCodeRepository;
import com.idea101.backendengine.auth.repository.UserRepository;
import com.idea101.backendengine.auth.service.OtpService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class OtpServiceImpl implements OtpService {

    private final OtpCodeRepository otpCodeRepository;
    private final UserRepository userRepository;
    private final AuthEventPublisher authEventPublisher;
    private static final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    @Override
    public UUID requestOtp(GenerateOtpRequestDto dto) {
        String otp = String.valueOf(secureRandom.nextInt(9000) + 1000);

        Optional<User> optionalUser;
        if (dto.getPhoneNumber() != null) {
            log.info("Looking up user by phone: {} and role: {}", dto.getPhoneNumber(), dto.getRole());
            optionalUser = userRepository.findByPhoneNumberAndRole(dto.getPhoneNumber(), dto.getRole());
        } else {
            log.info("Looking up user by email: {} and role: {}", dto.getEmailId(), dto.getRole());
            optionalUser = userRepository.findByEmailAndRole(dto.getEmailId(), dto.getRole());
        }

        User user = optionalUser.orElseGet(() -> {
            log.info("No existing user found. Creating new user with email: {}, phone: {}, role: {}",
                    dto.getEmailId(), dto.getPhoneNumber(), dto.getRole());
            User newUser = new User();
            newUser.setEmail(dto.getEmailId());
            newUser.setRole(dto.getRole());
            newUser.setPhoneNumber(dto.getPhoneNumber());
            userRepository.save(newUser);
            return newUser;
        });

        log.info("Requesting OTP for {} via {}. User ID: {}",
                dto.getPhoneNumber() != null ? dto.getPhoneNumber() : dto.getEmailId(),
                dto.getPhoneNumber() != null ? "SMS" : "EMAIL",
                user.getId());

        OtpCode otpCode = new OtpCode();
        otpCode.setUser(user);
        otpCode.setEmail(dto.getEmailId());
        otpCode.setPhoneNumber(dto.getPhoneNumber());
        otpCode.setOtpCode(otp);
        otpCode.setPurpose(dto.getReason());
        otpCode.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpCodeRepository.save(otpCode);

        authEventPublisher.publishOtpEvent(
                user.getId(),
                dto.getPhoneNumber() != null ? "SMS" : "EMAIL",
                otp,
                dto.getReason().name(),
                dto.getEmailId(),
                dto.getPhoneNumber()
        );

        log.info("OTP {} generated and dispatched for purpose: {}. OTP ID: {}", otp, dto.getReason(), otpCode.getId());
        return otpCode.getId();
    }

    @Transactional
    @Override
    public OtpCode verifyOtp(VerifyOtpRequestDto dto) throws IllegalArgumentException, IllegalStateException {
        log.info("Verifying OTP for ID: {}", dto.getId());

        OtpCode otpCode = otpCodeRepository
                .findByIdAndIsUsedFalseAndExpiresAtAfter(dto.getId(), LocalDateTime.now())
                .orElseThrow(() -> {
                    log.warn("OTP verification failed: No valid OTP found for ID {}", dto.getId());
                    return new IllegalStateException("No valid OTP found. It may have expired or already been used.");
                });

        if (!otpCode.getOtpCode().equals(dto.getOtpCode())) {
            log.warn("OTP mismatch for ID {}. Expected: {}, Provided: {}", dto.getId(), otpCode.getOtpCode(), dto.getOtpCode());
            throw new IllegalArgumentException("Invalid OTP");
        }

        otpCode.setIsUsed(true);
        otpCodeRepository.save(otpCode);

        log.info("OTP verified successfully for ID: {}", dto.getId());
        return otpCode;
    }
}