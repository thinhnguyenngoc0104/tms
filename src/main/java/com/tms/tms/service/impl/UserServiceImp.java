package com.tms.tms.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tms.tms.entity.UserEntity;
import com.tms.tms.io.UserRequest;
import com.tms.tms.io.UserResponse;
import com.tms.tms.repository.UserRepository;
import com.tms.tms.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse add(UserRequest request) {
        UserEntity user = toEntity(request);

        // Set ID
        // long counter = userRepository.count() + 1;
        // user.setUserId("U" + String.format("%03d", counter)); // U001, U002, ...

        user = userRepository.save(user);
        return toResponse(user);
    }

    @Override
    public String getUserRole(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email))
                .getRole(); // .getRole().name()
    }

    @Override
    public List<UserResponse> read() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public UserResponse profile(Long id) {
        return userRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    private UserResponse toResponse(UserEntity user) {
        return UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole()) // user.getRole().name()
                .build();
    }

    private UserEntity toEntity(UserRequest request) {
        return UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole()) // UserEntity.Role.valueOf(request.getRole().toUpperCase())
                .build();
    }
}
