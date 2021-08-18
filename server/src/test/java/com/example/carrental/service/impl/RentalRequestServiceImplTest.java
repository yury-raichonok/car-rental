package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.HOUR_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY;
import static com.example.carrental.constants.ApplicationConstants.MINUTES_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.rentaldetails.CreateRentalRequestRequest;
import com.example.carrental.entity.rentaldetails.RentalRequest;
import com.example.carrental.entity.rentaldetails.RentalRequestType;
import com.example.carrental.entity.user.User;
import com.example.carrental.entity.user.UserDrivingLicense;
import com.example.carrental.entity.user.UserPassport;
import com.example.carrental.repository.RentalRequestRepository;
import com.example.carrental.service.NotificationService;
import com.example.carrental.service.UserDrivingLicenseService;
import com.example.carrental.service.UserPassportService;
import com.example.carrental.service.UserSecurityService;
import com.example.carrental.service.UserService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@SpringBootTest
@AutoConfigureMockMvc
class RentalRequestServiceImplTest {

  private RentalRequest rentalRequest;

  @Autowired
  private RentalRequestServiceImpl rentalRequestService;

  @MockBean
  private NotificationService notificationService;

  @MockBean
  private RentalRequestRepository rentalRequestRepository;

  @MockBean
  private UserDrivingLicenseService userDrivingLicenseService;

  @MockBean
  private UserSecurityService userSecurityService;

  @MockBean
  private UserService userService;

  @MockBean
  private UserPassportService userPassportService;

  @BeforeEach
  public void setup() {
    var sentDate = LocalDateTime.now().minusDays(1L);
    var considerationDate = LocalDateTime.now();
    var passport = UserPassport.builder().id(1L).build();
    var drivingLicense = UserDrivingLicense.builder().id(1L).build();
    var user = User.builder().id(1L).email("test@gmail.com").passport(passport)
        .drivingLicense(drivingLicense).build();
    rentalRequest = RentalRequest.builder().id(1L).user(user)
        .rentalRequestType(RentalRequestType.PASSPORT_CONFIRMATION_REQUEST).sentDate(sentDate)
        .considerationDate(considerationDate).considered(true).comments("comments").build();
  }

  @Test
  void givenValidRequest_whenFindAll_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    when(rentalRequestRepository.findAll(pageable))
        .thenReturn(new PageImpl<>(Collections.singletonList(rentalRequest)));

    var rentalAllRequestResponse = rentalRequestService.findAll(pageable);

    assertThat(rentalAllRequestResponse).isNotNull();
    assertThat(rentalAllRequestResponse.getTotalElements()).isEqualTo(1);
  }

  @Test
  void givenValidRequest_whenFindAllNew_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    when(rentalRequestRepository.findAll(pageable))
        .thenReturn(new PageImpl<>(Collections.singletonList(rentalRequest)));

    var rentalRequestResponse = rentalRequestService.findAll(pageable);

    assertThat(rentalRequestResponse).isNotNull();
    assertThat(rentalRequestResponse.getTotalElements()).isEqualTo(1);
  }

  @Test
  void givenValidRequest_whenFindById_thenSuccess() {
    when(rentalRequestRepository.findById(1L)).thenReturn(Optional.of(rentalRequest));

    var response = rentalRequestService.findById(1L);

    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(rentalRequest.getId());
    assertThat(response.getSentDate()).isEqualTo(rentalRequest.getSentDate());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindById_thenThrowIllegalStateException() {
    when(rentalRequestRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> rentalRequestService.findById(1L));
  }

  @Test
  void givenValidRequest_whenFindRequestDrivingLicenseData_thenSuccess() {
    rentalRequest.setRentalRequestType(RentalRequestType.DRIVING_LICENSE_CONFIRMATION_REQUEST);
    when(rentalRequestRepository.findById(1L)).thenReturn(Optional.of(rentalRequest));

    assertDoesNotThrow(() -> rentalRequestService.findRequestDrivingLicenseData(1L));
  }

  @Test
  void givenInvalidRequest_whenFindRequestDrivingLicenseData_thenSuccess() {
    when(rentalRequestRepository.findById(1L)).thenReturn(Optional.of(rentalRequest));

    assertThrows(IllegalStateException.class,
        () -> rentalRequestService.findRequestDrivingLicenseData(1L));
  }

  @Test
  void givenValidRequest_whenFindRequestPassportData_thenSuccess() {
    when(rentalRequestRepository.findById(1L)).thenReturn(Optional.of(rentalRequest));

    var response = rentalRequestService.findRequestPassportData(1L);

    assertThat(response).isNotNull();
  }

  @Test
  void givenInvalidRequest_whenFindRequestPassportData_thenSuccess() {
    rentalRequest.setRentalRequestType(RentalRequestType.DRIVING_LICENSE_CONFIRMATION_REQUEST);
    when(rentalRequestRepository.findById(1L)).thenReturn(Optional.of(rentalRequest));

    assertThrows(IllegalStateException.class,
        () -> rentalRequestService.findRequestPassportData(1L));
  }

  @Test
  void givenValidRequest_whenCreate_thenSuccess() {
    var createRentalRequestRequest = CreateRentalRequestRequest.builder()
        .rentalRequestType(RentalRequestType.PASSPORT_CONFIRMATION_REQUEST).build();
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(userService.findUserByEmail(userEmail)).thenReturn(rentalRequest.getUser());
    doNothing().when(userDrivingLicenseService).update(any(), any());
    doNothing().when(userPassportService).update(any(), any());
    when(rentalRequestRepository.save(any(RentalRequest.class))).thenReturn(new RentalRequest());

    assertDoesNotThrow(() -> rentalRequestService.create(createRentalRequestRequest));
  }

  @Test
  void givenValidRequest_whenApproveRequest_thenSuccess() {
    when(rentalRequestRepository.findById(1L)).thenReturn(Optional.of(rentalRequest));
    doNothing().when(userDrivingLicenseService).update(any(), any());
    doNothing().when(userPassportService).update(any(), any());
    doNothing().when(notificationService).sendNotification(any());
    when(rentalRequestRepository.save(any(RentalRequest.class))).thenReturn(new RentalRequest());

    assertDoesNotThrow(() -> rentalRequestService.approveRequest(1L));
  }

  @Test
  void givenValidRequest_whenRejectRequest_thenSuccess() {
    when(rentalRequestRepository.findById(1L)).thenReturn(Optional.of(rentalRequest));
    doNothing().when(userDrivingLicenseService).update(any(), any());
    doNothing().when(userPassportService).update(any(), any());
    doNothing().when(notificationService).sendNotification(any());
    when(rentalRequestRepository.save(any(RentalRequest.class))).thenReturn(new RentalRequest());

    assertDoesNotThrow(() -> rentalRequestService.approveRequest(1L));
  }

  @Test
  void givenValidRequest_whenFindNewRequestsAmount_thenSuccess() {
    when(rentalRequestRepository.countAllByConsideredFalse()).thenReturn(8);

    var expectedAmount = 8;
    var amount = rentalRequestService.findNewRequestsAmount();

    assertEquals(expectedAmount, amount);
  }

  @Test
  void givenValidRequest_whenFindNewRequestsAmountPerDay_thenSuccess() {
    when(rentalRequestRepository
        .countAllBySentDateAfter(LocalDateTime.of(LocalDate.now(),
            LocalTime.of(HOUR_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY,
                MINUTES_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY)))).thenReturn(8);

    var expectedAmount = 8;
    var amount = rentalRequestService.findNewRequestsAmountPerDay();

    assertEquals(expectedAmount, amount);
  }
}