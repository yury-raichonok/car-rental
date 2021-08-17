package com.example.carrental.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.carrental.controller.dto.car.CarBrandResponse;
import com.example.carrental.entity.car.CarBrand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CarBrandMapperTest {

  @Autowired
  private CarBrandMapper carBrandMapper;

  @Test
  void carBrandToCarBrandResponse() {
    CarBrand carBrand = CarBrand.builder().id(1L).name("name").brandImageLink("link").build();

    CarBrandResponse carBrandResponse = carBrandMapper.carBrandToCarBrandResponse(carBrand);

    assertThat(carBrandResponse).isNotNull();
    assertThat(carBrandResponse.getId()).isEqualTo(carBrand.getId());
    assertThat(carBrandResponse.getName()).isEqualTo(carBrand.getName());
    assertThat(carBrandResponse.getImageLink()).isEqualTo(carBrand.getBrandImageLink());
  }
}