package com.tms.tms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tms.tms.common.mapper.UserMapper;
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
    private final UserMapper userMapper;

    @Override
    public UserResponse add(UserRequest request) {
        UserEntity user = userMapper.toEntity(request);
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> read() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse profile(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    @Override
    public UserResponse update(Long id, UserRequest request) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

}
