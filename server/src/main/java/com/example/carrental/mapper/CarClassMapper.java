package com.example.carrental.mapper;

import com.example.carrental.controller.dto.car.CarClassNameResponse;
import com.example.carrental.controller.dto.car.CarClassNameWithTranslationsResponse;
import com.example.carrental.entity.car.CarClass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CarClassMapper {

  @Mapping(target = "id", source = "carClass.id")
  @Mapping(target = "name", source = "carClass.name")
  CarClassNameResponse carClassToCarClassNameResponse(CarClass carClass);

  @Mapping(target = "id", source = "carClass.id")
  @Mapping(target = "nameEn", source = "carClass.name")
  @Mapping(target = "nameRu", source = "carClass", qualifiedByName = "getNameRu")
  @Mapping(target = "nameBe", source = "carClass", qualifiedByName = "getNameBe")
  CarClassNameWithTranslationsResponse carClassToCarClassNameWithTranslationsResponse(CarClass carClass);

  @Named("getNameRu")
  default String getNameRu(CarClass carClass) {
    var nameRu = carClass.getCarClassTranslations().stream()
        .filter(translation -> "ru".equals(translation.getLanguage())).findFirst();
    if (nameRu.isPresent()) {
      return nameRu.get().getName();
    } else {
      return "Not specified";
    }
  }

  @Named("getNameBe")
  default String getNameBe(CarClass carClass) {
    var nameBe = carClass.getCarClassTranslations().stream()
        .filter(translation -> "be".equals(translation.getLanguage())).findFirst();
    if (nameBe.isPresent()) {
      return nameBe.get().getName();
    } else {
      return "Not specified";
    }
  }
}
