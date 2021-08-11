package com.example.carrental.controller;

import com.example.carrental.controller.dto.user.UserChangePasswordRequest;
import com.example.carrental.controller.dto.user.UserDataResponse;
import com.example.carrental.controller.dto.user.UserSearchRequest;
import com.example.carrental.controller.dto.user.UserForgotPasswordRequest;
import com.example.carrental.controller.dto.user.UserLoginRequest;
import com.example.carrental.controller.dto.user.UserRegistrationRequest;
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
import org.springframework.mail.MailSendException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
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

  @PostMapping
  public ResponseEntity<Page<UserDataResponse>> findAll(
      @RequestBody UserSearchRequest userSearchRequest) {
    var users = userService.findAll(userSearchRequest);
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping(path = "/profile")
  public ResponseEntity<?> getUserProfile() {
    try {
      var userProfileResponse = userService.getUserProfile();
      return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(path = "/auth/registration")
  public ResponseEntity<String> create(
      @Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
    try {
      var response = userService.create(userRegistrationRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (UsernameAlreadyTakenException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/auth/login")
  public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
    try {
      var userLoginResponse = userAuthenticationService.login(userLoginRequest);
      return new ResponseEntity<>(userLoginResponse, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/auth/userinfo")
  public ResponseEntity<?> getUserInfo(Principal user) {
    try {
      var userInfoResponse = userAuthenticationService.getUserInfo(user);
      return new ResponseEntity<>(userInfoResponse, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("/auth/forgot")
  public ResponseEntity<?> forgotPassword(
      @Valid @RequestBody UserForgotPasswordRequest forgotPasswordRequest) {
    try {
      var response = userService.forgotPassword(forgotPasswordRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (MailSendException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/auth/forgot/reset")
  public ResponseEntity<String> changePassword(
      @Valid @RequestBody UserChangePasswordRequest changePasswordRequest) {
    try {
      var response = userService.changePassword(changePasswordRequest);
      if (response == null) {
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
      }
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (TokenExpireException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "/email/confirm")
  public ResponseEntity<String> confirmEmail(
      @NotBlank @RequestParam("token") String token) {
    try {
      var message = userService.confirmEmail(token);
      return new ResponseEntity<>(message, HttpStatus.OK);
    } catch (TokenExpireException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "/email/confirm/send")
  public ResponseEntity<String> sendEmailConfirmationMessage() {
    try {
      var message = userService.sendEmailConfirmationMessage();
      return new ResponseEntity<>(message, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<String> update(@NotNull @Positive @PathVariable Long id,
      @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
    try {
      var response = userService.update(id, userUpdateRequest);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (UsernameAlreadyTakenException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }
  }

  @PutMapping(path = "/status/{id}")
  public ResponseEntity<String> updateUserStatus(@NotNull @Positive @PathVariable Long id) {
    try {
      var response = userService.updateUserStatus(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/{id}/admin")
  public ResponseEntity<String> updateUserRoleToAdmin(@NotNull @Positive @PathVariable Long id) {
    try {
      var response = userService.updateUserRoleToAdmin(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(path = "/{id}/user")
  public ResponseEntity<String> updateUserRoleToUser(@NotNull @Positive @PathVariable Long id) {
    try {
      var response = userService.updateUserRoleToUser(id);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
