package com.example.carrental.service;

import com.example.carrental.controller.dto.user.UserChangePasswordRequest;
import com.example.carrental.controller.dto.user.UserDataResponse;
import com.example.carrental.controller.dto.user.UserForgotPasswordRequest;
import com.example.carrental.controller.dto.user.UserProfileResponse;
import com.example.carrental.controller.dto.user.UserRegistrationRequest;
import com.example.carrental.controller.dto.user.UserSearchRequest;
import com.example.carrental.controller.dto.user.UserUpdateRequest;
import com.example.carrental.entity.user.User;
import com.example.carrental.service.exceptions.TokenExpireException;
import com.example.carrental.service.exceptions.UsernameAlreadyTakenException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

  Page<UserDataResponse> findAll(UserSearchRequest userSearchRequest);

  User findById(Long id);

  String checkExistedEmail(String email) throws UsernameAlreadyTakenException;

  String create(UserRegistrationRequest userRegistrationRequest)
      throws UsernameAlreadyTakenException;

  String confirmEmail(String token) throws TokenExpireException;

  String update(Long id, UserUpdateRequest userUpdateRequest)
      throws UsernameAlreadyTakenException;

  String forgotPassword(UserForgotPasswordRequest forgotPasswordRequest);

  String changePassword(UserChangePasswordRequest changePasswordRequest)
      throws TokenExpireException;

  UserProfileResponse getUserProfile();

  String sendEmailConfirmationMessage();

  User findUserByEmail(String username);

  String updateLastLoginDate(String email);

  String updateUserStatus(Long id);

  String updateUserRoleToAdmin(Long id);

  String updateUserRoleToUser(Long id);

  int findNewUsersAmountPerDay();
}
