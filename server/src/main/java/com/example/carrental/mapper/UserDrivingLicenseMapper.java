package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_FORMAT_PATTERN;

import com.example.carrental.controller.dto.user.UserDrivingLicenseConfirmationDataResponse;
import com.example.carrental.entity.user.UserDrivingLicense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * The interface for mapping User Driving License entity to DTO.
 *
 * @author Yury Raichonak
 */
@Mapper(componentModel = "spring")
public interface UserDrivingLicenseMapper {

  /**
   * @param userDrivingLicense data.
   * @return UserDrivingLicenseConfirmationDataResponse DTO.
   */
  @Mapping(target = "dateOfIssue", source = "userDrivingLicense.dateOfIssue",
      dateFormat = RESPONSE_DATE_FORMAT_PATTERN)
  @Mapping(target = "validityPeriod", source = "userDrivingLicense.validityPeriod",
      dateFormat = RESPONSE_DATE_FORMAT_PATTERN)
  @Mapping(target = "organizationThatIssued", source = "userDrivingLicense.organizationThatIssued")
  @Mapping(target = "documentsFileLink", source = "userDrivingLicense.documentsFileLink")
  UserDrivingLicenseConfirmationDataResponse drivingLicenseToUserDrivingLicenseConfirmationDataResponse(
      UserDrivingLicense userDrivingLicense);
}
