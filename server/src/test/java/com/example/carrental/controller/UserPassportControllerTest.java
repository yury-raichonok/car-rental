package com.example.carrental.controller;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.carrental.controller.dto.user.CreateOrUpdateUserPassportRequest;
import com.example.carrental.controller.dto.user.UserDocumentsDownloadRequest;
import com.example.carrental.controller.dto.user.UserPassportConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserPassportDataResponse;
import com.example.carrental.service.UserPassportService;
import com.example.carrental.service.exceptions.DrivingLicenseNotConfirmedException;
import com.example.carrental.service.exceptions.PassportNotConfirmedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserPassportControllerTest {

  private static UserPassportDataResponse userPassportDataResponse;
  private static UserPassportConfirmationDataResponse userPassportConfirmationDataResponse;
  private static CreateOrUpdateUserPassportRequest createOrUpdateUserPassportRequest;
  private static UserDocumentsDownloadRequest userDocumentsDownloadRequest;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserPassportService userPassportService;

  @BeforeAll
  public static void setup() {
    userPassportDataResponse = UserPassportDataResponse.builder().firstName("name")
        .lastName("name").dateOfBirth(LocalDate.now().minusDays(1)).passportNumber("series")
        .passportSeries("number").dateOfIssue(LocalDate.now().plusDays(1))
        .validityPeriod(LocalDate.now().plusDays(2)).organizationThatIssued("name").build();
    userPassportConfirmationDataResponse = UserPassportConfirmationDataResponse.builder()
        .firstName("name").lastName("name").dateOfBirth("date").passportNumber("series")
        .passportSeries("number").dateOfIssue("date").validityPeriod("date")
        .organizationThatIssued("name").build();
    createOrUpdateUserPassportRequest = CreateOrUpdateUserPassportRequest.builder()
        .firstName("name")
        .lastName("name").dateOfBirth(LocalDate.now().minusDays(1)).passportNumber("series")
        .passportSeries("number").dateOfIssue(LocalDate.now().minusDays(1))
        .validityPeriod(LocalDate.now().plusDays(2)).organizationThatIssued("name").build();
    userDocumentsDownloadRequest = UserDocumentsDownloadRequest.builder().directory("directory")
        .build();
  }

  @AfterAll
  public static void teardown() {
    userPassportDataResponse = null;
    userPassportConfirmationDataResponse = null;
    createOrUpdateUserPassportRequest = null;
    userDocumentsDownloadRequest = null;
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenGetUserPassportData_thenReturnResponse200() throws Exception {
    when(userPassportService.getUserPassportData()).thenReturn(userPassportDataResponse);

    mockMvc.perform(get("/passports/data")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenValidRequestUnauthorized_whenGetUserPassportData_thenReturnResponse401()
      throws Exception {
    when(userPassportService.getUserPassportData()).thenReturn(userPassportDataResponse);

    mockMvc.perform(get("/passports/data")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenFindPassportDataById_thenReturnResponse200() throws Exception {
    when(userPassportService.findPassportDataById(1L))
        .thenReturn(userPassportConfirmationDataResponse);

    mockMvc.perform(get("/passports/{id}", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenValidRequestUnauthorized_whenFindPassportDataById_thenReturnResponse401()
      throws Exception {
    when(userPassportService.findPassportDataById(1L))
        .thenReturn(userPassportConfirmationDataResponse);

    mockMvc.perform(get("/passports/{id}", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenCreateOrUpdate_thenReturnResponse200() throws Exception {
    doNothing().when(userPassportService).createOrUpdate(createOrUpdateUserPassportRequest);

    mockMvc.perform(post("/passports")
        .content(objectMapper.writeValueAsString(createOrUpdateUserPassportRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void givenValidRequestUnauthorized_whenCreateOrUpdateFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(userPassportService).createOrUpdate(createOrUpdateUserPassportRequest);

    mockMvc.perform(post("/passports")
        .content(objectMapper.writeValueAsString(createOrUpdateUserPassportRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenUpdatePassportStatus_thenReturnResponse200() throws Exception {
    doNothing().when(userPassportService).updatePassportStatus(1L);

    mockMvc.perform(put("/passports/{id}/status", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void givenRequestWithNoDocumentsConfirmed_whenUpdatePassportStatus_thenReturnResponse405()
      throws Exception {
    doThrow(new PassportNotConfirmedException("No data specified"))
        .when(userPassportService).updatePassportStatus(1L);

    mockMvc.perform(put("/passports/{id}/status", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isMethodNotAllowed())
        .andExpect(content().string("No data specified"));
  }

  @Test
  void givenValidRequestUnauthorized_whenUpdatePassportStatus_thenReturnResponse401()
      throws Exception {
    doNothing().when(userPassportService).updatePassportStatus(1L);

    mockMvc.perform(put("/passports/{id}/status", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenDownloadFiles_thenReturnResponse200() throws Exception {
    var response = new ByteArrayResource(new byte[]{});
    when(userPassportService.downloadFiles(userDocumentsDownloadRequest))
        .thenReturn(response);

    mockMvc.perform(put("/passports/download")
        .content(objectMapper.writeValueAsString(userDocumentsDownloadRequest))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenDownloadFiles_thenReturnResponse403() throws Exception {
    var response = new ByteArrayResource(new byte[]{});
    when(userPassportService.downloadFiles(userDocumentsDownloadRequest))
        .thenReturn(response);

    mockMvc.perform(put("/passports/download")
        .content(objectMapper.writeValueAsString(userDocumentsDownloadRequest))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenDownloadFiles_thenReturnResponse401() throws Exception {
    var response = new ByteArrayResource(new byte[]{});
    when(userPassportService.downloadFiles(userDocumentsDownloadRequest))
        .thenReturn(response);

    mockMvc.perform(put("/passports/download")
        .content(objectMapper.writeValueAsString(userDocumentsDownloadRequest))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenUploadFileSuccessful_thenReturnResponse200() throws Exception {
    var passportFile = new MockMultipartFile("passportFile", "dummy.csv",
        "multipart/form-data", "Some dataset...".getBytes());

    doNothing().when(userPassportService).uploadFile(passportFile);

    mockMvc.perform(multipart("/passports/upload").file(passportFile)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void givenValidRequestUnauthorized_whenUploadBrandImageFailed_thenReturnResponse401()
      throws Exception {
    var passportFile = new MockMultipartFile("passportFile", "dummy.csv",
        "multipart/form-data", "Some dataset...".getBytes());

    doNothing().when(userPassportService).uploadFile(passportFile);

    mockMvc.perform(multipart("/passports/upload").file(passportFile)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}