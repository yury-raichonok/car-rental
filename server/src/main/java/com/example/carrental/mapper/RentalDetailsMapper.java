package com.example.carrental.mapper;

import com.example.carrental.controller.dto.rentaldetails.RentalDetailsResponse;
import com.example.carrental.entity.rentaldetails.RentalDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RentalDetailsMapper {

  @Mapping(target = "email", source = "rentalDetails.email")
  @Mapping(target = "phoneNumber", source = "rentalDetails.phone")
  @Mapping(target = "locationId", source = "rentalDetails.location.id")
  @Mapping(target = "location", source = "rentalDetails.location.name")
  @Mapping(target = "fromDayToWeekCoefficient", source = "rentalDetails.fromDayToWeekCoefficient")
  @Mapping(target = "fromWeekCoefficient", source = "rentalDetails.fromWeekCoefficient")
  @Mapping(target = "billValidityPeriod", source = "rentalDetails.paymentBillValidityPeriodInMinutes")
  RentalDetailsResponse rentalDetailsToRentalDetailsResponse(RentalDetails rentalDetails);
}
