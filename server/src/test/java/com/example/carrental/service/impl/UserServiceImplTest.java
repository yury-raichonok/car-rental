package com.example.carrental.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.user.UserChangePasswordRequest;
import com.example.carrental.controller.dto.user.UserForgotPasswordRequest;
import com.example.carrental.controller.dto.user.UserRegistrationRequest;
import com.example.carrental.controller.dto.user.UserSearchRequest;
import com.example.carrental.controller.dto.user.UserUpdateRequest;
import com.example.carrental.entity.user.User;
import com.example.carrental.entity.user.UserConfirmationToken;
import com.example.carrental.entity.user.UserDocumentStatus;
import com.example.carrental.entity.user.UserDrivingLicense;
import com.example.carrental.entity.user.UserPassport;
import com.example.carrental.entity.user.UserPhone;
import com.example.carrental.entity.user.UserRole;
import com.example.carrental.repository.UserCriteriaRepository;
import com.example.carrental.repository.UserRepository;
import com.example.carrental.service.EmailService;
import com.example.carrental.service.UserConfirmationTokenService;
import com.example.carrental.service.UserRoleService;
import com.example.carrental.service.UserSecurityService;
import com.example.carrental.service.exceptions.TokenExpireException;
import com.example.carrental.service.exceptions.UsernameAlreadyTakenException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceImplTest {

  private User user;

  @Autowired
  private UserServiceImpl userService;

  @MockBean
  private EmailService emailService;

  @MockBean
  private UserConfirmationTokenService userConfirmationTokenService;

  @MockBean
  private UserCriteriaRepository userCriteriaRepository;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private UserRoleService userRoleServiceService;

  @MockBean
  private UserSecurityService userSecurityService;

  @BeforeEach
  public void setup() {
    var userPassport = UserPassport.builder().id(1L).status(UserDocumentStatus.CONFIRMED).build();
    var userDrivingLicense = UserDrivingLicense.builder().id(2L)
        .status(UserDocumentStatus.CONFIRMED).build();
    var phone = UserPhone.builder().id(1L).phone("+375111234567").active(true).build();
    var role = UserRole.builder().id(1L).role("USER").roleDescription("User").build();
    user = User.builder().id(1L).email("test@gmail.com").role(role).locked(false)
        .isEmailConfirmed(true).passport(userPassport).drivingLicense(userDrivingLicense).phones(
            Collections.singletonList(phone)).build();
  }

  @Test
  void givenValidRequest_whenGetUserProfile_thenSuccess() {
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

    var userProfileResponse = userService.getUserProfile();

    assertThat(userProfileResponse).isNotNull();
    assertThat(userProfileResponse.getId()).isEqualTo(user.getId());
    assertThat(userProfileResponse.getEmail()).isEqualTo(user.getEmail());
  }

  @Test
  void givenValidRequest_whenSendEmailConfirmationMessage_thenSuccess() {
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
    doNothing().when(userConfirmationTokenService).createUserConfirmationToken(any());
    doNothing().when(emailService).sendEmail(any(), any(), any());

    assertDoesNotThrow(() -> userService.sendEmailConfirmationMessage());
  }

  @Test
  void givenValidRequest_whenConfirmEmail_thenSuccess() {
    var token = UserConfirmationToken.builder().id(1L).user(user).token("token")
        .expirationTime(LocalDateTime.now().plusDays(1)).build();
    when(userConfirmationTokenService.getUserConfirmationTokenByToken("token")).thenReturn(token);
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    doNothing().when(userConfirmationTokenService).updateUserConfirmationTokenConfirmedAt(any());
    when(userRepository.save(any())).thenReturn(new User());

    assertDoesNotThrow(() -> userService.confirmEmail("token"));
  }

  @Test
  void givenRequestWithExpiredToken_whenConfirmEmail_thenThrowTokenExpireException() {
    var token = UserConfirmationToken.builder().id(1L).user(user).token("token")
        .expirationTime(LocalDateTime.now().minusDays(1)).build();
    when(userConfirmationTokenService.getUserConfirmationTokenByToken("token")).thenReturn(token);
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    doNothing().when(userConfirmationTokenService).updateUserConfirmationTokenConfirmedAt(any());
    when(userRepository.save(any())).thenReturn(new User());

    assertThrows(TokenExpireException.class, () -> userService.confirmEmail("token"));
  }

  @Test
  void givenRequestWithConfirmedEmail_whenConfirmEmail_thenThrowIllegalStateException() {
    var token = UserConfirmationToken.builder().id(1L).user(user).token("token")
        .expirationTime(LocalDateTime.now().minusDays(1))
        .confirmationTime(LocalDateTime.now().minusDays(2)).build();
    when(userConfirmationTokenService.getUserConfirmationTokenByToken("token")).thenReturn(token);
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    doNothing().when(userConfirmationTokenService).updateUserConfirmationTokenConfirmedAt(any());
    when(userRepository.save(any())).thenReturn(new User());

    assertThrows(IllegalStateException.class, () -> userService.confirmEmail("token"));
  }

  @Test
  void givenValidRequest_whenChangePassword_thenSuccess() {
    var changePasswordRequest = UserChangePasswordRequest.builder().token("token")
        .password("123qwerQWER").confirmPassword("123qwerQWER").build();
    var token = UserConfirmationToken.builder().id(1L).user(user).token("token")
        .expirationTime(LocalDateTime.now().plusDays(1)).build();
    when(userConfirmationTokenService.getUserConfirmationTokenByToken("token")).thenReturn(token);
    doNothing().when(userConfirmationTokenService).updateUserConfirmationTokenConfirmedAt(any());
    when(userRepository.save(any())).thenReturn(new User());

    assertDoesNotThrow(() -> userService.changePassword(changePasswordRequest));
  }

  @Test
  void givenRequestWithPasswordMismatch_whenChangePassword_thenThrowIllegalStateException() {
    var changePasswordRequest = UserChangePasswordRequest.builder().token("token")
        .password("123qwerQWER").confirmPassword("123qwerQWERR").build();
    var token = UserConfirmationToken.builder().id(1L).user(user).token("token")
        .expirationTime(LocalDateTime.now().minusDays(1))
        .confirmationTime(LocalDateTime.now().minusDays(2)).build();
    when(userConfirmationTokenService.getUserConfirmationTokenByToken("token")).thenReturn(token);
    doNothing().when(userConfirmationTokenService).updateUserConfirmationTokenConfirmedAt(any());
    when(userRepository.save(any())).thenReturn(new User());

    assertThrows(IllegalStateException.class,
        () -> userService.changePassword(changePasswordRequest));
  }

  @Test
  void givenRequestWithExpiredToken_whenChangePassword_thenThrowTokenExpireException() {
    var changePasswordRequest = UserChangePasswordRequest.builder().token("token")
        .password("123qwerQWER").confirmPassword("123qwerQWER").build();
    var token = UserConfirmationToken.builder().id(1L).user(user).token("token")
        .expirationTime(LocalDateTime.now().minusDays(1)).build();
    when(userConfirmationTokenService.getUserConfirmationTokenByToken("token")).thenReturn(token);
    doNothing().when(userConfirmationTokenService).updateUserConfirmationTokenConfirmedAt(any());
    when(userRepository.save(any())).thenReturn(new User());

    assertThrows(TokenExpireException.class,
        () -> userService.changePassword(changePasswordRequest));
  }

  @Test
  void givenValidRequest_whenCreate_thenSuccess() {
    var userRegistrationRequest = UserRegistrationRequest.builder().email("test@gmail.com")
        .password("123qwerQWER").confirmPassword("123qwerQWER").build();
    when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
    when(userRepository.save(any())).thenReturn(new User());
    doNothing().when(userConfirmationTokenService).createUserConfirmationToken(any());
    doNothing().when(emailService).sendEmail(any(), any(), any());

    assertDoesNotThrow(() -> userService.create(userRegistrationRequest));
  }

  @Test
  void givenRequestWithPasswordMismatch_whenCreate_thenThrowIllegalStateException() {
    var userRegistrationRequest = UserRegistrationRequest.builder().email("test@gmail.com")
        .password("123qwerQWER").confirmPassword("123qwerQWERR").build();
    when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
    when(userRepository.save(any())).thenReturn(new User());
    doNothing().when(userConfirmationTokenService).createUserConfirmationToken(any());
    doNothing().when(emailService).sendEmail(any(), any(), any());

    assertThrows(IllegalStateException.class, () -> userService.create(userRegistrationRequest));
  }

  @Test
  void givenValidRequest_whenFindAll_thenSuccess() {
    var userSearchRequest = new UserSearchRequest();
    when(userCriteriaRepository.findUsers(userSearchRequest))
        .thenReturn(new PageImpl<>(Collections.singletonList(user)));

    var userDataResponse = userService.findAll(userSearchRequest);

    assertThat(userDataResponse).isNotNull();
    assertThat(userDataResponse.getTotalElements()).isEqualTo(1);
  }

  @Test
  void givenValidRequest_whenForgotPassword_thenSuccess() {
    var forgotPasswordRequest = UserForgotPasswordRequest.builder().email("test@gmail.com").build();
    when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
    doNothing().when(userConfirmationTokenService).createUserConfirmationToken(any());
    doNothing().when(emailService).sendEmail(any(), any(), any());

    assertDoesNotThrow(() -> userService.forgotPassword(forgotPasswordRequest));
  }

  @Test
  void givenValidRequest_whenUpdate_thenSuccess() {
    var userUpdateRequest = UserUpdateRequest.builder().email("test@gmail.com")
        .password("123qwerQWER")
        .confirmPassword("123qwerQWER").build();
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
    when(userRepository.save(any())).thenReturn(new User());

    assertDoesNotThrow(() -> userService.update(1L, userUpdateRequest));
  }

  @Test
  void givenValidRequest_whenUpdateUserRoleToAdmin_thenSuccess_thenSuccess() {
    var role = UserRole.builder().role("ADMIN").build();
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userRoleServiceService.findByRole("ADMIN")).thenReturn(role);
    when(userRepository.save(any())).thenReturn(new User());

    assertDoesNotThrow(() -> userService.updateUserRoleToAdmin(1L));
    assertEquals("ADMIN", user.getRole().getRole());
  }

  @Test
  void givenValidRequest_whenUpdateUserRoleToUser_thenSuccess() {
    var role = UserRole.builder().role("USER").build();
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userRoleServiceService.findByRole("ADMIN")).thenReturn(role);
    when(userRepository.save(any())).thenReturn(new User());

    assertDoesNotThrow(() -> userService.updateUserRoleToAdmin(1L));
    assertEquals("USER", user.getRole().getRole());
  }

  @Test
  void givenValidRequest_whenUpdateUserStatus_thenSuccess() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    assertDoesNotThrow(() -> userService.updateUserRoleToAdmin(1L));
  }

  @Test
  void givenValidRequest_whenCheckExistedEmail_thenSuccess() {
    when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.empty());

    assertDoesNotThrow(() -> userService.checkExistedEmail("test@gmail.com"));
  }

  @Test
  void givenRequestWithExistingEmail_whenCheckExistedEmail_thenThrowUsernameAlreadyTakenException() {
    when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));

    assertThrows(UsernameAlreadyTakenException.class,
        () -> userService.checkExistedEmail("test@gmail.com"));
  }

  @Test
  void givenValidRequest_whenFindById_thenSuccess() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    var response = userService.findById(1L);

    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(user.getId());
    assertThat(response.getEmail()).isEqualTo(user.getEmail());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindById_thenThrowIllegalStateException() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> userService.findById(1L));
  }

  @Test
  void givenValidRequest_whenFindUserByEmail_thenSuccess() {
    when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));

    var response = userService.findUserByEmail("test@gmail.com");

    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(user.getId());
    assertThat(response.getEmail()).isEqualTo(user.getEmail());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindUserByEmail_thenThrowUsernameNotFoundException() {
    when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class,
        () -> userService.findUserByEmail("test@gmail.com"));
  }

  @Test
  void givenValidRequest_whenUpdateLastLoginDate_thenSuccess() {
    when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
    when(userRepository.save(any())).thenReturn(new User());

    assertDoesNotThrow(() -> userService.updateLastLoginDate("test@gmail.com"));
  }
}