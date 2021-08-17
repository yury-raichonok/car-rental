package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_FORMAT_PATTERN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.carrental.entity.user.UserDrivingLicense;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserDrivingLicenseMapperTest {

  private UserDrivingLicense userDrivingLicense;

  @Autowired
  private UserDrivingLicenseMapper userDrivingLicenseMapper;

  @BeforeEach
  public void setup() {
    var dateOfIssue = LocalDate.now();
    var validityPeriod = LocalDate.now().plusDays(1L);
    userDrivingLicense = UserDrivingLicense.builder().dateOfIssue(dateOfIssue)
        .validityPeriod(validityPeriod).organizationThatIssued("name").documentsFileLink("link")
        .build();
  }

  @Test
  void drivingLicenseToUserDrivingLicenseConfirmationDataResponse() {
    var userDrivingLicenseConfirmationDataResponse = userDrivingLicenseMapper
        .drivingLicenseToUserDrivingLicenseConfirmationDataResponse(userDrivingLicense);

    assertThat(userDrivingLicenseConfirmationDataResponse).isNotNull();
    assertThat(userDrivingLicenseConfirmationDataResponse.getDateOfIssue())
        .isEqualTo(userDrivingLicense.getDateOfIssue().format(
            DateTimeFormatter.ofPattern(RESPONSE_DATE_FORMAT_PATTERN)));
    assertThat(userDrivingLicenseConfirmationDataResponse.getValidityPeriod())
        .isEqualTo(userDrivingLicense.getValidityPeriod().format(
            DateTimeFormatter.ofPattern(RESPONSE_DATE_FORMAT_PATTERN)));
    assertThat(userDrivingLicenseConfirmationDataResponse.getOrganizationThatIssued())
        .isEqualTo(userDrivingLicense.getOrganizationThatIssued());
    assertThat(userDrivingLicenseConfirmationDataResponse.getDocumentsFileLink())
        .isEqualTo(userDrivingLicense.getDocumentsFileLink());
  }
}