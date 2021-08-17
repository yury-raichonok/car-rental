package com.example.carrental.mapper;

import com.example.carrental.controller.dto.car.CarModelBrandNameResponse;
import com.example.carrental.controller.dto.car.CarModelResponse;
import com.example.carrental.entity.car.CarModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * The interface for mapping Car Model entity to DTO.
 *
 * @author Yury Raichonak
 */
@Mapper(componentModel = "spring")
public interface CarModelMapper {

  /**
   * @param carModel data.
   * @return CarModelResponse DTO.
   */
  @Mapping(target = "id", source = "carModel.id")
  @Mapping(target = "name", source = "carModel.name")
  @Mapping(target = "brandId", source = "carModel.brand.id")
  CarModelResponse carModelToCarModelResponse(CarModel carModel);

  /**
   * @param carModel data.
   * @return CarModelBrandNameResponse DTO.
   */
  @Mapping(target = "id", source = "carModel.id")
  @Mapping(target = "name", source = "carModel.name")
  @Mapping(target = "brand", source = "carModel.brand.name")
  CarModelBrandNameResponse carModelToCarModelBrandNameResponse(CarModel carModel);
}
