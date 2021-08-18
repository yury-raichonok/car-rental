package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.bill.PaymentBillSearchRequest;
import com.example.carrental.entity.bill.PaymentBill;
import com.example.carrental.entity.car.Car;
import com.example.carrental.entity.car.CarBodyType;
import com.example.carrental.entity.car.CarBrand;
import com.example.carrental.entity.car.CarClass;
import com.example.carrental.entity.car.CarEngineType;
import com.example.carrental.entity.car.CarModel;
import com.example.carrental.entity.location.Location;
import com.example.carrental.entity.order.Order;
import com.example.carrental.entity.order.OrderPaymentStatus;
import com.example.carrental.entity.order.OrderRentalStatus;
import com.example.carrental.entity.user.User;
import com.example.carrental.repository.PaymentBillCriteriaRepository;
import com.example.carrental.repository.PaymentBillRepository;
import com.example.carrental.service.LocationTranslationService;
import com.example.carrental.service.UserSecurityService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
class PaymentBillServiceImplTest {

  private PaymentBill paymentBill;

  @Autowired
  private PaymentBillServiceImpl paymentBillService;

  @MockBean
  private PaymentBillRepository paymentBillRepository;

  @MockBean
  private PaymentBillCriteriaRepository paymentBillCriteriaRepository;

  @MockBean
  private LocationTranslationService locationTranslationService;

  @MockBean
  private UserSecurityService userSecurityService;

  @BeforeEach
  public void setup() {
    var sentDate = LocalDateTime.now().minusDays(2);
    var pickUpDate = LocalDateTime.now().plusDays(1);
    var returnDate = LocalDateTime.now().plusDays(2);
    var denyingDate = LocalDateTime.now().plusDays(1);
    var user = User.builder().id(1L).email("test@gmail.com").build();
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
    var order = Order.builder().id(1L).pickUpDate(pickUpDate).returnDate(returnDate).totalCost(
        BigDecimal.valueOf(5)).paymentStatus(OrderPaymentStatus.NOT_PAID).sentDate(sentDate)
        .rentalStatus(OrderRentalStatus.NOT_STARTED).comments("comments")
        .denyingDate(denyingDate).car(car).location(location).user(user)
        .paymentBill(paymentBill).build();
    paymentBill = PaymentBill.builder().id(1L).totalCost(BigDecimal.valueOf(5))
        .sentDate(sentDate).expirationTime(LocalDateTime.now().plusMinutes(60)).order(order).build();
  }

  @Test
  void givenValidRequest_whenFindAll_thenSuccess() {
    var paymentBillSearchRequest = new PaymentBillSearchRequest();
    var response = new PageImpl<>(Collections.singletonList(paymentBill));
    when(paymentBillCriteriaRepository.findAll(paymentBillSearchRequest)).thenReturn(response);
    doNothing().when(locationTranslationService).setTranslation(any(), any());

    var paymentBillResponse = paymentBillService.findAll(paymentBillSearchRequest, ENGLISH);

    assertThat(paymentBillResponse).isNotNull();
    assertThat(paymentBillResponse.getTotalElements()).isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenFindAllNewUserBills_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    var response = new PageImpl<>(Collections.singletonList(paymentBill));
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(paymentBillRepository
        .findAllByExpirationTimeIsAfterAndOrder_UserEmailAndOrder_PaymentStatus(any(), any(), any(),
            any())).thenReturn(response);
    doNothing().when(locationTranslationService).setTranslation(any(), any());

    var userNewPaymentBillsResponse = paymentBillService.findAllNewUserBills(pageable, ENGLISH);

    assertThat(userNewPaymentBillsResponse).isNotNull();
    assertThat(userNewPaymentBillsResponse.getTotalElements())
        .isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenFindAllUserBillsHistory_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    var response = new PageImpl<>(Collections.singletonList(paymentBill));
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(paymentBillRepository
        .findAllByOrder_UserEmailAndPaymentDateNotNull(userEmail, pageable)).thenReturn(response);
    doNothing().when(locationTranslationService).setTranslation(any(), any());

    var userPaymentBillsResponse = paymentBillService.findAllUserBillsHistory(pageable, ENGLISH);

    assertThat(userPaymentBillsResponse).isNotNull();
    assertThat(userPaymentBillsResponse.getTotalElements()).isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenApproveWithoutPayment_thenSuccess() {
    when(paymentBillRepository.findById(1L)).thenReturn(Optional.of(paymentBill));
    when(paymentBillRepository.save(any(PaymentBill.class))).thenReturn(new PaymentBill());

    assertDoesNotThrow(() -> paymentBillService.approveWithoutPayment(1L));
  }

  @Test
  void givenValidRequest_whenPayBill_thenSuccess() {
    when(paymentBillRepository.findById(1L)).thenReturn(Optional.of(paymentBill));
    when(paymentBillRepository.save(any(PaymentBill.class))).thenReturn(new PaymentBill());

    assertDoesNotThrow(() -> paymentBillService.payBill(1L));
  }

  @Test
  void givenValidRequest_whenCreate_thenSuccess() {
    when(paymentBillRepository.save(any(PaymentBill.class))).thenReturn(new PaymentBill());

    assertDoesNotThrow(() -> paymentBillService.create(paymentBill.getOrder()));
  }

  @Test
  void givenValidRequest_whenFindById_thenSuccess() {
    when(paymentBillRepository.findById(1L)).thenReturn(Optional.of(paymentBill));

    var paymentBillResponse = paymentBillService.findById(1L);

    assertThat(paymentBillResponse).isNotNull();
    assertThat(paymentBillResponse.getId()).isEqualTo(paymentBill.getId());
    assertThat(paymentBillResponse.getExpirationTime()).isEqualTo(paymentBill.getExpirationTime());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindById_thenThrowIllegalStateException() {
    when(paymentBillRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> paymentBillService.findById(1L));
  }

  @Test
  void givenValidRequest_whenFindNewUserBillsAmount_thenSuccess() {
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(paymentBillRepository
        .countAllByExpirationTimeIsAfterAndOrder_UserEmailAndOrder_PaymentStatus(
            any(), any(), any())).thenReturn(8);

    var expectedAmount = 8;
    var amount = paymentBillService.findNewUserBillsAmount();

    assertEquals(expectedAmount, amount);
  }
}