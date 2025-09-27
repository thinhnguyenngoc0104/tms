package com.tms.tms.service;

import java.util.List;

import com.tms.tms.io.UserResponse;

public interface UserService {
    List<UserResponse> read();
    UserResponse findById(Long id);
}
