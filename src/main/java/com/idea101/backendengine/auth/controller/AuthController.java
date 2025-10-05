package com.idea101.backendengine.auth.controller;

import com.idea101.backendengine.auth.dto.GenerateOtpRequestDto;
import com.idea101.backendengine.auth.dto.GenerateOtpResponseDto;
import com.idea101.backendengine.auth.dto.VerifyOtpRequestDto;
import com.idea101.backendengine.auth.dto.VerifyOtpResponseDto;
import com.idea101.backendengine.auth.service.OtpService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final OtpService otpService;

    public AuthController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<?> generateOtp(@Valid @RequestBody GenerateOtpRequestDto requestDto) {
        try {
            UUID id = otpService.requestOtp(requestDto);

            GenerateOtpResponseDto response = new GenerateOtpResponseDto(id, "OTP sent successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating OTP: " + e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequestDto requestDto) {
        try {
            String jwtToken = otpService.verifyOtp(requestDto);
            VerifyOtpResponseDto response = new VerifyOtpResponseDto(jwtToken);
            return ResponseEntity.ok().body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.GONE).body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error verifying OTP: " + e.getMessage());
        }
    }
}
