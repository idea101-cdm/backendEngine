package com.idea101.backendengine.auth.service.impl;

import com.idea101.backendengine.auth.repository.UserRepository;
import com.idea101.backendengine.auth.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void updateCredentials(UUID id, String email, String phoneNumber) {
        userRepository.findUserById(id)
                .map(user -> {
                    user.setEmail(email);
                    user.setPhoneNumber(phoneNumber);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID : " + id));
    }

    @Transactional
    @Override
    public void activateUser(UUID id) {
        userRepository.findUserById(id)
                .map( user -> {
                    user.setIsActive(true);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID : " + id));
    }

    @Transactional
    @Override
    public void deactivateUser(UUID id) {
        userRepository.findUserById(id)
                .map( user -> {
                    user.setIsActive(false);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID : " + id));
    }

    @Transactional
    @Override
    public void deleteUser(UUID id) {
        userRepository.findUserById(id);
    }

}
