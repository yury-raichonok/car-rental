package com.example.carrental.controller;

import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static com.example.carrental.constants.ApplicationConstants.LANGUAGE_COOKIE_NAME;
import static com.example.carrental.constants.ApplicationConstants.NO_CONTENT;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.carrental.controller.dto.faq.CreateFaqRequest;
import com.example.carrental.controller.dto.faq.FaqResponse;
import com.example.carrental.controller.dto.faq.FaqWithTranslationsResponse;
import com.example.carrental.controller.dto.faq.UpdateFaqRequest;
import com.example.carrental.service.FaqService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import javax.servlet.http.Cookie;
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
class FaqControllerTest {

  private static Pageable pageable;
  private static FaqResponse firstFaqResponse;
  private static FaqResponse secondFaqResponse;
  private static FaqWithTranslationsResponse faqWithTranslationsResponse;
  private static CreateFaqRequest createFaqRequest;
  private static UpdateFaqRequest updateFaqRequest;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FaqService faqService;

  @BeforeAll
  public static void setup() {
    pageable = Pageable.ofSize(10).withPage(0);
    firstFaqResponse = FaqResponse.builder().id(1L).question("question").answer("answer").build();
    secondFaqResponse = FaqResponse.builder().id(2L).question("question1").answer("answer").build();
    faqWithTranslationsResponse = FaqWithTranslationsResponse.builder().id(1L)
        .questionEn("question").questionRu("question").questionBe("question")
        .answerBe("answer").answerEn("answer").answerRu("answer").build();
    createFaqRequest = CreateFaqRequest.builder().questionEn("question")
        .questionRu("question").questionBe("question").answerBe("answer").answerEn("answer")
        .answerRu("answer").build();
    updateFaqRequest = UpdateFaqRequest.builder().questionEn("question")
        .questionRu("question").questionBe("question").answerBe("answer").answerEn("answer")
        .answerRu("answer").build();
  }

  @AfterAll
  public static void teardown() {
    pageable = null;
    firstFaqResponse = null;
    secondFaqResponse = null;
    faqWithTranslationsResponse = null;
    createFaqRequest = null;
    updateFaqRequest = null;

  }

  @Test
  void givenValidRequest_whenFindAll_thenReturnResponse200() throws Exception {
    var response = Arrays.asList(firstFaqResponse, secondFaqResponse);

    when(faqService.findAll(ENGLISH)).thenReturn(response);

    mockMvc.perform(get("/faqs")
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  void givenValidRequest_whenFindAllNoContent_thenReturnResponse204() throws Exception {
    when(faqService.findAll(ENGLISH)).thenThrow(new NoContentException(NO_CONTENT));

    mockMvc.perform(get("/faqs")
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isNoContent())
        .andExpect(content().string(NO_CONTENT));
  }

  @Test
  void givenValidRequest_whenFindAllPaged_thenReturnResponse200() throws Exception {
    Page<FaqWithTranslationsResponse> page = new PageImpl<>(
        Collections.singletonList(faqWithTranslationsResponse));

    when(faqService.findAllPaged(pageable)).thenReturn(page);

    mockMvc.perform(get("/faqs/paged?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenCreate_thenReturnResponse201() throws Exception {
    doNothing().when(faqService).create(createFaqRequest);

    mockMvc.perform(post("/faqs")
        .content(objectMapper.writeValueAsString(createFaqRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequestWithExistedBrandName_whenCreateFailed_thenReturnResponse406()
      throws Exception {
    doThrow(new EntityAlreadyExistsException("Entity with same question already exists"))
        .when(faqService).create(createFaqRequest);

    mockMvc.perform(post("/faqs")
        .content(objectMapper.writeValueAsString(createFaqRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string(containsString("Entity with same question already exists")));
  }

  @Test
  void givenValidRequestUnauthorized_whenCreateFailed_thenReturnResponse401()
      throws Exception {
    mockMvc.perform(post("/faqs")
        .content(objectMapper.writeValueAsString(createFaqRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenUpdateSuccessful_thenReturnResponse200() throws Exception {
    doNothing().when(faqService).update(1L, updateFaqRequest);

    mockMvc.perform(put("/faqs/{id}", 1)
        .content(objectMapper.writeValueAsString(updateFaqRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequest_whenUpdateFailed_thenReturnResponse400() throws Exception {
    doThrow(new IllegalStateException("Bad request")).when(faqService)
        .update(1L, updateFaqRequest);

    mockMvc.perform(put("/faqs/{id}", 1)
        .content(objectMapper.writeValueAsString(updateFaqRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Bad request")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenUpdateFailed_thenReturnResponse403() throws Exception {
    doNothing().when(faqService).update(1L, updateFaqRequest);

    mockMvc.perform(put("/faqs/{id}", 1)
        .content(objectMapper.writeValueAsString(updateFaqRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenUpdateFailed_thenReturnResponse401() throws Exception {
    doNothing().when(faqService).update(1L, updateFaqRequest);

    mockMvc.perform(put("/faqs/{id}", 1)
        .content(objectMapper.writeValueAsString(updateFaqRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenDeleteSuccessful_thenReturnResponse200() throws Exception {
    doNothing().when(faqService).delete(1L);

    mockMvc.perform(delete("/faqs/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void givenValidRequestUnauthorized_whenDeleteFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(faqService).delete(1L);

    mockMvc.perform(delete("/faqs/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}