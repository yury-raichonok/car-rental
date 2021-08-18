package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.HOUR_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY;
import static com.example.carrental.constants.ApplicationConstants.MINUTES_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY;
import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_TIME_FORMAT_PATTERN;
import static com.example.carrental.entity.notification.NotificationStatus.NEW;
import static com.example.carrental.entity.notification.NotificationType.ACCEPT;
import static com.example.carrental.entity.notification.NotificationType.INFO;
import static com.example.carrental.entity.order.OrderPaymentStatus.NOT_PAID;
import static com.example.carrental.entity.order.OrderPaymentStatus.PAID;
import static com.example.carrental.entity.order.OrderRentalStatus.CANCELED;
import static com.example.carrental.entity.order.OrderRentalStatus.FINISHED;
import static com.example.carrental.entity.order.OrderRentalStatus.FINISHED_WITH_PENALTY;
import static com.example.carrental.entity.order.OrderRentalStatus.IN_PROCESS;
import static com.example.carrental.entity.order.OrderRentalStatus.NOT_STARTED;
import static com.example.carrental.entity.user.UserDocumentStatus.CONFIRMED;
import static java.math.MathContext.DECIMAL64;

import com.example.carrental.config.ApplicationPropertiesConfig;
import com.example.carrental.controller.dto.bill.CreateRepairBillRequest;
import com.example.carrental.controller.dto.order.CreateOrderRequest;
import com.example.carrental.controller.dto.order.OrderCompleteWithPenaltyRequest;
import com.example.carrental.controller.dto.order.OrderInformationResponse;
import com.example.carrental.controller.dto.order.OrderNewResponse;
import com.example.carrental.controller.dto.order.OrderRejectRequest;
import com.example.carrental.controller.dto.order.OrderResponse;
import com.example.carrental.controller.dto.order.OrderSearchRequest;
import com.example.carrental.controller.dto.order.OrderTotalCostRequest;
import com.example.carrental.controller.dto.order.OrderTotalCostResponse;
import com.example.carrental.controller.dto.order.UserOrderResponse;
import com.example.carrental.entity.notification.Notification;
import com.example.carrental.entity.notification.NotificationType;
import com.example.carrental.entity.order.Order;
import com.example.carrental.entity.order.OrderRentalStatus;
import com.example.carrental.mapper.OrderMapper;
import com.example.carrental.repository.OrderCriteriaRepository;
import com.example.carrental.repository.OrderRepository;
import com.example.carrental.service.CarService;
import com.example.carrental.service.EmailService;
import com.example.carrental.service.LocationTranslationService;
import com.example.carrental.service.NotificationService;
import com.example.carrental.service.OrderService;
import com.example.carrental.service.PDFService;
import com.example.carrental.service.PaymentBillService;
import com.example.carrental.service.RepairBillService;
import com.example.carrental.service.UserSecurityService;
import com.example.carrental.service.UserService;
import com.example.carrental.service.exceptions.DocumentNotGeneratedException;
import com.example.carrental.service.exceptions.DocumentsNotConfirmedException;
import com.example.carrental.service.exceptions.FontNotFoundException;
import com.example.carrental.service.exceptions.OrderPeriodValidationException;
import com.example.carrental.service.exceptions.PhoneNotSpecifiedException;
import io.jsonwebtoken.lang.Collections;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The service for Orders.
 * <p>
 * This class performs the CRUD operations for Orders.
 * </p>
 * @author Yury Raichonak
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private static final String ORDER_CANCELED_EMAIL_TOPIC = "Order canceled";
  private static final String ORDER_REQUEST_EMAIL_TOPIC = "Order request for car rental";
  private static final String ORDER_REQUEST_REJECTED_EMAIL_TOPIC = "Order request rejected";

  private final ApplicationPropertiesConfig applicationPropertiesConfig;
  private final CarService carService;
  private final EmailService emailService;
  private final LocationTranslationService locationTranslationService;
  private final NotificationService notificationService;
  private final OrderCriteriaRepository orderCriteriaRepository;
  private final OrderMapper orderMapper;
  private final OrderRepository orderRepository;
  private final PaymentBillService paymentBillService;
  private final PDFService pdfService;
  private final RepairBillService repairBillService;
  private final UserSecurityService userSecurityService;
  private final UserService userService;

  /**
   * @param pageable data.
   * @param language selected language.
   * @return page of order information response.
   */
  @Override
  public Page<OrderInformationResponse> findAllCurrent(Pageable pageable, String language) {
    var ordersPage = orderRepository.findAllByRentalStatus(IN_PROCESS, pageable);
    List<OrderInformationResponse> ordersResponse = new ArrayList<>();
    ordersPage.forEach(order -> {
      locationTranslationService.setTranslation(order.getLocation(), language);
      ordersResponse.add(orderMapper.orderToOrderInformationResponse(order));
    });

    return new PageImpl<>(ordersResponse, ordersPage.getPageable(), ordersPage.getTotalElements());
  }

  /**
   * @param pageable data.
   * @param language selected language.
   * @return page of orders information.
   */
  @Override
  public Page<OrderInformationResponse> findAllFuture(Pageable pageable, String language) {
    var ordersPage = orderRepository.findAllByRentalStatusAndPaymentStatus(NOT_STARTED, PAID,
        pageable);
    List<OrderInformationResponse> ordersResponse = new ArrayList<>();
    ordersPage.forEach(order -> {
      locationTranslationService.setTranslation(order.getLocation(), language);
      ordersResponse.add(orderMapper.orderToOrderInformationResponse(order));
    });

    return new PageImpl<>(ordersResponse, ordersPage.getPageable(), ordersPage.getTotalElements());
  }

  /**
   * @param pageable data.
   * @param language selected language.
   * @return page of orders.
   */
  @Override
  public Page<OrderNewResponse> findAllNew(Pageable pageable, String language) {
    var ordersPage = orderRepository.findAllByRentalStatus(OrderRentalStatus.NEW, pageable);
    List<OrderNewResponse> ordersResponse = new ArrayList<>();
    ordersPage.forEach(order -> {
      locationTranslationService.setTranslation(order.getLocation(), language);
      ordersResponse.add(orderMapper.orderToOrderNewResponse(order));
    });
    return new PageImpl<>(ordersResponse, ordersPage.getPageable(), ordersPage.getTotalElements());
  }

  /**
   * @param pageable data.
   * @param language selected language.
   * @return page of orders.
   */
  @Override
  public Page<UserOrderResponse> findAllNewUserOrders(Pageable pageable, String language) {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var orderPage = orderRepository.findAllByRentalStatusAndUser_EmailAndPaymentStatus(NOT_STARTED,
        userEmail, PAID, pageable);

    List<UserOrderResponse> ordersResponse = new ArrayList<>();
    orderPage.forEach(order -> {
      locationTranslationService.setTranslation(order.getLocation(), language);
      ordersResponse.add(orderMapper.orderToNewUserOrderResponse(order));
    });
    return new PageImpl<>(ordersResponse, orderPage.getPageable(), orderPage.getTotalElements());
  }

  /**
   * @param pageable data.
   * @param language selected language.
   * @return page of orders.
   */
  @Override
  public Page<UserOrderResponse> findAllUserOrdersHistory(Pageable pageable, String language) {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var orderPage = orderRepository.findAllByRentalStatusOrRentalStatusAndUser_Email(FINISHED,
        FINISHED_WITH_PENALTY, userEmail, pageable);

    List<UserOrderResponse> ordersResponse = new ArrayList<>();
    orderPage.forEach(order -> {
      locationTranslationService.setTranslation(order.getLocation(), language);
      ordersResponse.add(orderMapper.orderToNewUserOrderResponse(order));
    });
    return new PageImpl<>(ordersResponse, orderPage.getPageable(), orderPage.getTotalElements());
  }

  /**
   * @param orderTotalCostRequest data for calculating total cost.
   * @return total cost response.
   * @throws OrderPeriodValidationException if specified invalid rental period.
   */
  @Override
  public OrderTotalCostResponse calculateTotalCost(OrderTotalCostRequest orderTotalCostRequest)
      throws OrderPeriodValidationException {
    if (orderTotalCostRequest.getPickUpDate().isAfter(orderTotalCostRequest.getReturnDate())) {
      log.error("Invalid order total cost calculation request. Pick-up date: {}, return date: {}",
          orderTotalCostRequest.getPickUpDate(), orderTotalCostRequest.getReturnDate());
      throw new OrderPeriodValidationException(String
          .format("Invalid order total cost calculation request. Pick-up date: %s, return date: %s",
              orderTotalCostRequest.getPickUpDate(), orderTotalCostRequest.getReturnDate()));
    }
    double totalCost;
    double costPerHour = orderTotalCostRequest.getCostPerHour();
    var duration = Duration.between(orderTotalCostRequest.getPickUpDate(),
        orderTotalCostRequest.getReturnDate()).toHours();

    if (duration == 0) {
      log.error("Invalid order total cost calculation request. Rental period 0 hours");
      throw new OrderPeriodValidationException(
          "Invalid order total cost calculation request. Rental period 0 hours");
    } else {
      totalCost = costPerHour * duration;
    }
    return new OrderTotalCostResponse(BigDecimal.valueOf(totalCost).round(DECIMAL64));
  }

  /**
   * @param createOrderRequest data for creating new order.
   * @throws DocumentsNotConfirmedException if user documents are not confirmed.
   * @throws PhoneNotSpecifiedException if user contact phone is not specified.
   */
  @Override
  @Transactional
  public void create(CreateOrderRequest createOrderRequest) {
    var car = carService.findById(createOrderRequest.getCarId());
    var location = car.getLocation();
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    var user = userService.findUserByEmail(userEmail);

    var passportNotConfirmed = Optional.ofNullable(user.getPassport()).isEmpty() ||
        !user.getPassport().getStatus().equals(CONFIRMED);
    var drivingLicenseNotConfirmed = Optional.ofNullable(user.getDrivingLicense()).isEmpty() ||
        !user.getDrivingLicense().getStatus().equals(CONFIRMED);

    if (passportNotConfirmed || drivingLicenseNotConfirmed) {
      log.error("Documents of user {} are not confirmed", userEmail);
      throw new DocumentsNotConfirmedException(
          String.format("Documents of user %s are not confirmed", userEmail));
    }

    if (Collections.isEmpty(user.getPhones())) {
      log.error("Phones of user {} are not specified", user.getEmail());
      throw new PhoneNotSpecifiedException(String.format("Phones of user %s are not specified",
          user.getEmail()));
    }

    var totalCost = calculateTotalCost(OrderTotalCostRequest
        .builder()
        .costPerHour(car.getCostPerHour().doubleValue())
        .pickUpDate(createOrderRequest.getPickUpDate())
        .returnDate(createOrderRequest.getReturnDate())
        .build());

    orderRepository.save(Order
        .builder()
        .pickUpDate(createOrderRequest.getPickUpDate())
        .returnDate(createOrderRequest.getReturnDate())
        .totalCost(totalCost.getTotalCost())
        .paymentStatus(NOT_PAID)
        .sentDate(LocalDateTime.now())
        .rentalStatus(OrderRentalStatus.NEW)
        .car(car)
        .location(location)
        .user(user)
        .build());
  }

  /**
   * @param orderSearchRequest search request.
   * @param language selected language.
   * @return page of orders.
   */
  @Override
  public Page<OrderResponse> findAll(OrderSearchRequest orderSearchRequest, String language) {
    var ordersPage = orderCriteriaRepository.findAll(orderSearchRequest);
    List<OrderResponse> ordersResponse = new ArrayList<>();
    ordersPage.forEach(order -> {
      locationTranslationService.setTranslation(order.getLocation(), language);
      ordersResponse.add(orderMapper.orderToOrderResponse(order));
    });
    return new PageImpl<>(ordersResponse, ordersPage.getPageable(), ordersPage.getTotalElements());
  }

  /**
   * @param id of order.
   */
  @Override
  @Transactional
  public void approveOrder(Long id) {
    var order = findById(id);
    paymentBillService.create(order);

    var message = String.format(
        "Order №%d for renting a %s %s has been accepted. "
            + "You have %d minutes to pay for your order in the \"Bills\" tab. "
            + "At %s the car reservation will be canceled", order.getId(),
        order.getCar().getModel().getBrand().getName(), order.getCar().getModel().getName(),
        applicationPropertiesConfig.getBillValidityPeriodInMinutes(),
        LocalDateTime.now().plusMinutes(applicationPropertiesConfig.getBillValidityPeriodInMinutes())
            .format(DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));

    var notification = Notification
        .builder()
        .message(message)
        .notificationType(ACCEPT)
        .sentDate(LocalDateTime.now())
        .status(NEW)
        .user(order.getUser())
        .build();
    notificationService.sendNotification(notification);
    emailService.sendEmail(order.getUser().getEmail(), message, ORDER_REQUEST_EMAIL_TOPIC);

    order.setRentalStatus(NOT_STARTED);
    orderRepository.save(order);
  }

  /**
   * @param id of order.
   * @param orderRejectRequest data canceling order.
   */
  @Override
  @Transactional
  public void cancelOrderAfterPayment(Long id, OrderRejectRequest orderRejectRequest) {
    var order = findById(id);
    order.setComments(orderRejectRequest.getComments());
    order.setRentalStatus(CANCELED);
    orderRepository.save(order);

    var message = String.format(
        "Your order №%d for a %s %s has been canceled. "
            + "For a refund, contact the administration by e-mail, "
            + "or the phone number listed on the website.", order.getId(),
        order.getCar().getModel().getBrand().getName(), order.getCar().getModel().getName());

    var notification = Notification
        .builder()
        .message(message)
        .notificationType(INFO)
        .sentDate(LocalDateTime.now())
        .status(NEW)
        .user(order.getUser())
        .build();
    notificationService.sendNotification(notification);
    emailService.sendEmail(order.getUser().getEmail(), message, ORDER_CANCELED_EMAIL_TOPIC);
  }

  /**
   * @param id of order.
   */
  @Override
  @Transactional
  public void completeOrder(Long id) {
    var order = findById(id);
    order.setRentalStatus(FINISHED);
    orderRepository.save(order);

    var notification = Notification
        .builder()
        .message(String.format(
            "Order №%d for renting a %s %s has been successfully completed. "
                + "Thank you for using our car rental service!", order.getId(),
            order.getCar().getModel().getBrand().getName(), order.getCar().getModel().getName()))
        .notificationType(INFO)
        .sentDate(LocalDateTime.now())
        .status(NEW)
        .user(order.getUser())
        .build();
    notificationService.sendNotification(notification);
  }

  /**
   * @param id of order.
   * @param orderCompleteWithPenaltyRequest data to complete order.
   */
  @Override
  @Transactional
  public void completeOrderWithPenalty(Long id,
      OrderCompleteWithPenaltyRequest orderCompleteWithPenaltyRequest) {
    var order = findById(id);
    order.setRentalStatus(FINISHED_WITH_PENALTY);
    order.setComments(orderCompleteWithPenaltyRequest.getMessage());
    orderRepository.save(order);

    var notification = Notification
        .builder()
        .message(orderCompleteWithPenaltyRequest.getMessage())
        .notificationType(INFO)
        .sentDate(LocalDateTime.now())
        .status(NEW)
        .user(order.getUser())
        .build();
    notificationService.sendNotification(notification);

    repairBillService.create(CreateRepairBillRequest
        .builder()
        .message(orderCompleteWithPenaltyRequest.getMessage())
        .totalCost(BigDecimal.valueOf(orderCompleteWithPenaltyRequest.getTotalCost()))
        .order(order)
        .build());
  }

  /**
   * @param id of order.
   * @param orderRejectRequest data to reject order.
   */
  @Override
  @Transactional
  public void rejectOrder(Long id, OrderRejectRequest orderRejectRequest) {
    var order = findById(id);
    order.setComments(orderRejectRequest.getComments());
    order.setDenyingDate(LocalDateTime.now());
    order.setRentalStatus(OrderRentalStatus.DENIED);
    orderRepository.save(order);

    var notification = Notification
        .builder()
        .message(orderRejectRequest.getComments())
        .notificationType(NotificationType.DENY)
        .sentDate(LocalDateTime.now())
        .user(order.getUser())
        .status(NEW)
        .build();
    notificationService.sendNotification(notification);
    emailService.sendEmail(order.getUser().getEmail(), orderRejectRequest.getComments(),
        ORDER_REQUEST_REJECTED_EMAIL_TOPIC);
  }

  /**
   * @param id of order.
   */
  @Override
  @Transactional
  public void startRentalPeriod(Long id) {
    var order = findById(id);
    order.setRentalStatus(IN_PROCESS);
    orderRepository.save(order);
  }

  /**
   * @param id of order.
   * @param createOrderRequest data for updating order.
   */
  @Override
  @Transactional
  public void update(Long id, CreateOrderRequest createOrderRequest) {
    var order = findById(id);
    var car = carService.findById(createOrderRequest.getCarId());

    order.setPickUpDate(createOrderRequest.getPickUpDate());
    order.setReturnDate(createOrderRequest.getReturnDate());
    order.setTotalCost(BigDecimal.valueOf(createOrderRequest.getTotalCost()));
    order.setCar(car);

    orderRepository.save(order);
  }

  /**
   * @param id of order.
   * @return byte array resource.
   * @throws FontNotFoundException if font for PDF file is not found.
   * @throws DocumentNotGeneratedException if PDF document is not generated.
   */
  @Override
  public ByteArrayResource exportOrderToPDF(Long id)
      throws FontNotFoundException, DocumentNotGeneratedException {
    var order = findById(id);
    return pdfService.exportOrderToPDF(order);
  }

  /**
   * @param id of order.
   * @return order.
   */
  @Override
  public Order findById(Long id) {
    return orderRepository.findById(id).orElseThrow(() -> {
      log.error("Order with id {} does not exists", id);
      throw new IllegalStateException(String.format("Order with id %d does not exists", id));
    });
  }

  /**
   * @return amount of new orders.
   */
  @Override
  public int findNewOrdersAmount() {
    return orderRepository.countAllByRentalStatus(OrderRentalStatus.NEW);
  }

  /**
   * @return amount of new orders per day.
   */
  @Override
  public int findNewOrdersAmountPerDay() {
    return orderRepository.countAllBySentDateAfter(LocalDateTime.of(LocalDate.now(),
        LocalTime.of(HOUR_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY,
            MINUTES_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY)));
  }

  /**
   * @return amount of new user orders.
   */
  @Override
  public int findUserOrdersAmount() {
    var userEmail = userSecurityService.getUserEmailFromSecurityContext();
    return orderRepository.countAllByUser_EmailAndRentalStatusAndPaymentStatus(userEmail,
        NOT_STARTED, PAID);
  }
}
