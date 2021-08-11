package com.example.carrental.service.impl;

import static com.example.carrental.entity.notification.NotificationStatus.NEW;
import static com.example.carrental.entity.notification.NotificationType.INFO;
import static com.example.carrental.entity.order.OrderPaymentStatus.PAID;
import static com.example.carrental.entity.order.OrderRentalStatus.CANCELED;
import static com.example.carrental.entity.order.OrderRentalStatus.FINISHED;
import static com.example.carrental.entity.order.OrderRentalStatus.FINISHED_WITH_PENALTY;
import static com.example.carrental.entity.order.OrderRentalStatus.IN_PROCESS;
import static com.example.carrental.entity.order.OrderRentalStatus.NOT_STARTED;
import static com.example.carrental.entity.user.UserDocumentStatus.CONFIRMED;

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
import com.example.carrental.entity.order.OrderPaymentStatus;
import com.example.carrental.entity.order.OrderRentalStatus;
import com.example.carrental.entity.user.User;
import com.example.carrental.mapper.OrderMapper;
import com.example.carrental.repository.OrderCriteriaRepository;
import com.example.carrental.repository.OrderRepository;
import com.example.carrental.service.CarService;
import com.example.carrental.service.LocationTranslationService;
import com.example.carrental.service.NotificationService;
import com.example.carrental.service.OrderService;
import com.example.carrental.service.PDFService;
import com.example.carrental.service.PaymentBillService;
import com.example.carrental.service.RentalDetailsService;
import com.example.carrental.service.RepairBillService;
import com.example.carrental.service.UserService;
import com.example.carrental.service.exceptions.CarAlreadyBookedException;
import com.example.carrental.service.exceptions.DocumentsNotConfirmedException;
import com.example.carrental.service.exceptions.FontNotFoundException;
import java.math.BigDecimal;
import java.math.MathContext;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final NotificationService notificationService;
  private final UserService userService;
  private final OrderCriteriaRepository orderCriteriaRepository;
  private final CarService carService;
  private final OrderMapper orderMapper;
  private final PDFService pdfService;
  private final LocationTranslationService locationTranslationService;

  private PaymentBillService paymentBillService;
  private RepairBillService repairBillService;
  private RentalDetailsService rentalDetailsService;


  @Autowired
  public void setPaymentBillService(PaymentBillService paymentBillService) {
    this.paymentBillService = paymentBillService;
  }

  @Autowired
  public void setRepairBillService(RepairBillService repairBillService) {
    this.repairBillService = repairBillService;
  }

  @Autowired
  public void setRentalDetailsService(RentalDetailsService rentalDetailsService) {
    this.rentalDetailsService = rentalDetailsService;
  }

  @Override
  public Page<OrderResponse> findAll(OrderSearchRequest orderSearchRequest, String language) {
    var ordersPage = orderCriteriaRepository.findAll(orderSearchRequest);
    List<OrderResponse> ordersList = new ArrayList<>();
    ordersPage.forEach(order -> {
      if (!"en".equals(language)) {
        locationTranslationService.setTranslation(order.getLocation(), language);
      }
      ordersList.add(orderMapper.orderToOrderResponse(order));
    });

    return new PageImpl<>(ordersList, ordersPage.getPageable(), ordersPage.getTotalElements());
  }

  @Override
  public Page<OrderNewResponse> findAllNew(Pageable pageable, String language) {
    var ordersPage = orderRepository.findAllByRentalStatus(OrderRentalStatus.NEW, pageable);
    List<OrderNewResponse> ordersList = new ArrayList<>();
    ordersPage.forEach(order -> {
      if (!"en".equals(language)) {
        locationTranslationService.setTranslation(order.getLocation(), language);
      }
      ordersList.add(orderMapper.orderToOrderNewResponse(order));
    });
    return new PageImpl<>(ordersList, ordersPage.getPageable(), ordersPage.getTotalElements());
  }

  @Override
  public int findNewOrdersAmount() {
    return orderRepository.countAllByRentalStatus(OrderRentalStatus.NEW);
  }

  @Override
  public Page<OrderInformationResponse> findAllCurrent(Pageable pageable, String language) {
    var ordersPage = orderRepository.findAllByRentalStatus(IN_PROCESS, pageable);
    List<OrderInformationResponse> ordersList = new ArrayList<>();
    ordersPage.forEach(order -> {
      if (!"en".equals(language)) {
        locationTranslationService.setTranslation(order.getLocation(), language);
      }
      ordersList.add(orderMapper.orderToOrderInformationResponse(order));
    });

    return new PageImpl<>(ordersList, ordersPage.getPageable(), ordersPage.getTotalElements());
  }

  @Override
  public Page<OrderInformationResponse> findAllFuture(Pageable pageable, String language) {
    var ordersPage = orderRepository.findAllByRentalStatusAndPaymentStatus(NOT_STARTED, PAID,
        pageable);
    List<OrderInformationResponse> ordersList = new ArrayList<>();
    ordersPage.forEach(order -> {
      if (!"en".equals(language)) {
        locationTranslationService.setTranslation(order.getLocation(), language);
      }
      ordersList.add(orderMapper.orderToOrderInformationResponse(order));
    });

    return new PageImpl<>(ordersList, ordersPage.getPageable(), ordersPage.getTotalElements());
  }

  @Override
  public Order findById(Long id) {
    Optional<Order> optionalOrder = orderRepository.findById(id);
    if (optionalOrder.isEmpty()) {
      log.error("Order with id {} does not exists", id);
      throw new IllegalStateException(String.format("Order with id %d does not exists", id));
    }
    return optionalOrder.get();
  }

  @Override
  public OrderTotalCostResponse calculateTotalCost(OrderTotalCostRequest orderTotalCostRequest) {
    int dayHours = 24;
    int weekHours = 24 * 7;
    double totalCost;
    double costPerHour = orderTotalCostRequest.getCostPerHour();
    var orderCostDetails = rentalDetailsService.getRentalDetails();

    var duration = Duration
        .between(orderTotalCostRequest.getPickUpDate(), orderTotalCostRequest.getReturnDate())
        .toHours();

    if (duration < dayHours) {
      totalCost = costPerHour * (duration + 1);
    } else if (duration < weekHours) {
      totalCost = costPerHour * duration * orderCostDetails.getFromDayToWeekCoefficient()
          .doubleValue();
    } else {
      totalCost = costPerHour * duration * orderCostDetails.getFromWeekCoefficient()
          .doubleValue();
    }

    return new OrderTotalCostResponse(BigDecimal.valueOf(totalCost).round(MathContext.DECIMAL64));
  }

  @Override
  @Transactional
  public String create(CreateOrderRequest createOrderRequest)
      throws DocumentsNotConfirmedException, CarAlreadyBookedException {

    var car = carService.findById(createOrderRequest.getCarId());
    var location = car.getLocation();
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    var user = userService.findUserByEmail(username);

    boolean isPassportConfirmed = Optional.ofNullable(user.getPassport()).isPresent() &&
        user.getPassport().getStatus().equals(CONFIRMED);
    boolean isDrivingLicenseConfirmed = Optional.ofNullable(user.getDrivingLicense()).isPresent() &&
        user.getDrivingLicense().getStatus().equals(CONFIRMED);

    if (!isPassportConfirmed && !isDrivingLicenseConfirmed) {
      log.error("Passport and driving license of user {} is not confirmed", user.getEmail());
      throw new DocumentsNotConfirmedException(
          "Your passport and driving license are not confirmed!");
    } else if (!isPassportConfirmed) {
      log.error("Passport of user {} is not confirmed", user.getEmail());
      throw new DocumentsNotConfirmedException("Your passport is not confirmed!");
    } else if (!isDrivingLicenseConfirmed) {
      log.error("Driving license of user {} is not confirmed", user.getEmail());
      throw new DocumentsNotConfirmedException("Your driving license is not confirmed!");
    }
    if (Optional.ofNullable(user.getPhones()).isEmpty()) {
      log.error("Phone number of user {} is not specified", user.getEmail());
      throw new DocumentsNotConfirmedException("Your phone number is not specified");
    }

    var activeOrders = orderRepository
        .getAllByRentalStatusOrRentalStatusAndCar_Vin(NOT_STARTED, IN_PROCESS, car.getVin());

    for (Order order : activeOrders) {
      if (NOT_STARTED.equals(order.getRentalStatus()) && order.getPaymentBill().getExpirationTime()
          .isBefore(LocalDateTime.now()) && (
          (createOrderRequest.getPickUpDate().isAfter(order.getPickUpDate()) && createOrderRequest
              .getPickUpDate().isBefore(createOrderRequest.getReturnDate())) || (
              createOrderRequest.getReturnDate().isAfter(order.getPickUpDate())
                  && createOrderRequest.getReturnDate()
                  .isBefore(createOrderRequest.getReturnDate())))) {
        log.error("Car with VIN {} already booked", car.getVin());
        throw new CarAlreadyBookedException(String.format("Car with VIN %s already booked",
            car.getVin()));
      }
      if (IN_PROCESS.equals(order.getRentalStatus()) &&
          (createOrderRequest.getPickUpDate().isAfter(order.getPickUpDate()) && createOrderRequest
              .getPickUpDate().isBefore(createOrderRequest.getReturnDate())) || (
          createOrderRequest.getReturnDate().isAfter(order.getPickUpDate())
              && createOrderRequest.getReturnDate()
              .isBefore(createOrderRequest.getReturnDate()))) {
        log.error("Car with VIN {} already booked", car.getVin());
        throw new CarAlreadyBookedException(String.format("Car with VIN %s already booked",
            car.getVin()));
      }
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
        .paymentStatus(OrderPaymentStatus.NOT_PAID)
        .sentDate(LocalDateTime.now())
        .rentalStatus(OrderRentalStatus.NEW)
        .car(car)
        .location(location)
        .user(user)
        .build());

    return "Success";
  }

  @Override
  @Transactional
  public String update(Long id, CreateOrderRequest createOrderRequest) {
    var order = findById(id);
    var car = carService.findById(createOrderRequest.getCarId());

    order.setPickUpDate(createOrderRequest.getPickUpDate());
    order.setReturnDate(createOrderRequest.getReturnDate());
    order.setTotalCost(BigDecimal.valueOf(createOrderRequest.getTotalCost()));
    order.setCar(car);

    orderRepository.save(order);
    return "Success";
  }

  @Override
  @Transactional
  public String rejectOrder(Long id, OrderRejectRequest orderRejectRequest) {
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
    return "Success";
  }

  @Override
  @Transactional
  public String approveOrder(Long id) {
    var order = findById(id);
    var orderDetails = rentalDetailsService.getRentalDetails();

    paymentBillService.create(orderMapper.orderToCreatePaymentBillRequest(order));

    var notification = Notification
        .builder()
        .message(String.format(
            "Order №%d for renting a %s %s has been accepted. "
                + "You have %d minutes to pay for your order in the \"Bills\" tab. "
                + "At %s the car reservation will be canceled", order.getId(),
            order.getCar().getModel().getBrand().getName(), order.getCar().getModel().getName(),
            orderDetails.getPaymentBillValidityPeriodInMinutes(),
            LocalDateTime.now().plusMinutes(orderDetails.getPaymentBillValidityPeriodInMinutes())
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))))
        .notificationType(NotificationType.ACCEPT)
        .sentDate(LocalDateTime.now())
        .status(NEW)
        .user(order.getUser())
        .build();
    notificationService.sendNotification(notification);

    order.setRentalStatus(NOT_STARTED);
    orderRepository.save(order);
    return "Success";
  }

  @Override
  @Transactional
  public String completeOrder(Long id) {
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
    return "Success";
  }

  @Override
  @Transactional
  public String completeOrderWithPenalty(Long id,
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
        .orderId(order.getId())
        .build());

    return "Success";
  }

  @Override
  @Transactional
  public String startRentalPeriod(Long id) {
    var order = findById(id);
    order.setRentalStatus(IN_PROCESS);
    orderRepository.save(order);
    return "Success";
  }

  @Override
  @Transactional
  public String cancelOrderAfterPayment(Long id, OrderRejectRequest orderRejectRequest) {
    var order = findById(id);
    order.setComments(orderRejectRequest.getComments());
    order.setRentalStatus(CANCELED);
    orderRepository.save(order);

    var notification = Notification
        .builder()
        .message(String.format(
            "Your order №%d for a %s %s has been canceled. "
                + "For a refund, contact the administration by e-mail, "
                + "or the phone number listed on the website.", order.getId(),
            order.getCar().getModel().getBrand().getName(), order.getCar().getModel().getName()))
        .notificationType(INFO)
        .sentDate(LocalDateTime.now())
        .status(NEW)
        .user(order.getUser())
        .build();
    notificationService.sendNotification(notification);
    return null;
  }

  @Override
  public int findNewOrdersAmountPerDay() {
    return orderRepository
        .countAllBySentDateAfter(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)));
  }

  @Override
  public String updatePaymentDateAndStatusToPaid(Order order) {
    order.setPaymentDate(LocalDateTime.now());
    order.setPaymentStatus(PAID);
    orderRepository.save(order);
    return "Success";
  }

  @Override
  public int findNewUserOrdersAmount(String email) {
    return orderRepository.countAllByUser_EmailAndRentalStatusAndPaymentStatus(email, NOT_STARTED,
        PAID);
  }

  @Override
  public Page<UserOrderResponse> findAllNewUserOrders(Pageable pageable, String language) {
    var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (Optional.ofNullable(user).isEmpty()) {
      log.error("User not authenticated!");
      throw new IllegalStateException("User not authenticated");
    }

    var orderPage = orderRepository.findAllByRentalStatusAndUser_EmailAndPaymentStatus(NOT_STARTED,
        user.getEmail(), PAID, pageable);

    List<UserOrderResponse> responses = new ArrayList<>();
    orderPage.forEach(order -> {
      if (!"en".equals(language)) {
        locationTranslationService.setTranslation(order.getLocation(), language);
      }
      responses.add(orderMapper.orderToNewUserOrderResponse(order));
    });
    return new PageImpl<>(responses, orderPage.getPageable(), orderPage.getTotalElements());
  }

  @Override
  public Page<UserOrderResponse> findAllUserOrdersHistory(Pageable pageable, String language) {
    var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (Optional.ofNullable(user).isEmpty()) {
      log.error("User not authenticated!");
      throw new IllegalStateException("User not authenticated");
    }

    var orderPage = orderRepository.findAllByRentalStatusOrRentalStatusAndUser_Email(FINISHED,
        FINISHED_WITH_PENALTY, user.getEmail(), pageable);
    List<UserOrderResponse> responses = new ArrayList<>();
    orderPage.forEach(order -> {
      if (!"en".equals(language)) {
        locationTranslationService.setTranslation(order.getLocation(), language);
      }
      responses.add(orderMapper.orderToNewUserOrderResponse(order));
    });
    return new PageImpl<>(responses, orderPage.getPageable(), orderPage.getTotalElements());
  }

  @Override
  public ByteArrayResource exportOrderToPDF(Long id) throws FontNotFoundException {
    var order = findById(id);
    var rentalDetails = rentalDetailsService.getRentalDetails();
    return pdfService.exportOrderToPDF(order, rentalDetails);
  }
}
