package com.gov.systemservice.service;

import com.gov.systemservice.dto.LoginRequest;
import com.gov.systemservice.dto.LoginResponse;
import com.gov.systemservice.dto.RegisterRequest;
import com.gov.systemservice.dto.ResetPasswordRequest;
import com.gov.systemservice.pojo.User;

public interface UserService {
    LoginResponse login(LoginRequest request, String ip);
    boolean register(RegisterRequest request);
    boolean resetPassword(ResetPasswordRequest request);
    User getUserById(Long id);
    User getUserByUsername(String username);
}
