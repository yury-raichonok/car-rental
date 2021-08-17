package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_FORMAT_PATTERN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.carrental.entity.user.UserPassport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserPassportMapperTest {

  private UserPassport userPassport;

  @Autowired
  private UserPassportMapper userPassportMapper;

  @BeforeEach
  public void setup() {
    var dateOfBirth = LocalDate.now().minusDays(2);
    var dateOfIssue = LocalDate.now().minusDays(1);
    var validityPeriod = LocalDate.now().plusDays(2);
    userPassport = UserPassport.builder().firstName("firstName").middleName("middleName")
        .lastName("lastName").dateOfBirth(dateOfBirth).passportSeries("series")
        .passportNumber("number").dateOfIssue(dateOfIssue).validityPeriod(validityPeriod)
        .organizationThatIssued("name").documentsFileLink("link").build();
  }

  @Test
  void userPassportToUserPassportDataResponse() {
    var userPassportDataResponse = userPassportMapper
        .userPassportToUserPassportDataResponse(userPassport);

    assertThat(userPassportDataResponse).isNotNull();
    assertThat(userPassportDataResponse.getFirstName()).isEqualTo(userPassport.getFirstName());
    assertThat(userPassportDataResponse.getMiddleName()).isEqualTo(userPassport.getMiddleName());
    assertThat(userPassportDataResponse.getLastName()).isEqualTo(userPassport.getLastName());
    assertThat(userPassportDataResponse.getDateOfBirth()).isEqualTo(userPassport.getDateOfBirth());
    assertThat(userPassportDataResponse.getPassportSeries())
        .isEqualTo(userPassport.getPassportSeries());
    assertThat(userPassportDataResponse.getPassportNumber())
        .isEqualTo(userPassport.getPassportNumber());
    assertThat(userPassportDataResponse.getDateOfIssue()).isEqualTo(userPassport.getDateOfIssue());
    assertThat(userPassportDataResponse.getValidityPeriod())
        .isEqualTo(userPassport.getValidityPeriod());
    assertThat(userPassportDataResponse.getOrganizationThatIssued())
        .isEqualTo(userPassport.getOrganizationThatIssued());
  }

  @Test
  void userPassportToUserPassportConfirmationDataResponse() {
    var userPassportConfirmationDataResponse = userPassportMapper
        .userPassportToUserPassportConfirmationDataResponse(userPassport);

    assertThat(userPassportConfirmationDataResponse).isNotNull();
    assertThat(userPassportConfirmationDataResponse.getFirstName())
        .isEqualTo(userPassport.getFirstName());
    assertThat(userPassportConfirmationDataResponse.getMiddleName())
        .isEqualTo(userPassport.getMiddleName());
    assertThat(userPassportConfirmationDataResponse.getLastName())
        .isEqualTo(userPassport.getLastName());
    assertThat(userPassportConfirmationDataResponse.getDateOfBirth())
        .isEqualTo(userPassport.getDateOfBirth().format(
            DateTimeFormatter.ofPattern(RESPONSE_DATE_FORMAT_PATTERN)));
    assertThat(userPassportConfirmationDataResponse.getPassportSeries())
        .isEqualTo(userPassport.getPassportSeries());
    assertThat(userPassportConfirmationDataResponse.getPassportNumber())
        .isEqualTo(userPassport.getPassportNumber());
    assertThat(userPassportConfirmationDataResponse.getDateOfIssue())
        .isEqualTo(userPassport.getDateOfIssue().format(
            DateTimeFormatter.ofPattern(RESPONSE_DATE_FORMAT_PATTERN)));
    assertThat(userPassportConfirmationDataResponse.getValidityPeriod())
        .isEqualTo(userPassport.getValidityPeriod().format(
            DateTimeFormatter.ofPattern(RESPONSE_DATE_FORMAT_PATTERN)));
    assertThat(userPassportConfirmationDataResponse.getOrganizationThatIssued())
        .isEqualTo(userPassport.getOrganizationThatIssued());
    assertThat(userPassportConfirmationDataResponse.getDocumentsFileLink())
        .isEqualTo(userPassport.getDocumentsFileLink());
  }
}