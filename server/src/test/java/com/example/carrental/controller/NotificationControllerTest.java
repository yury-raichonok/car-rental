package com.example.carrental.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.carrental.controller.dto.notification.NotificationResponse;
import com.example.carrental.service.NotificationService;
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
class NotificationControllerTest {

  private static Pageable pageable;
  private static NotificationResponse notificationResponse;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private NotificationService notificationService;

  @BeforeAll
  public static void setup() {
    pageable = Pageable.ofSize(10).withPage(0);
    notificationResponse = NotificationResponse.builder().id(1L).message("message")
        .notificationType("type").sentDate("date").build();
  }

  @AfterAll
  public static void teardown() {
    pageable = null;
    notificationResponse = null;
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindAllNew_thenReturnResponse200() throws Exception {
    Page<NotificationResponse> page = new PageImpl<>(
        Collections.singletonList(notificationResponse));

    when(notificationService.findAllNew(pageable)).thenReturn(page);

    mockMvc.perform(get("/notifications?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAllNew_thenReturnResponse401()
      throws Exception {
    Page<NotificationResponse> page = new PageImpl<>(
        Collections.singletonList(notificationResponse));

    when(notificationService.findAllNew(pageable)).thenReturn(page);

    mockMvc.perform(get("/notifications?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindAllNotificationsHistory_thenReturnResponse200() throws Exception {
    Page<NotificationResponse> page = new PageImpl<>(
        Collections.singletonList(notificationResponse));

    when(notificationService.findAllNotificationsHistory(pageable)).thenReturn(page);

    mockMvc.perform(get("/notifications/history?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAllNotificationsHistory_thenReturnResponse401()
      throws Exception {
    Page<NotificationResponse> page = new PageImpl<>(
        Collections.singletonList(notificationResponse));

    when(notificationService.findAllNotificationsHistory(pageable)).thenReturn(page);

    mockMvc.perform(get("/notifications/history?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindNewMessagesAmount_thenReturnResponse200() throws Exception {
    when(notificationService.findNewUserNotificationsAmount()).thenReturn(1);

    mockMvc.perform(get("/notifications/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenValidRequestUnauthorized_whenFindNewMessagesAmount_thenReturnResponse401()
      throws Exception {
    when(notificationService.findNewUserNotificationsAmount()).thenReturn(1);

    mockMvc.perform(get("/notifications/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenUpdateNotificationAsReadSuccessful_thenReturnResponse200()
      throws Exception {
    doNothing().when(notificationService).updateNotificationAsRead(1L);

    mockMvc.perform(put("/notifications/{id}", 1))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequest_whenUpdateNotificationAsReadFailed_thenReturnResponse400()
      throws Exception {
    doThrow(new IllegalStateException("Bad request")).when(notificationService)
        .updateNotificationAsRead(1L);

    mockMvc.perform(put("/notifications/{id}", 1))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Bad request")));
  }

  @Test
  void givenValidRequestUnauthorized_whenUpdateMessageAsReadFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(notificationService).updateNotificationAsRead(1L);

    mockMvc.perform(put("/notifications/{id}", 1))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenDeleteSuccessful_thenReturnResponse200() throws Exception {
    doNothing().when(notificationService).delete(1L);

    mockMvc.perform(delete("/notifications/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void givenValidRequestUnauthorized_whenDeleteFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(notificationService).delete(1L);

    mockMvc.perform(delete("/notifications/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}