package com.example.carrental.controller;

import static com.example.carrental.constants.ApplicationConstants.NO_CONTENT;
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

import com.example.carrental.controller.dto.user.CreateOrUpdateUserDrivingLicenseRequest;
import com.example.carrental.controller.dto.user.UserDocumentsDownloadRequest;
import com.example.carrental.controller.dto.user.UserDrivingLicenseConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserDrivingLicenseDataResponse;
import com.example.carrental.service.UserDrivingLicenseService;
import com.example.carrental.service.exceptions.DrivingLicenseNotConfirmedException;
import com.example.carrental.service.exceptions.NoContentException;
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
class UserDrivingLicenseControllerTest {

  private static UserDrivingLicenseConfirmationDataResponse userDrivingLicenseConfirmationDataResponse;
  private static UserDrivingLicenseDataResponse userDrivingLicenseDataResponse;
  private static CreateOrUpdateUserDrivingLicenseRequest createOrUpdateUserDrivingLicenseRequest;
  private static UserDocumentsDownloadRequest userDocumentsDownloadRequest;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserDrivingLicenseService userDrivingLicenseService;

  @BeforeAll
  public static void setup() {
    userDrivingLicenseConfirmationDataResponse = UserDrivingLicenseConfirmationDataResponse
        .builder().dateOfIssue("date").validityPeriod("date")
        .organizationThatIssued("organizationThatIssued").build();
    userDrivingLicenseDataResponse = UserDrivingLicenseDataResponse.builder().dateOfIssue(
        LocalDate.now().minusDays(1)).validityPeriod(LocalDate.now().plusDays(1))
        .organizationThatIssued("organizationThatIssued").build();
    createOrUpdateUserDrivingLicenseRequest = CreateOrUpdateUserDrivingLicenseRequest.builder()
        .dateOfIssue(LocalDate.now().minusDays(1)).validityPeriod(LocalDate.now().plusDays(1))
        .organizationThatIssued("organizationThatIssued").build();
    userDocumentsDownloadRequest = UserDocumentsDownloadRequest.builder().directory("directory")
        .build();
  }

  @AfterAll
  public static void teardown() {
    userDrivingLicenseConfirmationDataResponse = null;
    userDrivingLicenseDataResponse = null;
    createOrUpdateUserDrivingLicenseRequest = null;
    userDocumentsDownloadRequest = null;
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenFindDrivingLicenseById_thenReturnResponse200() throws Exception {
    when(userDrivingLicenseService.findDrivingLicenseById(1L))
        .thenReturn(userDrivingLicenseConfirmationDataResponse);

    mockMvc.perform(get("/drivinglicenses/{id}", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  void givenValidRequestUnauthorized_whenFindDrivingLicenseById_thenReturnResponse401()
      throws Exception {
    when(userDrivingLicenseService.findDrivingLicenseById(1L))
        .thenReturn(userDrivingLicenseConfirmationDataResponse);

    mockMvc.perform(get("/drivinglicenses/{id}", 1)
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenFindUserDrivingLicenseData_thenReturnResponse200() throws Exception {
    when(userDrivingLicenseService.findUserDrivingLicenseData())
        .thenReturn(userDrivingLicenseDataResponse);

    mockMvc.perform(get("/drivinglicenses/data")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  }

  @Test
  @WithMockUser
  void givenRequestWhenNoContent_whenFindUserDrivingLicenseData_thenReturnResponse204()
      throws Exception {
    when(userDrivingLicenseService.findUserDrivingLicenseData())
        .thenThrow(new NoContentException(NO_CONTENT));

    mockMvc.perform(get("/drivinglicenses/data")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isNoContent())
        .andExpect(content().string(NO_CONTENT));
  }

  @Test
  void givenValidRequestUnauthorized_whenFindUserDrivingLicenseData_thenReturnResponse401()
      throws Exception {
    when(userDrivingLicenseService.findUserDrivingLicenseData())
        .thenReturn(userDrivingLicenseDataResponse);

    mockMvc.perform(get("/drivinglicenses/data")
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenCreateOrUpdate_thenReturnResponse200() throws Exception {
    doNothing().when(userDrivingLicenseService)
        .createOrUpdate(createOrUpdateUserDrivingLicenseRequest);

    mockMvc.perform(post("/drivinglicenses")
        .content(objectMapper.writeValueAsString(createOrUpdateUserDrivingLicenseRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void givenValidRequestUnauthorized_whenCreateOrUpdateFailed_thenReturnResponse401()
      throws Exception {
    doNothing().when(userDrivingLicenseService)
        .createOrUpdate(createOrUpdateUserDrivingLicenseRequest);

    mockMvc.perform(post("/drivinglicenses")
        .content(objectMapper.writeValueAsString(createOrUpdateUserDrivingLicenseRequest))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenUpdateDrivingLicenseStatus_thenReturnResponse200() throws Exception {
    doNothing().when(userDrivingLicenseService).updateDrivingLicenseStatus(1L);

    mockMvc.perform(put("/drivinglicenses/{id}/status", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void givenRequestWithNoDocumentsSpecified_whenUpdateDrivingLicenseStatus_thenReturnResponse405()
      throws Exception {
    doThrow(new DrivingLicenseNotConfirmedException("No data specified"))
        .when(userDrivingLicenseService).updateDrivingLicenseStatus(1L);

    mockMvc.perform(put("/drivinglicenses/{id}/status", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isMethodNotAllowed())
        .andExpect(content().string("No data specified"));
  }

  @Test
  void givenValidRequestUnauthorized_whenUpdateDrivingLicenseStatus_thenReturnResponse401()
      throws Exception {
    doNothing().when(userDrivingLicenseService).updateDrivingLicenseStatus(1L);

    mockMvc.perform(put("/drivinglicenses/{id}/status", 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser
  void givenValidRequest_whenUploadFileSuccessful_thenReturnResponse200() throws Exception {
    var drivingLicenseFile = new MockMultipartFile("drivingLicenseFile", "dummy.csv",
        "multipart/form-data", "Some dataset...".getBytes());

    doNothing().when(userDrivingLicenseService).uploadFile(drivingLicenseFile);

    mockMvc.perform(multipart("/drivinglicenses/upload").file(drivingLicenseFile)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void givenValidRequestUnauthorized_whenUploadFileFailed_thenReturnResponse401()
      throws Exception {
    var drivingLicenseFile = new MockMultipartFile("drivingLicenseFile", "dummy.csv",
        "multipart/form-data", "Some dataset...".getBytes());

    doNothing().when(userDrivingLicenseService).uploadFile(drivingLicenseFile);

    mockMvc.perform(multipart("/drivinglicenses/upload").file(drivingLicenseFile)
        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"ADMIN"})
  void givenValidRequest_whenDownloadFiles_thenReturnResponse200() throws Exception {
    var response = new ByteArrayResource(new byte[]{});
    when(userDrivingLicenseService.downloadFiles(userDocumentsDownloadRequest))
        .thenReturn(response);

    mockMvc.perform(put("/drivinglicenses/download")
        .content(objectMapper.writeValueAsString(userDocumentsDownloadRequest))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", authorities = {"USER"})
  void givenValidRequestAsUser_whenDownloadFiles_thenReturnResponse403() throws Exception {
    var response = new ByteArrayResource(new byte[]{});
    when(userDrivingLicenseService.downloadFiles(userDocumentsDownloadRequest))
        .thenReturn(response);

    mockMvc.perform(put("/drivinglicenses/download")
        .content(objectMapper.writeValueAsString(userDocumentsDownloadRequest))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void givenValidRequestUnauthorized_whenDownloadFiles_thenReturnResponse401() throws Exception {
    var response = new ByteArrayResource(new byte[]{});
    when(userDrivingLicenseService.downloadFiles(userDocumentsDownloadRequest))
        .thenReturn(response);

    mockMvc.perform(put("/drivinglicenses/download")
        .content(objectMapper.writeValueAsString(userDocumentsDownloadRequest))
        .contentType(APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }
}