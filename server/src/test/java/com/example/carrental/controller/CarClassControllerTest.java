package com.example.carrental.controller;

import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static com.example.carrental.constants.ApplicationConstants.LANGUAGE_COOKIE_NAME;
import static com.example.carrental.constants.ApplicationConstants.NO_CONTENT;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.carrental.controller.dto.car.CarClassNameResponse;
import com.example.carrental.controller.dto.car.CarClassNameWithTranslationsResponse;
import com.example.carrental.controller.dto.car.CreateCarClassRequest;
import com.example.carrental.service.CarClassService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CarClassControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CarClassService carClassService;

  @Test
  void givenValidRequest_whenFindAll_thenReturnResponse200() throws Exception {
    var response = Arrays.asList(CarClassNameResponse.builder().id(1).name("name").build(),
        CarClassNameResponse.builder().id(2).name("name1").build());
    when(carClassService.findAll(ENGLISH)).thenReturn(response);

    mockMvc.perform(get("/carclasses").cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
        .andExpect(jsonPath("$[*].name", containsInAnyOrder("name", "name1")));
  }

  @Test
  void givenValidRequest_whenFindAllNoContent_thenReturnResponse204() throws Exception {
    when(carClassService.findAll(ENGLISH)).thenThrow(new NoContentException(NO_CONTENT));

    mockMvc.perform(get("/carclasses").cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isNoContent())
        .andExpect(content().string(NO_CONTENT));
  }

  @Test
  void givenValidRequest_findAllPaged_thenReturnResponse200() throws Exception {
    Pageable pageable = Pageable.ofSize(10).withPage(0);
    var responses = Arrays.asList(CarClassNameWithTranslationsResponse.builder().build(),
        CarClassNameWithTranslationsResponse.builder().build());
    var page = new PageImpl<>(responses);

    when(carClassService.findAllPaged(pageable)).thenReturn(page);

    mockMvc.perform(get("/carclasses/paged?page=0&size=10")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenCreate_thenReturnResponse200() throws Exception {
    var createCarClassRequest = CreateCarClassRequest.builder().nameRu("name").nameEn("name")
        .nameBe("name").build();
    doNothing().when(carClassService).create(createCarClassRequest);

    mockMvc.perform(post("/carclasses")
        .content(objectMapper.writeValueAsString(createCarClassRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequestWithExistedClassName_whenCreateFailed_thenReturnResponse406()
      throws Exception {
    var createCarClassRequest = CreateCarClassRequest.builder().nameRu("name").nameEn("name")
        .nameBe("name").build();
    doThrow(new EntityAlreadyExistsException("Entity with same name already exists"))
        .when(carClassService).create(createCarClassRequest);

    mockMvc.perform(post("/carclasses")
        .content(objectMapper.writeValueAsString(createCarClassRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string(containsString("Entity with same name already exists")));
  }

  @Test
  void givenValidRequestUnauthorized_whenCreateFailed_thenReturnResponse401()
      throws Exception {
    var createCarClassRequest = CreateCarClassRequest.builder().nameRu("name").nameEn("name")
        .nameBe("name").build();

    mockMvc.perform(post("/carclasses")
        .content(objectMapper.writeValueAsString(createCarClassRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenUpdateSuccessful_thenReturnResponse200() throws Exception {
    var updateCarClassRequest = CreateCarClassRequest.builder().nameRu("name").nameEn("name")
        .nameBe("name").build();

    doNothing().when(carClassService).update(1L, updateCarClassRequest);

    mockMvc.perform(put("/carclasses/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarClassRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequestWithExistingName_whenUpdateFailed_thenReturnResponse406()
      throws Exception {
    var updateCarClassRequest = CreateCarClassRequest.builder().nameRu("name").nameEn("name")
        .nameBe("name").build();

    doThrow(new EntityAlreadyExistsException("Entity with same name already exists"))
        .when(carClassService).update(1L, updateCarClassRequest);

    mockMvc.perform(put("/carclasses/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarClassRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string(containsString("Entity with same name already exists")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequest_whenUpdateFailed_thenReturnResponse400()
      throws Exception {
    var updateCarClassRequest = CreateCarClassRequest.builder().nameRu("name").nameEn("name")
        .nameBe("name").build();

    doThrow(new IllegalStateException("Bad request")).when(carClassService)
        .update(1L, updateCarClassRequest);

    mockMvc.perform(put("/carclasses/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarClassRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Bad request")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenUpdateCarBrandFailed_thenReturnResponse403()
      throws Exception {
    var updateCarClassRequest = CreateCarClassRequest.builder().nameRu("name").nameEn("name")
        .nameBe("name").build();

    doNothing().when(carClassService).update(1L, updateCarClassRequest);

    mockMvc.perform(put("/carclasses/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarClassRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenUpdateCarBrandFailed_thenReturnResponse401()
      throws Exception {
    var updateCarClassRequest = CreateCarClassRequest.builder().nameRu("name").nameEn("name")
        .nameBe("name").build();

    doNothing().when(carClassService).update(1L, updateCarClassRequest);

    mockMvc.perform(put("/carclasses/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarClassRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}