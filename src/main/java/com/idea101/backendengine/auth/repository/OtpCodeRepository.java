package com.idea101.backendengine.auth.repository;

import com.idea101.backendengine.auth.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, UUID> {

    Optional<OtpCode> findByIdAndIsUsedFalseAndExpiresAtAfter(
            UUID id,
            LocalDateTime currentTime
    );
}
