package com.example.carrental.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.carrental.controller.dto.car.CarModelBrandNameResponse;
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
class CarModelMapperTest {

  private CarModel carModel;

  @Autowired
  private CarModelMapper carModelMapper;

  @BeforeEach
  public void setup() {
    CarBrand carBrand = CarBrand.builder().id(1L).name("carBrandName").build();
    carModel = CarModel.builder().id(1L).name("carModelName").brand(carBrand).build();
  }

  @Test
  void carModelToCarModelResponse() {
    var carModelResponse = carModelMapper.carModelToCarModelResponse(carModel);

    assertThat(carModelResponse).isNotNull();
    assertThat(carModelResponse.getId()).isEqualTo(carModel.getBrand().getId());
    assertThat(carModelResponse.getBrandId()).isEqualTo(carModel.getBrand().getId());
    assertThat(carModelResponse.getName()).isEqualTo(carModel.getName());
  }

  @Test
  void carModelToCarModelBrandNameResponse() {
    var carModelBrandNameResponse = carModelMapper.carModelToCarModelBrandNameResponse(carModel);

    assertThat(carModelBrandNameResponse).isNotNull();
    assertThat(carModelBrandNameResponse.getId()).isEqualTo(carModel.getId());
    assertThat(carModelBrandNameResponse.getName()).isEqualTo(carModel.getName());
    assertThat(carModelBrandNameResponse.getBrand()).isEqualTo(carModel.getBrand().getName());
  }
}