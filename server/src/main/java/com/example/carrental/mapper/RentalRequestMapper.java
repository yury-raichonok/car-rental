package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_FORMAT_PATTERN;
import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_TIME_FORMAT_PATTERN;

import com.example.carrental.controller.dto.rentalDetails.RentalAllRequestResponse;
import com.example.carrental.controller.dto.rentalDetails.RentalRequestResponse;
import com.example.carrental.controller.dto.user.UserPassportConfirmationDataResponse;
import com.example.carrental.entity.rentalDetails.RentalRequest;
import com.example.carrental.entity.user.UserPassport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring")
public interface RentalRequestMapper {

  @Mapping(target = "id", source = "rentalRequest.id")
  @Mapping(target = "userEmail", source = "rentalRequest.user.email")
  @Mapping(target = "requestType", source = "rentalRequest.rentalRequestType")
  @Mapping(target = "sentDate", source = "rentalRequest.sentDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "considered", source = "rentalRequest.considered")
  @Mapping(target = "considerationDate", source = "rentalRequest.considerationDate",
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "comments", source = "rentalRequest.comments",
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  RentalAllRequestResponse rentalRequestToRentalAllRequestResponse(RentalRequest rentalRequest);

  @Mapping(target = "id", source = "rentalRequest.id")
  @Mapping(target = "userEmail", source = "rentalRequest.user.email")
  @Mapping(target = "requestType", source = "rentalRequest.rentalRequestType")
  @Mapping(target = "sentDate", source = "rentalRequest.sentDate",
      dateFormat = RESPONSE_DATE_TIME_FORMAT_PATTERN)
  @Mapping(target = "considered", source = "rentalRequest.considered")
  RentalRequestResponse rentalRequestToRentalRequestResponse(RentalRequest rentalRequest);

  @Mapping(target = "firstName", source = "passport.firstName")
  @Mapping(target = "middleName", source = "passport.middleName")
  @Mapping(target = "lastName", source = "passport.lastName")
  @Mapping(target = "dateOfBirth", source = "passport.dateOfBirth",
      dateFormat = RESPONSE_DATE_FORMAT_PATTERN)
  @Mapping(target = "passportSeries", source = "passport.passportSeries")
  @Mapping(target = "dateOfIssue", source = "passport.dateOfIssue",
      dateFormat = RESPONSE_DATE_FORMAT_PATTERN)
  @Mapping(target = "validityPeriod", source = "passport.validityPeriod",
      dateFormat = RESPONSE_DATE_FORMAT_PATTERN)
  @Mapping(target = "organizationThatIssued", source = "passport.organizationThatIssued")
  @Mapping(target = "documentsFileLink", source = "passport.documentsFileLink")
  UserPassportConfirmationDataResponse passportToUserPassportConfirmationDataResponse(
      UserPassport passport);
}
