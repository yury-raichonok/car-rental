package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.DAY_HOURS;
import static com.example.carrental.constants.ApplicationConstants.WEEK_HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.order.OrderTotalCostRequest;
import com.example.carrental.entity.location.Location;
import com.example.carrental.entity.rentalDetails.RentalDetails;
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
import com.example.carrental.service.UserSecurityService;
import com.example.carrental.service.UserService;
import com.example.carrental.service.exceptions.OrderPeriodValidationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class OrderServiceImplTest {

  private static final Long TEST_USER_ID = 1L;
  private static final String TEST_EMAIL = "test@gmail.com";
  private static final String TEST_PHONE = "+375111111111";
  private static final double TEST_FROM_DAY_TO_WEEK_COEFFICIENT = 0.9;
  private static final double TEST_FROM_WEEK_COEFFICIENT = 0.8;
  private static final double TEST_COST_PER_HOUR = 10;
  private static final int TEST_PAYMENT_BILL_VALIDITY_PERIOD = 60;
  private static RentalDetails rentalDetails;
  private static Location location;
  @Autowired
  private OrderService orderService;
  @MockBean
  private OrderRepository orderRepository;
  @MockBean
  private NotificationService notificationService;
  @MockBean
  private UserService userService;
  @MockBean
  private OrderCriteriaRepository orderCriteriaRepository;
  @MockBean
  private CarService carService;
  @MockBean
  private OrderMapper orderMapper;
  @MockBean
  private PDFService pdfService;
  @MockBean
  private LocationTranslationService locationTranslationService;
  @MockBean
  private UserSecurityService userSecurityService;
  @MockBean
  private PaymentBillService paymentBillService;
  @MockBean
  private RepairBillService repairBillService;
  @MockBean
  private RentalDetailsService rentalDetailsService;

  @BeforeAll
  public static void setUp() {
    location = Location.builder().build();
    rentalDetails = RentalDetails.builder().id(TEST_USER_ID).email(TEST_EMAIL)
        .phone(TEST_PHONE).location(location)
        .fromDayToWeekCoefficient(BigDecimal.valueOf(TEST_FROM_DAY_TO_WEEK_COEFFICIENT))
        .fromWeekCoefficient(BigDecimal.valueOf(TEST_FROM_WEEK_COEFFICIENT))
        .paymentBillValidityPeriodInMinutes(TEST_PAYMENT_BILL_VALIDITY_PERIOD)
        .build();
  }

  @Test
  void givenOneHourRentalPeriod_whenCalculateTotalCost_thenReturnValidValue()
      throws OrderPeriodValidationException {
    when(rentalDetailsService.getRentalDetails()).thenReturn(rentalDetails);
    var calculateTotalCostRequest = OrderTotalCostRequest.builder().costPerHour(TEST_COST_PER_HOUR)
        .pickUpDate(LocalDateTime.now()).returnDate(LocalDateTime.now().plusHours(1L)).build();

    var response = orderService.calculateTotalCost(calculateTotalCostRequest);
    assertEquals(TEST_COST_PER_HOUR, response.getTotalCost().doubleValue());
  }



  @Test
  void givenOneDayRentalPeriod_whenCalculateTotalCost_thenReturnValidValue()
      throws OrderPeriodValidationException {
    when(rentalDetailsService.getRentalDetails()).thenReturn(rentalDetails);
    var calculateTotalCostRequest = OrderTotalCostRequest.builder().costPerHour(TEST_COST_PER_HOUR)
        .pickUpDate(LocalDateTime.now()).returnDate(LocalDateTime.now().plusHours(DAY_HOURS))
        .build();
    var response = orderService.calculateTotalCost(calculateTotalCostRequest);
    assertEquals(TEST_COST_PER_HOUR * TEST_FROM_DAY_TO_WEEK_COEFFICIENT * DAY_HOURS,
        response.getTotalCost().doubleValue());
  }

  @Test
  void givenOneWeekRentalPeriod_whenCalculateTotalCost_thenReturnValidValue()
      throws OrderPeriodValidationException {
    when(rentalDetailsService.getRentalDetails()).thenReturn(rentalDetails);
    var calculateTotalCostRequest = OrderTotalCostRequest.builder().costPerHour(TEST_COST_PER_HOUR)
        .pickUpDate(LocalDateTime.now()).returnDate(LocalDateTime.now().plusHours(WEEK_HOURS))
        .build();
    var response = orderService.calculateTotalCost(calculateTotalCostRequest);
    assertEquals(TEST_COST_PER_HOUR * TEST_FROM_WEEK_COEFFICIENT * WEEK_HOURS,
        response.getTotalCost().doubleValue());
  }

  @Test
  void givenSamePickUpAndReturnDate_whenCalculateTotalCost_thenThrowOrderPeriodValidationException() {
    when(rentalDetailsService.getRentalDetails()).thenReturn(rentalDetails);
    var calculateTotalCostRequest = OrderTotalCostRequest.builder().costPerHour(TEST_COST_PER_HOUR)
        .pickUpDate(LocalDateTime.now()).returnDate(LocalDateTime.now()).build();
    assertThrows(OrderPeriodValidationException.class,
        () -> orderService.calculateTotalCost(calculateTotalCostRequest));
  }

  @Test
  void givenReturnDateBeforePickUpDate_whenCalculateTotalCost_thenThrowOrderPeriodValidationException() {
    when(rentalDetailsService.getRentalDetails()).thenReturn(rentalDetails);
    var calculateTotalCostRequest = OrderTotalCostRequest.builder().costPerHour(TEST_COST_PER_HOUR)
        .pickUpDate(LocalDateTime.now()).returnDate(LocalDateTime.now().minusHours(DAY_HOURS))
        .build();
    assertThrows(OrderPeriodValidationException.class,
        () -> orderService.calculateTotalCost(calculateTotalCostRequest));
  }
}