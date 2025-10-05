package com.idea101.backendengine.auth.repository;

import com.idea101.backendengine.auth.entity.OtpCode;
import com.idea101.backendengine.auth.entity.User;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE OtpCode o SET o.isUsed = true WHERE o.user = :user AND o.isUsed = false")
    void invalidateOldOtps(User user);

    Optional<OtpCode> findByUserIdAndIsUsedFalseAndExpiresAtAfter(
            UUID userId,
            LocalDateTime currentTime
    );
}
