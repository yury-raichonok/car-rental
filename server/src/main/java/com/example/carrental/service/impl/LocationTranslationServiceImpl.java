package com.example.carrental.service.impl;

import com.example.carrental.controller.dto.location.CreateLocationRequest;
import com.example.carrental.entity.location.Location;
import com.example.carrental.entity.location.LocationTranslation;
import com.example.carrental.repository.LocationTranslationRepository;
import com.example.carrental.service.LocationTranslationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationTranslationServiceImpl implements LocationTranslationService {

  private final LocationTranslationRepository locationTranslationRepository;

  @Override
  @Transactional
  public void create(CreateLocationRequest createLocationRequest, Location location) {
    locationTranslationRepository.save(LocationTranslation
        .builder()
        .name(createLocationRequest.getNameBe())
        .location(location)
        .language("be")
        .build());

    locationTranslationRepository.save(LocationTranslation
        .builder()
        .name(createLocationRequest.getNameRu())
        .location(location)
        .language("ru")
        .build());
  }

  @Override
  @Transactional
  public void update(CreateLocationRequest createLocationRequest,
      List<LocationTranslation> translations) {
    var locationTranslationRu = translations.stream()
        .filter(translation -> "ru".equals(translation.getLanguage())).findFirst();
    locationTranslationRu.ifPresent(carClassTranslation -> {
      carClassTranslation.setName(createLocationRequest.getNameRu());
      locationTranslationRepository.save(carClassTranslation);
    });

    var locationTranslationBe = translations.stream()
        .filter(translation -> "be".equals(translation.getLanguage())).findFirst();
    locationTranslationBe.ifPresent(carClassTranslation -> {
      carClassTranslation.setName(createLocationRequest.getNameBe());
      locationTranslationRepository.save(carClassTranslation);
    });
  }

  @Override
  public void setTranslation(Location location, String language) {
    location.getLocationTranslations()
        .stream()
        .filter(translation -> language.equals(translation.getLanguage()))
        .findFirst()
        .ifPresent(locationTranslation -> location.setName(locationTranslation.getName()));
  }
}
