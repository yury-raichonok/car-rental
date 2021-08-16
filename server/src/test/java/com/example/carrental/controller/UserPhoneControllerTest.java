package com.example.carrental.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.carrental.controller.dto.user.UserPhoneConfirmationRequest;
import com.example.carrental.controller.dto.user.UserSmsRequest;
import com.example.carrental.service.UserPhoneService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserPhoneControllerTest {

  private static UserPhoneConfirmationRequest userPhoneConfirmationRequest;
  private static UserSmsRequest userSmsRequest;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserPhoneService userPhoneService;

  @BeforeAll
  public static void setup() {
    userPhoneConfirmationRequest = UserPhoneConfirmationRequest.builder().token("1111")
        .phoneNumber("+375111234567").build();
    userSmsRequest = UserSmsRequest.builder().phoneNumber("+375111234567").build();
  }

  @AfterAll
  public static void teardown() {
    userPhoneConfirmationRequest = null;
    userSmsRequest = null;
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenCreate_thenReturnResponse200() throws Exception {
    doNothing().when(userPhoneService).create(userPhoneConfirmationRequest);

    mockMvc.perform(post("/users/phone")
        .content(objectMapper.writeValueAsString(userPhoneConfirmationRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void givenRequestWithExistingPhone_whenCreate_thenReturnResponse406() throws Exception {
    doThrow(new EntityAlreadyExistsException("Entity already exists")).when(userPhoneService)
        .create(userPhoneConfirmationRequest);

    mockMvc.perform(post("/users/phone")
        .content(objectMapper.writeValueAsString(userPhoneConfirmationRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string("Entity already exists"));
  }

  @Test
  void givenValidRequestUnauthorized_whenCreate_thenReturnResponse401() throws Exception {
    doNothing().when(userPhoneService).create(userPhoneConfirmationRequest);

    mockMvc.perform(post("/users/phone")
        .content(objectMapper.writeValueAsString(userPhoneConfirmationRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenSendConfirmationSms_thenReturnResponse200() throws Exception {
    doNothing().when(userPhoneService).sendConfirmationSms(userSmsRequest);

    mockMvc.perform(put("/users/phone")
        .content(objectMapper.writeValueAsString(userSmsRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void givenRequestWithExistingPhone_whenSendConfirmationSms_thenReturnResponse406()
      throws Exception {
    doThrow(new EntityAlreadyExistsException("Entity already exists")).when(userPhoneService)
        .sendConfirmationSms(userSmsRequest);

    mockMvc.perform(put("/users/phone")
        .content(objectMapper.writeValueAsString(userSmsRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string("Entity already exists"));
  }

  @Test
  void givenValidRequestUnauthorized_whenSendConfirmationSms_thenReturnResponse401()
      throws Exception {
    doNothing().when(userPhoneService).sendConfirmationSms(userSmsRequest);

    mockMvc.perform(put("/users/phone")
        .content(objectMapper.writeValueAsString(userSmsRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenUpdatePhoneStatus_thenReturnResponse200() throws Exception {
    doNothing().when(userPhoneService).updatePhoneStatus(1L);

    mockMvc.perform(put("/users/phone/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void givenRequestWithExistingPhone_whenUpdatePhoneStatus_thenReturnResponse406()
      throws Exception {
    doThrow(new EntityAlreadyExistsException("Entity already exists")).when(userPhoneService)
        .updatePhoneStatus(1L);

    mockMvc.perform(put("/users/phone/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string("Entity already exists"));
  }

  @Test
  void givenValidRequestUnauthorized_whenUpdatePhoneStatus_thenReturnResponse401()
      throws Exception {
    doNothing().when(userPhoneService).updatePhoneStatus(1L);

    mockMvc.perform(put("/users/phone/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}