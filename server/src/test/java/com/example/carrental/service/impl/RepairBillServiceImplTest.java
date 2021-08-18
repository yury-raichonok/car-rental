package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.bill.CreateRepairBillRequest;
import com.example.carrental.controller.dto.bill.RepairBillSearchRequest;
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
import com.example.carrental.repository.RepairBillCriteriaRepository;
import com.example.carrental.repository.RepairBillRepository;
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
class RepairBillServiceImplTest {

  private RepairBill repairBill;

  @Autowired
  private RepairBillServiceImpl repairBillService;

  @MockBean
  private RepairBillRepository repairBillRepository;

  @MockBean
  private RepairBillCriteriaRepository repairBillCriteriaRepository;

  @MockBean
  private LocationTranslationService locationTranslationService;

  @MockBean
  private UserSecurityService userSecurityService;

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
  void givenValidRequest_whenFindAll_thenSuccess() {
    var repairBillSearchRequest = new RepairBillSearchRequest();
    var response = new PageImpl<>(Collections.singletonList(repairBill));
    when(repairBillCriteriaRepository.findAll(repairBillSearchRequest)).thenReturn(response);
    doNothing().when(locationTranslationService).setTranslation(any(), any());

    var repairBillResponse = repairBillService.findAll(repairBillSearchRequest, ENGLISH);

    assertThat(repairBillResponse).isNotNull();
    assertThat(repairBillResponse.getTotalElements()).isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenCreate_thenSuccess() {
    var createRepairBillRequest = CreateRepairBillRequest.builder().message("message")
        .totalCost(BigDecimal.valueOf(5)).order(repairBill.getOrder()).build();
    when(repairBillRepository.save(any(RepairBill.class))).thenReturn(new RepairBill());

    assertDoesNotThrow(() -> repairBillService.create(createRepairBillRequest));
  }

  @Test
  void givenValidRequest_whenFindAllUserBillsHistory_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    var response = new PageImpl<>(Collections.singletonList(repairBill));
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(repairBillRepository.findAllByOrder_UserEmailAndPaymentDateNotNull(userEmail,
        pageable)).thenReturn(response);
    doNothing().when(locationTranslationService).setTranslation(any(), any());

    var repairBillHistoryResponse = repairBillService.findAllUserBillsHistory(pageable, ENGLISH);

    assertThat(repairBillHistoryResponse).isNotNull();
    assertThat(repairBillHistoryResponse.getTotalElements()).isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenFindAllNewUserBills_thenSuccess() {
    var pageable = Pageable.ofSize(10).withPage(0);
    var response = new PageImpl<>(Collections.singletonList(repairBill));
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(repairBillRepository.findAllByOrder_UserEmailAndPaymentDateNull(userEmail,
        pageable)).thenReturn(response);
    doNothing().when(locationTranslationService).setTranslation(any(), any());

    var repairBillNewResponse = repairBillService.findAllNewUserBills(pageable, ENGLISH);

    assertThat(repairBillNewResponse).isNotNull();
    assertThat(repairBillNewResponse.getTotalElements()).isEqualTo(response.getTotalElements());
  }

  @Test
  void givenValidRequest_whenPayBill_thenSuccess() {
    when(repairBillRepository.findById(1L)).thenReturn(Optional.of(repairBill));
    when(repairBillRepository.save(any(RepairBill.class))).thenReturn(new RepairBill());

    assertDoesNotThrow(() -> repairBillService.payBill(1L));
  }

  @Test
  void givenValidRequest_whenFindNewUserRepairBillsAmount_thenSuccess() {
    var userEmail = "email@gmail.com";
    when(userSecurityService.getUserEmailFromSecurityContext()).thenReturn(userEmail);
    when(repairBillRepository.countAllByOrder_UserEmailAndPaymentDateNull(userEmail)).thenReturn(8);

    var expectedAmount = 8;
    var amount = repairBillService.findNewUserRepairBillsAmount();

    assertEquals(expectedAmount, amount);
  }

  @Test
  void givenValidRequest_whenFindById_thenSuccess() {
    when(repairBillRepository.findById(1L)).thenReturn(Optional.of(repairBill));

    var response = repairBillService.findById(1L);

    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(repairBill.getId());
    assertThat(response.getSentDate()).isEqualTo(repairBill.getSentDate());
  }

  @Test
  void givenRequestWithNotExistingId_whenFindById_thenThrowIllegalStateException() {
    when(repairBillRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> repairBillService.findById(1L));
  }
}