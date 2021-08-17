package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.HOUR_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY;
import static com.example.carrental.constants.ApplicationConstants.MINUTES_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY;

import com.example.carrental.config.ApplicationPropertiesConfig;
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
import com.example.carrental.service.UserSecurityService;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private static final int TIME_FOR_CONFIRMATION = 15;
  private static final String ADMIN_ROLE = "ADMIN";
  private static final String EMAIL_ALREADY_CONFIRMED = "Email already confirmed";
  private static final String PASSWORD_MISMATCH = "Password mismatch";
  private static final String TOKEN_EXPIRED = "Token expired!";
  private static final String USER_ROLE = "USER";

  private final ApplicationPropertiesConfig applicationPropertiesConfig;
  private final BCryptPasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final UserConfirmationTokenService userConfirmationTokenService;
  private final UserCriteriaRepository userCriteriaRepository;
  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final UserRoleService userRoleServiceService;
  private final UserSecurityService userSecurityService;

  @Override
  public UserProfileResponse getUserProfile() {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var user = findUserByEmail(userEmail);
    return userMapper.userToUserProfileResponse(user);
  }

  @Override
  public void sendEmailConfirmationMessage() {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var user = findUserByEmail(userEmail);
    var token = UUID.randomUUID().toString();
    UserConfirmationToken confirmationToken = UserConfirmationToken
        .builder()
        .token(token)
        .creationTime(LocalDateTime.now())
        .expirationTime(LocalDateTime.now().plusMinutes(TIME_FOR_CONFIRMATION))
        .user(user)
        .build();
    userConfirmationTokenService.createUserConfirmationToken(confirmationToken);

    var link = String.format(applicationPropertiesConfig.getEmailConfirmationLink(), token);
    var message = String.format("Click on link to confirm your email: %s", link);

    emailService.sendEmail(user.getEmail(), message, "Email confirmation");
  }

  @Override
  @Transactional
  public void confirmEmail(String token) throws TokenExpireException {
    var confirmationToken = userConfirmationTokenService.getUserConfirmationTokenByToken(token);
    if (confirmationToken.getConfirmationTime() != null) {
      log.error(EMAIL_ALREADY_CONFIRMED);
      throw new IllegalStateException(EMAIL_ALREADY_CONFIRMED);
    }

    var expirationTime = confirmationToken.getExpirationTime();
    if (expirationTime.isBefore(LocalDateTime.now())) {
      log.error("Token {} expired", token);
      throw new TokenExpireException(TOKEN_EXPIRED);
    }

    var user = findById(confirmationToken.getUser().getId());
    user.setIsEmailConfirmed(true);
    userRepository.save(user);

    userConfirmationTokenService.updateUserConfirmationTokenConfirmedAt(token);
  }

  @Override
  @Transactional
  public void changePassword(UserChangePasswordRequest changePasswordRequest)
      throws TokenExpireException {
    var confirmationToken = userConfirmationTokenService
        .getUserConfirmationTokenByToken(changePasswordRequest.getToken());
    if (!changePasswordRequest.getPassword().equals(changePasswordRequest.getConfirmPassword())) {
      log.error(PASSWORD_MISMATCH);
      throw new IllegalStateException(PASSWORD_MISMATCH);
    }
    if (confirmationToken.getConfirmationTime() != null) {
      log.error("Token already used!");
      throw new TokenExpireException("Token already used!");
    }

    var expirationTime = confirmationToken.getExpirationTime();
    if (expirationTime.isBefore(LocalDateTime.now())) {
      log.error("Token {} expired", changePasswordRequest.getToken());
      throw new TokenExpireException(TOKEN_EXPIRED);
    }

    userConfirmationTokenService
        .updateUserConfirmationTokenConfirmedAt(changePasswordRequest.getToken());

    var user = confirmationToken.getUser();
    user.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
    user.setChangedAt(LocalDateTime.now());
    userRepository.save(user);
  }

  @Override
  @Transactional
  public void create(UserRegistrationRequest userRegistrationRequest)
      throws UsernameAlreadyTakenException {
    if (!userRegistrationRequest.getPassword()
        .equals(userRegistrationRequest.getConfirmPassword())) {
      log.error(PASSWORD_MISMATCH);
      throw new IllegalStateException(PASSWORD_MISMATCH);
    }

    checkExistedEmail(userRegistrationRequest.getEmail());

    var encodedPassword = passwordEncoder.encode(userRegistrationRequest.getPassword());
    var user = User
        .builder()
        .email(userRegistrationRequest.getEmail())
        .password(encodedPassword)
        .role(userRoleServiceService.findByRole(USER_ROLE))
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

    var link = String.format(applicationPropertiesConfig.getEmailConfirmationLink(), token);
    var message = String.format("Click on link to confirm your email: %s", link);
    emailService.sendEmail(user.getEmail(), message, "Email confirmation");
  }

  @Override
  public Page<UserDataResponse> findAll(UserSearchRequest userSearchRequest) {
    var usersPage = userCriteriaRepository.findUsers(userSearchRequest);
    List<UserDataResponse> usersResponse = new ArrayList<>();
    usersPage.forEach(user -> usersResponse.add(userMapper.userToUserDataResponse(user)));
    return new PageImpl<>(usersResponse, usersPage.getPageable(), usersPage.getTotalElements());
  }

  @Override
  @Transactional
  public void forgotPassword(UserForgotPasswordRequest forgotPasswordRequest) {
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

    var link = String.format(applicationPropertiesConfig.getEmailRestorationLink(), token);
    var message = String.format("Click on link to recover your password: %s", link);
    emailService.sendEmail(user.getEmail(), message, "Password recovery");
  }

  @Override
  @Transactional
  public void update(Long id, UserUpdateRequest userUpdateRequest)
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
      log.error(PASSWORD_MISMATCH);
      throw new IllegalStateException(PASSWORD_MISMATCH);
    }
    user.setChangedAt(LocalDateTime.now());
    userRepository.save(user);
  }

  @Override
  @Transactional
  public void updateUserRoleToAdmin(Long id) {
    var user = findById(id);
    var role = userRoleServiceService.findByRole(ADMIN_ROLE);
    user.setRole(role);
    userRepository.save(user);
  }

  @Override
  @Transactional
  public void updateUserRoleToUser(Long id) {
    var user = findById(id);
    var role = userRoleServiceService.findByRole(USER_ROLE);
    user.setRole(role);
    userRepository.save(user);
  }

  @Override
  @Transactional
  public void updateUserStatus(Long id) {
    var user = findById(id);
    user.setLocked(!user.getLocked());
    userRepository.save(user);
  }

  @Override
  public void checkExistedEmail(String email) throws UsernameAlreadyTakenException {
    if (userRepository.findByEmail(email).isPresent()) {
      log.error("Email {} is already taken", email);
      throw new UsernameAlreadyTakenException(String.format("Email %s is already taken", email));
    }
  }

  @Override
  public User findById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> {
      log.error("User with id {} does not exist", id);
      throw new IllegalStateException(String.format("User with id %d does not exists", id));
    });
  }

  @Override
  public int findNewUsersAmountPerDay() {
    return userRepository
        .countAllByCreatedAtAfter(LocalDateTime.of(LocalDate.now(),
            LocalTime.of(HOUR_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY,
                MINUTES_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY)));
  }

  @Override
  public User findUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(() -> {
      log.error("User with email {} does not exist", email);
      throw new UsernameNotFoundException(String.format("User with email %s not found", email));
    });
  }

  @Override
  @Transactional
  public void updateLastLoginDate(String email) {
    var user = findUserByEmail(email);
    user.setLastLogin(LocalDateTime.now());
    userRepository.save(user);
  }
}
