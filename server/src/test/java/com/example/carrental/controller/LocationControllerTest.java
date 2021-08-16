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

import com.example.carrental.controller.dto.location.CreateLocationRequest;
import com.example.carrental.controller.dto.location.LocationNameResponse;
import com.example.carrental.controller.dto.location.LocationWithTranslationsResponse;
import com.example.carrental.service.LocationService;
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
class LocationControllerTest {

  private static Pageable pageable;
  private static LocationNameResponse firstLocationNameResponse;
  private static LocationNameResponse secondLocationNameResponse;
  private static LocationWithTranslationsResponse locationWithTranslationsResponse;
  private static CreateLocationRequest createLocationRequest;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LocationService locationService;

  @BeforeAll
  public static void setup() {
    pageable = Pageable.ofSize(10).withPage(0);
    firstLocationNameResponse = LocationNameResponse.builder().id(1L).name("name").build();
    secondLocationNameResponse = LocationNameResponse.builder().id(2L).name("name1").build();
    locationWithTranslationsResponse = LocationWithTranslationsResponse.builder().id(1L)
        .nameBe("name")
        .nameEn("name").nameRu("name").coordinateX(1).coordinateY(1).zoom(1).build();
    createLocationRequest = CreateLocationRequest.builder().nameBe("name").nameEn("name")
        .nameRu("name").coordinateX(1).coordinateY(1).zoom(1).build();
  }

  @AfterAll
  public static void teardown() {
    pageable = null;
    firstLocationNameResponse = null;
    secondLocationNameResponse = null;
    locationWithTranslationsResponse = null;
    createLocationRequest = null;
  }

  @Test
  void givenValidRequest_whenFindAllForSelect_thenReturnResponse200() throws Exception {
    var response = Arrays.asList(firstLocationNameResponse, secondLocationNameResponse);

    when(locationService.findAllForSelect(ENGLISH)).thenReturn(response);

    mockMvc.perform(get("/locations/select")
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
        .andExpect(jsonPath("$[*].name", containsInAnyOrder("name", "name1")));
  }

  @Test
  void givenValidRequest_whenFindAllForSelectNoContent_thenReturnResponse204() throws Exception {
    when(locationService.findAllForSelect(ENGLISH)).thenThrow(new NoContentException(NO_CONTENT));

    mockMvc.perform(get("/locations/select")
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isNoContent())
        .andExpect(content().string(NO_CONTENT));
  }

  @Test
  void givenValidRequest_whenFindAllPaged_thenReturnResponse200() throws Exception {
    Page<LocationWithTranslationsResponse> page = new PageImpl<>(
        Collections.singletonList(locationWithTranslationsResponse));

    when(locationService.findAllPaged(pageable)).thenReturn(page);

    mockMvc.perform(get("/locations/paged?page=0&size=10")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenCreate_thenReturnResponse200() throws Exception {
    doNothing().when(locationService).create(createLocationRequest);

    mockMvc.perform(post("/locations")
        .content(objectMapper.writeValueAsString(createLocationRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequestWithExistedBrandName_whenCreateFailed_thenReturnResponse406()
      throws Exception {
    doThrow(new EntityAlreadyExistsException("Entity with same name already exists"))
        .when(locationService).create(createLocationRequest);

    mockMvc.perform(post("/locations")
        .content(objectMapper.writeValueAsString(createLocationRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isNotAcceptable())
        .andExpect(content().string(containsString("Entity with same name already exists")));
  }

  @Test
  void givenValidRequestUnauthorized_whenCreateFailed_thenReturnResponse401()
      throws Exception {
    mockMvc.perform(post("/locations")
        .content(objectMapper.writeValueAsString(createLocationRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenUpdateSuccessful_thenReturnResponse200() throws Exception {
    doNothing().when(locationService).update(1L, createLocationRequest);

    mockMvc.perform(put("/locations/{id}", 1)
        .content(objectMapper.writeValueAsString(createLocationRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenInvalidRequest_whenUpdateFailed_thenReturnResponse400()
      throws Exception {
    doThrow(new IllegalStateException("Bad request")).when(locationService)
        .update(1L, createLocationRequest);

    mockMvc.perform(put("/locations/{id}", 1)
        .content(objectMapper.writeValueAsString(createLocationRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Bad request")));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenUpdateFailed_thenReturnResponse403()
      throws Exception {
    doNothing().when(locationService).update(1L, createLocationRequest);

    mockMvc.perform(put("/locations/{id}", 1)
        .content(objectMapper.writeValueAsString(createLocationRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenUpdateFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(locationService).update(1L, createLocationRequest);

    mockMvc.perform(put("/locations/{id}", 1)
        .content(objectMapper.writeValueAsString(createLocationRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}