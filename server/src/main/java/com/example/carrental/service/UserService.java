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
import org.springframework.stereotype.Service;

/**
 * The service for Users.
 * <p>
 * This interface describes actions on Users.
 * </p>
 * @author Yury Raichonak
 */
@Service
public interface UserService {

  UserProfileResponse getUserProfile();

  void sendEmailConfirmationMessage();

  void confirmEmail(String token) throws TokenExpireException;

  void changePassword(UserChangePasswordRequest changePasswordRequest)
      throws TokenExpireException;

  void create(UserRegistrationRequest userRegistrationRequest)
      throws UsernameAlreadyTakenException;

  Page<UserDataResponse> findAll(UserSearchRequest userSearchRequest);

  void forgotPassword(UserForgotPasswordRequest forgotPasswordRequest);

  void update(Long id, UserUpdateRequest userUpdateRequest)
      throws UsernameAlreadyTakenException;

  void updateUserRoleToAdmin(Long id);

  void updateUserRoleToUser(Long id);

  void updateUserStatus(Long id);

  void checkExistedEmail(String email) throws UsernameAlreadyTakenException;

  User findById(Long id);

  int findNewUsersAmountPerDay();

  User findUserByEmail(String username);

  void updateLastLoginDate(String email);
}
