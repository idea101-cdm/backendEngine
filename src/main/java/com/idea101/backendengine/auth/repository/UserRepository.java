package com.idea101.backendengine.auth.repository;

import com.idea101.backendengine.auth.entity.User;
import com.idea101.backendengine.common.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByPhoneNumberAndRole(String phoneNumber, UserRole role);

    Optional<User> findByEmailAndRole(String email, UserRole role);

    @Modifying
    @Query("UPDATE User u SET u.isActive = :status WHERE u.id = :id")
    void updateUserActiveStatus(UUID id, boolean status);

    @Modifying
    @Query("UPDATE User u SET u.email = :email, u.phoneNumber = :phoneNumber WHERE u.id = :id")
    void updateUserCredentials(UUID id, String email, String phoneNumber);

    void deleteUserById(UUID id);
}

