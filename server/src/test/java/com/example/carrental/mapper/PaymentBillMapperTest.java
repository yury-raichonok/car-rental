package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_TIME_FORMAT_PATTERN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PaymentBillMapperTest {

  private PaymentBill paymentBill;

  @Autowired
  private PaymentBillMapper paymentBillMapper;

  @BeforeEach
  public void setup() {
    var sentDate = LocalDateTime.now().minusDays(2);
    var paymentDate = LocalDateTime.now().minusDays(1);
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
        BigDecimal.valueOf(5)).paymentStatus(OrderPaymentStatus.PAID).sentDate(sentDate)
        .paymentDate(paymentDate).rentalStatus(OrderRentalStatus.NOT_STARTED).comments("comments")
        .denyingDate(denyingDate).car(car).location(location).user(user)
        .paymentBill(paymentBill).build();
    paymentBill = PaymentBill.builder().id(1L).totalCost(BigDecimal.valueOf(5))
        .sentDate(sentDate).expirationTime(sentDate.plusMinutes(60)).paymentDate(paymentDate)
        .order(order)
        .build();
  }

  @Test
  void paymentBillToPaymentBillResponse() {
    var paymentBillResponse = paymentBillMapper.paymentBillToPaymentBillResponse(paymentBill);

    assertThat(paymentBillResponse).isNotNull();
    assertThat(paymentBillResponse.getId()).isEqualTo(paymentBill.getId());
    assertThat(paymentBillResponse.getUserEmail())
        .isEqualTo(paymentBill.getOrder().getUser().getEmail());
    assertThat(paymentBillResponse.getSentDate()).isEqualTo(paymentBill.getSentDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(paymentBillResponse.getExpirationTime())
        .isEqualTo(paymentBill.getExpirationTime().format(
            DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(paymentBillResponse.getTotalCost()).isEqualTo(paymentBill.getTotalCost());
    assertThat(paymentBillResponse.getOrderId()).isEqualTo(paymentBill.getOrder().getId());
    assertThat(paymentBillResponse.getCarBrandModel()).isEqualTo(String.format("%s %s",
        paymentBill.getOrder().getCar().getModel().getBrand().getName(),
        paymentBill.getOrder().getCar().getModel().getName()));
    assertThat(paymentBillResponse.getCarVin()).isEqualTo(paymentBill.getOrder().getCar().getVin());
    assertThat(paymentBillResponse.getLocationName())
        .isEqualTo(paymentBill.getOrder().getLocation().getName());
    assertThat(paymentBillResponse.isStatus())
        .isEqualTo(!paymentBill.getExpirationTime().isBefore(LocalDateTime.now()));
    assertThat(paymentBillResponse.getPaymentDate()).isEqualTo(paymentBill.getPaymentDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
  }

  @Test
  void paymentBillToUserPaymentBillsResponse() {
    var userPaymentBillsResponse = paymentBillMapper
        .paymentBillToUserPaymentBillsResponse(paymentBill);

    assertThat(userPaymentBillsResponse).isNotNull();
    assertThat(userPaymentBillsResponse.getId()).isEqualTo(paymentBill.getId());
    assertThat(userPaymentBillsResponse.getSentDate()).isEqualTo(paymentBill.getSentDate()
        .format(DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(userPaymentBillsResponse.getTotalCost())
        .isEqualTo(paymentBill.getTotalCost().doubleValue());
    assertThat(userPaymentBillsResponse.getOrderId()).isEqualTo(paymentBill.getOrder().getId());
    assertThat(userPaymentBillsResponse.getCarBrandModel()).isEqualTo(String.format("%s %s",
        paymentBill.getOrder().getCar().getModel().getBrand().getName(),
        paymentBill.getOrder().getCar().getModel().getName()));
    assertThat(userPaymentBillsResponse.getCarVin())
        .isEqualTo(paymentBill.getOrder().getCar().getVin());
    assertThat(userPaymentBillsResponse.getLocationName())
        .isEqualTo(paymentBill.getOrder().getLocation().getName());
    assertThat(userPaymentBillsResponse.getPaymentDate())
        .isEqualTo(paymentBill.getPaymentDate().format(
            DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
  }

  @Test
  void paymentBillToUserNewPaymentBillsResponse() {
    var userNewPaymentBillsResponse = paymentBillMapper
        .paymentBillToUserNewPaymentBillsResponse(paymentBill);

    assertThat(userNewPaymentBillsResponse).isNotNull();
    assertThat(userNewPaymentBillsResponse.getId()).isEqualTo(paymentBill.getId());
    assertThat(userNewPaymentBillsResponse.getSentDate()).isEqualTo(paymentBill.getSentDate()
        .format(DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(userNewPaymentBillsResponse.getExpirationTime()).isEqualTo(
        paymentBill.getExpirationTime()
            .format(DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(userNewPaymentBillsResponse.getTotalCost())
        .isEqualTo(paymentBill.getTotalCost().doubleValue());
    assertThat(userNewPaymentBillsResponse.getOrderId()).isEqualTo(paymentBill.getOrder().getId());
    assertThat(userNewPaymentBillsResponse.getCarBrandModel()).isEqualTo(String.format("%s %s",
        paymentBill.getOrder().getCar().getModel().getBrand().getName(),
        paymentBill.getOrder().getCar().getModel().getName()));
    assertThat(userNewPaymentBillsResponse.getCarVin())
        .isEqualTo(paymentBill.getOrder().getCar().getVin());
    assertThat(userNewPaymentBillsResponse.getLocationName())
        .isEqualTo(paymentBill.getOrder().getLocation().getName());
  }

  @Test
  void formatCarBrandModel() {
    var carBrandModel = paymentBillMapper.formatCarBrandModel(paymentBill);

    assertThat(carBrandModel).isNotNull();
    assertThat(carBrandModel).isEqualTo(String.format("%s %s",
        paymentBill.getOrder().getCar().getModel().getBrand().getName(),
        paymentBill.getOrder().getCar().getModel().getName()));
  }

  @Test
  void getPaymentBillStatus() {
    var status = paymentBillMapper.getPaymentBillStatus(paymentBill);

    assertThat(status).isEqualTo(!paymentBill.getExpirationTime().isBefore(LocalDateTime.now()));
  }
}