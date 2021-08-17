package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.BELORUSSIAN;
import static com.example.carrental.constants.ApplicationConstants.NOT_SPECIFIED;
import static com.example.carrental.constants.ApplicationConstants.RUSSIAN;

import com.example.carrental.controller.dto.car.CarClassNameResponse;
import com.example.carrental.controller.dto.car.CarClassNameWithTranslationsResponse;
import com.example.carrental.entity.car.CarClass;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * The interface for mapping Car Class entity to DTO.
 *
 * @author Yury Raichonak
 */
@Mapper(componentModel = "spring")
public interface CarClassMapper {

  /**
   * @param carClass data.
   * @return CarClassNameResponse DTO
   */
  @Mapping(target = "id", source = "carClass.id")
  @Mapping(target = "name", source = "carClass.name")
  CarClassNameResponse carClassToCarClassNameResponse(CarClass carClass);

  /**
   * @param carClass data.
   * @return CarClassNameWithTranslationsResponse DTO.
   */
  @Mapping(target = "id", source = "carClass.id")
  @Mapping(target = "nameEn", source = "carClass.name")
  @Mapping(target = "nameRu", source = "carClass", qualifiedByName = "getNameRu")
  @Mapping(target = "nameBe", source = "carClass", qualifiedByName = "getNameBe")
  CarClassNameWithTranslationsResponse carClassToCarClassNameWithTranslationsResponse(
      CarClass carClass);

  /**
   * @param carClass data.
   * @return car class name in russian as String.
   */
  @Named("getNameRu")
  default String getNameRu(CarClass carClass) {
    if (Optional.ofNullable(carClass.getCarClassTranslations()).isPresent()) {
      var nameRu = carClass.getCarClassTranslations().stream()
          .filter(translation -> RUSSIAN.equals(translation.getLanguage())).findFirst();
      if (nameRu.isPresent()) {
        return nameRu.get().getName();
      } else {
        return NOT_SPECIFIED;
      }
    } else {
      return NOT_SPECIFIED;
    }
  }

  /**
   * @param carClass data.
   * @return car class name in belorussian as String.
   */
  @Named("getNameBe")
  default String getNameBe(CarClass carClass) {
    if (Optional.ofNullable(carClass.getCarClassTranslations()).isPresent()) {
      var nameBe = carClass.getCarClassTranslations().stream()
          .filter(translation -> BELORUSSIAN.equals(translation.getLanguage())).findFirst();
      if (nameBe.isPresent()) {
        return nameBe.get().getName();
      } else {
        return NOT_SPECIFIED;
      }
    } else {
      return NOT_SPECIFIED;
    }
  }
}
