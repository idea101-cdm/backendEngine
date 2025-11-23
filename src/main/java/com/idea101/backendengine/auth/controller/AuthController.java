package com.idea101.backendengine.auth.controller;

import com.idea101.backendengine.auth.dto.*;
import com.idea101.backendengine.auth.service.AuthService;
import com.idea101.backendengine.common.annotation.authentication.jwtUser;
import com.idea101.backendengine.common.annotation.authentication.JwtProtected;
import com.idea101.backendengine.common.context.jwtUserContext;
import com.idea101.backendengine.restaurant.entity.RestaurantUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<OtpResponse> initiateLogin(@RequestBody @Valid OtpLoginRequest request) {
        UUID otpId = authService.initiateLoginViaOtp(request.getIdentifier(), request.getRole());
        return ResponseEntity.accepted().body(new OtpResponse(otpId));
    }

    @PostMapping("/verify-login")
    public ResponseEntity<TokenResponse> verifyLogin(@RequestBody @Valid OtpVerificationRequest request) {
        String token = authService.verifyLoginOtp(request.getOtpId(), request.getOtpCode());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @JwtProtected
    @PostMapping("/update-phone/initiate")
    public ResponseEntity<OtpResponse> initiatePhoneUpdate(@RequestBody @Valid UpdatePhoneRequest request, @jwtUser jwtUserContext user) {
        UUID otpId = authService.initiatePhoneUpdate(user.id(), request.getNewPhoneNumber());
        return ResponseEntity.accepted().body(new OtpResponse(otpId));
    }

    @PostMapping("/update-phone/verify")
    public ResponseEntity<Void> verifyPhoneUpdate(@RequestBody @Valid VerifyPhoneOtpRequest request) {
        authService.verifyPhoneUpdateOtp(request.getOtpId(), request.getOtpCode(), request.getNewPhoneNumber());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-email/initiate")
    public ResponseEntity<OtpResponse> initiateEmailUpdate(@RequestBody @Valid UpdateEmailRequest request, @jwtUser jwtUserContext user) {
        UUID otpId = authService.initiateEmailUpdate(user.id(), request.getNewEmail());
        return ResponseEntity.accepted().body(new OtpResponse(otpId));
    }

    @PostMapping("/update-email/verify")
    public ResponseEntity<Void> verifyEmailUpdate(@RequestBody @Valid VerifyEmailOtpRequest request) {
        authService.verifyEmailUpdateOtp(request.getOtpId(), request.getOtpCode(), request.getNewEmail());
        return ResponseEntity.ok().build();
    }
}
