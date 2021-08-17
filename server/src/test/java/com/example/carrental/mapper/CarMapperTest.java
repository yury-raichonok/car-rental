package com.example.carrental.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.carrental.entity.car.Car;
import com.example.carrental.entity.car.CarBodyType;
import com.example.carrental.entity.car.CarBrand;
import com.example.carrental.entity.car.CarClass;
import com.example.carrental.entity.car.CarEngineType;
import com.example.carrental.entity.car.CarModel;
import com.example.carrental.entity.location.Location;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CarMapperTest {

  private Car car;

  @Autowired
  private CarMapper carMapper;

  @BeforeEach
  public void setup() {
    LocalDate dateOfIssue = LocalDate.now();
    CarBrand carBrand = CarBrand.builder().name("brandName").build();
    CarModel carModel = CarModel.builder().brand(carBrand).name("carModelName").build();
    CarClass carClass = CarClass.builder().id(1L).name("carClassName").build();
    Location location = Location.builder().id(1L).name("locationName").coordinateX(1).coordinateY(2)
        .build();
    car = Car.builder().id(1L).vin("vin").model(carModel).carClass(carClass)
        .dateOfIssue(dateOfIssue)
        .bodyType(CarBodyType.SEDAN).isAutomaticTransmission(true).color("color")
        .engineType(CarEngineType.DIESEL).passengersAmt(5).baggageAmt(3).hasConditioner(false)
        .costPerHour(BigDecimal.valueOf(10)).location(location).carImageLink("carImageLink")
        .inRental(true).build();
  }

  @Test
  void carToCarSearchResponse() {
    var carSearchResponse = carMapper.carToCarSearchResponse(car);

    assertThat(carSearchResponse).isNotNull();
    assertThat(carSearchResponse.getId()).isEqualTo(car.getId());
    assertThat(carSearchResponse.getBrand()).isEqualTo(car.getModel().getBrand().getName());
    assertThat(carSearchResponse.getModel()).isEqualTo(car.getModel().getName());
    assertThat(carSearchResponse.getCarClass()).isEqualTo(car.getCarClass().getName());
    assertThat(carSearchResponse.getYearOfIssue())
        .isEqualTo(String.valueOf(car.getDateOfIssue().getYear()));
    assertThat(carSearchResponse.getBodyType()).isEqualTo(car.getBodyType().getBodyType());
    assertThat(carSearchResponse.isAutomaticTransmission())
        .isEqualTo(car.isAutomaticTransmission());
    assertThat(carSearchResponse.getColor()).isEqualTo(car.getColor());
    assertThat(carSearchResponse.getEngineType()).isEqualTo(car.getEngineType().getEngineType());
    assertThat(carSearchResponse.getPassengersAmt()).isEqualTo(car.getPassengersAmt());
    assertThat(carSearchResponse.getBaggageAmt()).isEqualTo(car.getBaggageAmt());
    assertThat(carSearchResponse.isHasConditioner()).isEqualTo(car.isHasConditioner());
    assertThat(carSearchResponse.getCostPerHour()).isEqualTo(car.getCostPerHour());
    assertThat(carSearchResponse.getLocationName()).isEqualTo(car.getLocation().getName());
    assertThat(carSearchResponse.getCarImageLink()).isEqualTo(car.getCarImageLink());
  }

  @Test
  void carAndRentalDetailsToCarByIdResponse() {
    var carByIdResponse = carMapper.carToCarByIdResponse(car);

    assertThat(carByIdResponse).isNotNull();
    assertThat(carByIdResponse.getId()).isEqualTo(car.getId());
    assertThat(carByIdResponse.getBrand()).isEqualTo(car.getModel().getBrand().getName());
    assertThat(carByIdResponse.getModel()).isEqualTo(car.getModel().getName());
    assertThat(carByIdResponse.getCarClass()).isEqualTo(car.getCarClass().getName());
    assertThat(carByIdResponse.getYearOfIssue())
        .isEqualTo(String.valueOf(car.getDateOfIssue().getYear()));
    assertThat(carByIdResponse.getBodyType()).isEqualTo(car.getBodyType().getBodyType());
    assertThat(carByIdResponse.isAutomaticTransmission()).isEqualTo(car.isAutomaticTransmission());
    assertThat(carByIdResponse.getColor()).isEqualTo(car.getColor());
    assertThat(carByIdResponse.getEngineType()).isEqualTo(car.getEngineType().getEngineType());
    assertThat(carByIdResponse.getPassengersAmt()).isEqualTo(car.getPassengersAmt());
    assertThat(carByIdResponse.getBaggageAmt()).isEqualTo(car.getBaggageAmt());
    assertThat(carByIdResponse.isHasConditioner()).isEqualTo(car.isHasConditioner());
    assertThat(carByIdResponse.getCostPerHour()).isEqualTo(car.getCostPerHour());
    assertThat(carByIdResponse.getLocationName()).isEqualTo(car.getLocation().getName());
    assertThat(carByIdResponse.getLocationCoordinateX())
        .isEqualTo(car.getLocation().getCoordinateX());
    assertThat(carByIdResponse.getLocationCoordinateY())
        .isEqualTo(car.getLocation().getCoordinateY());
    assertThat(carByIdResponse.getCarImageLink()).isEqualTo(car.getCarImageLink());
  }

  @Test
  void carToCarChipOfferResponse() {
    var carProfitableOfferResponse = carMapper.carToCarChipOfferResponse(car);

    assertThat(carProfitableOfferResponse).isNotNull();
    assertThat(carProfitableOfferResponse.getId()).isEqualTo(car.getId());
    assertThat(carProfitableOfferResponse.getBrand())
        .isEqualTo(car.getModel().getBrand().getName());
    assertThat(carProfitableOfferResponse.getModel()).isEqualTo(car.getModel().getName());
    assertThat(carProfitableOfferResponse.getCarClass()).isEqualTo(car.getCarClass().getName());
    assertThat(carProfitableOfferResponse.getAmountOfSeats()).isEqualTo(car.getPassengersAmt());
    assertThat(carProfitableOfferResponse.getAmountOfBaggage()).isEqualTo(car.getBaggageAmt());
    assertThat(carProfitableOfferResponse.isAutoTransmission())
        .isEqualTo(car.isAutomaticTransmission());
    assertThat(carProfitableOfferResponse.isAirConditioner()).isEqualTo(car.isHasConditioner());
    assertThat(carProfitableOfferResponse.getCarImageLink()).isEqualTo(car.getCarImageLink());
    assertThat(carProfitableOfferResponse.getCostPerHour())
        .isEqualTo(String.valueOf(car.getCostPerHour()));
  }

  @Test
  void carToCarCarAdminSearchResponse() {
    var carAdminSearchResponse = carMapper.carToCarCarAdminSearchResponse(car);

    assertThat(carAdminSearchResponse).isNotNull();
    assertThat(carAdminSearchResponse.getId()).isEqualTo(car.getId());
    assertThat(carAdminSearchResponse.getVin()).isEqualTo(car.getVin());
    assertThat(carAdminSearchResponse.getBrand()).isEqualTo(car.getModel().getBrand().getName());
    assertThat(carAdminSearchResponse.getModel()).isEqualTo(car.getModel().getName());
    assertThat(carAdminSearchResponse.getCarClass()).isEqualTo(car.getCarClass().getName());
    assertThat(carAdminSearchResponse.getCarClassId()).isEqualTo(car.getCarClass().getId());
    assertThat(carAdminSearchResponse.getDateOfIssue()).isEqualTo(car.getDateOfIssue());
    assertThat(carAdminSearchResponse.getBodyType()).isEqualTo(car.getBodyType().getBodyType());
    assertThat(carAdminSearchResponse.isAutomaticTransmission())
        .isEqualTo(car.isAutomaticTransmission());
    assertThat(carAdminSearchResponse.getColor()).isEqualTo(car.getColor());
    assertThat(carAdminSearchResponse.getEngineType())
        .isEqualTo(car.getEngineType().getEngineType());
    assertThat(carAdminSearchResponse.getPassengersAmt()).isEqualTo(car.getPassengersAmt());
    assertThat(carAdminSearchResponse.getBaggageAmt()).isEqualTo(car.getBaggageAmt());
    assertThat(carAdminSearchResponse.isHasConditioner()).isEqualTo(car.isHasConditioner());
    assertThat(carAdminSearchResponse.getCostPerHour())
        .isEqualTo(car.getCostPerHour().doubleValue());
    assertThat(carAdminSearchResponse.getLocationName()).isEqualTo(car.getLocation().getName());
    assertThat(carAdminSearchResponse.getLocationId()).isEqualTo(car.getLocation().getId());
    assertThat(carAdminSearchResponse.isInRental()).isEqualTo(car.isInRental());
    assertThat(carAdminSearchResponse.getCarImageLink()).isEqualTo(car.getCarImageLink());
  }

  @Test
  void getYearOfIssue() {
    var yearOfIssue = carMapper.getYearOfIssue(car);

    assertThat(yearOfIssue).isNotNull();
    assertThat(yearOfIssue).isEqualTo(String.valueOf(car.getDateOfIssue().getYear()));
  }
}