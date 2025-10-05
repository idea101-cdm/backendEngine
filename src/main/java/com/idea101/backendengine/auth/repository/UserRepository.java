package com.idea101.backendengine.auth.repository;

import com.idea101.backendengine.auth.entity.User;
import com.idea101.backendengine.common.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByPhoneNumberAndRole(String phone, UserRole role);
    Optional<User> findByEmailAndRole(String email, UserRole role);
}
