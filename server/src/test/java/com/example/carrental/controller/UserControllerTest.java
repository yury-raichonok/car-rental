package com.example.carrental.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void getAllUsers() throws Exception {
    this.mockMvc.perform(get("/users"))
        .andDo(print())
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  void getUserProfile() {
  }

  @Test
  void createUser() {
  }

  @Test
  void login() {
  }

  @Test
  void getUserInfo() {
  }

  @Test
  void forgotPassword() {
  }

  @Test
  void changePassword() {
  }

  @Test
  void confirmEmail() {
  }

  @Test
  void sendEmailConfirmationMessage() {
  }

  @Test
  void updateUser() {
  }
}