package com.example.carrental.service.impl;

import com.example.carrental.controller.dto.rentalDetails.RentalAdminDetailsStatisticResponse;
import com.example.carrental.controller.dto.rentalDetails.RentalDetailsAndStatisticResponse;
import com.example.carrental.controller.dto.rentalDetails.RentalDetailsContactInformationResponse;
import com.example.carrental.controller.dto.rentalDetails.RentalDetailsUpdateRequest;
import com.example.carrental.controller.dto.rentalDetails.RentalUserDetailsStatisticResponse;
import com.example.carrental.entity.rentalDetails.RentalDetails;
import com.example.carrental.repository.RentalDetailsRepository;
import com.example.carrental.service.LocationService;
import com.example.carrental.service.LocationTranslationService;
import com.example.carrental.service.MessageService;
import com.example.carrental.service.NotificationService;
import com.example.carrental.service.OrderService;
import com.example.carrental.service.PaymentBillService;
import com.example.carrental.service.RentalDetailsService;
import com.example.carrental.service.RentalRequestService;
import com.example.carrental.service.RepairBillService;
import com.example.carrental.service.UserSecurityService;
import com.example.carrental.service.UserService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RentalDetailsServiceImpl implements RentalDetailsService {

  private static final Long RENTAL_DETAILS_ID = 1L;

  private final RentalDetailsRepository rentalDetailsRepository;
  private final MessageService messageService;
  private final UserService userService;
  private final RentalRequestService rentalRequestService;
  private final LocationService locationService;
  private final LocationTranslationService locationTranslationService;
  private final NotificationService notificationService;
  private final UserSecurityService userSecurityService;

  private OrderService orderService;
  private PaymentBillService paymentBillService;
  private RepairBillService repairBillService;

  @Autowired
  public void setOrderService(OrderService orderService) {
    this.orderService = orderService;
  }

  @Autowired
  public void setPaymentBillService(PaymentBillService paymentBillService) {
    this.paymentBillService = paymentBillService;
  }

  @Autowired
  public void setRepairBillService(RepairBillService repairBillService) {
    this.repairBillService = repairBillService;
  }

  @Override
  public RentalAdminDetailsStatisticResponse getAdminDetailsStatistic() {
    int messagesAmt = messageService.findNewMessagesAmount();
    int requests = rentalRequestService.findNewRequestsAmount();
    int orders = orderService.findNewOrdersAmount();
    return RentalAdminDetailsStatisticResponse
        .builder()
        .messagesAmt(messagesAmt)
        .requests(requests)
        .orders(orders)
        .build();
  }

  @Override
  public RentalDetailsContactInformationResponse getContactInformation(String language) {
    var rentalDetails = getRentalDetails();
    var location = rentalDetails.getLocation();
    locationTranslationService.setTranslation(location, language);

    return RentalDetailsContactInformationResponse
        .builder()
        .phone(rentalDetails.getPhone())
        .email(rentalDetails.getEmail())
        .locationName(location.getName())
        .locationCoordinateX(rentalDetails.getLocation().getCoordinateX())
        .locationCoordinateY(rentalDetails.getLocation().getCoordinateY())
        .zoom(rentalDetails.getLocation().getZoom())
        .build();
  }

  @Override
  public RentalDetails getRentalDetails() {
    return rentalDetailsRepository.findById(RENTAL_DETAILS_ID).orElseThrow(() -> {
      log.error("Rental details does not set");
      throw new IllegalStateException("Rental details does not set");
    });
  }

  @Override
  public RentalDetailsAndStatisticResponse getRentalDetailsAndStatistic(String language) {
    var rentalDetails = getRentalDetails();
    var newMessagesPerDay = messageService.findNewMessagesAmountPerDay();
    var newOrdersPerDay = orderService.findNewOrdersAmountPerDay();
    var newUsersPerDay = userService.findNewUsersAmountPerDay();
    var newRequestsPerDay = rentalRequestService.findNewRequestsAmountPerDay();
    var location = rentalDetails.getLocation();
    locationTranslationService.setTranslation(location, language);

    return RentalDetailsAndStatisticResponse
        .builder()
        .email(rentalDetails.getEmail())
        .phoneNumber(rentalDetails.getPhone())
        .locationId(rentalDetails.getLocation().getId())
        .location(location.getName())
        .fromDayToWeekCoefficient(rentalDetails.getFromDayToWeekCoefficient().doubleValue())
        .fromWeekCoefficient(rentalDetails.getFromWeekCoefficient().doubleValue())
        .billValidityPeriod(rentalDetails.getPaymentBillValidityPeriodInMinutes())
        .newMessages(newMessagesPerDay)
        .newOrders(newOrdersPerDay)
        .newUsers(newUsersPerDay)
        .newRequests(newRequestsPerDay)
        .build();
  }

  @Override
  public RentalUserDetailsStatisticResponse getUserDetailsStatistic() {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    int paymentBills = paymentBillService.findNewUserBillsAmount(userEmail);
    int repairBills = repairBillService.findNewUserBillsAmount(userEmail);
    int orders = orderService.findNewUserOrdersAmount(userEmail);
    int notifications = notificationService.findNewUserNotificationsAmount(userEmail);

    return RentalUserDetailsStatisticResponse
        .builder()
        .paymentBills(paymentBills)
        .repairBills(repairBills)
        .orders(orders)
        .notifications(notifications)
        .build();
  }

  @Override
  public void createOrUpdate(RentalDetailsUpdateRequest rentalDetailsUpdateRequest) {
    var location = locationService.findById(rentalDetailsUpdateRequest.getLocation());
    rentalDetailsRepository.findById(RENTAL_DETAILS_ID).ifPresentOrElse(
        details -> {
          details.setEmail(rentalDetailsUpdateRequest.getEmail());
          details.setPhone(rentalDetailsUpdateRequest.getPhoneNumber());
          details.setLocation(location);
          details.setFromDayToWeekCoefficient(
              BigDecimal.valueOf(rentalDetailsUpdateRequest.getFromDayToWeekCoefficient()));
          details.setFromWeekCoefficient(
              BigDecimal.valueOf(rentalDetailsUpdateRequest.getFromWeekCoefficient()));
          details.setPaymentBillValidityPeriodInMinutes(
              rentalDetailsUpdateRequest.getBillValidityPeriod());
          rentalDetailsRepository.save(details);
        },
        () -> rentalDetailsRepository.save(RentalDetails
            .builder()
            .email(rentalDetailsUpdateRequest.getEmail())
            .phone(rentalDetailsUpdateRequest.getPhoneNumber())
            .location(location)
            .fromDayToWeekCoefficient(
                BigDecimal.valueOf(rentalDetailsUpdateRequest.getFromDayToWeekCoefficient()))
            .fromWeekCoefficient(
                BigDecimal.valueOf(rentalDetailsUpdateRequest.getFromWeekCoefficient()))
            .paymentBillValidityPeriodInMinutes(rentalDetailsUpdateRequest.getBillValidityPeriod())
            .build())
    );
  }
}
