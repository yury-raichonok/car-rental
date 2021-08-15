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

  @Test
  void givenValidRequest_whenFindAll_thenReturnResponse200() throws Exception {
    var responses = Arrays.asList(CarBrandResponse.builder().id(1L).name("name")
        .imageLink("link").build(), CarBrandResponse.builder().id(2L).name("name1")
        .imageLink("link1").build());
    when(carBrandService.findAll()).thenReturn(responses);

    mockMvc.perform(get("/brands/all"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
        .andExpect(jsonPath("$[*].name", containsInAnyOrder("name", "name1")));
  }

  @Test
  void givenValidRequest_whenFindAllNoContent_thenReturnResponse204() throws Exception {
    when(carBrandService.findAll()).thenThrow(new NoContentException(NO_CONTENT));

    mockMvc.perform(get("/brands/all"))
        .andDo(print())
        .andExpect(status().isNoContent())
        .andExpect(content().string(NO_CONTENT));
  }

  @Test
  void givenValidRequest_whenFindAllBrandsWithRentalOffers_thenReturnResponse200()
      throws Exception {
    var responses = Arrays.asList(CarBrandResponse.builder().id(1L).name("name")
        .imageLink("link").build(), CarBrandResponse.builder().id(2L).name("name1")
        .imageLink("link1").build());
    when(carBrandService.findAllBrandsWithRentalOffers()).thenReturn(responses);

    mockMvc.perform(get("/brands"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
        .andExpect(jsonPath("$[*].name", containsInAnyOrder("name", "name1")));
  }

  @Test
  void givenValidRequest_whenFindAllBrandsWithRentalOffersNoContent_thenReturnResponse204()
      throws Exception {
    when(carBrandService.findAllBrandsWithRentalOffers())
        .thenThrow(new NoContentException(NO_CONTENT));

    mockMvc.perform(get("/brands"))
        .andDo(print())
        .andExpect(status().isNoContent())
        .andExpect(content().string(NO_CONTENT));
  }

  @Test
  void givenValidRequest_whenFindAllPaged_thenReturnResponse200() throws Exception {
    var pageable = Pageable.ofSize(10).withPage(0);
    var responses = Arrays.asList(CarBrandResponse.builder().id(1L).name("name")
        .imageLink("link").build(), CarBrandResponse.builder().id(2L).name("name1")
        .imageLink("link1").build());
    Page<CarBrandResponse> page = new PageImpl<>(responses);

    when(carBrandService.findAllPaged(pageable)).thenReturn(page);

    mockMvc.perform(get("/brands/all/paged?page=0&size=10")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenCreate_thenReturnResponse201() throws Exception {
    var createCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();
    doNothing().when(carBrandService).create(createCarBrandRequest);

    mockMvc.perform(post("/brands")
        .content(objectMapper.writeValueAsString(createCarBrandRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequestWithExistedBrandName_whenCreateFailed_thenReturnResponse406()
      throws Exception {
    var createCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();
    doThrow(new EntityAlreadyExistsException("Entity with same name already exists"))
        .when(carBrandService).create(createCarBrandRequest);

    mockMvc.perform(post("/brands")
        .content(objectMapper.writeValueAsString(createCarBrandRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string(containsString("Entity with same name already exists")));
  }

  @Test
  void givenValidRequestUnauthorized_whenCreateFailed_thenReturnResponse401()
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
    var brandFile = new MockMultipartFile("brandFile", "dummy.csv",
        "multipart/form-data", "Some dataset...".getBytes());

    doNothing().when(carBrandService).uploadBrandImage(1L, brandFile);

    mockMvc.perform(multipart("/brands/1/upload/image").file(brandFile)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequest_whenUploadBrandImageFailed_thenReturnResponse400() throws Exception {
    var brandFile = new MockMultipartFile("brandFile", "dummy.csv",
        "multipart/form-data", "Some dataset...".getBytes());

    doThrow(new IllegalStateException("Bad request")).when(carBrandService)
        .uploadBrandImage(1L, brandFile);

    mockMvc.perform(multipart("/brands/1/upload/image").file(brandFile)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Bad request")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenUploadBrandImageFailed_thenReturnResponse403() throws Exception {
    var brandFile = new MockMultipartFile("brandFile", "dummy.csv",
        "multipart/form-data", "Some dataset...".getBytes());

    doNothing().when(carBrandService).uploadBrandImage(1L, brandFile);

    mockMvc.perform(multipart("/brands/1/upload/image").file(brandFile)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenUploadBrandImageFailed_thenReturnResponse401()
      throws Exception {
    var brandFile = new MockMultipartFile("brandFile", "dummy.csv",
        "multipart/form-data", "Some dataset...".getBytes());

    doNothing().when(carBrandService).uploadBrandImage(1L, brandFile);

    mockMvc.perform(multipart("/brands/1/upload/image").file(brandFile)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenUpdateSuccessful_thenReturnResponse200() throws Exception {
    var updateCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();

    doNothing().when(carBrandService).update(1L, updateCarBrandRequest);

    mockMvc.perform(put("/brands/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarBrandRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequestWithExistingName_whenUpdateFailed_thenReturnResponse406()
      throws Exception {
    var updateCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();

    doThrow(new EntityAlreadyExistsException("Entity with same name already exists"))
        .when(carBrandService).update(1L, updateCarBrandRequest);

    mockMvc.perform(put("/brands/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarBrandRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string(containsString("Entity with same name already exists")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequest_whenUpdateFailed_thenReturnResponse400()
      throws Exception {
    var updateCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();

    doThrow(new IllegalStateException("Bad request")).when(carBrandService)
        .update(1L, updateCarBrandRequest);

    mockMvc.perform(put("/brands/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarBrandRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Bad request")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenUpdateFailed_thenReturnResponse403()
      throws Exception {
    var updateCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();

    doNothing().when(carBrandService).update(1L, updateCarBrandRequest);

    mockMvc.perform(put("/brands/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarBrandRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenUpdateFailed_thenReturnResponse401()
      throws Exception {
    var updateCarBrandRequest = CreateCarBrandRequest.builder().name("name").build();

    doNothing().when(carBrandService).update(1L, updateCarBrandRequest);

    mockMvc.perform(put("/brands/{id}", 1)
        .content(objectMapper.writeValueAsString(updateCarBrandRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}