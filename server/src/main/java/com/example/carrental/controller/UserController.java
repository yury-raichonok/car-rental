package com.example.carrental.controller;

import com.example.carrental.controller.dto.user.UserChangePasswordRequest;
import com.example.carrental.controller.dto.user.UserDataResponse;
import com.example.carrental.controller.dto.user.UserForgotPasswordRequest;
import com.example.carrental.controller.dto.user.UserInfoResponse;
import com.example.carrental.controller.dto.user.UserLoginRequest;
import com.example.carrental.controller.dto.user.UserLoginResponse;
import com.example.carrental.controller.dto.user.UserProfileResponse;
import com.example.carrental.controller.dto.user.UserRegistrationRequest;
import com.example.carrental.controller.dto.user.UserSearchRequest;
import com.example.carrental.controller.dto.user.UserUpdateRequest;
import com.example.carrental.service.UserAuthenticationService;
import com.example.carrental.service.UserService;
import com.example.carrental.service.exceptions.TokenExpireException;
import com.example.carrental.service.exceptions.UsernameAlreadyTakenException;
import java.security.Principal;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

  private final UserService userService;
  private final UserAuthenticationService userAuthenticationService;

  @GetMapping("/auth/userinfo")
  public ResponseEntity<UserInfoResponse> getUserInfo(Principal user) {
    var userInfoResponse = userAuthenticationService.getUserInfo(user);
    return new ResponseEntity<>(userInfoResponse, HttpStatus.OK);
  }

  @GetMapping(path = "/profile")
  public ResponseEntity<UserProfileResponse> getUserProfile() {
    var userProfileResponse = userService.getUserProfile();
    return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
  }

  @GetMapping(path = "/email/confirm/send")
  public ResponseEntity<HttpStatus> sendEmailConfirmationMessage() {
    userService.sendEmailConfirmationMessage();
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping(path = "/email/confirm")
  public ResponseEntity<HttpStatus> confirmEmail(
      @NotBlank @RequestParam("token") String token) throws TokenExpireException {
    userService.confirmEmail(token);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/auth/forgot/reset")
  public ResponseEntity<HttpStatus> changePassword(
      @Valid @RequestBody UserChangePasswordRequest changePasswordRequest)
      throws TokenExpireException {
    userService.changePassword(changePasswordRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping(path = "/auth/registration")
  public ResponseEntity<String> create(
      @Valid @RequestBody UserRegistrationRequest userRegistrationRequest)
      throws UsernameAlreadyTakenException {
    userService.create(userRegistrationRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Page<UserDataResponse>> findAll(
      @Valid @RequestBody UserSearchRequest userSearchRequest) {
    var users = userService.findAll(userSearchRequest);
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @PostMapping("/auth/forgot")
  public ResponseEntity<HttpStatus> forgotPassword(
      @Valid @RequestBody UserForgotPasswordRequest forgotPasswordRequest) {
    userService.forgotPassword(forgotPasswordRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/auth/login")
  public ResponseEntity<UserLoginResponse> login(
      @Valid @RequestBody UserLoginRequest userLoginRequest) {
    var userLoginResponse = userAuthenticationService.login(userLoginRequest);
    return new ResponseEntity<>(userLoginResponse, HttpStatus.OK);
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<HttpStatus> update(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody UserUpdateRequest userUpdateRequest)
      throws UsernameAlreadyTakenException {
    userService.update(id, userUpdateRequest);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/{id}/admin")
  public ResponseEntity<HttpStatus> updateUserRoleToAdmin(
      @NotNull @Positive @PathVariable Long id) {
    userService.updateUserRoleToAdmin(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/{id}/user")
  public ResponseEntity<HttpStatus> updateUserRoleToUser(@NotNull @Positive @PathVariable Long id) {
    userService.updateUserRoleToUser(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(path = "/status/{id}")
  public ResponseEntity<HttpStatus> updateUserStatus(@NotNull @Positive @PathVariable Long id) {
    userService.updateUserStatus(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
