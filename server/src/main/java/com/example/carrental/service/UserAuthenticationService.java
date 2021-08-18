package com.example.carrental.service;

import com.example.carrental.controller.dto.user.UserInfoResponse;
import com.example.carrental.controller.dto.user.UserLoginRequest;
import com.example.carrental.controller.dto.user.UserLoginResponse;
import java.security.Principal;
import org.springframework.stereotype.Service;

/**
 * The service for User Authentication.
 * <p>
 * This interface describes actions on User Authentication.
 * </p>
 * @author Yury Raichonak
 */
@Service
public interface UserAuthenticationService {

  UserInfoResponse getUserInfo(Principal user);

  UserLoginResponse login(UserLoginRequest userLoginRequest);
}
