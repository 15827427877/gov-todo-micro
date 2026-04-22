package com.gov.systemservice.service;

import com.gov.common.Result;
import com.gov.systemservice.dto.LoginRequest;
import com.gov.systemservice.dto.LoginResponse;
import com.gov.systemservice.dto.RegisterRequest;
import com.gov.systemservice.dto.ResetPasswordRequest;
import com.gov.systemservice.pojo.User;

import java.util.List;

public interface UserService {
    LoginResponse login(LoginRequest request, String ip);
    boolean register(RegisterRequest request);
    User selectByUsername(String username);
    Result<String> resetPassword(ResetPasswordRequest request);
    User getUserById(Long id);
    User getUserByUsername(String username);
    List<User> getUsers();
    User createUser(User user);
    User updateUser(User user);
    boolean deleteUser(Long id);
}
