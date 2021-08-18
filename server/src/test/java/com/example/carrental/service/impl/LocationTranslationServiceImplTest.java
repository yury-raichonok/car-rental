package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.BELORUSSIAN;
import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static com.example.carrental.constants.ApplicationConstants.RUSSIAN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.carrental.controller.dto.location.CreateLocationRequest;
import com.example.carrental.entity.location.Location;
import com.example.carrental.entity.location.LocationTranslation;
import com.example.carrental.repository.LocationTranslationRepository;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
class LocationTranslationServiceImplTest {

  @Autowired
  private LocationTranslationServiceImpl locationTranslationService;

  @MockBean
  private LocationTranslationRepository locationTranslationRepository;

  @Test
  void givenValidRequest_whenCreate_thenSuccess() {
    var createLocationRequest = CreateLocationRequest.builder().nameBe("name").nameEn("name")
        .nameRu("name").coordinateX(1).coordinateY(1).zoom(1).build();
    var location = Location.builder().id(1L).name("name").build();
    when(locationTranslationRepository.save(any(LocationTranslation.class)))
        .thenReturn(new LocationTranslation());

    assertDoesNotThrow(() -> locationTranslationService.create(createLocationRequest, location));
  }

  @Test
  void givenValidRequest_whenUpdate_thenSuccess() {
    var translations = Arrays.asList(LocationTranslation.builder().name("nameRu")
        .language(RUSSIAN).build(), LocationTranslation.builder().name("nameBe")
        .language(BELORUSSIAN).build());
    var updateLocationRequest = CreateLocationRequest.builder().nameBe("nameBe").nameEn("nameEn")
        .nameRu("nameRu").coordinateX(1).coordinateY(1).zoom(1).build();
    when(locationTranslationRepository.save(any(LocationTranslation.class)))
        .thenReturn(new LocationTranslation());

    assertDoesNotThrow(
        () -> locationTranslationService.update(updateLocationRequest, translations));
  }

  @Test
  void givenRequestWithEnglishLanguage_whenSetTranslation_thenSuccess() {
    var translations = Arrays.asList(LocationTranslation.builder().name("nameRu")
        .language(RUSSIAN).build(), LocationTranslation.builder().name("nameBe")
        .language(BELORUSSIAN).build());
    var location = Location.builder().id(1L).name("name").locationTranslations(translations)
        .build();

    locationTranslationService.setTranslation(location, ENGLISH);

    assertThat(location.getName()).isEqualTo("name");
  }

  @Test
  void givenRequestWithRussianLanguage_whenSetTranslation_thenSuccess() {
    var translations = Arrays.asList(LocationTranslation.builder().name("nameRu")
        .language(RUSSIAN).build(), LocationTranslation.builder().name("nameBe")
        .language(BELORUSSIAN).build());
    var location = Location.builder().id(1L).name("name").locationTranslations(translations)
        .build();

    locationTranslationService.setTranslation(location, RUSSIAN);

    assertThat(location.getName()).isEqualTo("nameRu");
  }

  @Test
  void givenRequestWithBelorussianLanguage_whenSetTranslation_thenSuccess() {
    var translations = Arrays.asList(LocationTranslation.builder().name("nameRu")
        .language(RUSSIAN).build(), LocationTranslation.builder().name("nameBe")
        .language(BELORUSSIAN).build());
    var location = Location.builder().id(1L).name("name").locationTranslations(translations)
        .build();

    locationTranslationService.setTranslation(location, BELORUSSIAN);

    assertThat(location.getName()).isEqualTo("nameBe");
  }
}