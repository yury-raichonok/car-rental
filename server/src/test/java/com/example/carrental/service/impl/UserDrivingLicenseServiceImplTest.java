package com.example.carrental.service.impl;

import static com.example.carrental.entity.user.UserDocumentStatus.CONFIRMED;
import static com.example.carrental.entity.user.UserDocumentStatus.NOT_CONFIRMED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.user.CreateOrUpdateUserDrivingLicenseRequest;
import com.example.carrental.entity.user.User;
import com.example.carrental.entity.user.UserDrivingLicense;
import com.example.carrental.repository.UserDrivingLicenseRepository;
import com.example.carrental.service.FileStoreService;
import com.example.carrental.service.UserSecurityService;
import com.example.carrental.service.UserService;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
class UserDrivingLicenseServiceImplTest {

  private UserDrivingLicense userDrivingLicense;

  @Autowired
  private UserDrivingLicenseServiceImpl userDrivingLicenseService;

  @MockBean
  private FileStoreService fileStoreService;

  @MockBean
  private UserDrivingLicenseRepository userDrivingLicenseRepository;

  @MockBean
  private UserSecurityService userSecurityService;

  @MockBean
  private UserService userService;

  @BeforeEach
  public void setup() {
    var dateOfIssue = LocalDate.now();
    var validityPeriod = LocalDate.now().plusDays(1L);
    var user = User.builder().id(1L).drivingLicense(userDrivingLicense).build();
    userDrivingLicense = UserDrivingLicense.builder().status(NOT_CONFIRMED).dateOfIssue(dateOfIssue)
        .user(user).validityPeriod(validityPeriod).organizationThatIssued("name")
        .documentsFileLink("link").build();
  }

  @Test
  void givenValidRequest_whenFindDrivingLicenseById_thenSuccess() {
    when(userDrivingLicenseRepository.findById(1L)).thenReturn(Optional.of(userDrivingLicense));

    var userDrivingLicenseConfirmationDataResponse = userDrivingLicenseService
        .findDrivingLicenseById(1L);

    assertThat(userDrivingLicenseConfirmationDataResponse).isNotNull();
    assertThat(userDrivingLicenseConfirmationDataResponse.getOrganizationThatIssued())
        .isEqualTo(userDrivingLicense.getOrganizationThatIssued());
  }

  @Test
  void givenValidRequest_whenFindUserDrivingLicenseData_thenSuccess() {
    var user = User.builder().id(1L).drivingLicense(userDrivingLicense).build();
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(userService.findUserByEmail(any())).thenReturn(user);

    var userDrivingLicenseDataResponse = userDrivingLicenseService.findUserDrivingLicenseData();

    assertThat(userDrivingLicenseDataResponse).isNotNull();
    assertThat(userDrivingLicenseDataResponse.getOrganizationThatIssued())
        .isEqualTo(userDrivingLicense.getOrganizationThatIssued());
  }

  @Test
  void givenValidRequest_whenFindById_thenSuccess() {
    when(userDrivingLicenseRepository.findById(1L)).thenReturn(Optional.of(userDrivingLicense));

    var drivingLicense = userDrivingLicenseService.findById(1L);

    assertThat(drivingLicense).isNotNull();
    assertThat(drivingLicense.getId()).isEqualTo(userDrivingLicense.getId());
    assertThat(drivingLicense.getOrganizationThatIssued())
        .isEqualTo(userDrivingLicense.getOrganizationThatIssued());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindById_thenThrowIllegalStateException() {
    when(userDrivingLicenseRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> userDrivingLicenseService.findById(1L));
  }

  @Test
  void givenValidRequest_whenCreateOrUpdate_thenSuccess() {
    var createOrUpdateUserDrivingLicenseRequest = CreateOrUpdateUserDrivingLicenseRequest.builder()
        .dateOfIssue(LocalDate.now().minusDays(1)).validityPeriod(LocalDate.now().plusDays(1))
        .organizationThatIssued("organizationThatIssued").build();
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(userService.findUserByEmail(any())).thenReturn(userDrivingLicense.getUser());
    when(userDrivingLicenseRepository.save(any())).thenReturn(new UserDrivingLicense());

    assertDoesNotThrow(
        () -> userDrivingLicenseService.createOrUpdate(createOrUpdateUserDrivingLicenseRequest));
  }

  @Test
  void givenValidRequest_whenUpdate_thenSuccess() {
    when(userDrivingLicenseRepository.findById(1L)).thenReturn(Optional.of(userDrivingLicense));
    when(userDrivingLicenseRepository.save(any())).thenReturn(new UserDrivingLicense());

    assertDoesNotThrow(() -> userDrivingLicenseService.update(1L, userDrivingLicense));
  }

  @Test
  void givenValidRequest_whenUpdateDrivingLicenseStatus_thenSuccess() {
    when(userDrivingLicenseRepository.findById(1L)).thenReturn(Optional.of(userDrivingLicense));
    when(userDrivingLicenseRepository.save(any())).thenReturn(new UserDrivingLicense());

    userDrivingLicenseService.updateDrivingLicenseStatus(1L);

    assertEquals(CONFIRMED, userDrivingLicense.getStatus());
  }

  @Test
  void givenValidRequest_whenUploadFile_thenSuccess() {
    var drivingLicenseFile = new MockMultipartFile("drivingLicenseFile", "drivingLicenseFile.txt",
        "any type", "Some dataset...".getBytes());
    var userEmail = "email@gmail.com";
    var dateOfIssue = LocalDate.now();
    var validityPeriod = LocalDate.now().plusDays(1L);
    var userDrivingLicense = UserDrivingLicense.builder().status(NOT_CONFIRMED)
        .dateOfIssue(dateOfIssue).validityPeriod(validityPeriod).organizationThatIssued("name")
        .documentsFileLink("link").build();
    var user = User.builder().id(1L).drivingLicense(userDrivingLicense).build();
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(userService.findUserByEmail(any())).thenReturn(user);
    doNothing().when(fileStoreService).uploadFile(any(), any(), any());
    when(userDrivingLicenseRepository.save(any())).thenReturn(new UserDrivingLicense());

    assertDoesNotThrow(() -> userDrivingLicenseService.uploadFile(drivingLicenseFile));
  }

  @Test
  void givenRequestWithEmptyFile_whenUploadFile_thenThrowIllegalStateException() {
    var drivingLicenseFile = new MockMultipartFile("drivingLicenseFile", "drivingLicenseFile.txt",
        "any type", new byte[0]);

    assertThrows(IllegalStateException.class,
        () -> userDrivingLicenseService.uploadFile(drivingLicenseFile));
  }
}