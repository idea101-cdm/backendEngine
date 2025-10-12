package com.idea101.backendengine.common.context;

import com.idea101.backendengine.common.enums.UserRole;

import java.util.UUID;

public record jwtUserContext(
   UUID id,
   UserRole role
) {}
