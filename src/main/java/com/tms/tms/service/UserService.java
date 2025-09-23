package com.tms.tms.service;

import java.util.List;

import com.tms.tms.io.UserRequest;
import com.tms.tms.io.UserResponse;

public interface UserService {
    UserResponse add(UserRequest request);
    List<UserResponse> read();
}
