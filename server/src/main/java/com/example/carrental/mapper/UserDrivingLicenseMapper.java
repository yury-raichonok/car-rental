package com.example.carrental.mapper;

import com.example.carrental.controller.dto.user.UserDrivingLicenseConfirmationDataResponse;
import com.example.carrental.entity.user.UserDrivingLicense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDrivingLicenseMapper {

  String DRIVING_LICENSE_DATE_FORMAT_PATTERN = "dd.MM.yyyy";

  @Mapping(target = "dateOfIssue", source = "userDrivingLicense.dateOfIssue", dateFormat = DRIVING_LICENSE_DATE_FORMAT_PATTERN)
  @Mapping(target = "validityPeriod", source = "userDrivingLicense.validityPeriod", dateFormat = DRIVING_LICENSE_DATE_FORMAT_PATTERN)
  @Mapping(target = "organizationThatIssued", source = "userDrivingLicense.organizationThatIssued")
  @Mapping(target = "documentsFileLink", source = "userDrivingLicense.documentsFileLink")
  UserDrivingLicenseConfirmationDataResponse drivingLicenseToUserDrivingLicenseConfirmationDataResponse(
      UserDrivingLicense userDrivingLicense);
}
