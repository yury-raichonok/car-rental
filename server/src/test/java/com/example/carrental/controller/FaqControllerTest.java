package com.example.carrental.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.carrental.controller.dto.faq.CreateFaqRequest;
import com.example.carrental.controller.dto.faq.UpdateFaqRequest;
import com.example.carrental.entity.faq.Faq;
import com.example.carrental.service.FaqService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
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
class FaqControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FaqService faqService;

//  @Test
//  void givenValidRequest_whenFindAllFaqHasData_thenReturnResponse200() throws Exception {
//    when(faqService.findAll()).thenReturn(Arrays
//        .asList(Faq.builder().id(1L).question("question").answer("answer").build(),
//            Faq.builder().id(2L).question("question1").answer("answer1").build()));
//
//    mockMvc.perform(get("/faqs"))
//        .andDo(print())
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//        .andExpect(jsonPath("$", hasSize(2)))
//        .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
//        .andExpect(jsonPath("$[*].question", containsInAnyOrder("question", "question1")));
//  }
//
//  @Test
//  void givenValidRequest_whenFindAllFaqHasNoContent_thenReturnResponse204() throws Exception {
//    mockMvc.perform(get("/faqs"))
//        .andDo(print())
//        .andExpect(status().isNoContent());
//  }
//
//  @Test
//  @WithMockUser(username = "user", authorities = {"ADMIN"})
//  void givenValidRequest_whenCreateFaqSuccessful_thenReturnResponse201() throws Exception {
//    var createFaqRequest = CreateFaqRequest.builder().question("question").answer("answer").build();
//
//    when(faqService.create(createFaqRequest)).thenReturn("Success");
//
//    mockMvc.perform(post("/faqs")
//        .content(objectMapper.writeValueAsString(createFaqRequest))
//        .contentType(MediaType.APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isCreated())
//        .andExpect(content().string(containsString("Success")));
//  }
//
//  @Test
//  @WithMockUser(username = "user", authorities = {"ADMIN"})
//  void givenInvalidRequest_whenCreateFaqFailed_thenReturnResponse400() throws Exception {
//    var createFaqRequest = CreateFaqRequest.builder().question("question").answer("answer").build();
//
//    when(faqService.create(createFaqRequest))
//        .thenThrow(new IllegalStateException("Bad request"));
//
//    mockMvc.perform(post("/faqs")
//        .content(objectMapper.writeValueAsString(createFaqRequest))
//        .contentType(MediaType.APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isBadRequest())
//        .andExpect(content().string(containsString("Bad request")));
//  }
//
//  @Test
//  void givenValidRequestUnauthorized_whenCreateFaqFailed_thenReturnResponse401() throws Exception {
//    var createFaqRequest = CreateFaqRequest.builder().question("question").answer("answer").build();
//
//    mockMvc.perform(post("/faqs")
//        .content(objectMapper.writeValueAsString(createFaqRequest))
//        .contentType(MediaType.APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isUnauthorized());
//  }
//
//  @Test
//  @WithMockUser(username = "user", authorities = {"ADMIN"})
//  void givenValidRequest_whenUpdateFaqSuccessful_thenReturnResponse200() throws Exception {
//    var updateFaqRequest = UpdateFaqRequest.builder().question("question").answer("answer")
//        .build();
//
//    when(faqService.update(1L, updateFaqRequest)).thenReturn("Success");
//
//    mockMvc.perform(put("/faqs/" + 1)
//        .content(objectMapper.writeValueAsString(updateFaqRequest))
//        .contentType(MediaType.APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isOk())
//        .andExpect(content().string(containsString("Success")));
//  }
//
//  @Test
//  @WithMockUser(username = "user", authorities = {"ADMIN"})
//  void givenInvalidRequest_whenUpdateFaqFailed_thenReturnResponse400() throws Exception {
//    var updateFaqRequest = UpdateFaqRequest.builder().question("question").answer("answer")
//        .build();
//
//    when(faqService.update(1L, updateFaqRequest))
//        .thenThrow(new IllegalStateException("Bad request"));
//
//    mockMvc.perform(put("/faqs/" + 1)
//        .content(objectMapper.writeValueAsString(updateFaqRequest))
//        .contentType(MediaType.APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isBadRequest())
//        .andExpect(content().string(containsString("Bad request")));
//  }
//
//  @Test
//  void givenValidRequestUnauthorized_whenUpdateFaqFailed_thenReturnResponse401() throws Exception {
//    var updateFaqRequest = UpdateFaqRequest.builder().question("question").answer("answer")
//        .build();
//
//    mockMvc.perform(put("/faqs/" + 1)
//        .content(objectMapper.writeValueAsString(updateFaqRequest))
//        .contentType(MediaType.APPLICATION_JSON_VALUE))
//        .andDo(print())
//        .andExpect(status().isUnauthorized());
//  }
}