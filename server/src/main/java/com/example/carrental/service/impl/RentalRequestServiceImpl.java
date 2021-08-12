package com.example.carrental.service.impl;

import static com.example.carrental.entity.notification.NotificationType.ACCEPT;
import static com.example.carrental.entity.notification.NotificationType.DENY;
import static com.example.carrental.entity.rentalDetails.RentalRequestType.DRIVING_LICENSE_CONFIRMATION_REQUEST;
import static com.example.carrental.entity.rentalDetails.RentalRequestType.PASSPORT_CONFIRMATION_REQUEST;

import com.example.carrental.controller.dto.rentalDetails.CreateRentalRequestRequest;
import com.example.carrental.controller.dto.rentalDetails.RentalAllRequestResponse;
import com.example.carrental.controller.dto.rentalDetails.RentalRequestRejectRequest;
import com.example.carrental.controller.dto.rentalDetails.RentalRequestResponse;
import com.example.carrental.controller.dto.user.UserDrivingLicenseConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserPassportConfirmationDataResponse;
import com.example.carrental.entity.notification.Notification;
import com.example.carrental.entity.notification.NotificationStatus;
import com.example.carrental.entity.rentalDetails.RentalRequest;
import com.example.carrental.entity.user.UserDocumentStatus;
import com.example.carrental.mapper.UserDrivingLicenseMapper;
import com.example.carrental.mapper.RentalRequestMapper;
import com.example.carrental.repository.RentalRequestRepository;
import com.example.carrental.service.NotificationService;
import com.example.carrental.service.RentalRequestService;
import com.example.carrental.service.UserDrivingLicenseService;
import com.example.carrental.service.UserPassportService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalRequestServiceImpl implements RentalRequestService {

  private final RentalRequestRepository rentalRequestRepository;
  private final UserService userService;
  private final NotificationService notificationService;
  private final UserPassportService userPassportService;
  private final UserDrivingLicenseService userDrivingLicenseService;
  private final RentalRequestMapper rentalRequestMapper;
  private final UserDrivingLicenseMapper userDrivingLicenseMapper;

  @Override
  public Page<RentalAllRequestResponse> findAll(Pageable pageable) {
    var rentalRequestsPage = rentalRequestRepository.findAll(pageable);
    List<RentalAllRequestResponse> requestResponseList = new ArrayList<>();
    rentalRequestsPage.forEach(request -> requestResponseList
        .add(rentalRequestMapper.rentalRequestToRentalAllRequestResponse(request)));
    return new PageImpl<>(requestResponseList, rentalRequestsPage.getPageable(),
        rentalRequestsPage.getTotalElements());
  }

  @Override
  public Page<RentalRequestResponse> findAllNew(Pageable pageable) {
    var rentalRequestsPage = rentalRequestRepository.findAllByConsideredFalse(pageable);
    List<RentalRequestResponse> requestResponseList = new ArrayList<>();
    rentalRequestsPage.forEach(request -> requestResponseList
        .add(rentalRequestMapper.rentalRequestToRentalRequestResponse(request)));
    return new PageImpl<>(requestResponseList, rentalRequestsPage.getPageable(),
        rentalRequestsPage.getTotalElements());
  }

  @Override
  public RentalRequest findById(Long id) {
    Optional<RentalRequest> optionalRentalRequest = rentalRequestRepository.findById(id);
    if (optionalRentalRequest.isEmpty()) {
      log.error("Request with id {} does not exist", id);
      throw new IllegalStateException(String.format("Request with id %d does not exists", id));
    }
    return optionalRentalRequest.get();
  }

  @Override
  public UserDrivingLicenseConfirmationDataResponse findRequestDrivingLicenseData(Long id) {
    var request = findById(id);
    if (!DRIVING_LICENSE_CONFIRMATION_REQUEST.equals(request.getRentalRequestType())) {
      log.error("Type of request does not {}", DRIVING_LICENSE_CONFIRMATION_REQUEST);
      throw new IllegalStateException(
          String.format("Type of request does not %s", DRIVING_LICENSE_CONFIRMATION_REQUEST));
    }

    var optionalDrivingLicense = Optional.ofNullable(request.getUser().getDrivingLicense());
    if (optionalDrivingLicense.isEmpty()) {
      log.error("Driving license of user {} does not exists", request.getUser().getEmail());
      throw new IllegalStateException(
          String
              .format("Driving license of user %s does not exists", request.getUser().getEmail()));
    }

    var drivingLicense = optionalDrivingLicense.get();

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

    var optionalPassport = Optional.ofNullable(request.getUser().getPassport());
    if (optionalPassport.isEmpty()) {
      log.error("Passport of user {} does not exists", request.getUser().getEmail());
      throw new IllegalStateException(
          String.format("Passport of user %s does not exists", request.getUser().getEmail()));
    }

    var passport = optionalPassport.get();

    return rentalRequestMapper.passportToUserPassportConfirmationDataResponse(passport);
  }

  @Override
  @Transactional
  public void create(CreateRentalRequestRequest createRentalRequestRequest) {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    if ("anonymousUser".equals(username)) {
      log.error("User is not authenticated!");
      throw new IllegalStateException("User is not authenticated!");
    }

    var user = userService.findUserByEmail(username);

    switch (createRentalRequestRequest.getRentalRequestType()) {
      case DRIVING_LICENSE_CONFIRMATION_REQUEST:
        var drivingLicense = user.getDrivingLicense();
        drivingLicense.setStatus(UserDocumentStatus.UNDER_CONSIDERATION);
        userDrivingLicenseService.update(drivingLicense.getId(), drivingLicense);
        break;
      case PASSPORT_CONFIRMATION_REQUEST:
        var passport = user.getPassport();
        passport.setStatus(UserDocumentStatus.UNDER_CONSIDERATION);
        userPassportService.update(passport.getId(), passport);
        break;
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

    switch (rentalRequest.getRentalRequestType()) {
      case PASSPORT_CONFIRMATION_REQUEST:
        notification.setMessage("Your application for passport confirmation has been approved.");
        var passport = rentalRequest.getUser().getPassport();
        passport.setConfirmedAt(LocalDateTime.now());
        passport.setStatus(UserDocumentStatus.CONFIRMED);
        userPassportService.update(passport.getId(), passport);
        break;
      case DRIVING_LICENSE_CONFIRMATION_REQUEST:
        notification.setMessage("Your driving license confirmation application is approved.");
        var drivingLicense = rentalRequest.getUser().getDrivingLicense();
        drivingLicense.setConfirmedAt(LocalDateTime.now());
        drivingLicense.setStatus(UserDocumentStatus.CONFIRMED);
        userDrivingLicenseService.update(drivingLicense.getId(), drivingLicense);
        break;
    }

    rentalRequest.setConsiderationDate(LocalDateTime.now());
    rentalRequest.setConsidered(true);
    rentalRequest.setComments("Accepted");

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

    switch (rentalRequest.getRentalRequestType()) {
      case PASSPORT_CONFIRMATION_REQUEST:
        var passport = rentalRequest.getUser().getPassport();
        passport.setStatus(UserDocumentStatus.NOT_CONFIRMED);
        userPassportService.update(passport.getId(), passport);
        break;
      case DRIVING_LICENSE_CONFIRMATION_REQUEST:
        var drivingLicense = rentalRequest.getUser().getDrivingLicense();
        drivingLicense.setStatus(UserDocumentStatus.NOT_CONFIRMED);
        userDrivingLicenseService.update(drivingLicense.getId(), drivingLicense);
        break;
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
    return rentalRequestRepository
        .countAllBySentDateAfter(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)));
  }
}
