package com.example.carrental.service;

import com.example.carrental.controller.dto.user.UserInfoResponse;
import com.example.carrental.controller.dto.user.UserLoginRequest;
import com.example.carrental.controller.dto.user.UserLoginResponse;
import com.example.carrental.service.exceptions.UserNotAuthenticatedException;
import java.security.Principal;
import org.springframework.stereotype.Service;

@Service
public interface UserAuthenticationService {

  UserInfoResponse getUserInfo(Principal user) throws UserNotAuthenticatedException;

  UserLoginResponse login(UserLoginRequest userLoginRequest);
}
