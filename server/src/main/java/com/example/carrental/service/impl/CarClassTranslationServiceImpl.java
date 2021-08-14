package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.BELORUSSIAN;
import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static com.example.carrental.constants.ApplicationConstants.RUSSIAN;

import com.example.carrental.controller.dto.car.CreateCarClassRequest;
import com.example.carrental.entity.car.CarClass;
import com.example.carrental.entity.car.CarClassTranslation;
import com.example.carrental.repository.CarClassTranslationRepository;
import com.example.carrental.service.CarClassTranslationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarClassTranslationServiceImpl implements CarClassTranslationService {

  private final CarClassTranslationRepository carClassTranslationRepository;

  @Override
  @Transactional
  public void create(CreateCarClassRequest createCarClassRequest, CarClass carClass) {
    carClassTranslationRepository.save(CarClassTranslation
        .builder()
        .name(createCarClassRequest.getNameBe())
        .carClass(carClass)
        .language(BELORUSSIAN)
        .build());
    carClassTranslationRepository.save(CarClassTranslation
        .builder()
        .name(createCarClassRequest.getNameRu())
        .carClass(carClass)
        .language(RUSSIAN)
        .build());
  }

  @Override
  @Transactional
  public void update(CreateCarClassRequest createCarClassRequest,
      List<CarClassTranslation> translations) {
    var carClassTranslationRu = translations.stream()
        .filter(translation -> RUSSIAN.equals(translation.getLanguage())).findFirst();
    carClassTranslationRu.ifPresent(carClassTranslation -> {
      carClassTranslation.setName(createCarClassRequest.getNameRu());
      carClassTranslationRepository.save(carClassTranslation);
    });

    var carClassTranslationBe = translations.stream()
        .filter(translation -> BELORUSSIAN.equals(translation.getLanguage())).findFirst();
    carClassTranslationBe.ifPresent(carClassTranslation -> {
      carClassTranslation.setName(createCarClassRequest.getNameBe());
      carClassTranslationRepository.save(carClassTranslation);
    });
  }

  @Override
  public void setTranslation(CarClass carClass, String language) {
    if (!ENGLISH.equals(language)) {
      carClass.getCarClassTranslations()
          .stream()
          .filter(translation -> language.equals(translation.getLanguage()))
          .findFirst()
          .ifPresent(classTranslation -> carClass.setName(classTranslation.getName()));
    }
  }
}
