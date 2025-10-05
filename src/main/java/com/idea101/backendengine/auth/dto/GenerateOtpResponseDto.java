package com.idea101.backendengine.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class GenerateOtpResponseDto {
    private UUID id;
    private String message;
}
