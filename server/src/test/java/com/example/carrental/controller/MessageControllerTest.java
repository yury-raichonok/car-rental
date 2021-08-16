package com.example.carrental.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.carrental.controller.dto.message.CreateMessageRequest;
import com.example.carrental.controller.dto.message.MessageResponse;
import com.example.carrental.controller.dto.notification.NotificationResponse;
import com.example.carrental.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {

  private static Pageable pageable;
  private static MessageResponse messageResponse;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MessageService messageService;

  @BeforeAll
  public static void setup() {
    pageable = Pageable.ofSize(10).withPage(0);
    messageResponse = MessageResponse.builder().id(1L).name("name")
        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
        .readed(false).build();
  }

  @AfterAll
  public static void teardown() {
    pageable = null;
    messageResponse = null;
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindAll_thenReturnResponse200() throws Exception {
    Page<MessageResponse> page = new PageImpl<>(Collections.singletonList(messageResponse));

    when(messageService.findAll(pageable)).thenReturn(page);

    mockMvc.perform(get("/messages/all?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindAll_thenReturnResponse403()
      throws Exception {
    Page<MessageResponse> page = new PageImpl<>(Collections.singletonList(messageResponse));

    when(messageService.findAll(pageable)).thenReturn(page);

    mockMvc.perform(get("/messages/all?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAll_thenReturnResponse401()
      throws Exception {
    Page<MessageResponse> page = new PageImpl<>(Collections.singletonList(messageResponse));

    when(messageService.findAll(pageable)).thenReturn(page);

    mockMvc.perform(get("/messages/all?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindAllNewMessages_thenReturnResponse200() throws Exception {
    Page<MessageResponse> page = new PageImpl<>(Collections.singletonList(messageResponse));

    when(messageService.findAllNewMessages(pageable)).thenReturn(page);

    mockMvc.perform(get("/messages/new?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindAllNewMessages_thenReturnResponse403()
      throws Exception {
    Page<MessageResponse> page = new PageImpl<>(Collections.singletonList(messageResponse));

    when(messageService.findAllNewMessages(pageable)).thenReturn(page);

    mockMvc.perform(get("/messages/new?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAllNewMessages_thenReturnResponse401()
      throws Exception {
    Page<MessageResponse> page = new PageImpl<>(Collections.singletonList(messageResponse));

    when(messageService.findAllNewMessages(pageable)).thenReturn(page);

    mockMvc.perform(get("/messages/new?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindNewMessagesAmount_thenReturnResponse200() throws Exception {
    when(messageService.findNewMessagesAmount()).thenReturn(1);

    mockMvc.perform(get("/messages/new/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindNewMessagesAmount_thenReturnResponse403()
      throws Exception {
    when(messageService.findNewMessagesAmount()).thenReturn(1);

    mockMvc.perform(get("/messages/new/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindNewMessagesAmount_thenReturnResponse401()
      throws Exception {
    when(messageService.findNewMessagesAmount()).thenReturn(1);

    mockMvc.perform(get("/messages/new/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindNewMessagesAmountPerDay_thenReturnResponse200() throws Exception {
    when(messageService.findNewMessagesAmountPerDay()).thenReturn(1);

    mockMvc.perform(get("/messages/new/amount/day")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindNewMessagesAmountPerDay_thenReturnResponse403()
      throws Exception {
    when(messageService.findNewMessagesAmountPerDay()).thenReturn(1);

    mockMvc.perform(get("/messages/new/amount/day")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindNewMessagesAmountPerDay_thenReturnResponse401()
      throws Exception {
    when(messageService.findNewMessagesAmountPerDay()).thenReturn(1);

    mockMvc.perform(get("/messages/new/amount/day")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  void givenValidRequest_whenCreate_thenReturnResponse200() throws Exception {
    var createMessageRequest = CreateMessageRequest.builder().name("name").email("test@gmail.com")
        .phone("+375111234567").message("message").build();
    doNothing().when(messageService).create(createMessageRequest);

    mockMvc.perform(post("/messages")
        .content(objectMapper.writeValueAsString(createMessageRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenUpdateMessageAsReadSuccessful_thenReturnResponse200()
      throws Exception {
    doNothing().when(messageService).updateMessageAsRead(1L);

    mockMvc.perform(put("/messages/{id}", 1))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequest_whenUpdateMessageAsReadFailed_thenReturnResponse400()
      throws Exception {
    doThrow(new IllegalStateException("Bad request")).when(messageService)
        .updateMessageAsRead(1L);

    mockMvc.perform(put("/messages/{id}", 1))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Bad request")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenUpdateMessageAsReadFailed_thenReturnResponse403()
      throws Exception {
    doNothing().when(messageService).updateMessageAsRead(1L);

    mockMvc.perform(put("/messages/{id}", 1))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenUpdateMessageAsReadFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(messageService).updateMessageAsRead(1L);

    mockMvc.perform(put("/messages/{id}", 1))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}