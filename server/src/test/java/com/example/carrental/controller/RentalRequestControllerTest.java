package com.example.carrental.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.carrental.controller.dto.rentaldetails.CreateRentalRequestRequest;
import com.example.carrental.controller.dto.rentaldetails.RentalAllRequestResponse;
import com.example.carrental.controller.dto.rentaldetails.RentalRequestRejectRequest;
import com.example.carrental.controller.dto.rentaldetails.RentalRequestResponse;
import com.example.carrental.controller.dto.user.UserDrivingLicenseConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserPassportConfirmationDataResponse;
import com.example.carrental.entity.rentaldetails.RentalRequestType;
import com.example.carrental.service.RentalRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class RentalRequestControllerTest {

  private static RentalAllRequestResponse rentalAllRequestResponse;
  private static RentalRequestResponse rentalRequestResponse;
  private static UserDrivingLicenseConfirmationDataResponse userDrivingLicenseConfirmationDataResponse;
  private static UserPassportConfirmationDataResponse userPassportConfirmationDataResponse;
  private static CreateRentalRequestRequest createRentalRequestRequest;
  private static RentalRequestRejectRequest rentalRequestRejectRequest;
  private static Pageable pageable;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RentalRequestService rentalRequestService;

  @BeforeAll
  public static void setup() {
    rentalAllRequestResponse = RentalAllRequestResponse.builder().id(1L).userEmail("test@gmail.com")
        .requestType(RentalRequestType.PASSPORT_CONFIRMATION_REQUEST).sentDate("date")
        .considered(true).build();
    rentalRequestResponse = RentalRequestResponse.builder().id(1L).userEmail("test@gmail.com")
        .requestType(RentalRequestType.PASSPORT_CONFIRMATION_REQUEST).sentDate("date")
        .considered(true).build();
    userDrivingLicenseConfirmationDataResponse = UserDrivingLicenseConfirmationDataResponse
        .builder().dateOfIssue("date").validityPeriod("date").organizationThatIssued("name")
        .build();
    userPassportConfirmationDataResponse = UserPassportConfirmationDataResponse.builder()
        .firstName("name").lastName("name").dateOfBirth("date").passportSeries("name")
        .passportNumber("number").dateOfIssue("date").validityPeriod("date")
        .organizationThatIssued("name").build();
    createRentalRequestRequest = CreateRentalRequestRequest.builder()
        .rentalRequestType(RentalRequestType.PASSPORT_CONFIRMATION_REQUEST).build();
    rentalRequestRejectRequest = RentalRequestRejectRequest.builder().comments("message").build();
    pageable = Pageable.ofSize(10).withPage(0);
  }

  @AfterAll
  public static void teardown() {
    rentalAllRequestResponse = null;
    rentalRequestResponse = null;
    userDrivingLicenseConfirmationDataResponse = null;
    userPassportConfirmationDataResponse = null;
    createRentalRequestRequest = null;
    rentalRequestRejectRequest = null;
    pageable = null;
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindAll_thenReturnResponse200() throws Exception {
    Page<RentalAllRequestResponse> page = new PageImpl<>(
        Collections.singletonList(rentalAllRequestResponse));

    when(rentalRequestService.findAll(pageable)).thenReturn(page);

    mockMvc.perform(get("/requests?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindAll_thenReturnResponse403()
      throws Exception {
    Page<RentalAllRequestResponse> page = new PageImpl<>(
        Collections.singletonList(rentalAllRequestResponse));

    when(rentalRequestService.findAll(pageable)).thenReturn(page);

    mockMvc.perform(get("/requests?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAll_thenReturnResponse401()
      throws Exception {
    Page<RentalAllRequestResponse> page = new PageImpl<>(
        Collections.singletonList(rentalAllRequestResponse));

    when(rentalRequestService.findAll(pageable)).thenReturn(page);

    mockMvc.perform(get("/requests?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindAllNew_thenReturnResponse200() throws Exception {
    Page<RentalRequestResponse> page = new PageImpl<>(
        Collections.singletonList(rentalRequestResponse));

    when(rentalRequestService.findAllNew(pageable)).thenReturn(page);

    mockMvc.perform(get("/requests/new?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindAllNew_thenReturnResponse403()
      throws Exception {
    Page<RentalRequestResponse> page = new PageImpl<>(
        Collections.singletonList(rentalRequestResponse));

    when(rentalRequestService.findAllNew(pageable)).thenReturn(page);

    mockMvc.perform(get("/requests/new?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindAllNew_thenReturnResponse401()
      throws Exception {
    Page<RentalRequestResponse> page = new PageImpl<>(
        Collections.singletonList(rentalRequestResponse));

    when(rentalRequestService.findAllNew(pageable)).thenReturn(page);

    mockMvc.perform(get("/requests/new?page={page}&size={size}", 0, 10)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindRequestDrivingLicenseData_thenReturnResponse200()
      throws Exception {
    when(rentalRequestService.findRequestDrivingLicenseData(1L))
        .thenReturn(userDrivingLicenseConfirmationDataResponse);

    mockMvc.perform(get("/requests/drivinglicenses/{id}", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindRequestDrivingLicenseData_thenReturnResponse403()
      throws Exception {
    when(rentalRequestService.findRequestDrivingLicenseData(1L))
        .thenReturn(userDrivingLicenseConfirmationDataResponse);

    mockMvc.perform(get("/requests/drivinglicenses/{id}", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindRequestDrivingLicenseData_thenReturnResponse401()
      throws Exception {
    when(rentalRequestService.findRequestDrivingLicenseData(1L))
        .thenReturn(userDrivingLicenseConfirmationDataResponse);

    mockMvc.perform(get("/requests/drivinglicenses/{id}", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindRequestPassportData_thenReturnResponse200() throws Exception {
    when(rentalRequestService.findRequestPassportData(1L))
        .thenReturn(userPassportConfirmationDataResponse);

    mockMvc.perform(get("/requests/passports/{id}", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindRequestPassportData_thenReturnResponse403()
      throws Exception {
    when(rentalRequestService.findRequestPassportData(1L))
        .thenReturn(userPassportConfirmationDataResponse);

    mockMvc.perform(get("/requests/passports/{id}", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindRequestPassportData_thenReturnResponse401()
      throws Exception {
    when(rentalRequestService.findRequestPassportData(1L))
        .thenReturn(userPassportConfirmationDataResponse);

    mockMvc.perform(get("/requests/passports/{id}", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindNewRequestsAmount_thenReturnResponse200() throws Exception {
    when(rentalRequestService.findNewRequestsAmount()).thenReturn(1);

    mockMvc.perform(get("/requests/new/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindNewRequestsAmount_thenReturnResponse403()
      throws Exception {
    when(rentalRequestService.findNewRequestsAmount()).thenReturn(1);

    mockMvc.perform(get("/requests/new/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindNewRequestsAmount_thenReturnResponse401()
      throws Exception {
    when(rentalRequestService.findNewRequestsAmount()).thenReturn(1);

    mockMvc.perform(get("/requests/new/amount")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenFindNewRequestsAmountPerDay_thenReturnResponse200() throws Exception {
    when(rentalRequestService.findNewRequestsAmountPerDay()).thenReturn(1);

    mockMvc.perform(get("/requests/new/amount/day")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenFindNewRequestsAmountPerDay_thenReturnResponse403()
      throws Exception {
    when(rentalRequestService.findNewRequestsAmountPerDay()).thenReturn(1);

    mockMvc.perform(get("/requests/new/amount/day")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenFindNewRequestsAmountPerDay_thenReturnResponse401()
      throws Exception {
    when(rentalRequestService.findNewRequestsAmountPerDay()).thenReturn(1);

    mockMvc.perform(get("/requests/new/amount/day")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER", "ADMIN"})
  void givenValidRequest_whenCreate_thenReturnResponse200() throws Exception {
    doNothing().when(rentalRequestService).create(createRentalRequestRequest);

    mockMvc.perform(post("/requests")
        .content(objectMapper.writeValueAsString(createRentalRequestRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void givenValidRequestUnauthorized_whenCreateFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(rentalRequestService).create(createRentalRequestRequest);

    mockMvc.perform(post("/requests")
        .content(objectMapper.writeValueAsString(createRentalRequestRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenApproveRequestSuccessful_thenReturnResponse200() throws Exception {
    doNothing().when(rentalRequestService).approveRequest(1L);

    mockMvc.perform(put("/requests/approve/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenApproveRequestFailed_thenReturnResponse403() throws Exception {
    doNothing().when(rentalRequestService).approveRequest(1L);

    mockMvc.perform(put("/requests/approve/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenApproveRequestFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(rentalRequestService).approveRequest(1L);

    mockMvc.perform(put("/requests/approve/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenRejectRequestSuccessful_thenReturnResponse200() throws Exception {
    doNothing().when(rentalRequestService).rejectRequest(1L, rentalRequestRejectRequest);

    mockMvc.perform(put("/requests/reject/{id}", 1)
        .content(objectMapper.writeValueAsString(rentalRequestRejectRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenRejectRequestFailed_thenReturnResponse403() throws Exception {
    doNothing().when(rentalRequestService).rejectRequest(1L, rentalRequestRejectRequest);

    mockMvc.perform(put("/requests/reject/{id}", 1)
        .content(objectMapper.writeValueAsString(rentalRequestRejectRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenRejectRequestFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(rentalRequestService).rejectRequest(1L, rentalRequestRejectRequest);

    mockMvc.perform(put("/requests/reject/{id}", 1)
        .content(objectMapper.writeValueAsString(rentalRequestRejectRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}