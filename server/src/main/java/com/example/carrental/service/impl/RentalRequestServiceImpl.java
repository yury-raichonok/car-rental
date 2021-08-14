package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.HOUR_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY;
import static com.example.carrental.constants.ApplicationConstants.MINUTES_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY;
import static com.example.carrental.entity.notification.NotificationType.ACCEPT;
import static com.example.carrental.entity.notification.NotificationType.DENY;
import static com.example.carrental.entity.rentaldetails.RentalRequestType.DRIVING_LICENSE_CONFIRMATION_REQUEST;
import static com.example.carrental.entity.rentaldetails.RentalRequestType.PASSPORT_CONFIRMATION_REQUEST;
import static com.example.carrental.entity.user.UserDocumentStatus.CONFIRMED;
import static com.example.carrental.entity.user.UserDocumentStatus.UNDER_CONSIDERATION;

import com.example.carrental.controller.dto.rentaldetails.CreateRentalRequestRequest;
import com.example.carrental.controller.dto.rentaldetails.RentalAllRequestResponse;
import com.example.carrental.controller.dto.rentaldetails.RentalRequestRejectRequest;
import com.example.carrental.controller.dto.rentaldetails.RentalRequestResponse;
import com.example.carrental.controller.dto.user.UserDrivingLicenseConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserPassportConfirmationDataResponse;
import com.example.carrental.entity.notification.Notification;
import com.example.carrental.entity.notification.NotificationStatus;
import com.example.carrental.entity.rentaldetails.RentalRequest;
import com.example.carrental.entity.user.UserDocumentStatus;
import com.example.carrental.mapper.RentalRequestMapper;
import com.example.carrental.mapper.UserDrivingLicenseMapper;
import com.example.carrental.repository.RentalRequestRepository;
import com.example.carrental.service.NotificationService;
import com.example.carrental.service.RentalRequestService;
import com.example.carrental.service.UserDrivingLicenseService;
import com.example.carrental.service.UserPassportService;
import com.example.carrental.service.UserSecurityService;
import com.example.carrental.service.UserService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalRequestServiceImpl implements RentalRequestService {

  private static final String DRIVING_LICENSE_REQUEST_APPROVED = "Your driving license confirmation application is approved.";
  private static final String PASSPORT_REQUEST_APPROVED = "Your application for passport confirmation has been approved.";
  private static final String REQUEST_ACCEPTED = "Accepted";

  private final NotificationService notificationService;
  private final RentalRequestMapper rentalRequestMapper;
  private final RentalRequestRepository rentalRequestRepository;
  private final UserDrivingLicenseMapper userDrivingLicenseMapper;
  private final UserDrivingLicenseService userDrivingLicenseService;
  private final UserSecurityService userSecurityService;
  private final UserService userService;
  private final UserPassportService userPassportService;

  @Override
  public Page<RentalAllRequestResponse> findAll(Pageable pageable) {
    var rentalRequestsPage = rentalRequestRepository.findAll(pageable);
    List<RentalAllRequestResponse> requestsResponse = new ArrayList<>();
    rentalRequestsPage.forEach(request -> requestsResponse
        .add(rentalRequestMapper.rentalRequestToRentalAllRequestResponse(request)));
    return new PageImpl<>(requestsResponse, rentalRequestsPage.getPageable(),
        rentalRequestsPage.getTotalElements());
  }

  @Override
  public Page<RentalRequestResponse> findAllNew(Pageable pageable) {
    var rentalRequestsPage = rentalRequestRepository.findAllByConsideredFalse(pageable);
    List<RentalRequestResponse> requestsResponse = new ArrayList<>();
    rentalRequestsPage.forEach(request -> requestsResponse
        .add(rentalRequestMapper.rentalRequestToRentalRequestResponse(request)));
    return new PageImpl<>(requestsResponse, rentalRequestsPage.getPageable(),
        rentalRequestsPage.getTotalElements());
  }

  @Override
  public RentalRequest findById(Long id) {
    return rentalRequestRepository.findById(id).orElseThrow(() -> {
      log.error("Request with id {} does not exist", id);
      throw new IllegalStateException(String.format("Request with id %d does not exists", id));
    });
  }

  @Override
  public UserDrivingLicenseConfirmationDataResponse findRequestDrivingLicenseData(Long id) {
    var request = findById(id);
    if (!DRIVING_LICENSE_CONFIRMATION_REQUEST.equals(request.getRentalRequestType())) {
      log.error("Type of request does not {}", DRIVING_LICENSE_CONFIRMATION_REQUEST);
      throw new IllegalStateException(
          String.format("Type of request does not %s", DRIVING_LICENSE_CONFIRMATION_REQUEST));
    }

    var drivingLicense = Optional.ofNullable(request.getUser().getDrivingLicense())
        .orElseThrow(() -> {
          log.error("Driving license of user {} does not exists", request.getUser().getEmail());
          throw new IllegalStateException(
              String.format("Driving license of user %s does not exists",
                  request.getUser().getEmail()));
        });

    return userDrivingLicenseMapper
        .drivingLicenseToUserDrivingLicenseConfirmationDataResponse(drivingLicense);
  }

  @Override
  public UserPassportConfirmationDataResponse findRequestPassportData(Long id) {
    var request = findById(id);
    if (!PASSPORT_CONFIRMATION_REQUEST.equals(request.getRentalRequestType())) {
      log.error("Type of request does not {}", PASSPORT_CONFIRMATION_REQUEST);
      throw new IllegalStateException(
          String.format("Type of request does not %s", PASSPORT_CONFIRMATION_REQUEST));
    }

    var passport = Optional.ofNullable(request.getUser().getPassport()).orElseThrow(() -> {
      log.error("Passport of user {} does not exists", request.getUser().getEmail());
      throw new IllegalStateException(
          String.format("Passport of user %s does not exists", request.getUser().getEmail()));
    });

    return rentalRequestMapper.passportToUserPassportConfirmationDataResponse(passport);
  }

  @Override
  @Transactional
  public void create(CreateRentalRequestRequest createRentalRequestRequest) {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var user = userService.findUserByEmail(userEmail);

    if (DRIVING_LICENSE_CONFIRMATION_REQUEST
        .equals(createRentalRequestRequest.getRentalRequestType())) {
      var drivingLicense = user.getDrivingLicense();
      drivingLicense.setStatus(UNDER_CONSIDERATION);
      userDrivingLicenseService.update(drivingLicense.getId(), drivingLicense);
    } else if (PASSPORT_CONFIRMATION_REQUEST
        .equals(createRentalRequestRequest.getRentalRequestType())) {
      var passport = user.getPassport();
      passport.setStatus(UNDER_CONSIDERATION);
      userPassportService.update(passport.getId(), passport);
    }

    var rentalRequest = RentalRequest
        .builder()
        .rentalRequestType(createRentalRequestRequest.getRentalRequestType())
        .sentDate(LocalDateTime.now())
        .considered(false)
        .user(user)
        .build();
    rentalRequestRepository.save(rentalRequest);
  }

  @Override
  @Transactional
  public void approveRequest(Long id) {
    var rentalRequest = findById(id);
    var notification = Notification
        .builder()
        .notificationType(ACCEPT)
        .sentDate(LocalDateTime.now())
        .status(NotificationStatus.NEW)
        .user(rentalRequest.getUser())
        .build();

    if (PASSPORT_CONFIRMATION_REQUEST.equals(rentalRequest.getRentalRequestType())) {
      notification.setMessage(PASSPORT_REQUEST_APPROVED);
      var passport = rentalRequest.getUser().getPassport();
      passport.setConfirmedAt(LocalDateTime.now());
      passport.setStatus(CONFIRMED);
      userPassportService.update(passport.getId(), passport);
    } else if (DRIVING_LICENSE_CONFIRMATION_REQUEST.equals(rentalRequest.getRentalRequestType())) {
      notification.setMessage(DRIVING_LICENSE_REQUEST_APPROVED);
      var drivingLicense = rentalRequest.getUser().getDrivingLicense();
      drivingLicense.setConfirmedAt(LocalDateTime.now());
      drivingLicense.setStatus(CONFIRMED);
      userDrivingLicenseService.update(drivingLicense.getId(), drivingLicense);
    }

    rentalRequest.setConsiderationDate(LocalDateTime.now());
    rentalRequest.setConsidered(true);
    rentalRequest.setComments(REQUEST_ACCEPTED);

    notificationService.sendNotification(notification);
    rentalRequestRepository.save(rentalRequest);
  }

  @Override
  @Transactional
  public void rejectRequest(Long id, RentalRequestRejectRequest rentalRequestRejectRequest) {
    var rentalRequest = findById(id);
    rentalRequest.setConsiderationDate(LocalDateTime.now());
    rentalRequest.setConsidered(true);
    rentalRequest.setComments(rentalRequestRejectRequest.getComments());

    var notification = Notification
        .builder()
        .message(rentalRequestRejectRequest.getComments())
        .notificationType(DENY)
        .sentDate(LocalDateTime.now())
        .status(NotificationStatus.NEW)
        .user(rentalRequest.getUser())
        .build();

    if (PASSPORT_CONFIRMATION_REQUEST.equals(rentalRequest.getRentalRequestType())) {
      var passport = rentalRequest.getUser().getPassport();
      passport.setStatus(UserDocumentStatus.NOT_CONFIRMED);
      userPassportService.update(passport.getId(), passport);
    } else if (DRIVING_LICENSE_CONFIRMATION_REQUEST.equals(rentalRequest.getRentalRequestType())) {
      var drivingLicense = rentalRequest.getUser().getDrivingLicense();
      drivingLicense.setStatus(UserDocumentStatus.NOT_CONFIRMED);
      userDrivingLicenseService.update(drivingLicense.getId(), drivingLicense);
    }

    notificationService.sendNotification(notification);
    rentalRequestRepository.save(rentalRequest);
  }

  @Override
  public int findNewRequestsAmount() {
    return rentalRequestRepository.countAllByConsideredFalse();
  }

  @Override
  public int findNewRequestsAmountPerDay() {
    return   rentalRequestRepository
        .countAllBySentDateAfter(LocalDateTime.of(LocalDate.now(),
            LocalTime.of(HOUR_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY,
                MINUTES_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY)));
  }
}
