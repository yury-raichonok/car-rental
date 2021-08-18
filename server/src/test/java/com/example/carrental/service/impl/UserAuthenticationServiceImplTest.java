package com.example.carrental.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.carrental.entity.user.User;
import com.example.carrental.entity.user.UserRole;
import com.example.carrental.service.UserSecurityService;
import java.security.Principal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
class UserAuthenticationServiceImplTest {

  @Autowired
  private UserAuthenticationServiceImpl userAuthenticationService;

  @MockBean
  private Principal principal;

  @MockBean
  private UserSecurityService userSecurityService;

  @Test
  void givenValidRequest_whenGetUserInfo_thenSuccess() {
    var role = UserRole.builder().id(1L).role("USER").roleDescription("User").build();
    var user = User.builder().id(1L).email("user@gmail.com").role(role).build();
    when(userSecurityService.loadUserByUsername(any())).thenReturn(user);

    var userInfoResponse = userAuthenticationService.getUserInfo(principal);

    assertThat(userInfoResponse).isNotNull();
    assertThat(userInfoResponse.getEmail()).isEqualTo(user.getEmail());
  }

  @Test
  void givenRequestUnauthorized_whenGetUserInfo_thenThrowIllegalStateException() {
    assertThrows(IllegalStateException.class, () -> userAuthenticationService.getUserInfo(null));
  }
}