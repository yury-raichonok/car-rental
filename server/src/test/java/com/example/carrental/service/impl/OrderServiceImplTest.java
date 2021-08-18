package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static com.example.carrental.constants.ApplicationConstants.HOUR_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY;
import static com.example.carrental.constants.ApplicationConstants.MINUTES_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY;
import static com.example.carrental.entity.order.OrderPaymentStatus.PAID;
import static com.example.carrental.entity.order.OrderRentalStatus.FINISHED;
import static com.example.carrental.entity.order.OrderRentalStatus.FINISHED_WITH_PENALTY;
import static com.example.carrental.entity.order.OrderRentalStatus.IN_PROCESS;
import static com.example.carrental.entity.order.OrderRentalStatus.NOT_STARTED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.bill.CreateRepairBillRequest;
import com.example.carrental.controller.dto.order.CreateOrderRequest;
import com.example.carrental.controller.dto.order.OrderCompleteWithPenaltyRequest;
import com.example.carrental.controller.dto.order.OrderRejectRequest;
import com.example.carrental.controller.dto.order.OrderSearchRequest;
import com.example.carrental.controller.dto.order.OrderTotalCostRequest;
import com.example.carrental.entity.bill.PaymentBill;
import com.example.carrental.entity.bill.RepairBill;
import com.example.carrental.entity.car.Car;
import com.example.carrental.entity.car.CarBodyType;
import com.example.carrental.entity.car.CarBrand;
import com.example.carrental.entity.car.CarClass;
import com.example.carrental.entity.car.CarEngineType;
import com.example.carrental.entity.car.CarModel;
import com.example.carrental.entity.location.Location;
import com.example.carrental.entity.notification.Notification;
import com.example.carrental.entity.order.Order;
import com.example.carrental.entity.order.OrderPaymentStatus;
import com.example.carrental.entity.order.OrderRentalStatus;
import com.example.carrental.entity.user.User;
import com.example.carrental.entity.user.UserDocumentStatus;
import com.example.carrental.entity.user.UserDrivingLicense;
import com.example.carrental.entity.user.UserPassport;
import com.example.carrental.entity.user.UserPhone;
import com.example.carrental.repository.OrderCriteriaRepository;
import com.example.carrental.repository.OrderRepository;
import com.example.carrental.service.CarService;
import com.example.carrental.service.EmailService;
import com.example.carrental.service.LocationTranslationService;
import com.example.carrental.service.NotificationService;
import com.example.carrental.service.PDFService;
import com.example.carrental.service.PaymentBillService;
import com.example.carrental.service.RepairBillService;
import com.example.carrental.service.UserSecurityService;
import com.example.carrental.service.UserService;
import com.example.carrental.service.exceptions.DocumentsNotConfirmedException;
import com.example.carrental.service.exceptions.OrderPeriodValidationException;
import com.example.carrental.service.exceptions.PhoneNotSpecifiedException;
import java.math.BigDecimal;
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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@SpringBootTest
@AutoConfigureMockMvc
class OrderServiceImplTest {

  private Order order;

  @Autowired
  private OrderServiceImpl orderService;

  @MockBean
  private CarService carService;

  @MockBean
  private EmailService emailService;

  @MockBean
  private LocationTranslationService locationTranslationService;

  @MockBean
  private NotificationService notificationService;

  @MockBean
  private OrderCriteriaRepository orderCriteriaRepository;

  @MockBean
  private OrderRepository orderRepository;

  @MockBean
  private PaymentBillService paymentBillService;

  @MockBean
  private PDFService pdfService;

  @MockBean
  private RepairBillService repairBillService;

  @MockBean
  private UserSecurityService userSecurityService;

  @MockBean
  private UserService userService;

  @BeforeEach
  public void setup() {
    var sentDate = LocalDateTime.now().minusDays(2);
    var paymentDate = LocalDateTime.now().minusDays(1);
    var pickUpDate = LocalDateTime.now().plusDays(1);
    var returnDate = LocalDateTime.now().plusDays(2);
    var denyingDate = LocalDateTime.now().plusDays(1);
    var user = User.builder().id(1L).email("test@gmail.com").build();
    var paymentBill = PaymentBill.builder().id(1L).totalCost(BigDecimal.valueOf(5))
        .sentDate(sentDate).expirationTime(sentDate.plusMinutes(60)).paymentDate(paymentDate)
        .build();
    var repairBill = RepairBill.builder().id(1L).totalCost(BigDecimal.valueOf(5))
        .sentDate(sentDate).paymentDate(paymentDate).build();
    var dateOfIssue = LocalDate.now();
    var carBrand = CarBrand.builder().name("brandName").build();
    var carModel = CarModel.builder().brand(carBrand).name("carModelName").build();
    var carClass = CarClass.builder().id(1L).name("carClassName").build();
    var location = Location.builder().id(1L).name("locationName").coordinateX(1).coordinateY(2)
        .build();
    var car = Car.builder().id(1L).vin("vin").model(carModel).carClass(carClass)
        .dateOfIssue(dateOfIssue)
        .bodyType(CarBodyType.SEDAN).isAutomaticTransmission(true).color("color")
        .engineType(CarEngineType.DIESEL).passengersAmt(5).baggageAmt(3).hasConditioner(false)
        .costPerHour(BigDecimal.valueOf(10)).location(location).carImageLink("carImageLink")
        .inRental(true).build();
    order = Order.builder().id(1L).pickUpDate(pickUpDate).returnDate(returnDate).totalCost(
        BigDecimal.valueOf(5)).paymentStatus(OrderPaymentStatus.PAID).sentDate(sentDate)
        .paymentDate(paymentDate).rentalStatus(OrderRentalStatus.NOT_STARTED).comments("comments")
        .denyingDate(denyingDate).repairBill(repairBill).car(car).location(location).user(user)
        .paymentBill(paymentBill).build();
  }

  @Test
  void givenValidRequest_whenFindAllCurrent_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    var response = new PageImpl<>(Collections.singletonList(order));
    when(orderRepository.findAllByRentalStatus(IN_PROCESS, pageable)).thenReturn(response);
    doNothing().when(locationTranslationService).setTranslation(any(), any());

    var orderInformationResponse = orderService.findAllCurrent(pageable, ENGLISH);

    assertThat(orderInformationResponse).isNotNull();
    assertThat(orderInformationResponse.getTotalElements()).isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenFindAllFuture_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    var response = new PageImpl<>(Collections.singletonList(order));
    when(orderRepository.findAllByRentalStatusAndPaymentStatus(NOT_STARTED, PAID, pageable))
        .thenReturn(response);
    doNothing().when(locationTranslationService).setTranslation(any(), any());

    var orderInformationResponse = orderService.findAllFuture(pageable, ENGLISH);

    assertThat(orderInformationResponse).isNotNull();
    assertThat(orderInformationResponse.getTotalElements()).isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenFindAllNew_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    var response = new PageImpl<>(Collections.singletonList(order));
    when(orderRepository.findAllByRentalStatus(OrderRentalStatus.NEW, pageable))
        .thenReturn(response);
    doNothing().when(locationTranslationService).setTranslation(any(), any());

    var orderInformationResponse = orderService.findAllNew(pageable, ENGLISH);

    assertThat(orderInformationResponse).isNotNull();
    assertThat(orderInformationResponse.getTotalElements()).isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenFindAllNewUserOrders_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    var response = new PageImpl<>(Collections.singletonList(order));
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(orderRepository.findAllByRentalStatusAndUser_EmailAndPaymentStatus(NOT_STARTED,
        userEmail, PAID, pageable)).thenReturn(response);
    doNothing().when(locationTranslationService).setTranslation(any(), any());

    var orderInformationResponse = orderService.findAllNewUserOrders(pageable, ENGLISH);

    assertThat(orderInformationResponse).isNotNull();
    assertThat(orderInformationResponse.getTotalElements()).isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenFindAllNewUserOrdersHistory_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    var response = new PageImpl<>(Collections.singletonList(order));
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(orderRepository.findAllByRentalStatusOrRentalStatusAndUser_Email(FINISHED,
        FINISHED_WITH_PENALTY, userEmail, pageable)).thenReturn(response);
    doNothing().when(locationTranslationService).setTranslation(any(), any());

    var orderInformationResponse = orderService.findAllUserOrdersHistory(pageable, ENGLISH);

    assertThat(orderInformationResponse).isNotNull();
    assertThat(orderInformationResponse.getTotalElements()).isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenCalculateTotalCost_thenSuccess() {
    var orderTotalCostRequest = OrderTotalCostRequest.builder().costPerHour(5).pickUpDate(
        LocalDateTime.now().plusHours(1L)).returnDate(LocalDateTime.now().plusHours(2L)).build();

    var expectedCost = 5;
    var totalCost = orderService.calculateTotalCost(orderTotalCostRequest);

    assertEquals(expectedCost, totalCost.getTotalCost().doubleValue());
  }

  @Test
  void givenInvalidRequestWithPickUpDateAfterReturnDate_whenCalculateTotalCost_thenThrowOrderPeriodValidationException() {
    var orderTotalCostRequest = OrderTotalCostRequest.builder().costPerHour(5).pickUpDate(
        LocalDateTime.now().plusHours(2L)).returnDate(LocalDateTime.now().plusHours(1L)).build();

    assertThrows(OrderPeriodValidationException.class,
        () -> orderService.calculateTotalCost(orderTotalCostRequest));
  }

  @Test
  void givenInvalidRequestWithPickUpDateSameAsReturnDate_whenCalculateTotalCost_thenThrowOrderPeriodValidationException() {
    var orderTotalCostRequest = OrderTotalCostRequest.builder().costPerHour(5).pickUpDate(
        LocalDateTime.now().plusHours(1L)).returnDate(LocalDateTime.now().plusHours(1L)).build();

    assertThrows(OrderPeriodValidationException.class,
        () -> orderService.calculateTotalCost(orderTotalCostRequest));
  }

  @Test
  void givenValidRequest_whenCreate_thenSuccess() {
    var userPassport = UserPassport.builder().id(1L).status(UserDocumentStatus.CONFIRMED).build();
    var userDrivingLicense = UserDrivingLicense.builder().id(1L)
        .status(UserDocumentStatus.CONFIRMED).build();
    var userPhone = UserPhone.builder().active(true).build();
    var user = User.builder().id(1L).email("email@gmail.com").passport(userPassport)
        .drivingLicense(userDrivingLicense).phones(Collections.singletonList(userPhone)).build();
    var createOrderRequest = CreateOrderRequest.builder().pickUpDate(LocalDateTime.now()
        .plusHours(1L)).returnDate(LocalDateTime.now().plusHours(2L)).totalCost(5).carId(1L)
        .build();
    var userEmail = "email@gmail.com";
    when(carService.findById(createOrderRequest.getCarId())).thenReturn(order.getCar());
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(userService.findUserByEmail(userEmail)).thenReturn(user);
    when(orderRepository.save(any(Order.class))).thenReturn(new Order());

    assertDoesNotThrow(() -> orderService.create(createOrderRequest));
  }

  @Test
  void givenInvalidRequestWithNotConfirmedPassport_whenCreate_thenThrowDocumentsNotConfirmedException() {
    var userPassport = UserPassport.builder().id(1L).status(UserDocumentStatus.NOT_CONFIRMED)
        .build();
    var userDrivingLicense = UserDrivingLicense.builder().id(1L)
        .status(UserDocumentStatus.CONFIRMED).build();
    var userPhone = UserPhone.builder().active(true).build();
    var user = User.builder().id(1L).email("email@gmail.com").passport(userPassport)
        .drivingLicense(userDrivingLicense).phones(Collections.singletonList(userPhone)).build();
    var createOrderRequest = CreateOrderRequest.builder().pickUpDate(LocalDateTime.now()
        .plusHours(1L)).returnDate(LocalDateTime.now().plusHours(2L)).totalCost(5).carId(1L)
        .build();
    var userEmail = "email@gmail.com";
    when(carService.findById(createOrderRequest.getCarId())).thenReturn(order.getCar());
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(userService.findUserByEmail(userEmail)).thenReturn(user);
    when(orderRepository.save(any(Order.class))).thenReturn(new Order());

    assertThrows(DocumentsNotConfirmedException.class,
        () -> orderService.create(createOrderRequest));
  }

  @Test
  void givenInvalidRequestWithNotConfirmedDrivingLicense_whenCreate_thenThrowDocumentsNotConfirmedException() {
    var userPassport = UserPassport.builder().id(1L).status(UserDocumentStatus.CONFIRMED).build();
    var userDrivingLicense = UserDrivingLicense.builder().id(1L)
        .status(UserDocumentStatus.NOT_CONFIRMED).build();
    var userPhone = UserPhone.builder().active(true).build();
    var user = User.builder().id(1L).email("email@gmail.com").passport(userPassport)
        .drivingLicense(userDrivingLicense).phones(Collections.singletonList(userPhone)).build();
    var createOrderRequest = CreateOrderRequest.builder().pickUpDate(LocalDateTime.now()
        .plusHours(1L)).returnDate(LocalDateTime.now().plusHours(2L)).totalCost(5).carId(1L)
        .build();
    var userEmail = "email@gmail.com";
    when(carService.findById(createOrderRequest.getCarId())).thenReturn(order.getCar());
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(userService.findUserByEmail(userEmail)).thenReturn(user);
    when(orderRepository.save(any(Order.class))).thenReturn(new Order());

    assertThrows(DocumentsNotConfirmedException.class,
        () -> orderService.create(createOrderRequest));
  }

  @Test
  void givenInvalidRequestWithNotSpecifiedPhones_whenCreate_thenThrowPhoneNotSpecifiedException() {
    var userPassport = UserPassport.builder().id(1L).status(UserDocumentStatus.CONFIRMED).build();
    var userDrivingLicense = UserDrivingLicense.builder().id(1L)
        .status(UserDocumentStatus.CONFIRMED).build();
    var user = User.builder().id(1L).email("email@gmail.com").passport(userPassport)
        .drivingLicense(userDrivingLicense).build();
    var createOrderRequest = CreateOrderRequest.builder().pickUpDate(LocalDateTime.now()
        .plusHours(1L)).returnDate(LocalDateTime.now().plusHours(2L)).totalCost(5).carId(1L)
        .build();
    var userEmail = "email@gmail.com";
    when(carService.findById(createOrderRequest.getCarId())).thenReturn(order.getCar());
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(userService.findUserByEmail(userEmail)).thenReturn(user);
    when(orderRepository.save(any(Order.class))).thenReturn(new Order());

    assertThrows(PhoneNotSpecifiedException.class, () -> orderService.create(createOrderRequest));
  }

  @Test
  void givenValidRequest_whenFindAll_thenSuccess() {
    var orderSearchRequest = new OrderSearchRequest();
    var response = new PageImpl<>(Collections.singletonList(order));
    when(orderCriteriaRepository.findAll(orderSearchRequest)).thenReturn(response);
    doNothing().when(locationTranslationService).setTranslation(any(), any());
    when(orderRepository.save(any(Order.class))).thenReturn(new Order());

    var orderResponse = orderService.findAll(orderSearchRequest, ENGLISH);

    assertThat(orderResponse).isNotNull();
    assertThat(orderResponse.getTotalElements()).isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenApproveOrder_thenSuccess() {
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    doNothing().when(paymentBillService).create(any(Order.class));
    doNothing().when(notificationService).sendNotification(any(Notification.class));
    doNothing().when(emailService).sendEmail(any(), any(), any());
    when(orderRepository.save(any(Order.class))).thenReturn(new Order());

    assertDoesNotThrow(() -> orderService.approveOrder(1L));
  }

  @Test
  void givenValidRequest_whenCancelOrderAfterPayment_thenSuccess() {
    var orderRejectRequest = OrderRejectRequest.builder().comments("name").build();
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(orderRepository.save(any(Order.class))).thenReturn(new Order());
    doNothing().when(notificationService).sendNotification(any(Notification.class));
    doNothing().when(emailService).sendEmail(any(), any(), any());

    assertDoesNotThrow(() -> orderService.cancelOrderAfterPayment(1L, orderRejectRequest));
  }

  @Test
  void givenValidRequest_whenCompleteOrder_thenSuccess() {
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(orderRepository.save(any(Order.class))).thenReturn(new Order());
    doNothing().when(notificationService).sendNotification(any(Notification.class));

    assertDoesNotThrow(() -> orderService.completeOrder(1L));
  }

  @Test
  void givenValidRequest_whenCompleteOrderWithPenalty_thenSuccess() {
    var orderCompleteWithPenaltyRequest = OrderCompleteWithPenaltyRequest.builder()
        .message("message").totalCost(5).build();
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(orderRepository.save(any(Order.class))).thenReturn(new Order());
    doNothing().when(notificationService).sendNotification(any(Notification.class));
    doNothing().when(repairBillService).create(any(CreateRepairBillRequest.class));

    assertDoesNotThrow(
        () -> orderService.completeOrderWithPenalty(1L, orderCompleteWithPenaltyRequest));
  }

  @Test
  void givenValidRequest_whenRejectOrder_thenSuccess() {
    var orderRejectRequest = OrderRejectRequest.builder().comments("name").build();
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    doNothing().when(notificationService).sendNotification(any(Notification.class));
    doNothing().when(emailService).sendEmail(any(), any(), any());
    when(orderRepository.save(any(Order.class))).thenReturn(new Order());

    assertDoesNotThrow(() -> orderService.rejectOrder(1L, orderRejectRequest));
  }

  @Test
  void givenValidRequest_whenStartRentalPeriod_thenSuccess() {
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(orderRepository.save(any(Order.class))).thenReturn(new Order());

    assertDoesNotThrow(() -> orderService.startRentalPeriod(1L));
  }

  @Test
  void givenValidRequest_whenUpdate_thenSuccess() {
    var createOrderRequest = CreateOrderRequest.builder().pickUpDate(LocalDateTime.now()
        .plusDays(1)).returnDate(LocalDateTime.now().plusDays(2)).totalCost(4).carId(1).build();
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(carService.findById(createOrderRequest.getCarId())).thenReturn(order.getCar());
    doNothing().when(locationTranslationService).update(any(), any());
    when(orderRepository.save(any(Order.class))).thenReturn(new Order());

    assertDoesNotThrow(() -> orderService.update(1L, createOrderRequest));
  }

  @Test
  void givenValidRequest_whenExportOrderToPDF_thenSuccess() {
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(pdfService.exportOrderToPDF(order)).thenReturn(new ByteArrayResource(new byte[0]));

    assertDoesNotThrow(() -> orderService.exportOrderToPDF(1L));
  }

  @Test
  void givenValidRequest_whenFindById_thenSuccess() {
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

    var orderResponse = orderService.findById(1L);

    assertThat(orderResponse).isNotNull();
    assertThat(orderResponse.getId()).isEqualTo(order.getId());
    assertThat(orderResponse.getReturnDate()).isEqualTo(order.getReturnDate());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindById_thenThrowIllegalStateException() {
    when(orderRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> orderService.findById(1L));
  }

  @Test
  void givenValidRequest_whenFindNewOrdersAmount_thenSuccess() {
    when(orderRepository.countAllByRentalStatus(OrderRentalStatus.NEW)).thenReturn(8);

    var expectedAmount = 8;
    var amount = orderService.findNewOrdersAmount();

    assertEquals(expectedAmount, amount);
  }

  @Test
  void givenValidRequest_whenFindNewOrdersAmountPerDay_thenSuccess() {
    when(orderRepository.countAllBySentDateAfter(LocalDateTime.of(LocalDate.now(),
        LocalTime.of(HOUR_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY,
            MINUTES_OF_START_OF_COUNTING_STATISTIC_FOR_THE_DAY)))).thenReturn(8);

    var expectedAmount = 8;
    var amount = orderService.findNewOrdersAmountPerDay();

    assertEquals(expectedAmount, amount);
  }

  @Test
  void givenValidRequest_whenFindUserOrdersAmount_thenSuccess() {
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(orderRepository.countAllByUser_EmailAndRentalStatusAndPaymentStatus(userEmail,
        NOT_STARTED, PAID)).thenReturn(8);

    var expectedAmount = 8;
    var amount = orderService.findUserOrdersAmount();

    assertEquals(expectedAmount, amount);
  }
}