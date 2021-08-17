package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.BELORUSSIAN;
import static com.example.carrental.constants.ApplicationConstants.NOT_SPECIFIED;
import static com.example.carrental.constants.ApplicationConstants.RUSSIAN;

import com.example.carrental.controller.dto.location.LocationNameResponse;
import com.example.carrental.controller.dto.location.LocationWithTranslationsResponse;
import com.example.carrental.entity.location.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface LocationMapper {

  @Mapping(target = "id", source = "location.id")
  @Mapping(target = "nameEn", source = "location.name")
  @Mapping(target = "nameRu", source = "location", qualifiedByName = "getNameRu")
  @Mapping(target = "nameBe", source = "location", qualifiedByName = "getNameBe")
  @Mapping(target = "coordinateX", source = "location.coordinateX")
  @Mapping(target = "coordinateY", source = "location.coordinateY")
  @Mapping(target = "zoom", source = "location.zoom")
  LocationWithTranslationsResponse locationToLocationWithTranslationsResponse(Location location);

  @Mapping(target = "id", source = "location.id")
  @Mapping(target = "name", source = "location.name")
  LocationNameResponse locationToLocationNameResponse(Location location);

  @Named("getNameRu")
  default String getNameRu(Location location) {
    var translationRu = location.getLocationTranslations().stream()
        .filter(translation -> RUSSIAN.equals(translation.getLanguage())).findFirst();
    if (translationRu.isPresent()) {
      return translationRu.get().getName();
    } else {
      return NOT_SPECIFIED;
    }
  }

  @Named("getNameBe")
  default String getNameBe(Location location) {
    var translationBe = location.getLocationTranslations().stream()
        .filter(translation -> BELORUSSIAN.equals(translation.getLanguage())).findFirst();
    if (translationBe.isPresent()) {
      return translationBe.get().getName();
    } else {
      return NOT_SPECIFIED;
    }
  }
}
