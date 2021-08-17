package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_FORMAT_PATTERN;
import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_TIME_FORMAT_PATTERN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
class RepairBillMapperTest {

  private RepairBill repairBill;

  @Autowired
  private RepairBillMapper repairBillMapper;

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
        .denyingDate(denyingDate).repairBill(repairBill).car(car).location(location).user(user)
        .build();
    repairBill = RepairBill.builder().id(1L).totalCost(BigDecimal.valueOf(5)).order(order)
        .sentDate(sentDate).message("message").paymentDate(paymentDate).build();
  }

  @Test
  void repairBillToRepairBillResponse() {
    var repairBillResponse = repairBillMapper.repairBillToRepairBillResponse(repairBill);

    assertThat(repairBillResponse).isNotNull();
    assertThat(repairBillResponse.getId()).isEqualTo(repairBill.getId());
    assertThat(repairBillResponse.getUserEmail())
        .isEqualTo(repairBill.getOrder().getUser().getEmail());
    assertThat(repairBillResponse.getSentDate()).isEqualTo(repairBill.getSentDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_FORMAT_PATTERN)));
    assertThat(repairBillResponse.getTotalCost()).isEqualTo(repairBill.getTotalCost());
    assertThat(repairBillResponse.getMessage()).isEqualTo(repairBill.getMessage());
    assertThat(repairBillResponse.getOrderId()).isEqualTo(repairBill.getOrder().getId());
    assertThat(repairBillResponse.getCarBrandModel()).isEqualTo(
        String.format("%s %s", repairBill.getOrder().getCar().getModel().getBrand().getName(),
            repairBill.getOrder().getCar().getModel().getName()));
    assertThat(repairBillResponse.getCarVin()).isEqualTo(repairBill.getOrder().getCar().getVin());
    assertThat(repairBillResponse.getLocationName())
        .isEqualTo(repairBill.getOrder().getLocation().getName());
    assertThat(repairBillResponse.getPaymentDate()).isEqualTo(repairBill.getPaymentDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
  }

  @Test
  void repairBillToRepairBillNewResponse() {
    var repairBillNewResponse = repairBillMapper.repairBillToRepairBillNewResponse(repairBill);

    assertThat(repairBillNewResponse).isNotNull();
    assertThat(repairBillNewResponse.getId()).isEqualTo(repairBill.getId());
    assertThat(repairBillNewResponse.getSentDate()).isEqualTo(repairBill.getSentDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_FORMAT_PATTERN)));
    assertThat(repairBillNewResponse.getTotalCost()).isEqualTo(repairBill.getTotalCost());
    assertThat(repairBillNewResponse.getMessage()).isEqualTo(repairBill.getMessage());
    assertThat(repairBillNewResponse.getOrderId()).isEqualTo(repairBill.getOrder().getId());
    assertThat(repairBillNewResponse.getCarBrandModel()).isEqualTo(
        String.format("%s %s", repairBill.getOrder().getCar().getModel().getBrand().getName(),
            repairBill.getOrder().getCar().getModel().getName()));
    assertThat(repairBillNewResponse.getCarVin())
        .isEqualTo(repairBill.getOrder().getCar().getVin());
    assertThat(repairBillNewResponse.getLocationName())
        .isEqualTo(repairBill.getOrder().getLocation().getName());
  }

  @Test
  void repairBillToRepairBillHistoryResponse() {
    var repairBillHistoryResponse = repairBillMapper
        .repairBillToRepairBillHistoryResponse(repairBill);

    assertThat(repairBillHistoryResponse).isNotNull();
    assertThat(repairBillHistoryResponse.getId()).isEqualTo(repairBill.getId());
    assertThat(repairBillHistoryResponse.getSentDate()).isEqualTo(repairBill.getSentDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_FORMAT_PATTERN)));
    assertThat(repairBillHistoryResponse.getTotalCost()).isEqualTo(repairBill.getTotalCost());
    assertThat(repairBillHistoryResponse.getMessage()).isEqualTo(repairBill.getMessage());
    assertThat(repairBillHistoryResponse.getOrderId()).isEqualTo(repairBill.getOrder().getId());
    assertThat(repairBillHistoryResponse.getCarBrandModel()).isEqualTo(
        String.format("%s %s", repairBill.getOrder().getCar().getModel().getBrand().getName(),
            repairBill.getOrder().getCar().getModel().getName()));
    assertThat(repairBillHistoryResponse.getCarVin())
        .isEqualTo(repairBill.getOrder().getCar().getVin());
    assertThat(repairBillHistoryResponse.getLocationName())
        .isEqualTo(repairBill.getOrder().getLocation().getName());
    assertThat(repairBillHistoryResponse.getPaymentDate())
        .isEqualTo(repairBill.getPaymentDate().format(
            DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
  }

  @Test
  void formatCarBrandModel() {
    var carBrandModel = repairBillMapper.formatCarBrandModel(repairBill);

    assertThat(carBrandModel).isNotNull();
    assertThat(carBrandModel).isEqualTo(
        String.format("%s %s", repairBill.getOrder().getCar().getModel().getBrand().getName(),
            repairBill.getOrder().getCar().getModel().getName()));
  }
}