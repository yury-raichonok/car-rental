package com.example.carrental.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.carrental.entity.user.UserConfirmationToken;
import com.example.carrental.repository.UserConfirmationTokenRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
class UserConfirmationTokenServiceImplTest {

  @Autowired
  private UserConfirmationTokenServiceImpl userConfirmationTokenService;

  @MockBean
  private UserConfirmationTokenRepository userConfirmationTokenRepository;

  @Test
  void givenValidRequest_whenCreateUserConfirmationToken_thenSuccess() {
    var token = UserConfirmationToken.builder().token("token").build();
    when(userConfirmationTokenRepository.save(any())).thenReturn(new UserConfirmationToken());

    assertDoesNotThrow(() -> userConfirmationTokenService.createUserConfirmationToken(token));
  }

  @Test
  void givenValidRequest_whenGetUserConfirmationTokenByToken_thenSuccess() {
    var token = UserConfirmationToken.builder().id(1L).token("token").build();
    when(userConfirmationTokenRepository.findByToken("token")).thenReturn(Optional.of(token));

    var userConfirmationToken = userConfirmationTokenService.getUserConfirmationTokenByToken("token");

    assertThat(userConfirmationToken).isNotNull();
    assertThat(userConfirmationToken.getId()).isEqualTo(token.getId());
    assertThat(userConfirmationToken.getToken()).isEqualTo(token.getToken());
  }

  @Test
  void givenRequestWithNotExistingId_whenGetUserConfirmationTokenByToken_thenThrowIllegalStateException() {
    when(userConfirmationTokenRepository.findByToken("token")).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> userConfirmationTokenService.getUserConfirmationTokenByToken("token"));
  }

  @Test
  void givenValidRequest_whenUpdateUserConfirmationTokenConfirmedAt_thenSuccess() {
    var token = UserConfirmationToken.builder().id(1L).token("token").build();
    when(userConfirmationTokenRepository.findByToken("token")).thenReturn(Optional.of(token));
    when(userConfirmationTokenRepository.save(any())).thenReturn(new UserConfirmationToken());

    assertDoesNotThrow(() -> userConfirmationTokenService.updateUserConfirmationTokenConfirmedAt("token"));
  }
}