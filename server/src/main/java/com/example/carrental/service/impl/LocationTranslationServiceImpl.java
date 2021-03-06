package com.example.carrental.service.impl;

import static com.example.carrental.constants.ApplicationConstants.BELORUSSIAN;
import static com.example.carrental.constants.ApplicationConstants.ENGLISH;
import static com.example.carrental.constants.ApplicationConstants.RUSSIAN;

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

/**
 * The service for Location Translations.
 * <p>
 * This class performs the CRUD operations for Location Translations.
 * </p>
 * @author Yury Raichonak
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LocationTranslationServiceImpl implements LocationTranslationService {

  private final LocationTranslationRepository locationTranslationRepository;

  /**
   * @param createLocationRequest data for creating new translations.
   * @param location for which creating translations.
   */
  @Override
  @Transactional
  public void create(CreateLocationRequest createLocationRequest, Location location) {
    locationTranslationRepository.save(LocationTranslation
        .builder()
        .name(createLocationRequest.getNameBe())
        .location(location)
        .language(BELORUSSIAN)
        .build());

    locationTranslationRepository.save(LocationTranslation
        .builder()
        .name(createLocationRequest.getNameRu())
        .location(location)
        .language(RUSSIAN)
        .build());
  }

  /**
   * @param createLocationRequest data for updating translations.
   * @param translations list of translations.
   */
  @Override
  @Transactional
  public void update(CreateLocationRequest createLocationRequest,
      List<LocationTranslation> translations) {
    var locationTranslationRu = translations.stream()
        .filter(translation -> RUSSIAN.equals(translation.getLanguage())).findFirst();
    locationTranslationRu.ifPresent(carClassTranslation -> {
      carClassTranslation.setName(createLocationRequest.getNameRu());
      locationTranslationRepository.save(carClassTranslation);
    });

    var locationTranslationBe = translations.stream()
        .filter(translation -> BELORUSSIAN.equals(translation.getLanguage())).findFirst();
    locationTranslationBe.ifPresent(carClassTranslation -> {
      carClassTranslation.setName(createLocationRequest.getNameBe());
      locationTranslationRepository.save(carClassTranslation);
    });
  }

  /**
   * @param location which name replaces by translated name.
   * @param language selected language.
   */
  @Override
  public void setTranslation(Location location, String language) {
    if (!ENGLISH.equals(language)) {
      location.getLocationTranslations()
          .stream()
          .filter(translation -> language.equals(translation.getLanguage()))
          .findFirst()
          .ifPresent(locationTranslation -> location.setName(locationTranslation.getName()));
    }
  }
}
