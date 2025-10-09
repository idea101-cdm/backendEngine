package com.idea101.backendengine.common.event.model.otp;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OtpEvent {
    private UUID userId;
    private String channel;
    private String otp;
    private String purpose;
    private String phoneNumber;
    private String email;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private String eventType = "OTP_GENERATION";
}
