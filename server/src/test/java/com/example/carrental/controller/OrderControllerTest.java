package com.example.carrental.controller;


import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.carrental.controller.dto.message.MessageResponse;
import com.example.carrental.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
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
class OrderControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderService orderService;

//  @Test
//  @WithMockUser(username = "user", authorities = {"ADMIN"})
//  void givenValidRequest_whenFindAll_thenReturnResponse200() throws Exception {
//    var pageable = Pageable.ofSize(10).withPage(0);
//    var responses = Arrays.asList(MessageResponse.builder().id(1L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build(), MessageResponse.builder().id(2L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build());
//    Page<MessageResponse> page = new PageImpl<>(responses);
//
//    when(messageService.findAll(pageable)).thenReturn(page);
//
//    mockMvc.perform(get("/messages/all?page=0&size=10")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
//  }
//
//  @Test
//  @WithMockUser(username = "user", authorities = {"USER"})
//  void givenValidRequestAsUser_whenFindAll_thenReturnResponse403()
//      throws Exception {
//    var pageable = Pageable.ofSize(10).withPage(0);
//    var responses = Arrays.asList(MessageResponse.builder().id(1L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build(), MessageResponse.builder().id(2L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build());
//    Page<MessageResponse> page = new PageImpl<>(responses);
//
//    when(messageService.findAll(pageable)).thenReturn(page);
//
//    mockMvc.perform(get("/messages/all?page=0&size=10")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isForbidden());
//  }
//
//  @Test
//  void givenValidRequestUnauthorized_whenFindAll_thenReturnResponse401()
//      throws Exception {
//    var pageable = Pageable.ofSize(10).withPage(0);
//    var responses = Arrays.asList(MessageResponse.builder().id(1L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build(), MessageResponse.builder().id(2L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build());
//    Page<MessageResponse> page = new PageImpl<>(responses);
//
//    when(messageService.findAll(pageable)).thenReturn(page);
//
//    mockMvc.perform(get("/messages/all?page=0&size=10")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isUnauthorized());
//  }
//
//  @Test
//  @WithMockUser(username = "user", authorities = {"ADMIN"})
//  void givenValidRequest_whenFindAll_thenReturnResponse200() throws Exception {
//    var pageable = Pageable.ofSize(10).withPage(0);
//    var responses = Arrays.asList(MessageResponse.builder().id(1L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build(), MessageResponse.builder().id(2L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build());
//    Page<MessageResponse> page = new PageImpl<>(responses);
//
//    when(messageService.findAll(pageable)).thenReturn(page);
//
//    mockMvc.perform(get("/messages/all?page=0&size=10")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
//  }
//
//  @Test
//  @WithMockUser(username = "user", authorities = {"USER"})
//  void givenValidRequestAsUser_whenFindAll_thenReturnResponse403()
//      throws Exception {
//    var pageable = Pageable.ofSize(10).withPage(0);
//    var responses = Arrays.asList(MessageResponse.builder().id(1L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build(), MessageResponse.builder().id(2L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build());
//    Page<MessageResponse> page = new PageImpl<>(responses);
//
//    when(messageService.findAll(pageable)).thenReturn(page);
//
//    mockMvc.perform(get("/messages/all?page=0&size=10")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isForbidden());
//  }
//
//  @Test
//  void givenValidRequestUnauthorized_whenFindAll_thenReturnResponse401()
//      throws Exception {
//    var pageable = Pageable.ofSize(10).withPage(0);
//    var responses = Arrays.asList(MessageResponse.builder().id(1L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build(), MessageResponse.builder().id(2L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build());
//    Page<MessageResponse> page = new PageImpl<>(responses);
//
//    when(messageService.findAll(pageable)).thenReturn(page);
//
//    mockMvc.perform(get("/messages/all?page=0&size=10")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isUnauthorized());
//  }
//
//  @Test
//  @WithMockUser(username = "user", authorities = {"ADMIN"})
//  void givenValidRequest_whenFindAll_thenReturnResponse200() throws Exception {
//    var pageable = Pageable.ofSize(10).withPage(0);
//    var responses = Arrays.asList(MessageResponse.builder().id(1L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build(), MessageResponse.builder().id(2L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build());
//    Page<MessageResponse> page = new PageImpl<>(responses);
//
//    when(messageService.findAll(pageable)).thenReturn(page);
//
//    mockMvc.perform(get("/messages/all?page=0&size=10")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
//  }
//
//  @Test
//  @WithMockUser(username = "user", authorities = {"USER"})
//  void givenValidRequestAsUser_whenFindAll_thenReturnResponse403()
//      throws Exception {
//    var pageable = Pageable.ofSize(10).withPage(0);
//    var responses = Arrays.asList(MessageResponse.builder().id(1L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build(), MessageResponse.builder().id(2L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build());
//    Page<MessageResponse> page = new PageImpl<>(responses);
//
//    when(messageService.findAll(pageable)).thenReturn(page);
//
//    mockMvc.perform(get("/messages/all?page=0&size=10")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isForbidden());
//  }
//
//  @Test
//  void givenValidRequestUnauthorized_whenFindAll_thenReturnResponse401()
//      throws Exception {
//    var pageable = Pageable.ofSize(10).withPage(0);
//    var responses = Arrays.asList(MessageResponse.builder().id(1L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build(), MessageResponse.builder().id(2L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build());
//    Page<MessageResponse> page = new PageImpl<>(responses);
//
//    when(messageService.findAll(pageable)).thenReturn(page);
//
//    mockMvc.perform(get("/messages/all?page=0&size=10")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isUnauthorized());
//  }
//
//  @Test
//  @WithMockUser(username = "user", authorities = {"USER"})
//  void givenValidRequest_whenFindAll_thenReturnResponse200() throws Exception {
//    var pageable = Pageable.ofSize(10).withPage(0);
//    var responses = Arrays.asList(MessageResponse.builder().id(1L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build(), MessageResponse.builder().id(2L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build());
//    Page<MessageResponse> page = new PageImpl<>(responses);
//
//    when(messageService.findAll(pageable)).thenReturn(page);
//
//    mockMvc.perform(get("/messages/all?page=0&size=10")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
//  }
//
//  @Test
//  void givenValidRequestUnauthorized_whenFindAll_thenReturnResponse401()
//      throws Exception {
//    var pageable = Pageable.ofSize(10).withPage(0);
//    var responses = Arrays.asList(MessageResponse.builder().id(1L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build(), MessageResponse.builder().id(2L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build());
//    Page<MessageResponse> page = new PageImpl<>(responses);
//
//    when(messageService.findAll(pageable)).thenReturn(page);
//
//    mockMvc.perform(get("/messages/all?page=0&size=10")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isUnauthorized());
//  }
//
//  @Test
//  @WithMockUser(username = "user", authorities = {"USER"})
//  void givenValidRequest_whenFindAll_thenReturnResponse200() throws Exception {
//    var pageable = Pageable.ofSize(10).withPage(0);
//    var responses = Arrays.asList(MessageResponse.builder().id(1L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build(), MessageResponse.builder().id(2L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build());
//    Page<MessageResponse> page = new PageImpl<>(responses);
//
//    when(messageService.findAll(pageable)).thenReturn(page);
//
//    mockMvc.perform(get("/messages/all?page=0&size=10")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
//  }
//
//  @Test
//  void givenValidRequestUnauthorized_whenFindAll_thenReturnResponse401()
//      throws Exception {
//    var pageable = Pageable.ofSize(10).withPage(0);
//    var responses = Arrays.asList(MessageResponse.builder().id(1L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build(), MessageResponse.builder().id(2L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build());
//    Page<MessageResponse> page = new PageImpl<>(responses);
//
//    when(messageService.findAll(pageable)).thenReturn(page);
//
//    mockMvc.perform(get("/messages/all?page=0&size=10")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isUnauthorized());
//  }
//
//  @Test
//  @WithMockUser(username = "user", authorities = {"ADMIN"})
//  void givenValidRequest_whenFindNewMessagesAmount_thenReturnResponse200() throws Exception {
//    when(messageService.findNewMessagesAmount()).thenReturn(1);
//
//    mockMvc.perform(get("/messages/new/amount")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
//  }
//
//  @Test
//  @WithMockUser(username = "user", authorities = {"USER"})
//  void givenValidRequestAsUser_whenFindNewMessagesAmount_thenReturnResponse403()
//      throws Exception {
//    when(messageService.findNewMessagesAmount()).thenReturn(1);
//
//    mockMvc.perform(get("/messages/new/amount")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isForbidden());
//  }
//
//  @Test
//  void givenValidRequestUnauthorized_whenFindNewMessagesAmount_thenReturnResponse401()
//      throws Exception {
//    when(messageService.findNewMessagesAmount()).thenReturn(1);
//
//    mockMvc.perform(get("/messages/new/amount")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isUnauthorized());
//  }
//
//  @Test
//  @WithMockUser(username = "user", authorities = {"ADMIN"})
//  void givenValidRequest_whenFindNewMessagesAmount_thenReturnResponse200() throws Exception {
//    when(messageService.findNewMessagesAmount()).thenReturn(1);
//
//    mockMvc.perform(get("/messages/new/amount")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
//  }
//
//  @Test
//  @WithMockUser(username = "user", authorities = {"USER"})
//  void givenValidRequestAsUser_whenFindNewMessagesAmount_thenReturnResponse403()
//      throws Exception {
//    when(messageService.findNewMessagesAmount()).thenReturn(1);
//
//    mockMvc.perform(get("/messages/new/amount")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isForbidden());
//  }
//
//  @Test
//  void givenValidRequestUnauthorized_whenFindNewMessagesAmount_thenReturnResponse401()
//      throws Exception {
//    when(messageService.findNewMessagesAmount()).thenReturn(1);
//
//    mockMvc.perform(get("/messages/new/amount")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isUnauthorized());
//  }
//
//  @Test
//  @WithMockUser(username = "user", authorities = {"USER"})
//  void givenValidRequest_whenFindNewMessagesAmount_thenReturnResponse200() throws Exception {
//    when(messageService.findNewMessagesAmount()).thenReturn(1);
//
//    mockMvc.perform(get("/messages/new/amount")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
//  }
//
//  @Test
//  void givenValidRequestUnauthorized_whenFindNewMessagesAmount_thenReturnResponse401()
//      throws Exception {
//    when(messageService.findNewMessagesAmount()).thenReturn(1);
//
//    mockMvc.perform(get("/messages/new/amount")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isUnauthorized());
//  }
//
//  @Test
//  @WithMockUser(username = "user", authorities = {"ADMIN"})
//  void givenValidRequest_whenFindAll_thenReturnResponse200() throws Exception {
//    var pageable = Pageable.ofSize(10).withPage(0);
//    var responses = Arrays.asList(MessageResponse.builder().id(1L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build(), MessageResponse.builder().id(2L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build());
//    Page<MessageResponse> page = new PageImpl<>(responses);
//
//    when(messageService.findAll(pageable)).thenReturn(page);
//
//    mockMvc.perform(get("/messages/all?page=0&size=10")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
//  }
//
//  @Test
//  void givenValidRequestUnauthorized_whenFindAll_thenReturnResponse401()
//      throws Exception {
//    var pageable = Pageable.ofSize(10).withPage(0);
//    var responses = Arrays.asList(MessageResponse.builder().id(1L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build(), MessageResponse.builder().id(2L).name("name")
//        .email("test@gmail.com").phone("+375121234567").message("message").sentDate("date")
//        .readed(false).build());
//    Page<MessageResponse> page = new PageImpl<>(responses);
//
//    when(messageService.findAll(pageable)).thenReturn(page);
//
//    mockMvc.perform(get("/messages/all?page=0&size=10")
//        .contentType(APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isUnauthorized());
//  }
}