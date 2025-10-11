package com.idea101.backendengine.auth.controller;

import com.idea101.backendengine.auth.dto.GenerateOtpRequestDto;
import com.idea101.backendengine.auth.dto.GenerateOtpResponseDto;
import com.idea101.backendengine.auth.dto.VerifyOtpRequestDto;
import com.idea101.backendengine.auth.dto.VerifyOtpResponseDto;
import com.idea101.backendengine.auth.service.OtpService;
import com.idea101.backendengine.auth.service.UserService;
import com.idea101.backendengine.common.error.ProblemDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final OtpService otpService;
    private final UserService userService;
    private final ProblemDetailService error;

    @PostMapping("/generate-otp")
    public ResponseEntity<GenerateOtpResponseDto> generateOtp(@Valid @RequestBody GenerateOtpRequestDto requestDto) {
        try {
            UUID otpId = otpService.requestOtp(requestDto);
            log.info("OTP generated successfully for {} via {}. OTP ID: {}",
                    requestDto.getPhoneNumber() != null ? requestDto.getPhoneNumber() : requestDto.getEmailId(),
                    requestDto.getPhoneNumber() != null ? "SMS" : "EMAIL",
                    otpId);

            return ResponseEntity.ok(new GenerateOtpResponseDto(otpId, "OTP sent successfully"));
        } catch (Exception e) {
            log.error("Failed to generate OTP for request: {}", requestDto, e);
            return ResponseEntity.of( error.create( HttpStatus.INTERNAL_SERVER_ERROR, "OTP Generation Failed", "Error generating OTP: " + e.getMessage())).build();
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<VerifyOtpResponseDto> verifyOtp(@Valid @RequestBody VerifyOtpRequestDto requestDto) {
        try {
            String jwtToken = userService.loginUserWithOtp(requestDto);
            log.info("OTP verified successfully for OTP ID: {}. JWT issued.", requestDto.getId());
            return ResponseEntity.ok(new VerifyOtpResponseDto(jwtToken));
        } catch (IllegalArgumentException e) {
            log.warn("Invalid OTP for ID {}: {}", requestDto.getId(), e.getMessage());
            return ResponseEntity.of(error.create( HttpStatus.BAD_REQUEST, "Invalid OTP", e.getMessage())).build();
        } catch (IllegalStateException e) {
            log.warn("Expired or used OTP for ID {}: {}", requestDto.getId(), e.getMessage());
            return ResponseEntity.of(error.create( HttpStatus.GONE, "OTP Expired or Used", e.getMessage())).build();
        } catch (Exception e) {
            log.error("Unexpected error during OTP verification for ID {}: {}", requestDto.getId(), e.getMessage(), e);
            return ResponseEntity.of(error.create(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Error verifying OTP: " + e.getMessage()
            )).build();
        }
    }
}
