package com.idea101.backendengine.auth.service.impl;

import com.idea101.backendengine.auth.dto.GenerateOtpRequestDto;
import com.idea101.backendengine.auth.dto.VerifyOtpRequestDto;
import com.idea101.backendengine.auth.entity.OtpCode;
import com.idea101.backendengine.auth.entity.User;
import com.idea101.backendengine.auth.repository.OtpCodeRepository;
import com.idea101.backendengine.auth.repository.UserRepository;
import com.idea101.backendengine.auth.service.OtpService;
import com.idea101.backendengine.common.jwt.JwtUtil;
import com.idea101.backendengine.common.enums.OtpReason;
import com.idea101.backendengine.auth.event.publisher.AuthEventPublisher;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class OtpServiceImpl implements OtpService {

    private final UserRepository userRepository;
    private final OtpCodeRepository otpCodeRepository;
    private final AuthEventPublisher authEventPublisher;
    private final JwtUtil jwtUtil;
    private static final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    public OtpServiceImpl(UserRepository userRepository, OtpCodeRepository otpCodeRepository, JwtUtil jwtUtil, AuthEventPublisher authEventPublisher) {
        this.userRepository = userRepository;
        this.otpCodeRepository = otpCodeRepository;
        this.jwtUtil = jwtUtil;
        this.authEventPublisher = authEventPublisher;
    }

    @Transactional
    @Override
    public UUID requestOtp(GenerateOtpRequestDto requestOtpDto) {
        try{
            Optional<User> userOpt;
            if (requestOtpDto.getPhoneNumber() != null) {
                userOpt = userRepository.findByPhoneNumberAndRole(requestOtpDto.getPhoneNumber(), requestOtpDto.getRole());
            } else {
                userOpt = userRepository.findByEmailAndRole(requestOtpDto.getEmailId(), requestOtpDto.getRole());
            }

            String otp = String.valueOf(secureRandom.nextInt(9000)+1000);

            User user;
            if(userOpt.isPresent()){
                user = userOpt.get();
            } else {
                user = new User();
                user.setPhoneNumber(requestOtpDto.getPhoneNumber());
                user.setEmail(requestOtpDto.getEmailId());
                user.setRole(requestOtpDto.getRole());
                user.setIsVerified(false);
                userRepository.save(user);
            }

            otpCodeRepository.invalidateOldOtps(user);

            OtpCode otpCode = new OtpCode();
            otpCode.setEmail(requestOtpDto.getEmailId());
            otpCode.setPhoneNumber(requestOtpDto.getPhoneNumber());
            otpCode.setUser(user);
            otpCode.setOtpCode(otp);
            otpCode.setPurpose(OtpReason.SIGN_IN_UP);
            otpCodeRepository.save(otpCode);

            authEventPublisher.publishOtpEvent(user.getId(), requestOtpDto.getPhoneNumber() != null ? "SMS" : "EMAIL", otp, "SIGNIN_UP", requestOtpDto.getEmailId(), requestOtpDto.getPhoneNumber());
            return user.getId();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public String verifyOtp(VerifyOtpRequestDto verifyOtpDto) {
        try{
            Optional<OtpCode> otpCodeOpt = otpCodeRepository.findByUserIdAndIsUsedFalseAndExpiresAtAfter(verifyOtpDto.getId(), LocalDateTime.now());
            if (otpCodeOpt.isEmpty()) {
                throw new IllegalStateException("No valid OTP found. It may have expired or already been used.");
            }

            OtpCode otpCode = otpCodeOpt.get();

            if (!otpCode.getOtpCode().equals(verifyOtpDto.getOtpCode())) {
                throw new IllegalArgumentException("Invalid OTP");
            }

            otpCode.setIsUsed(true);
            otpCodeRepository.save(otpCode);

            User user = otpCode.getUser();
            if (user != null && Boolean.FALSE.equals(user.getIsVerified())) {
                user.setIsVerified(true);
                userRepository.save(user);
            }

            assert otpCode.getUser() != null;
            return jwtUtil.generateToken(user.getId(), user.getRole(), user.getIsVerified(), user.getIsActive());

        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error verifying OTP", e);
        }
    }
}
