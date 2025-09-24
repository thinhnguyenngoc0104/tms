package com.tms.tms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tms.tms.common.mapper.AppMapper;
import com.tms.tms.io.UserResponse;
import com.tms.tms.repository.UserRepository;
import com.tms.tms.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final AppMapper appMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> read() {
        return appMapper.toUserResponses(userRepository.findAll());
    }
}
