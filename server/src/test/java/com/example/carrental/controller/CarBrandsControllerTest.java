package com.example.carrental.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.carrental.controller.dto.car.CarBrandResponse;
import com.example.carrental.controller.dto.car.CreateCarBrandRequest;
import com.example.carrental.service.CarBrandService;
import com.example.carrental.service.exceptions.EntityAlreadyExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CarBrandsControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CarBrandService carBrandService;

//  @Test
//  void givenValidRequest_whenGetAllBrandsHasData_thenReturnResponse200() throws Exception {
//    when(carBrandService.getAllBrands()).thenReturn(Arrays
//        .asList(CarBrandResponse.builder().id(1L).name("name").imageLink("link").build(),
//            CarBrandResponse.builder().id(2L).name("name1").imageLink("link1").build()));
//
//    mockMvc.perform(get("/brands/all"))
//        .andDo(print())
//        .andExpect(status().isOk())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//        .andExpect(jsonPath("$", hasSize(2)))
//        .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
//        .andExpect(jsonPath("$[*].name", containsInAnyOrder("name", "name1")));
//  }

  @Test
  void givenValidRequest_whenFindAllBrandsHasNoContent_thenReturnResponse204() throws Exception {
    mockMvc.perform(get("/brands/all"))
        .andDo(print())
        .andExpect(status().isNoContent());
  }

  @Test
  void givenValidRequest_whenFindAllBrandsWithRentalOffersHasData_thenReturnResponse200()
      throws Exception {
    when(carBrandService.findAllBrandsWithRentalOffers()).thenReturn(Arrays
        .asList(CarBrandResponse.builder().id(1L).name("name").imageLink("link").build(),
            CarBrandResponse.builder().id(2L).name("name1").imageLink("link1").build()));

    mockMvc.perform(get("/brands"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
        .andExpect(jsonPath("$[*].name", containsInAnyOrder("name", "name1")));
  }

  @Test
  void givenValidRequest_whenFindAllBrandsWithRentalOffersHasNoContent_thenReturnResponse204()
      throws Exception {
    mockMvc.perform(get("/brands"))
        .andDo(print())
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenCreateCarBrandSuccessful_thenReturnResponse201() throws Exception {
    var createCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();

    when(carBrandService.create(createCarBrandRequest)).thenReturn("Success");

    mockMvc.perform(post("/brands")
        .content(objectMapper.writeValueAsString(createCarBrandRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().string(containsString("Success")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequestWithExistedName_whenCreateCarBrandFailed_thenReturnResponse403()
      throws Exception {
    var createCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();

    when(carBrandService.create(createCarBrandRequest))
        .thenThrow(new EntityAlreadyExistsException("Entity with same name already exists"));

    mockMvc.perform(post("/brands")
        .content(objectMapper.writeValueAsString(createCarBrandRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(content().string(containsString("Entity with same name already exists")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequest_whenCreateCarBrandFailed_thenReturnResponse400()
      throws Exception {
    var createCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();

    when(carBrandService.create(createCarBrandRequest))
        .thenThrow(new IllegalStateException("Bad request"));

    mockMvc.perform(post("/brands")
        .content(objectMapper.writeValueAsString(createCarBrandRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Bad request")));
  }

  @Test
  void givenValidRequestUnauthorized_whenCreateCarBrandFailed_thenReturnResponse401()
      throws Exception {
    var createCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();

    mockMvc.perform(post("/brands")
        .content(objectMapper.writeValueAsString(createCarBrandRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenUploadBrandImageSuccessful_thenReturnResponse200() throws Exception {
    MockMultipartFile brandFile = new MockMultipartFile("brandFile", "dummy.csv",
        "multipart/form-data", "Some dataset...".getBytes());

    when(carBrandService.uploadBrandImage(1L, brandFile)).thenReturn("Success");

    mockMvc.perform(multipart("/brands/1/upload/image").file(brandFile)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().string(containsString("Success")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequest_whenUploadBrandImageFailed_thenReturnResponse400() throws Exception {
    MockMultipartFile brandFile = new MockMultipartFile("brandFile", "dummy.csv",
        "multipart/form-data", "Some dataset...".getBytes());

    when(carBrandService.uploadBrandImage(1L, brandFile))
        .thenThrow(new IllegalStateException("Bad request"));

    mockMvc.perform(multipart("/brands/1/upload/image").file(brandFile)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().string(containsString("Bad request")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenUploadBrandImageFailed_thenReturnResponse403() throws Exception {
    MockMultipartFile brandFile = new MockMultipartFile("brandFile", "dummy.csv",
        "multipart/form-data", "Some dataset...".getBytes());

    when(carBrandService.uploadBrandImage(1L, brandFile)).thenReturn("Success");

    mockMvc.perform(multipart("/brands/1/upload/image").file(brandFile)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenUploadBrandImageFailed_thenReturnResponse401()
      throws Exception {
    MockMultipartFile brandFile = new MockMultipartFile("brandFile", "dummy.csv",
        "multipart/form-data", "Some dataset...".getBytes());

    when(carBrandService.uploadBrandImage(1L, brandFile)).thenReturn("Success");

    mockMvc.perform(multipart("/brands/1/upload/image").file(brandFile)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }


  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenUpdateCarBrandSuccessful_thenReturnResponse200() throws Exception {
    var updateCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();

    when(carBrandService.update(1L, updateCarBrandRequest)).thenReturn("Success");

    mockMvc.perform(put("/brands/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarBrandRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Success")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequestWithExistingName_whenUpdateCarBrandFailed_thenReturnResponse403()
      throws Exception {
    var updateCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();

    when(carBrandService.update(1L, updateCarBrandRequest))
        .thenThrow(new EntityAlreadyExistsException("Entity with same name already exists"));

    mockMvc.perform(put("/brands/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarBrandRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(content().string(containsString("Entity with same name already exists")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequest_whenUpdateCarBrandFailed_thenReturnResponse400()
      throws Exception {
    var updateCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();

    when(carBrandService.update(1L, updateCarBrandRequest))
        .thenThrow(new IllegalStateException("Bad request"));

    mockMvc.perform(put("/brands/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarBrandRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Bad request")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenUpdateCarBrandFailed_thenReturnResponse403() throws Exception {
    var updateCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();

    when(carBrandService.update(1L, updateCarBrandRequest)).thenReturn("Success");

    mockMvc.perform(put("/brands/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarBrandRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenUpdateCarBrandFailed_thenReturnResponse401() throws Exception {
    var updateCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();

    when(carBrandService.update(1L, updateCarBrandRequest)).thenReturn("Success");

    mockMvc.perform(put("/brands/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarBrandRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}