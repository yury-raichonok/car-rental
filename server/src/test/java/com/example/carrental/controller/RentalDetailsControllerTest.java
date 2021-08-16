package com.example.carrental.controller;

import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static com.example.carrental.constants.ApplicationConstants.LANGUAGE_COOKIE_NAME;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.carrental.controller.dto.rentaldetails.RentalDetailsContactInformationResponse;
import com.example.carrental.controller.dto.rentaldetails.RentalDetailsResponse;
import com.example.carrental.controller.dto.rentaldetails.RentalDetailsUpdateRequest;
import com.example.carrental.service.RentalDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
class RentalDetailsControllerTest {

  private static RentalDetailsContactInformationResponse rentalDetailsContactInformationResponse;
  private static RentalDetailsResponse rentalDetailsResponse;
  private static RentalDetailsUpdateRequest rentalDetailsUpdateRequest;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RentalDetailsService rentalDetailsService;

  @BeforeAll
  public static void setup() {
    rentalDetailsContactInformationResponse = RentalDetailsContactInformationResponse.builder()
        .email("test@gmail.com").phone("+375111234567").locationName("name").locationCoordinateX(5)
        .locationCoordinateY(5).zoom(5).build();
    rentalDetailsResponse = RentalDetailsResponse.builder().email("test@gmail.com")
        .phoneNumber("+375111234567").location("name").locationId(1L).build();
    rentalDetailsUpdateRequest = RentalDetailsUpdateRequest.builder()
        .phoneNumber("+375111234567").location(1L).build();
  }

  @AfterAll
  public static void teardown() {
    rentalDetailsContactInformationResponse = null;
    rentalDetailsResponse = null;
    rentalDetailsUpdateRequest = null;
  }

  @Test
  void givenValidRequest_whenGetContactInformation_thenReturnResponse200() throws Exception {
    when(rentalDetailsService.getContactInformation(ENGLISH))
        .thenReturn(rentalDetailsContactInformationResponse);

    mockMvc.perform(get("/details/contacts")
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenValidRequestAndDetailsNotSet_whenGetContactInformation_thenReturnResponse400()
      throws Exception {
    when(rentalDetailsService.getContactInformation(ENGLISH))
        .thenThrow(new IllegalStateException("Details not set"));

    mockMvc.perform(get("/details/contacts")
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Details not set"));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenGetRentalDetailsResponse_thenReturnResponse200() throws Exception {
    when(rentalDetailsService.getRentalDetailsResponse(ENGLISH)).thenReturn(rentalDetailsResponse);

    mockMvc.perform(get("/details")
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequestWithDetailsNotSet_whenGetRentalDetailsResponse_thenReturnResponse400()
      throws Exception {
    when(rentalDetailsService.getRentalDetailsResponse(ENGLISH))
        .thenThrow(new IllegalStateException("Details not set"));

    mockMvc.perform(get("/details").cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Details not set"));
  }

  @Test
  void givenValidRequestUnauthorized_whenGetRentalDetailsResponse_thenReturnResponse401()
      throws Exception {
    when(rentalDetailsService.getRentalDetailsResponse(ENGLISH)).thenReturn(rentalDetailsResponse);

    mockMvc.perform(get("/details")
        .cookie(new Cookie(LANGUAGE_COOKIE_NAME, ENGLISH)))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenCreateOrUpdate_thenReturnResponse200() throws Exception {
    doNothing().when(rentalDetailsService).createOrUpdate(rentalDetailsUpdateRequest);

    mockMvc.perform(put("/details")
        .content(objectMapper.writeValueAsString(rentalDetailsUpdateRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void givenValidRequestUnauthorized_whenCreateOrUpdateFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(rentalDetailsService).createOrUpdate(rentalDetailsUpdateRequest);

    mockMvc.perform(put("/details")
        .content(objectMapper.writeValueAsString(rentalDetailsUpdateRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}