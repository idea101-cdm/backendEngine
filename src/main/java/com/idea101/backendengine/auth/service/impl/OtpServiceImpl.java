package com.idea101.backendengine.auth.service.impl;

import com.idea101.backendengine.auth.entity.OtpCode;
import com.idea101.backendengine.auth.entity.User;
import com.idea101.backendengine.auth.event.publisher.AuthEventPublisher;
import com.idea101.backendengine.auth.repository.OtpCodeRepository;
import com.idea101.backendengine.auth.service.OtpService;
import com.idea101.backendengine.common.enums.OtpReason;
import com.idea101.backendengine.common.exception.InvalidOtpException;
import com.idea101.backendengine.common.exception.OtpExpiredException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class OtpServiceImpl implements OtpService {

    private final OtpCodeRepository otpCodeRepository;
    private final AuthEventPublisher authEventPublisher;
    private static final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    @Override
    public UUID requestOtp(User user, String phoneNumber, String email, OtpReason reason) {
        String otp = String.valueOf(secureRandom.nextInt(9000) + 1000);

        log.info("Requesting OTP for {} via {}. User ID: {}",
                phoneNumber != null ? phoneNumber : email,
                phoneNumber != null ? "SMS" : "EMAIL",
                user.getId());

        OtpCode otpCode = new OtpCode();
        otpCode.setUser(user);
        otpCode.setEmail(email);
        otpCode.setPhoneNumber(phoneNumber);
        otpCode.setOtpCode(otp);
        otpCode.setPurpose(reason);
        otpCode.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpCodeRepository.save(otpCode);

        authEventPublisher.publishOtpEvent(
                user.getId(),
                phoneNumber != null ? "SMS" : "EMAIL",
                otp,
                reason.toString(),
                email,
                phoneNumber
        );

        log.info("OTP {} generated and dispatched for purpose: {}. OTP ID: {}", otp, reason, otpCode.getId());
        return otpCode.getId();
    }

    @Transactional
    @Override
    public OtpCode verifyOtp(UUID otpId, String code) throws IllegalArgumentException, IllegalStateException {
        log.info("Verifying OTP for ID: {}", otpId);

        OtpCode otpCode = otpCodeRepository
                .findByIdAndIsUsedFalseAndExpiresAtAfter(otpId, LocalDateTime.now())
                .orElseThrow(() -> {
                    log.warn("OTP verification failed: No valid OTP found for ID {}", otpId);
                    return new OtpExpiredException("OTP has expired or already been used for ID: " + otpId);
                });

        if (!otpCode.getOtpCode().equals(code)) {
            log.warn("OTP mismatch for ID {}. Expected: {}, Provided: {}", otpId, otpCode.getOtpCode(), otpCode);
            throw new InvalidOtpException("Invalid OTP code for ID: " + otpId);
        }

        otpCode.setIsUsed(true);
        otpCodeRepository.save(otpCode);

        log.info("OTP verified successfully for ID: {}", otpId);
        return otpCode;
    }
}