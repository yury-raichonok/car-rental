package com.example.carrental.mapper;

import com.example.carrental.controller.dto.car.CarBrandResponse;
import com.example.carrental.entity.car.CarBrand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * The interface for mapping Car Brand entity to DTO.
 *
 * @author Yury Raichonak
 */
@Mapper(componentModel = "spring")
public interface CarBrandMapper {

  /**
   * @param carBrand entity.
   * @return CarBrandResponse DTO.
   */
  @Mapping(target = "id", source = "carBrand.id")
  @Mapping(target = "name", source = "carBrand.name")
  @Mapping(target = "imageLink", source = "carBrand.brandImageLink")
  CarBrandResponse carBrandToCarBrandResponse(CarBrand carBrand);
}
