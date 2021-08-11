package com.example.carrental.mapper;

import com.example.carrental.controller.dto.user.UserPassportConfirmationDataResponse;
import com.example.carrental.controller.dto.user.UserPassportDataResponse;
import com.example.carrental.entity.user.UserPassport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserPassportMapper {

  String PASSPORT_DATE_FORMAT_PATTERN = "dd.MM.yyyy";

  @Mapping(target = "firstName", source = "userPassport.firstName")
  @Mapping(target = "middleName", source = "userPassport.middleName")
  @Mapping(target = "lastName", source = "userPassport.lastName")
  @Mapping(target = "dateOfBirth", source = "userPassport.dateOfBirth")
  @Mapping(target = "passportSeries", source = "userPassport.passportSeries")
  @Mapping(target = "passportNumber", source = "userPassport.passportNumber")
  @Mapping(target = "dateOfIssue", source = "userPassport.dateOfIssue")
  @Mapping(target = "validityPeriod", source = "userPassport.validityPeriod")
  @Mapping(target = "organizationThatIssued", source = "userPassport.organizationThatIssued")
  UserPassportDataResponse userPassportToUserPassportDataResponse(UserPassport userPassport);

  @Mapping(target = "firstName", source = "userPassport.firstName")
  @Mapping(target = "middleName", source = "userPassport.middleName")
  @Mapping(target = "lastName", source = "userPassport.lastName")
  @Mapping(target = "dateOfBirth", source = "userPassport.dateOfBirth", dateFormat = PASSPORT_DATE_FORMAT_PATTERN)
  @Mapping(target = "passportSeries", source = "userPassport.passportSeries")
  @Mapping(target = "passportNumber", source = "userPassport.passportNumber")
  @Mapping(target = "dateOfIssue", source = "userPassport.dateOfIssue", dateFormat = PASSPORT_DATE_FORMAT_PATTERN)
  @Mapping(target = "validityPeriod", source = "userPassport.validityPeriod", dateFormat = PASSPORT_DATE_FORMAT_PATTERN)
  @Mapping(target = "organizationThatIssued", source = "userPassport.organizationThatIssued")
  @Mapping(target = "documentsFileLink", source = "userPassport.documentsFileLink")
  UserPassportConfirmationDataResponse userPassportToUserPassportConfirmationDataResponse(
      UserPassport userPassport);
}
