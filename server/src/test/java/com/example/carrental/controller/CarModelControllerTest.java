package com.example.carrental.controller;

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

import com.example.carrental.controller.dto.car.CarModelBrandNameResponse;
import com.example.carrental.controller.dto.car.CarModelResponse;
import com.example.carrental.controller.dto.car.CreateCarModelRequest;
import com.example.carrental.controller.dto.car.UpdateCarModelRequest;
import com.example.carrental.service.CarModelService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.example.carrental.service.exceptions.NoContentException;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CarModelControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CarModelService carModelService;

  @Test
  void givenValidRequest_whenFindAll_thenReturnResponse200() throws Exception {
    var responses = Arrays
        .asList(CarModelResponse.builder().id(1L).name("name").brandId(1L).build(),
            CarModelResponse.builder().id(2L).name("name1").brandId(1L).build());
    when(carModelService.findAll()).thenReturn(responses);

    mockMvc.perform(get("/models"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
        .andExpect(jsonPath("$[*].name", containsInAnyOrder("name", "name1")));
  }

  @Test
  void givenValidRequest_whenFindAllNoContent_thenReturnResponse204() throws Exception {
    when(carModelService.findAll()).thenThrow(new NoContentException(NO_CONTENT));

    mockMvc.perform(get("/models"))
        .andDo(print())
        .andExpect(status().isNoContent())
        .andExpect(content().string(NO_CONTENT));
  }

  @Test
  void givenValidRequest_whenFindAllModelsWithBrandName_thenReturnResponse200() throws Exception {
    var pageable = Pageable.ofSize(10).withPage(0);
    var responses = Arrays
        .asList(CarModelBrandNameResponse.builder().id(1L).name("name").brand("name").build(),
            CarModelBrandNameResponse.builder().id(2L).name("name").brand("name").build());
    Page<CarModelBrandNameResponse> page = new PageImpl<>(responses);

    when(carModelService.findAllModelsWithBrandName(pageable)).thenReturn(page);

    mockMvc.perform(get("/models/list")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void givenValidRequest_whenFindModelsByBrandId_thenReturnResponse200() throws Exception {
    var responses = Arrays
        .asList(CarModelResponse.builder().id(1L).name("name").brandId(1L).build(),
            CarModelResponse.builder().id(2L).name("name1").brandId(2L).build());
    when(carModelService.findModelsByBrandId(1L)).thenReturn(responses);

    mockMvc.perform(get("/models/brand/id/{id}", 1L))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
        .andExpect(jsonPath("$[*].name", containsInAnyOrder("name", "name1")));
  }

  @Test
  void givenValidRequest_whenFindModelsByBrandIdNoContent_thenReturnResponse204() throws Exception {
    when(carModelService.findModelsByBrandId(1L)).thenThrow(new NoContentException(NO_CONTENT));

    mockMvc.perform(get("/models/brand/id/{id}", 1L))
        .andDo(print())
        .andExpect(status().isNoContent())
        .andExpect(content().string(NO_CONTENT));
  }

  @Test
  void givenValidRequest_whenFindModelsByBrandName_thenReturnResponse200() throws Exception {
    var responses = Arrays
        .asList(CarModelResponse.builder().id(1L).name("name").brandId(1L).build(),
            CarModelResponse.builder().id(2L).name("name1").brandId(2L).build());
    when(carModelService.findModelsByBrandName("name")).thenReturn(responses);

    mockMvc.perform(get("/models/brand/{name}", "name"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
        .andExpect(jsonPath("$[*].name", containsInAnyOrder("name", "name1")));
  }

  @Test
  void givenValidRequest_whenFindModelsByBrandNameNoContent_thenReturnResponse204()
      throws Exception {
    when(carModelService.findModelsByBrandName("name"))
        .thenThrow(new NoContentException(NO_CONTENT));

    mockMvc.perform(get("/models/brand/{name}", "name"))
        .andDo(print())
        .andExpect(status().isNoContent())
        .andExpect(content().string(NO_CONTENT));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenCreate_thenReturnResponse200() throws Exception {
    var createCarModelRequest = CreateCarModelRequest.builder().brandId(1L).name("name").build();
    doNothing().when(carModelService).create(createCarModelRequest);

    mockMvc.perform(post("/models")
        .content(objectMapper.writeValueAsString(createCarModelRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequestWithExistedBrandName_whenCreateFailed_thenReturnResponse406()
      throws Exception {
    var createCarModelRequest = CreateCarModelRequest.builder().brandId(1L).name("name").build();
    doThrow(new EntityAlreadyExistsException("Entity with same name already exists"))
        .when(carModelService).create(createCarModelRequest);

    mockMvc.perform(post("/models")
        .content(objectMapper.writeValueAsString(createCarModelRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string(containsString("Entity with same name already exists")));
  }

  @Test
  void givenValidRequestUnauthorized_whenCreateFailed_thenReturnResponse401()
      throws Exception {
    var createCarModelRequest = CreateCarModelRequest.builder().brandId(1L).name("name").build();

    mockMvc.perform(post("/models")
        .content(objectMapper.writeValueAsString(createCarModelRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenUpdateSuccessful_thenReturnResponse200() throws Exception {
    var updateCarModelRequest = UpdateCarModelRequest.builder().brand("name").name("name").build();

    doNothing().when(carModelService).update(1L, updateCarModelRequest);

    mockMvc.perform(put("/models/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarModelRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequestWithExistingName_whenUpdateFailed_thenReturnResponse406()
      throws Exception {
    var updateCarModelRequest = UpdateCarModelRequest.builder().brand("name").name("name").build();

    doThrow(new EntityAlreadyExistsException("Entity with same name already exists"))
        .when(carModelService).update(1L, updateCarModelRequest);

    mockMvc.perform(put("/models/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarModelRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string(containsString("Entity with same name already exists")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequest_whenUpdateFailed_thenReturnResponse400()
      throws Exception {
    var updateCarModelRequest = UpdateCarModelRequest.builder().brand("name").name("name").build();

    doThrow(new IllegalStateException("Bad request")).when(carModelService)
        .update(1L, updateCarModelRequest);

    mockMvc.perform(put("/models/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarModelRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Bad request")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenUpdateFailed_thenReturnResponse403()
      throws Exception {
    var updateCarModelRequest = UpdateCarModelRequest.builder().brand("name").name("name").build();

    doNothing().when(carModelService).update(1L, updateCarModelRequest);

    mockMvc.perform(put("/models/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarModelRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenUpdateFailed_thenReturnResponse401()
      throws Exception {
    var updateCarModelRequest = UpdateCarModelRequest.builder().brand("name").name("name").build();

    doNothing().when(carModelService).update(1L, updateCarModelRequest);

    mockMvc.perform(put("/models/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarModelRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}