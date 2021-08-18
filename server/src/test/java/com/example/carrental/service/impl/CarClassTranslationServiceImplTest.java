package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.BELORUSSIAN;
import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static com.example.carrental.constants.ApplicationConstants.RUSSIAN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.car.CreateCarClassRequest;
import com.example.carrental.entity.car.CarClass;
import com.example.carrental.entity.car.CarClassTranslation;
import com.example.carrental.repository.CarClassTranslationRepository;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
class CarClassTranslationServiceImplTest {

  @Autowired
  private CarClassTranslationServiceImpl carClassTranslationService;

  @MockBean
  private CarClassTranslationRepository carClassTranslationRepository;

  @Test
  void givenValidRequest_whenCreate_thenSuccess() {
    var createCarClassRequest = CreateCarClassRequest.builder().nameRu("nameRu").nameEn("nameEn")
        .nameBe("nameBe").build();
    var carClass = CarClass.builder().id(1L).name("nameEn").build();
    when(carClassTranslationRepository.save(any(CarClassTranslation.class)))
        .thenReturn(new CarClassTranslation());

    carClassTranslationService.create(createCarClassRequest, carClass);

    assertThat(carClass.getName()).isEqualTo(createCarClassRequest.getNameEn());
  }

  @Test
  void givenValidRequest_whenUpdate_thenSuccess() {
    var carClassTranslations = Arrays.asList(CarClassTranslation.builder().name("nameRu")
        .language(RUSSIAN).build(), CarClassTranslation.builder().name("nameBe")
        .language(BELORUSSIAN).build());
    var createCarClassRequest = CreateCarClassRequest.builder().nameRu("nameRu").nameEn("nameEn")
        .nameBe("nameBe").build();
    when(carClassTranslationRepository.save(any(CarClassTranslation.class)))
        .thenReturn(new CarClassTranslation());

    carClassTranslationService.update(createCarClassRequest, carClassTranslations);

    assertThat(carClassTranslations.size()).isEqualTo(2);
  }

  @Test
  void givenRequestWithEnglishLanguage_whenSetTranslation_thenSuccess() {
    var carClassTranslations = Arrays.asList(CarClassTranslation.builder().name("nameRu")
        .language(RUSSIAN).build(), CarClassTranslation.builder().name("nameBe")
        .language(BELORUSSIAN).build());
    var carClass = CarClass.builder().name("carClassName")
        .carClassTranslations(carClassTranslations).build();

    carClassTranslationService.setTranslation(carClass, ENGLISH);

    assertThat(carClass.getName()).isEqualTo("carClassName");
  }

  @Test
  void givenRequestWithRussianLanguage_whenSetTranslation_thenSuccess() {
    var carClassTranslations = Arrays.asList(CarClassTranslation.builder().name("nameRu")
        .language(RUSSIAN).build(), CarClassTranslation.builder().name("nameBe")
        .language(BELORUSSIAN).build());
    var carClass = CarClass.builder().name("carClassName")
        .carClassTranslations(carClassTranslations).build();

    carClassTranslationService.setTranslation(carClass, RUSSIAN);

    assertThat(carClass.getName()).isEqualTo("nameRu");
  }

  @Test
  void givenRequestWithBelorussianLanguage_whenSetTranslation_thenSuccess() {
    var carClassTranslations = Arrays.asList(CarClassTranslation.builder().name("nameRu")
        .language(RUSSIAN).build(), CarClassTranslation.builder().name("nameBe")
        .language(BELORUSSIAN).build());
    var carClass = CarClass.builder().name("carClassName")
        .carClassTranslations(carClassTranslations).build();

    carClassTranslationService.setTranslation(carClass, BELORUSSIAN);

    assertThat(carClass.getName()).isEqualTo("nameBe");
  }
}