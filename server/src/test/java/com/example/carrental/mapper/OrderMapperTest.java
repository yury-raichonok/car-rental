package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_TIME_FORMAT_PATTERN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.carrental.entity.bill.PaymentBill;
import com.example.carrental.entity.bill.RepairBill;
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
class OrderMapperTest {

  private Order order;

  @Autowired
  private OrderMapper orderMapper;

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
  void orderToOrderResponse() {
    var orderResponse = orderMapper.orderToOrderResponse(order);

    assertThat(orderResponse).isNotNull();
    assertThat(orderResponse.getId()).isEqualTo(order.getId());
    assertThat(orderResponse.getUserEmail()).isEqualTo(order.getUser().getEmail());
    assertThat(orderResponse.getPickUpDate()).isEqualTo(order.getPickUpDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(orderResponse.getReturnDate()).isEqualTo(order.getReturnDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(orderResponse.getTotalCost()).isEqualTo(order.getTotalCost());
    assertThat(orderResponse.getPaymentStatus()).isEqualTo(order.getPaymentStatus().getStatus());
    assertThat(orderResponse.getComments()).isEqualTo(order.getComments());
    assertThat(orderResponse.getSentDate()).isEqualTo(order.getSentDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(orderResponse.getCarBrandModel()).isEqualTo(String.format("%s %s",
        order.getCar().getModel().getBrand().getName(), order.getCar().getModel().getName()));
    assertThat(orderResponse.getRentalStatus()).isEqualTo(order.getRentalStatus().getStatus());
    assertThat(orderResponse.getCarVin()).isEqualTo(order.getCar().getVin());
    assertThat(orderResponse.getLocationName()).isEqualTo(order.getLocation().getName());
    assertThat(orderResponse.getPaymentDate()).isEqualTo(order.getPaymentDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(orderResponse.getDenyingDate()).isEqualTo(order.getDenyingDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(orderResponse.getPaymentBillId()).isEqualTo(order.getPaymentBill().getId());
    assertThat(orderResponse.getRepairBillId()).isEqualTo(order.getRepairBill().getId());
  }

  @Test
  void orderToOrderNewResponse() {
    var orderNewResponse = orderMapper.orderToOrderNewResponse(order);

    assertThat(orderNewResponse).isNotNull();
    assertThat(orderNewResponse.getId()).isEqualTo(order.getId());
    assertThat(orderNewResponse.getUserId()).isEqualTo(order.getUser().getId());
    assertThat(orderNewResponse.getUserEmail()).isEqualTo(order.getUser().getEmail());
    assertThat(orderNewResponse.getCarBrandModel()).isEqualTo(String
        .format("%s %s", order.getCar().getModel().getBrand().getName(),
            order.getCar().getModel().getName()));
    assertThat(orderNewResponse.getCarVin()).isEqualTo(order.getCar().getVin());
    assertThat(orderNewResponse.getLocationName()).isEqualTo(order.getLocation().getName());
    assertThat(orderNewResponse.getPickUpDate()).isEqualTo(order.getPickUpDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(orderNewResponse.getReturnDate()).isEqualTo(order.getReturnDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(orderNewResponse.getTotalCost()).isEqualTo(order.getTotalCost());
    assertThat(orderNewResponse.getSentDate()).isEqualTo(order.getSentDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
  }

  @Test
  void orderToOrderInformationResponse() {
    var orderInformationResponse = orderMapper.orderToOrderInformationResponse(order);

    assertThat(orderInformationResponse).isNotNull();
    assertThat(orderInformationResponse.getId()).isEqualTo(order.getId());
    assertThat(orderInformationResponse.getUserId()).isEqualTo(order.getUser().getId());
    assertThat(orderInformationResponse.getUserEmail()).isEqualTo(order.getUser().getEmail());
    assertThat(orderInformationResponse.getPickUpDate()).isEqualTo(order.getPickUpDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(orderInformationResponse.getReturnDate()).isEqualTo(order.getReturnDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(orderInformationResponse.getTotalCost()).isEqualTo(order.getTotalCost());
    assertThat(orderInformationResponse.getPaymentStatus())
        .isEqualTo(order.getPaymentStatus().getStatus());
    assertThat(orderInformationResponse.getSentDate()).isEqualTo(order.getSentDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(orderInformationResponse.getPaymentDate()).isEqualTo(order.getPaymentDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(orderInformationResponse.getPaymentBillId())
        .isEqualTo(order.getPaymentBill().getId());
    assertThat(orderInformationResponse.getCarBrandModel()).isEqualTo(String
        .format("%s %s", order.getCar().getModel().getBrand().getName(),
            order.getCar().getModel().getName()));
    assertThat(orderInformationResponse.getCarVin()).isEqualTo(order.getCar().getVin());
    assertThat(orderInformationResponse.getLocationName()).isEqualTo(order.getLocation().getName());
  }

  @Test
  void orderToNewUserOrderResponse() {
    var newUserOrderResponse = orderMapper.orderToNewUserOrderResponse(order);

    assertThat(newUserOrderResponse).isNotNull();
    assertThat(newUserOrderResponse.getId()).isEqualTo(order.getId());
    assertThat(newUserOrderResponse.getPickUpDate()).isEqualTo(order.getPickUpDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(newUserOrderResponse.getReturnDate()).isEqualTo(order.getReturnDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(newUserOrderResponse.getTotalCost()).isEqualTo(order.getTotalCost());
    assertThat(newUserOrderResponse.getSentDate()).isEqualTo(order.getSentDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(newUserOrderResponse.getCarBrandModel()).isEqualTo(String
        .format("%s %s", order.getCar().getModel().getBrand().getName(),
            order.getCar().getModel().getName()));
    assertThat(newUserOrderResponse.getCarVin()).isEqualTo(order.getCar().getVin());
    assertThat(newUserOrderResponse.getLocationName()).isEqualTo(order.getLocation().getName());
  }

  @Test
  void formatCarBrandModel() {
    var carBrandModel = orderMapper.formatCarBrandModel(order);

    assertThat(carBrandModel).isNotNull();
    assertThat(carBrandModel).isEqualTo(String.format("%s %s", order.getCar().getModel()
        .getBrand().getName(), order.getCar().getModel().getName()));
  }
}