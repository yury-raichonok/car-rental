package com.example.carrental.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.user.UserPhoneConfirmationRequest;
import com.example.carrental.controller.dto.user.UserSmsRequest;
import com.example.carrental.entity.user.User;
import com.example.carrental.entity.user.UserConfirmationToken;
import com.example.carrental.entity.user.UserPhone;
import com.example.carrental.repository.UserPhoneRepository;
import com.example.carrental.service.SmsSenderService;
import com.example.carrental.service.UserConfirmationTokenService;
import com.example.carrental.service.UserSecurityService;
import com.example.carrental.service.UserService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
class UserPhoneServiceImplTest {

  @Autowired
  private UserPhoneServiceImpl userPhoneService;

  @MockBean
  private SmsSenderService smsSenderService;

  @MockBean
  private UserConfirmationTokenService userConfirmationTokenService;

  @MockBean
  private UserPhoneRepository userPhoneRepository;

  @MockBean
  private UserSecurityService userSecurityService;

  @MockBean
  private UserService userService;

  @Test
  void givenValidRequest_whenFindById_thenSuccess() {
    var phone = UserPhone.builder().id(1L).phone("+375111234567").active(true).build();
    when(userPhoneRepository.findById(1L)).thenReturn(Optional.of(phone));

    var userPhone = userPhoneService.findById(1L);

    assertThat(userPhone).isNotNull();
    assertThat(userPhone.getId()).isEqualTo(phone.getId());
    assertThat(userPhone.getPhone()).isEqualTo(phone.getPhone());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindById_thenThrowIllegalStateException() {
    when(userPhoneRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> userPhoneService.findById(1L));
  }

  @Test
  void givenValidRequest_whenCreate_thenSuccess() {
    var userPhoneConfirmationRequest = UserPhoneConfirmationRequest.builder().token("1111")
        .phoneNumber("+375111234567").build();
    var token = UserConfirmationToken.builder().expirationTime(LocalDateTime.now().plusDays(1))
        .token("1111").build();
    when(userConfirmationTokenService.getUserConfirmationTokenByToken("1111")).thenReturn(token);
    doNothing().when(userConfirmationTokenService).updateUserConfirmationTokenConfirmedAt(any());
    when(userPhoneRepository.save(any(UserPhone.class))).thenReturn(new UserPhone());

    assertDoesNotThrow(() -> userPhoneService.create(userPhoneConfirmationRequest));
  }

  @Test
  void givenValidRequest_whenSendConfirmationSms_thenSuccess() {
    var userSmsRequest = UserSmsRequest.builder().phoneNumber("+375111234567").build();
    var phone = UserPhone.builder().id(1L).phone("+375111234567").active(true).build();
    var user = User.builder().id(1L).phones(Collections.singletonList(phone)).build();
    var userEmail = "email@gmail.com";
    when(userPhoneRepository.findByPhoneAndActiveTrue("+375111234567"))
        .thenReturn(Optional.empty());
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(userService.findUserByEmail(any())).thenReturn(user);
    doNothing().when(userConfirmationTokenService).createUserConfirmationToken(any());
    doNothing().when(smsSenderService).sendSms(any(), any());

    assertDoesNotThrow(() -> userPhoneService.sendConfirmationSms(userSmsRequest));
  }

  @Test
  void givenRequest_whenSendConfirmationSmsToExistingPhone_thenThrowEntityAlreadyExistsException() {
    var userSmsRequest = UserSmsRequest.builder().phoneNumber("+375111234567").build();
    var phone = UserPhone.builder().id(1L).phone("+375111234567").active(true).build();
    when(userPhoneRepository.findByPhoneAndActiveTrue("+375111234567"))
        .thenReturn(Optional.of(phone));

    assertThrows(EntityAlreadyExistsException.class,
        () -> userPhoneService.sendConfirmationSms(userSmsRequest));
  }

  @Test
  void givenValidRequest_whenUpdatePhoneStatus_thenSuccess() {
    var phone = UserPhone.builder().id(1L).phone("+375111234567").active(true).build();
    when(userPhoneRepository.findById(1L)).thenReturn(Optional.of(phone));
    when(userPhoneRepository.findByPhoneAndActiveTrue(phone.getPhone()))
        .thenReturn(Optional.empty());
    when(userPhoneRepository.save(any(UserPhone.class))).thenReturn(new UserPhone());

    assertDoesNotThrow(() -> userPhoneService.updatePhoneStatus(1L));
  }

  @Test
  void givenRequest_whenUpdatePhoneStatusToExistingPhone_thenThrowEntityAlreadyExistsException() {
    var phone = UserPhone.builder().id(1L).phone("+375111234567").active(false).build();
    when(userPhoneRepository.findById(1L)).thenReturn(Optional.of(phone));
    when(userPhoneRepository.findByPhoneAndActiveTrue(phone.getPhone()))
        .thenReturn(Optional.of(phone));
    when(userPhoneRepository.save(any(UserPhone.class))).thenReturn(new UserPhone());

    assertThrows(EntityAlreadyExistsException.class, () -> userPhoneService.updatePhoneStatus(1L));
  }
}