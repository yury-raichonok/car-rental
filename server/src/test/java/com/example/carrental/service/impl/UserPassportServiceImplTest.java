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

import com.example.carrental.controller.dto.user.CreateOrUpdateUserPassportRequest;
import com.example.carrental.entity.user.User;
import com.example.carrental.entity.user.UserPassport;
import com.example.carrental.repository.UserPassportRepository;
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
class UserPassportServiceImplTest {

  private UserPassport userPassport;

  @Autowired
  private UserPassportServiceImpl userPassportService;

  @MockBean
  private FileStoreService fileStoreService;

  @MockBean
  private UserPassportRepository userPassportRepository;

  @MockBean
  private UserSecurityService userSecurityService;

  @MockBean
  private UserService userService;

  @BeforeEach
  public void setup() {
    var dateOfBirth = LocalDate.now().minusDays(2);
    var dateOfIssue = LocalDate.now().minusDays(1);
    var validityPeriod = LocalDate.now().plusDays(2);
    userPassport = UserPassport.builder().firstName("firstName").middleName("middleName")
        .lastName("lastName").dateOfBirth(dateOfBirth).passportSeries("series")
        .passportNumber("number").dateOfIssue(dateOfIssue).validityPeriod(validityPeriod)
        .organizationThatIssued("name").documentsFileLink("link").status(NOT_CONFIRMED).build();
  }

  @Test
  void givenValidRequest_whenGetUserPassportData_thenSuccess() {
    var user = User.builder().id(1L).passport(userPassport).build();
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(userService.findUserByEmail(any())).thenReturn(user);

    var userPassportDataResponse = userPassportService.getUserPassportData();

    assertThat(userPassportDataResponse).isNotNull();
    assertThat(userPassportDataResponse.getFirstName()).isEqualTo(userPassport.getFirstName());
    assertThat(userPassportDataResponse.getLastName()).isEqualTo(userPassport.getLastName());
  }

  @Test
  void givenValidRequest_whenFindById_thenSuccess() {
    when(userPassportRepository.findById(1L)).thenReturn(Optional.of(userPassport));

    var passport = userPassportService.findById(1L);

    assertThat(passport).isNotNull();
    assertThat(passport.getFirstName()).isEqualTo(userPassport.getFirstName());
    assertThat(passport.getLastName()).isEqualTo(userPassport.getLastName());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindById_thenThrowIllegalStateException() {
    when(userPassportRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> userPassportService.findById(1L));
  }

  @Test
  void givenValidRequest_whenFindPassportDataById_thenSuccess() {
    when(userPassportRepository.findById(1L)).thenReturn(Optional.of(userPassport));

    var passport = userPassportService.findPassportDataById(1L);

    assertThat(passport).isNotNull();
    assertThat(passport.getFirstName()).isEqualTo(userPassport.getFirstName());
    assertThat(passport.getLastName()).isEqualTo(userPassport.getLastName());
  }

  @Test
  void givenValidRequest_whenCreateOrUpdate_thenSuccess() {
    var createOrUpdateUserPassportRequest = CreateOrUpdateUserPassportRequest.builder()
        .firstName("name")
        .lastName("name").dateOfBirth(LocalDate.now().minusDays(1)).passportNumber("series")
        .passportSeries("number").dateOfIssue(LocalDate.now().minusDays(1))
        .validityPeriod(LocalDate.now().plusDays(2)).organizationThatIssued("name").build();
    var dateOfBirth = LocalDate.now().minusDays(2);
    var dateOfIssue = LocalDate.now().minusDays(1);
    var validityPeriod = LocalDate.now().plusDays(2);
    var userPassport = UserPassport.builder().firstName("firstName").middleName("middleName")
        .lastName("lastName").dateOfBirth(dateOfBirth).passportSeries("series")
        .passportNumber("number").dateOfIssue(dateOfIssue).validityPeriod(validityPeriod)
        .organizationThatIssued("name").documentsFileLink("link").status(NOT_CONFIRMED).build();
    var user = User.builder().id(1L).passport(userPassport).build();
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(userService.findUserByEmail(any())).thenReturn(user);
    when(userPassportRepository.save(any())).thenReturn(new UserPassport());

    assertDoesNotThrow(() -> userPassportService.createOrUpdate(createOrUpdateUserPassportRequest));
  }

  @Test
  void givenValidRequest_whenUpdate_thenSuccess() {
    when(userPassportRepository.findById(1L)).thenReturn(Optional.of(userPassport));
    when(userPassportRepository.save(any())).thenReturn(new UserPassport());

    assertDoesNotThrow(() -> userPassportService.update(1L, userPassport));
  }

  @Test
  void givenValidRequest_whenUpdatePassportStatus_thenSuccess() {
    when(userPassportRepository.findById(1L)).thenReturn(Optional.of(userPassport));
    when(userPassportRepository.save(any())).thenReturn(new UserPassport());

    userPassportService.updatePassportStatus(1L);

    assertEquals(CONFIRMED, userPassport.getStatus());
  }

  @Test
  void givenValidRequest_whenUploadFile_thenSuccess() {
    var passportFile = new MockMultipartFile("passportFile", "passportFile.txt",
        "any type", "Some dataset...".getBytes());
    var userEmail = "email@gmail.com";
    var dateOfBirth = LocalDate.now().minusDays(2);
    var dateOfIssue = LocalDate.now().minusDays(1);
    var validityPeriod = LocalDate.now().plusDays(2);
    var userPassport = UserPassport.builder().firstName("firstName").middleName("middleName")
        .lastName("lastName").dateOfBirth(dateOfBirth).passportSeries("series")
        .passportNumber("number").dateOfIssue(dateOfIssue).validityPeriod(validityPeriod)
        .organizationThatIssued("name").documentsFileLink("link").status(NOT_CONFIRMED).build();
    var user = User.builder().id(1L).passport(userPassport).build();
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(userService.findUserByEmail(any())).thenReturn(user);
    doNothing().when(fileStoreService).uploadFile(any(), any(), any());
    when(userPassportRepository.save(any())).thenReturn(new UserPassport());

    assertDoesNotThrow(() -> userPassportService.uploadFile(passportFile));
  }

  @Test
  void givenRequestWithEmptyFile_whenUploadFile_thenThrowIllegalStateException() {
    var passportFile = new MockMultipartFile("passportFile", "passportFile.txt",
        "any type", new byte[0]);

    assertThrows(IllegalStateException.class, () -> userPassportService.uploadFile(passportFile));
  }
}