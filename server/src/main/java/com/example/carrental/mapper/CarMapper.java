package com.example.carrental.mapper;

import com.example.carrental.controller.dto.car.CarAdminSearchResponse;
import com.example.carrental.controller.dto.car.CarByIdResponse;
import com.example.carrental.controller.dto.car.CarProfitableOfferResponse;
import com.example.carrental.controller.dto.car.CarSearchResponse;
import com.example.carrental.entity.car.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * The interface for mapping Car entity to DTO.
 *
 * @author Yury Raichonak
 */
@Mapper(componentModel = "spring")
public interface CarMapper {

  /**
   * @param car data.
   * @return CarSearchResponse DTO.
   */
  @Mapping(target = "id", source = "car.id")
  @Mapping(target = "brand", source = "car.model.brand.name")
  @Mapping(target = "model", source = "car.model.name")
  @Mapping(target = "carClass", source = "car.carClass.name")
  @Mapping(target = "yearOfIssue", source = "car", qualifiedByName = "getYearOfIssue")
  @Mapping(target = "bodyType", source = "car.bodyType.bodyType")
  @Mapping(target = "isAutomaticTransmission", source = "car.automaticTransmission")
  @Mapping(target = "color", source = "car.color")
  @Mapping(target = "engineType", source = "car.engineType.engineType")
  @Mapping(target = "passengersAmt", source = "car.passengersAmt")
  @Mapping(target = "baggageAmt", source = "car.baggageAmt")
  @Mapping(target = "hasConditioner", source = "car.hasConditioner")
  @Mapping(target = "costPerHour", source = "car.costPerHour")
  @Mapping(target = "locationName", source = "car.location.name")
  @Mapping(target = "carImageLink", source = "car.carImageLink")
  CarSearchResponse carToCarSearchResponse(Car car);

  /**
   * @param car data.
   * @return CarByIdResponse DTO.
   */
  @Mapping(target = "id", source = "car.id")
  @Mapping(target = "brand", source = "car.model.brand.name")
  @Mapping(target = "model", source = "car.model.name")
  @Mapping(target = "carClass", source = "car.carClass.name")
  @Mapping(target = "yearOfIssue", source = "car", qualifiedByName = "getYearOfIssue")
  @Mapping(target = "bodyType", source = "car.bodyType.bodyType")
  @Mapping(target = "isAutomaticTransmission", source = "car.automaticTransmission")
  @Mapping(target = "color", source = "car.color")
  @Mapping(target = "engineType", source = "car.engineType.engineType")
  @Mapping(target = "passengersAmt", source = "car.passengersAmt")
  @Mapping(target = "baggageAmt", source = "car.baggageAmt")
  @Mapping(target = "hasConditioner", source = "car.hasConditioner")
  @Mapping(target = "costPerHour", source = "car.costPerHour")
  @Mapping(target = "locationName", source = "car.location.name")
  @Mapping(target = "locationCoordinateX", source = "car.location.coordinateX")
  @Mapping(target = "locationCoordinateY", source = "car.location.coordinateY")
  @Mapping(target = "carImageLink", source = "car.carImageLink")
  CarByIdResponse carToCarByIdResponse(Car car);

  /**
   * @param car data.
   * @return CarProfitableOfferResponse DTO.
   */
  @Mapping(target = "id", source = "car.id")
  @Mapping(target = "brand", source = "car.model.brand.name")
  @Mapping(target = "model", source = "car.model.name")
  @Mapping(target = "carClass", source = "car.carClass.name")
  @Mapping(target = "amountOfSeats", source = "car.passengersAmt")
  @Mapping(target = "amountOfBaggage", source = "car.baggageAmt")
  @Mapping(target = "autoTransmission", source = "car.automaticTransmission")
  @Mapping(target = "airConditioner", source = "car.hasConditioner")
  @Mapping(target = "carImageLink", source = "car.carImageLink")
  @Mapping(target = "costPerHour", source = "car.costPerHour")
  CarProfitableOfferResponse carToCarChipOfferResponse(Car car);

  /**
   * @param car data.
   * @return CarAdminSearchResponse DTO.
   */
  @Mapping(target = "id", source = "car.id")
  @Mapping(target = "vin", source = "car.vin")
  @Mapping(target = "brand", source = "car.model.brand.name")
  @Mapping(target = "model", source = "car.model.name")
  @Mapping(target = "carClass", source = "car.carClass.name")
  @Mapping(target = "carClassId", source = "car.carClass.id")
  @Mapping(target = "dateOfIssue", source = "car.dateOfIssue")
  @Mapping(target = "bodyType", source = "car.bodyType.bodyType")
  @Mapping(target = "isAutomaticTransmission", source = "car.automaticTransmission")
  @Mapping(target = "color", source = "car.color")
  @Mapping(target = "engineType", source = "car.engineType.engineType")
  @Mapping(target = "passengersAmt", source = "car.passengersAmt")
  @Mapping(target = "baggageAmt", source = "car.baggageAmt")
  @Mapping(target = "hasConditioner", source = "car.hasConditioner")
  @Mapping(target = "costPerHour", source = "car.costPerHour")
  @Mapping(target = "locationName", source = "car.location.name")
  @Mapping(target = "locationId", source = "car.location.id")
  @Mapping(target = "isInRental", source = "car.inRental")
  @Mapping(target = "carImageLink", source = "car.carImageLink")
  CarAdminSearchResponse carToCarCarAdminSearchResponse(Car car);

  /**
   * @param car data.
   * @return car year of issue as String.
   */
  @Named("getYearOfIssue")
  default String getYearOfIssue(Car car) {
    return String.valueOf(car.getDateOfIssue().getYear());
  }
}
