package com.example.carrental.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.example.carrental.entity.user.UserRole;
import com.example.carrental.service.UserAuthenticationService;
import com.example.carrental.service.UserService;
import com.example.carrental.service.exceptions.TokenExpireException;
import com.example.carrental.service.exceptions.UsernameAlreadyTakenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.Principal;
import java.util.Collections;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

  private static UserInfoResponse userInfoResponse;
  private static UserProfileResponse userProfileResponse;
  private static UserChangePasswordRequest changePasswordRequest;
  private static UserRegistrationRequest userRegistrationRequest;
  private static UserDataResponse userDataResponse;
  private static UserSearchRequest userSearchRequest;
  private static UserForgotPasswordRequest forgotPasswordRequest;
  private static UserLoginResponse userLoginResponse;
  private static UserLoginRequest userLoginRequest;
  private static UserUpdateRequest userUpdateRequest;
  private static UserRole userRole;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private UserAuthenticationService userAuthenticationService;

  @BeforeAll
  public static void setup() {
    userRole = UserRole.builder().role("USER").roleDescription("User").build();
    userInfoResponse = UserInfoResponse.builder().email("test@gmail.com").role(userRole).build();
    userProfileResponse = UserProfileResponse.builder().id(1L).email("test@gmail.com")
        .isEmailConfirmed(true).passportStatus("status").drivingLicenseStatus("status").build();
    changePasswordRequest = UserChangePasswordRequest.builder().token("token")
        .password("123qwerQWER").confirmPassword("123qwerQWER").build();
    userRegistrationRequest = UserRegistrationRequest.builder().email("test@gmail.com")
        .password("123qwerQWER").confirmPassword("123qwerQWER").build();
    userDataResponse = UserDataResponse.builder().email("test@gmail.com").role("USER").locked(false)
        .emailConfirmed(true).passportStatus("status").drivingLicenseStatus("status").build();
    userSearchRequest = new UserSearchRequest();
    forgotPasswordRequest = UserForgotPasswordRequest.builder().email("test@gmail.com").build();
    userLoginResponse = UserLoginResponse.builder().token("token").build();
    userLoginRequest = UserLoginRequest.builder().email("test@gmail.com").password("123qwerQWER")
        .build();
    userUpdateRequest = UserUpdateRequest.builder().email("test@gmail.com").password("123qwerQWER")
        .confirmPassword("123qwerQWER").build();
  }

  @AfterAll
  public static void teardown() {
    userInfoResponse = null;
    userProfileResponse = null;
    changePasswordRequest = null;
    userRegistrationRequest = null;
    userDataResponse = null;
    userSearchRequest = null;
    forgotPasswordRequest = null;
    userLoginResponse = null;
    userLoginRequest = null;
    userUpdateRequest = null;
    userRole = null;
  }

  @Test
  @WithMockUser
  void givenValidRequestAuthenticated_whenGetUserInfo_thenReturnResponse200() throws Exception {
    Principal mockPrincipal = mock(Principal.class);
    when(userAuthenticationService.getUserInfo(mockPrincipal)).thenReturn(userInfoResponse);

    mockMvc.perform(get("/users/auth/userinfo")
        .contentType(APPLICATION_JSON_VALUE)
        .principal(mockPrincipal))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void givenValidRequestUnauthorized_whenGetUserInfo_thenReturnResponse401() throws Exception {
    Principal mockPrincipal = mock(Principal.class);
    when(userAuthenticationService.getUserInfo(mockPrincipal)).thenReturn(userInfoResponse);

    mockMvc.perform(get("/users/auth/userinfo")
        .contentType(APPLICATION_JSON_VALUE)
        .principal(mockPrincipal))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void givenValidRequestAuthenticated_whenGetUserProfile_thenReturnResponse200() throws Exception {
    when(userService.getUserProfile()).thenReturn(userProfileResponse);

    mockMvc.perform(get("/users/profile")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void givenValidRequestUnauthorized_whenGetUserProfile_thenReturnResponse401() throws Exception {
    when(userService.getUserProfile()).thenReturn(userProfileResponse);

    mockMvc.perform(get("/users/profile")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenValidRequestAuthenticated_whenSendEmailConfirmationMessage_thenReturnResponse200()
      throws Exception {
    doNothing().when(userService).sendEmailConfirmationMessage();

    mockMvc.perform(get("/users/email/confirm/send")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenConfirmEmail_thenReturnResponse200() throws Exception {
    doNothing().when(userService).confirmEmail("token");

    mockMvc.perform(get("/users/email/confirm?token={token}", "token")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void givenRequestWithExpiredToken_whenConfirmEmail_thenReturnResponse405() throws Exception {
    doThrow(new TokenExpireException("Token expired")).when(userService).confirmEmail("token");

    mockMvc.perform(get("/users/email/confirm?token={token}", "token")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isMethodNotAllowed())
        .andExpect(content().string("Token expired"));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindNewUsersAmountPerDay_thenReturnResponse200() throws Exception {
    when(userService.findNewUsersAmountPerDay()).thenReturn(1);

    mockMvc.perform(get("/users/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindNewUsersAmountPerDay_thenReturnResponse403()
      throws Exception {
    when(userService.findNewUsersAmountPerDay()).thenReturn(1);

    mockMvc.perform(get("/users/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindNewUsersAmountPerDay_thenReturnResponse401()
      throws Exception {
    when(userService.findNewUsersAmountPerDay()).thenReturn(1);

    mockMvc.perform(get("/users/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenValidRequest_whenChangePassword_thenReturnResponse200() throws Exception {
    doNothing().when(userService).changePassword(changePasswordRequest);

    mockMvc.perform(post("/users/auth/forgot/reset")
        .contentType(APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(changePasswordRequest)))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void givenRequestWithExpiredToken_whenChangePassword_thenReturnResponse405() throws Exception {
    doThrow(new TokenExpireException("Token expired")).when(userService)
        .changePassword(changePasswordRequest);

    mockMvc.perform(post("/users/auth/forgot/reset")
        .contentType(APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(changePasswordRequest)))
        .andDo(print())
        .andExpect(status().isMethodNotAllowed())
        .andExpect(content().string("Token expired"));
  }

  @Test
  void givenValidRequest_whenCreate_thenReturnResponse200() throws Exception {
    doNothing().when(userService).create(userRegistrationRequest);

    mockMvc.perform(post("/users/auth/registration")
        .contentType(APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(userRegistrationRequest)))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void givenRequestWithExistingUsername_whenCreate_thenReturnResponse405() throws Exception {
    doThrow(new UsernameAlreadyTakenException("Username already taken")).when(userService)
        .create(userRegistrationRequest);

    mockMvc.perform(post("/users/auth/registration")
        .contentType(APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(userRegistrationRequest)))
        .andDo(print())
        .andExpect(status().isMethodNotAllowed())
        .andExpect(content().string("Username already taken"));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindAll_thenReturnResponse200() throws Exception {
    Page<UserDataResponse> page = new PageImpl<>(Collections.singletonList(userDataResponse));

    when(userService.findAll(userSearchRequest)).thenReturn(page);

    mockMvc.perform(post("/users")
        .content(objectMapper.writeValueAsString(userSearchRequest))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindAll_thenReturnResponse403() throws Exception {
    Page<UserDataResponse> page = new PageImpl<>(Collections.singletonList(userDataResponse));

    when(userService.findAll(userSearchRequest)).thenReturn(page);

    mockMvc.perform(post("/users")
        .content(objectMapper.writeValueAsString(userSearchRequest))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAll_thenReturnResponse401() throws Exception {
    Page<UserDataResponse> page = new PageImpl<>(Collections.singletonList(userDataResponse));

    when(userService.findAll(userSearchRequest)).thenReturn(page);

    mockMvc.perform(post("/users")
        .content(objectMapper.writeValueAsString(userSearchRequest))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenValidRequest_whenForgotPassword_thenReturnResponse200() throws Exception {
    doNothing().when(userService).forgotPassword(forgotPasswordRequest);

    mockMvc.perform(post("/users/auth/forgot")
        .content(objectMapper.writeValueAsString(forgotPasswordRequest))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void givenInvalidRequest_whenForgotPassword_thenReturnResponse400() throws Exception {
    doThrow(new IllegalStateException("Bad credentials")).when(userService)
        .forgotPassword(forgotPasswordRequest);

    mockMvc.perform(post("/users/auth/forgot")
        .content(objectMapper.writeValueAsString(forgotPasswordRequest))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Bad credentials"));
  }

  @Test
  void givenValidRequest_whenLogin_thenReturnResponse200() throws Exception {
    when(userAuthenticationService.login(userLoginRequest)).thenReturn(userLoginResponse);

    mockMvc.perform(post("/users/auth/login")
        .content(objectMapper.writeValueAsString(userLoginRequest))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenInvalidRequest_whenLogin_thenReturnResponse400() throws Exception {
    when(userAuthenticationService.login(userLoginRequest))
        .thenThrow(new IllegalStateException("Bad credentials"));

    mockMvc.perform(post("/users/auth/login")
        .content(objectMapper.writeValueAsString(userLoginRequest))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Bad credentials"));
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenUpdate_thenReturnResponse200() throws Exception {
    doNothing().when(userService).update(1L, userUpdateRequest);

    mockMvc.perform(put("/users/{id}", 1L)
        .contentType(APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(userUpdateRequest)))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void givenRequestWithExistingUsername_whenUpdate_thenReturnResponse405() throws Exception {
    doThrow(new UsernameAlreadyTakenException("Username already taken")).when(userService)
        .update(1L, userUpdateRequest);

    mockMvc.perform(put("/users/{id}", 1L)
        .contentType(APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(userUpdateRequest)))
        .andDo(print())
        .andExpect(status().isMethodNotAllowed())
        .andExpect(content().string("Username already taken"));
  }

  @Test
  @WithMockUser
  void givenRequestWithBadCredentials_whenUpdate_thenReturnResponse400() throws Exception {
    doThrow(new IllegalStateException("Bad credentials")).when(userService)
        .update(1L, userUpdateRequest);

    mockMvc.perform(put("/users/{id}", 1L)
        .contentType(APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(userUpdateRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Bad credentials"));
  }

  @Test
  void givenValidRequestUnauthorized_whenUpdate_thenReturnResponse401() throws Exception {
    doNothing().when(userService).update(1L, userUpdateRequest);

    mockMvc.perform(put("/users/{id}", 1L)
        .contentType(APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(userUpdateRequest)))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenUpdateUserRoleToAdmin_thenReturnResponse200() throws Exception {
    doNothing().when(userService).updateUserRoleToAdmin(1L);

    mockMvc.perform(put("/users/{id}/admin", 1L)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenUpdateUserRoleToAdmin_thenReturnResponse403() throws Exception {
    doNothing().when(userService).updateUserRoleToAdmin(1L);

    mockMvc.perform(put("/users/{id}/admin", 1L)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenUpdateUserRoleToAdmin_thenReturnResponse401() throws Exception {
    doNothing().when(userService).updateUserRoleToAdmin(1L);

    mockMvc.perform(put("/users/{id}/admin", 1L)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenUpdateUserRoleToUser_thenReturnResponse200() throws Exception {
    doNothing().when(userService).updateUserRoleToUser(1L);

    mockMvc.perform(put("/users/{id}/user", 1L)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenUpdateUserRoleToUser_thenReturnResponse403() throws Exception {
    doNothing().when(userService).updateUserRoleToUser(1L);

    mockMvc.perform(put("/users/{id}/user", 1L)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenUpdateUserRoleToUser_thenReturnResponse401() throws Exception {
    doNothing().when(userService).updateUserRoleToUser(1L);

    mockMvc.perform(put("/users/{id}/user", 1L)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenUpdateUserUserStatus_thenReturnResponse200() throws Exception {
    doNothing().when(userService).updateUserStatus(1L);

    mockMvc.perform(put("/users/status/{id}", 1L)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenUpdateUserStatus_thenReturnResponse403() throws Exception {
    doNothing().when(userService).updateUserStatus(1L);

    mockMvc.perform(put("/users/status/{id}", 1L)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenUpdateUserStatus_thenReturnResponse401() throws Exception {
    doNothing().when(userService).updateUserStatus(1L);

    mockMvc.perform(put("/users/status/{id}", 1L)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}