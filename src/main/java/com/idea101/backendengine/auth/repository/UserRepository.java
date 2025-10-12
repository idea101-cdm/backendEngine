package com.idea101.backendengine.auth.repository;

import com.idea101.backendengine.auth.entity.User;
import com.idea101.backendengine.common.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE (u.phoneNumber = :identifier OR u.email = :identifier) AND u.role = :role")
    Optional<User> findByPhoneNumberOrEmailAndRole(@Param("identifier") String identifier, @Param("role") UserRole role);

    boolean existsByPhoneNumberAndRoleAndIdNot(String phoneNumber, UserRole role, UUID id);

    boolean existsByEmailAndRoleAndIdNot(String email, UserRole role, UUID id);

    @Modifying
    @Query("UPDATE User u SET u.isActive = :status WHERE u.id = :id")
    void updateUserActiveStatus(UUID id, boolean status);

    void deleteUserById(UUID id);
}

