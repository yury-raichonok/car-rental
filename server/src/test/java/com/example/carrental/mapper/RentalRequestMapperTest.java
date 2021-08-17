package com.example.carrental.mapper;

import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_FORMAT_PATTERN;
import static com.example.carrental.constants.ApplicationConstants.RESPONSE_DATE_TIME_FORMAT_PATTERN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.carrental.entity.rentaldetails.RentalRequest;
import com.example.carrental.entity.rentaldetails.RentalRequestType;
import com.example.carrental.entity.user.User;
import com.example.carrental.entity.user.UserPassport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RentalRequestMapperTest {

  private RentalRequest rentalRequest;

  @Autowired
  private RentalRequestMapper rentalRequestMapper;

  @BeforeEach
  public void setup() {
    var sentDate = LocalDateTime.now().minusDays(1L);
    var considerationDate = LocalDateTime.now();
    var user = User.builder().id(1L).email("test@gmail.com").build();
    rentalRequest = RentalRequest.builder().id(1L).user(user)
        .rentalRequestType(RentalRequestType.PASSPORT_CONFIRMATION_REQUEST).sentDate(sentDate)
        .considerationDate(considerationDate).considered(true).comments("comments").build();
  }

  @Test
  void rentalRequestToRentalAllRequestResponse() {
    var rentalAllRequestsResponse = rentalRequestMapper
        .rentalRequestToRentalAllRequestResponse(rentalRequest);

    assertThat(rentalAllRequestsResponse).isNotNull();
    assertThat(rentalAllRequestsResponse.getId()).isEqualTo(rentalRequest.getId());
    assertThat(rentalAllRequestsResponse.getUserEmail())
        .isEqualTo(rentalRequest.getUser().getEmail());
    assertThat(rentalAllRequestsResponse.getRequestType())
        .isEqualTo(rentalRequest.getRentalRequestType());
    assertThat(rentalAllRequestsResponse.getSentDate())
        .isEqualTo(rentalRequest.getSentDate().format(
            DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(rentalAllRequestsResponse.isConsidered()).isEqualTo(rentalRequest.isConsidered());
    assertThat(rentalAllRequestsResponse.getConsiderationDate())
        .isEqualTo(rentalRequest.getConsiderationDate().format(
            DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(rentalAllRequestsResponse.getComments()).isEqualTo(rentalRequest.getComments());
  }

  @Test
  void rentalRequestToRentalRequestResponse() {
    var rentalRequestResponse = rentalRequestMapper
        .rentalRequestToRentalRequestResponse(rentalRequest);

    assertThat(rentalRequestResponse).isNotNull();
    assertThat(rentalRequestResponse.getId()).isEqualTo(rentalRequest.getId());
    assertThat(rentalRequestResponse.getUserEmail()).isEqualTo(rentalRequest.getUser().getEmail());
    assertThat(rentalRequestResponse.getRequestType())
        .isEqualTo(rentalRequest.getRentalRequestType());
    assertThat(rentalRequestResponse.getSentDate()).isEqualTo(rentalRequest.getSentDate().format(
        DateTimeFormatter.ofPattern(RESPONSE_DATE_TIME_FORMAT_PATTERN)));
    assertThat(rentalRequestResponse.isConsidered()).isEqualTo(rentalRequest.isConsidered());
    assertThat(rentalRequestResponse.getComments()).isEqualTo(rentalRequest.getComments());
  }

  @Test
  void passportToUserPassportConfirmationDataResponse() {
    var date = LocalDate.now();
    var userPassport = UserPassport.builder().firstName("name").middleName("middleName")
        .lastName("lastname").dateOfBirth(date).passportSeries("series").passportNumber("number")
        .dateOfIssue(date).validityPeriod(date).organizationThatIssued("name")
        .documentsFileLink("link").build();
    var userPassportConfirmationDataResponse = rentalRequestMapper
        .passportToUserPassportConfirmationDataResponse(userPassport);

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