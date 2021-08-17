package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.BELORUSSIAN;
import static com.example.carrental.constants.ApplicationConstants.NOT_SPECIFIED;
import static com.example.carrental.constants.ApplicationConstants.RUSSIAN;

import com.example.carrental.controller.dto.location.LocationNameResponse;
import com.example.carrental.controller.dto.location.LocationWithTranslationsResponse;
import com.example.carrental.entity.location.Location;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * The interface for mapping Location entity to DTO.
 *
 * @author Yury Raichonak
 */
@Mapper(componentModel = "spring")
public interface LocationMapper {

  /**
   * @param location data.
   * @return LocationWithTranslationsResponse DTO.
   */
  @Mapping(target = "id", source = "location.id")
  @Mapping(target = "nameEn", source = "location.name")
  @Mapping(target = "nameRu", source = "location", qualifiedByName = "getNameRu")
  @Mapping(target = "nameBe", source = "location", qualifiedByName = "getNameBe")
  @Mapping(target = "coordinateX", source = "location.coordinateX")
  @Mapping(target = "coordinateY", source = "location.coordinateY")
  @Mapping(target = "zoom", source = "location.zoom")
  LocationWithTranslationsResponse locationToLocationWithTranslationsResponse(Location location);

  /**
   * @param location data.
   * @return LocationNameResponse DTO.
   */
  @Mapping(target = "id", source = "location.id")
  @Mapping(target = "name", source = "location.name")
  LocationNameResponse locationToLocationNameResponse(Location location);

  /**
   * @param location data
   * @return location name in russian as String.
   */
  @Named("getNameRu")
  default String getNameRu(Location location) {
    if (Optional.ofNullable(location.getLocationTranslations()).isPresent()) {
      var translationRu = location.getLocationTranslations().stream()
          .filter(translation -> RUSSIAN.equals(translation.getLanguage())).findFirst();
      if (translationRu.isPresent()) {
        return translationRu.get().getName();
      } else {
        return NOT_SPECIFIED;
      }
    } else {
      return NOT_SPECIFIED;
    }
  }

  /**
   * @param location data
   * @return location name in belorussian as String.
   */
  @Named("getNameBe")
  default String getNameBe(Location location) {
    if (Optional.ofNullable(location.getLocationTranslations()).isPresent()) {
      var translationBe = location.getLocationTranslations().stream()
          .filter(translation -> BELORUSSIAN.equals(translation.getLanguage())).findFirst();
      if (translationBe.isPresent()) {
        return translationBe.get().getName();
      } else {
        return NOT_SPECIFIED;
      }
    } else {
      return NOT_SPECIFIED;
    }
  }
}
