package com.example.carrental.service.impl;

import com.example.carrental.controller.dto.user.UserChangePasswordRequest;
import com.example.carrental.controller.dto.user.UserDataResponse;
import com.example.carrental.controller.dto.user.UserForgotPasswordRequest;
import com.example.carrental.controller.dto.user.UserProfileResponse;
import com.example.carrental.controller.dto.user.UserRegistrationRequest;
import com.example.carrental.controller.dto.user.UserSearchRequest;
import com.example.carrental.controller.dto.user.UserUpdateRequest;
import com.example.carrental.entity.user.User;
import com.example.carrental.entity.user.UserConfirmationToken;
import com.example.carrental.mapper.UserMapper;
import com.example.carrental.repository.UserCriteriaRepository;
import com.example.carrental.repository.UserRepository;
import com.example.carrental.service.EmailService;
import com.example.carrental.service.UserConfirmationTokenService;
import com.example.carrental.service.UserRoleService;
import com.example.carrental.service.UserService;
import com.example.carrental.service.exceptions.TokenExpireException;
import com.example.carrental.service.exceptions.UsernameAlreadyTakenException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final static Integer TIME_FOR_CONFIRMATION = 15;

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final UserRoleService userRoleServiceService;
  private final UserConfirmationTokenService userConfirmationTokenService;
  private final EmailService emailService;
  private final UserCriteriaRepository userCriteriaRepository;
  private final UserMapper userMapper;

  @Override
  public Page<UserDataResponse> findAll(UserSearchRequest userSearchRequest) {
    var usersPage = userCriteriaRepository.findUsers(userSearchRequest);
    List<UserDataResponse> userDataResponse = new ArrayList<>();
    usersPage.forEach(user -> userDataResponse.add(userMapper.userToUserDataResponse(user)));
    return new PageImpl<>(userDataResponse, usersPage.getPageable(), usersPage.getTotalElements());
  }

  @Override
  public User findById(Long id) {
    var optionalUser = userRepository.findById(id);
    if (optionalUser.isEmpty()) {
      log.error("User with id {} does not exist", id);
      throw new IllegalStateException(String.format("User with id %d does not exists", id));
    }
    return optionalUser.get();
  }

  @Override
  public String checkExistedEmail(String email) throws UsernameAlreadyTakenException {
    var optionalUser = userRepository.findByEmail(email);
    if (optionalUser.isPresent()) {
      log.error("Email {} is already taken", email);
      throw new UsernameAlreadyTakenException(String.format("Email %s is already taken", email));
    }
    return String.format("Email %s is free", email);
  }

  @Override
  @Transactional
  public String create(UserRegistrationRequest userRegistrationRequest)
      throws UsernameAlreadyTakenException {
    if (!userRegistrationRequest.getPassword()
        .equals(userRegistrationRequest.getConfirmPassword())) {
      log.error("Password mismatch");
      throw new IllegalStateException("Password mismatch");
    }

    checkExistedEmail(userRegistrationRequest.getEmail());

    var encodedPassword = passwordEncoder.encode(userRegistrationRequest.getPassword());
    var user = User
        .builder()
        .email(userRegistrationRequest.getEmail())
        .password(encodedPassword)
        .role(userRoleServiceService.findByRole("USER"))
        .createdAt(LocalDateTime.now())
        .changedAt(LocalDateTime.now())
        .enabled(true)
        .locked(false)
        .isEmailConfirmed(false)
        .build();

    userRepository.save(user);

    var token = UUID.randomUUID().toString();
    var confirmationToken = UserConfirmationToken
        .builder()
        .token(token)
        .creationTime(LocalDateTime.now())
        .expirationTime(LocalDateTime.now().plusMinutes(TIME_FOR_CONFIRMATION))
        .user(user)
        .build();
    userConfirmationTokenService.createUserConfirmationToken(confirmationToken);

    var link = String.format("http://localhost:8080/users/email/confirm?token=%s", token);

    var message = String.format("Click on link to confirm your email: %s", link);

    emailService.sendEmail(user.getEmail(), message, "Email confirmation");

    return "Success";
  }

  @Override
  @Transactional
  public String forgotPassword(UserForgotPasswordRequest forgotPasswordRequest) {
    var user = findUserByEmail(forgotPasswordRequest.getEmail());

    var token = UUID.randomUUID().toString();

    var confirmationToken = UserConfirmationToken
        .builder()
        .token(token)
        .creationTime(LocalDateTime.now())
        .expirationTime(LocalDateTime.now().plusMinutes(TIME_FOR_CONFIRMATION))
        .user(user)
        .build();
    userConfirmationTokenService.createUserConfirmationToken(confirmationToken);

    var link = String.format("http://localhost:3000/reset/%s", token);

    var message = String.format("Click on link to recover your password: %s", link);

    emailService.sendEmail(user.getEmail(), message, "Password recovery");

    return "Success";
  }

  @Override
  @Transactional
  public String confirmEmail(String token) throws TokenExpireException {
    var confirmationToken = userConfirmationTokenService
        .getUserConfirmationTokenByToken(token);

    if (confirmationToken.getConfirmationTime() != null) {
      log.error("Email already confirmed");
      throw new IllegalStateException("Email already confirmed");
    }

    LocalDateTime expirationTime = confirmationToken.getExpirationTime();

    if (expirationTime.isBefore(LocalDateTime.now())) {
      log.error("Token {} expired", token);
      throw new TokenExpireException("Token expired!");
    }

    var user = findById(confirmationToken.getUser().getId());
    user.setIsEmailConfirmed(true);
    userRepository.save(user);

    userConfirmationTokenService.updateUserConfirmationTokenConfirmedAt(token);

    return "Email confirmed!";
  }

  @Override
  @Transactional
  public String update(Long id, UserUpdateRequest userUpdateRequest)
      throws UsernameAlreadyTakenException {
    var user = findById(id);

    if (Optional.ofNullable(userUpdateRequest.getEmail()).isPresent()) {
      checkExistedEmail(userUpdateRequest.getEmail());
      user.setEmail(userUpdateRequest.getEmail());
      user.setIsEmailConfirmed(false);
    }
    if (Optional.ofNullable(userUpdateRequest.getPassword()).isPresent() &&
        userUpdateRequest.getPassword()
            .equals(userUpdateRequest.getConfirmPassword())) {
      user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
    } else if (Optional.ofNullable(userUpdateRequest.getPassword()).isPresent() &&
        !userUpdateRequest.getPassword()
            .equals(userUpdateRequest.getConfirmPassword())) {
      log.error("Passwords mismatch");
      throw new IllegalStateException("Passwords mismatch");
    }
    user.setChangedAt(LocalDateTime.now());
    userRepository.save(user);

    return "Success";
  }

  @Override
  @Transactional
  public String changePassword(UserChangePasswordRequest changePasswordRequest)
      throws TokenExpireException {
    var confirmationToken = userConfirmationTokenService
        .getUserConfirmationTokenByToken(changePasswordRequest.getToken());

    if (!changePasswordRequest.getPassword().equals(changePasswordRequest.getConfirmPassword())) {
      log.error("Password mismatch");
      throw new IllegalStateException("Password mismatch");
    }

    if (confirmationToken.getConfirmationTime() != null) {
      log.error("Token already used!");
      throw new TokenExpireException("Token already used!");
    }

    LocalDateTime expirationTime = confirmationToken.getExpirationTime();

    if (expirationTime.isBefore(LocalDateTime.now())) {
      log.error("Token {} expired", changePasswordRequest.getToken());
      throw new TokenExpireException("Token expired");
    }

    userConfirmationTokenService
        .updateUserConfirmationTokenConfirmedAt(changePasswordRequest.getToken());

    var user = confirmationToken.getUser();
    user.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
    user.setChangedAt(LocalDateTime.now());
    userRepository.save(user);

    return "Success";
  }

  @Override
  public UserProfileResponse getUserProfile() {
    var email = SecurityContextHolder.getContext().getAuthentication().getName();

    if ("anonymousUser".equals(email)) {
      log.error("User is not authenticated!");
      throw new IllegalStateException("User is not authenticated!");
    }

    var user = findUserByEmail(email);
    return userMapper.userToUserProfileResponse(user);
  }

  @Override
  public String sendEmailConfirmationMessage() {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();

    if ("anonymousUser".equals(username)) {
      log.error("User is not authenticated!");
      throw new IllegalStateException("User is not authenticated!");
    }

    var user = findUserByEmail(username);

    var token = UUID.randomUUID().toString();
    UserConfirmationToken confirmationToken = UserConfirmationToken
        .builder()
        .token(token)
        .creationTime(LocalDateTime.now())
        .expirationTime(LocalDateTime.now().plusMinutes(TIME_FOR_CONFIRMATION))
        .user(user)
        .build();
    userConfirmationTokenService.createUserConfirmationToken(confirmationToken);

    var link = String.format("http://localhost:8080/users/email/confirm?token=%s", token);

    var message = String.format("Click on link to confirm your email: %s", link);

    emailService.sendEmail(user.getEmail(), message, "Email confirmation");

    return "Success";
  }

  @Override
  public User findUserByEmail(String email) {
    var optionalUser = userRepository.findByEmail(email);
    if (optionalUser.isEmpty()) {
      log.error("User with email {} does not exist", email);
      throw new UsernameNotFoundException(String.format("User with email %s not found", email));
    }
    return optionalUser.get();
  }

  @Override
  public String updateLastLoginDate(String email) {
    var user = findUserByEmail(email);
    user.setLastLogin(LocalDateTime.now());
    userRepository.save(user);
    return "Success";
  }

  @Override
  @Transactional
  public String updateUserStatus(Long id) {
    var user = findById(id);
    user.setLocked(!user.getLocked());
    userRepository.save(user);
    return "Success";
  }

  @Override
  @Transactional
  public String updateUserRoleToAdmin(Long id) {
    var user = findById(id);
    var role = userRoleServiceService.findByRole("ADMIN");
    user.setRole(role);
    userRepository.save(user);
    return "Success";
  }

  @Override
  @Transactional
  public String updateUserRoleToUser(Long id) {
    var user = findById(id);
    var role = userRoleServiceService.findByRole("USER");
    user.setRole(role);
    userRepository.save(user);
    return "Success";
  }

  @Override
  public int findNewUsersAmountPerDay() {
    return userRepository
        .countAllByCreatedAtAfter(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)));
  }
}
