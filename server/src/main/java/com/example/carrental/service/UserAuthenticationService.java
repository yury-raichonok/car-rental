package com.example.carrental.service;

import com.example.carrental.controller.dto.user.UserInfoResponse;
import com.example.carrental.controller.dto.user.UserLoginRequest;
import com.example.carrental.controller.dto.user.UserLoginResponse;
import java.security.Principal;
import org.springframework.stereotype.Service;

@Service
public interface UserAuthenticationService {

  UserLoginResponse login(UserLoginRequest userLoginRequest);

  UserInfoResponse getUserInfo(Principal user);
}
