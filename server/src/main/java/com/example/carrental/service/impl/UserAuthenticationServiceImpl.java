package com.example.carrental.service.impl;

import com.example.carrental.config.JwtTokenHelper;
import com.example.carrental.controller.dto.user.UserInfoResponse;
import com.example.carrental.controller.dto.user.UserLoginRequest;
import com.example.carrental.controller.dto.user.UserLoginResponse;
import com.example.carrental.entity.user.User;
import com.example.carrental.service.UserAuthenticationService;
import com.example.carrental.service.UserSecurityService;
import com.example.carrental.service.UserService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The service for User Authentication.
 *
 * @author Yury Raichonak
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenHelper jwtTokenHelper;
  private final UserSecurityService userSecurityService;
  private final UserService userService;

  /**
   * @param user data.
   * @return user information.
   */
  @Override
  public UserInfoResponse getUserInfo(Principal user) {
    if (null == user) {
      log.error("User is not authenticated");
      throw new IllegalStateException("User is not authenticated");
    }
    var userObj = (User) userSecurityService.loadUserByUsername(user.getName());
    return UserInfoResponse
        .builder()
        .email(userObj.getEmail())
        .role(userObj.getAuthorities())
        .build();
  }

  /**
   * @param userLoginRequest data for login.
   * @return user login response.
   */
  @Override
  @Transactional
  public UserLoginResponse login(UserLoginRequest userLoginRequest) {
    final Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(
            userLoginRequest.getEmail(), userLoginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    var authenticatedUser = (User) authentication.getPrincipal();
    String jwtToken = jwtTokenHelper.generateToken(authenticatedUser.getUsername());

    userService.updateLastLoginDate(userLoginRequest.getEmail());

    return new UserLoginResponse(jwtToken);
  }
}
