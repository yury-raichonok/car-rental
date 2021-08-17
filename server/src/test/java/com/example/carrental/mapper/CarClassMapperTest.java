package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.BELORUSSIAN;
import static com.example.carrental.constants.ApplicationConstants.NOT_SPECIFIED;
import static com.example.carrental.constants.ApplicationConstants.RUSSIAN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.carrental.entity.car.CarClass;
import com.example.carrental.entity.car.CarClassTranslation;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CarClassMapperTest {

  @Autowired
  private CarClassMapper carClassMapper;

  @Test
  void carClassToCarClassNameResponse() {
    var carClass = CarClass.builder().id(1L).name("name").build();

    var carClassNameResponse = carClassMapper.carClassToCarClassNameResponse(carClass);

    assertThat(carClassNameResponse).isNotNull();
    assertThat(carClassNameResponse.getId()).isEqualTo(carClass.getId());
    assertThat(carClassNameResponse.getName()).isEqualTo(carClass.getName());
  }

  @Test
  void carClassToCarClassNameWithTranslationsResponse() {
    var carClassTranslationRu = CarClassTranslation.builder().id(1L).name("nameRu")
        .language(RUSSIAN).build();
    var carClassTranslationBe = CarClassTranslation.builder().id(2L).name("nameBe")
        .language(BELORUSSIAN).build();
    var carClass = CarClass.builder().id(1L).name("name")
        .carClassTranslations(Arrays.asList(carClassTranslationRu, carClassTranslationBe)).build();

    var carClassNameWithTranslationsResponse = carClassMapper
        .carClassToCarClassNameWithTranslationsResponse(carClass);

    assertThat(carClassNameWithTranslationsResponse).isNotNull();
    assertThat(carClassNameWithTranslationsResponse.getId()).isEqualTo(carClass.getId());
    assertThat(carClassNameWithTranslationsResponse.getNameEn()).isEqualTo(carClass.getName());
    assertThat(carClassNameWithTranslationsResponse.getNameBe())
        .isEqualTo(carClassTranslationBe.getName());
    assertThat(carClassNameWithTranslationsResponse.getNameRu())
        .isEqualTo(carClassTranslationRu.getName());
  }

  @Test
  void getNameRu() {
    var carClassTranslationRu = CarClassTranslation.builder().id(1L).name("nameRu")
        .language(RUSSIAN).build();
    var carClass = CarClass.builder().id(1L).name("name").carClassTranslations(
        Collections.singletonList(carClassTranslationRu)).build();

    var carClassNameRu = carClassMapper.getNameRu(carClass);

    assertThat(carClassNameRu).isEqualTo(carClassTranslationRu.getName());
  }

  @Test
  void getNameRuIfNotSpecified() {
    var carClassTranslationBe = CarClassTranslation.builder().id(2L).name("nameBe")
        .language(BELORUSSIAN).build();
    var carClass = CarClass.builder().id(1L).name("name").carClassTranslations(
        Collections.singletonList(carClassTranslationBe)).build();

    var carClassNameRu = carClassMapper.getNameRu(carClass);

    assertThat(carClassNameRu).isEqualTo(NOT_SPECIFIED);
  }

  @Test
  void getNameBe() {
    var carClassTranslationBe = CarClassTranslation.builder().id(2L).name("nameBe")
        .language(BELORUSSIAN).build();
    var carClass = CarClass.builder().id(1L).name("name").carClassTranslations(
        Collections.singletonList(carClassTranslationBe)).build();

    var carClassNameRu = carClassMapper.getNameBe(carClass);

    assertThat(carClassNameRu).isEqualTo(carClassTranslationBe.getName());
  }

  @Test
  void getNameBeIfNotSpecified() {
    var carClassTranslationRu = CarClassTranslation.builder().id(1L).name("nameRu")
        .language(RUSSIAN).build();
    var carClass = CarClass.builder().id(1L).name("name").carClassTranslations(
        Collections.singletonList(carClassTranslationRu)).build();

    var carClassNameRu = carClassMapper.getNameBe(carClass);

    assertThat(carClassNameRu).isEqualTo(NOT_SPECIFIED);
  }
}